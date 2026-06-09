package com.example.ui

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.datasource.DefaultDataSource
import com.example.data.IslamicAudioItem
import com.example.data.IslamicVideo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

/**
 * Universal Unified Media3 ExoPlayer System
 * Handles Streaming live TV, Radio, HLS (m3u8), DASH, MP4 videos, MP3 audios, speed controls, sleep timers, and playlists.
 */
class IbadahExoPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // --- State Flow Exposures ---
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentTitle = MutableStateFlow("")
    val currentTitle: StateFlow<String> = _currentTitle.asStateFlow()

    private val _currentSubtitle = MutableStateFlow("")
    val currentSubtitle: StateFlow<String> = _currentSubtitle.asStateFlow()

    private val _currentUrl = MutableStateFlow("")
    val currentUrl: StateFlow<String> = _currentUrl.asStateFlow()

    private val _isVideoPlaying = MutableStateFlow(false)
    val isVideoPlaying: StateFlow<Boolean> = _isVideoPlaying.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _sleepTimerMinutes = MutableStateFlow(0) // Remaining sleep timer minutes (0 = inactive)
    val sleepTimerMinutes: StateFlow<Int> = _sleepTimerMinutes.asStateFlow()

    // --- Playlist mechanics ---
    private var playlistAudios = listOf<IslamicAudioItem>()
    private var currentPlaylistIdx = -1

    private var sleepTimerJob: Job? = null
    private var progressUpdateJob: Job? = null

    init {
        // Safe lazy initialization: do not initialize player during ViewModel construction
        // to prevent startup crashes when context is loading or initialized from secondary threads.
    }

    fun getPlayerInstance(): ExoPlayer {
        if (exoPlayer == null) {
            initializePlayer()
        }
        return exoPlayer ?: ExoPlayer.Builder(context)
            .setLooper(android.os.Looper.getMainLooper())
            .build()
    }

    private fun initializePlayer() {
        if (exoPlayer != null) return

        // Guard: ensure initialization runs on the Main Thread
        if (android.os.Looper.myLooper() != android.os.Looper.getMainLooper()) {
            try {
                android.os.Handler(android.os.Looper.getMainLooper()).post {
                    initializePlayer()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        try {
            val player = ExoPlayer.Builder(context)
                .setLooper(android.os.Looper.getMainLooper())
                .build().apply {
                // Setup Audio Attributes for proper focus management
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build()
                setAudioAttributes(audioAttributes, true)
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_OFF
            }

            player.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingChanged: Boolean) {
                    _isPlaying.value = isPlayingChanged
                    if (isPlayingChanged) {
                        startProgressTracker()
                    } else {
                        stopProgressTracker()
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    _isBuffering.value = (state == Player.STATE_BUFFERING)
                    if (state == Player.STATE_READY) {
                        _duration.value = exoPlayer?.duration ?: 0L
                    } else if (state == Player.STATE_ENDED) {
                        handleMediaEnded()
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    _isBuffering.value = false
                    _isPlaying.value = false
                    error.printStackTrace()
                    // Auto-reconnect stream model
                    try {
                        Toast.makeText(context, "কোডেক/স্ট্রীম লোড হচ্ছে না। পুনরায় সংযোগ করা হচ্ছে...", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {}
                    mainScope.launch {
                        delay(2500)
                        exoPlayer?.prepare()
                        exoPlayer?.play()
                    }
                }
            })

            exoPlayer = player
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- PLAYMETHODS ---

    private fun createMediaSource(url: String, mediaItem: MediaItem): MediaSource {
        val dataSourceFactory = DefaultDataSource.Factory(context)
        return try {
            if (url.contains(".m3u8") || url.endsWith(".m3u8")) {
                HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
            } else if (url.contains(".mpd") || url.endsWith(".mpd")) {
                DashMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
            } else {
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
        }
    }

    fun playStream(url: String, title: String, subtitle: String, isVideo: Boolean) {
        val player = getPlayerInstance()
        player.stop()
        player.clearMediaItems()

        _currentUrl.value = url
        _currentTitle.value = title
        _currentSubtitle.value = subtitle
        _isVideoPlaying.value = isVideo
        _isBuffering.value = true

        mainScope.launch {
            val resolvedUrl = if (url.startsWith("http")) {
                withContext(Dispatchers.IO) {
                    try {
                        resolveRedirectWithOkHttp(url)
                    } catch (e: Exception) {
                        url
                    }
                }
            } else {
                url
            }

            try {
                _currentUrl.value = resolvedUrl
                val mediaBuilder = MediaItem.Builder()
                    .setUri(Uri.parse(resolvedUrl))
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(title)
                            .setArtist(subtitle)
                            .build()
                    )

                if (resolvedUrl.endsWith(".m3u8") || resolvedUrl.contains(".m3u8")) {
                    mediaBuilder.setMimeType(MimeTypes.APPLICATION_M3U8)
                } else if (resolvedUrl.contains(".mpd") || resolvedUrl.endsWith(".mpd")) {
                    mediaBuilder.setMimeType(MimeTypes.APPLICATION_MPD)
                } else if (resolvedUrl.endsWith(".mp4") || resolvedUrl.contains(".mp4")) {
                    mediaBuilder.setMimeType(MimeTypes.VIDEO_MP4)
                } else {
                    mediaBuilder.setMimeType(MimeTypes.AUDIO_MPEG)
                }

                val mediaItem = mediaBuilder.build()
                val mediaSource = createMediaSource(resolvedUrl, mediaItem)

                player.setMediaSource(mediaSource)
                player.setPlaybackSpeed(_playbackSpeed.value)
                player.prepare()
                player.play()
            } catch (e: Exception) {
                e.printStackTrace()
                _isBuffering.value = false
            }
        }
    }

    private fun resolveRedirectWithOkHttp(initialUrl: String): String {
        if (!initialUrl.startsWith("http")) {
            return initialUrl
        }
        var url = initialUrl
        val maxHops = 5
        var hopCount = 0
        
        val client = okhttp3.OkHttpClient.Builder()
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(8, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(8, java.util.concurrent.TimeUnit.SECONDS)
            .build()

        while (hopCount < maxHops) {
            val request = okhttp3.Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                .header("Accept", "*/*")
                .head()
                .build()
            try {
                client.newCall(request).execute().use { response ->
                    val code = response.code
                    if (code in 300..399) {
                        val location = response.header("Location")
                        if (!location.isNullOrEmpty()) {
                            url = if (location.startsWith("//")) {
                                val uri = android.net.Uri.parse(url)
                                val scheme = uri.scheme ?: "https"
                                "$scheme:$location"
                            } else if (location.startsWith("/")) {
                                val uri = android.net.Uri.parse(url)
                                val scheme = uri.scheme ?: "https"
                                val host = uri.host ?: "archive.org"
                                "$scheme://$host$location"
                            } else {
                                location
                            }
                            hopCount++
                        } else {
                            break
                        }
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                try {
                    val getRequest = okhttp3.Request.Builder()
                        .url(url)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                        .header("Accept", "*/*")
                        .build()
                    client.newCall(getRequest).execute().use { getResponse ->
                        val code = getResponse.code
                        if (code in 300..399) {
                            val location = getResponse.header("Location")
                            if (!location.isNullOrEmpty()) {
                                url = if (location.startsWith("//")) {
                                    val uri = android.net.Uri.parse(url)
                                    val scheme = uri.scheme ?: "https"
                                    "$scheme:$location"
                                } else if (location.startsWith("/")) {
                                    val uri = android.net.Uri.parse(url)
                                    val scheme = uri.scheme ?: "https"
                                    val host = uri.host ?: "archive.org"
                                    "$scheme://$host$location"
                                } else {
                                    location
                                }
                                hopCount++
                            } else {
                                break
                            }
                        } else {
                            break
                        }
                    }
                } catch (ex: Exception) {
                    break
                }
            }
        }
        return url
    }

    fun playAudioItem(item: IslamicAudioItem, playlist: List<IslamicAudioItem> = emptyList()) {
        playlistAudios = playlist
        currentPlaylistIdx = playlist.indexOfFirst { it.id == item.id }

        // Local or remote url resolution
        val localFile = File(context.filesDir, "audio_${item.id}.mp3")
        val finalUrl = if (localFile.exists() && localFile.length() > 2000) {
            localFile.absolutePath
        } else {
            item.audioUrl
        }

        playStream(
            url = finalUrl,
            title = item.titleBn,
            subtitle = item.speakerBn,
            isVideo = false
        )
    }

    fun playVideoItem(item: IslamicVideo) {
        playStream(
            url = item.videoUrl,
            title = item.title,
            subtitle = item.speaker,
            isVideo = true
        )
    }

    // --- PLAYBACK ACTIONS ---

    fun togglePlayPause() {
        val player = getPlayerInstance()
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun stop() {
        exoPlayer?.stop()
        _isPlaying.value = false
        _currentUrl.value = ""
        _currentTitle.value = ""
        _currentSubtitle.value = ""
        _isVideoPlaying.value = false
        stopProgressTracker()
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    fun setSpeed(speed: Float) {
        _playbackSpeed.value = speed
        exoPlayer?.setPlaybackSpeed(speed)
    }

    // --- PLAYLIST OPERATIONS (NEXT/PREV) ---

    fun playNext() {
        if (playlistAudios.isNotEmpty() && currentPlaylistIdx != -1) {
            val nextIdx = (currentPlaylistIdx + 1) % playlistAudios.size
            playAudioItem(playlistAudios[nextIdx], playlistAudios)
        }
    }

    fun playPrev() {
        if (playlistAudios.isNotEmpty() && currentPlaylistIdx != -1) {
            val prevIdx = if (currentPlaylistIdx - 1 < 0) playlistAudios.size - 1 else currentPlaylistIdx - 1
            playAudioItem(playlistAudios[prevIdx], playlistAudios)
        }
    }

    private fun handleMediaEnded() {
        if (playlistAudios.isNotEmpty() && currentPlaylistIdx != -1) {
            playNext()
        } else {
            stop()
        }
    }

    // --- SLEEP TIMER ---

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        _sleepTimerMinutes.value = minutes

        if (minutes <= 0) return

        sleepTimerJob = mainScope.launch {
            var remainingSecs = minutes * 60
            while (remainingSecs > 0) {
                delay(1000)
                remainingSecs--
                _sleepTimerMinutes.value = (remainingSecs + 59) / 60
            }
            // Timer expired, pause audios gracefully
            stop()
            _sleepTimerMinutes.value = 0
            Toast.makeText(context, "স্লিপ টাইমারের সময় শেষ! মিডিয়া বন্ধ করা হয়েছে।", Toast.LENGTH_LONG).show()
        }
    }

    // --- PROGRESS TRACKER ---

    private fun startProgressTracker() {
        progressUpdateJob?.cancel()
        progressUpdateJob = mainScope.launch {
            while (isActive) {
                exoPlayer?.let { player ->
                    _currentPosition.value = player.currentPosition
                    _duration.value = player.duration
                }
                delay(500)
            }
        }
    }

    private fun stopProgressTracker() {
        progressUpdateJob?.cancel()
    }

    fun release() {
        stopProgressTracker()
        sleepTimerJob?.cancel()
        mainScope.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
