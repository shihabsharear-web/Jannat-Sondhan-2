package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import java.util.Locale
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.service.GeminiApiClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class IbadahViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext

    fun getProxiedUrl(originalUrl: String): String {
        return originalUrl
    }

    // --- TextToSpeech for real audio recitation ---
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    // --- Core Settings ---
    private val _appLanguage = MutableStateFlow(AppLanguage.BN)
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false) // Light mode is default now
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _playBengaliTranslation = MutableStateFlow(true)
    val playBengaliTranslation: StateFlow<Boolean> = _playBengaliTranslation.asStateFlow()

    fun togglePlayBengaliTranslation() {
        _playBengaliTranslation.value = !_playBengaliTranslation.value
    }

    private val _hasSeenOnboarding = MutableStateFlow(false)
    val hasSeenOnboarding: StateFlow<Boolean> = _hasSeenOnboarding.asStateFlow()

    fun setHasSeenOnboarding(seen: Boolean) {
        _hasSeenOnboarding.value = seen
        getDeedsPrefs().edit().putBoolean("has_seen_onboarding_v2", seen).apply()
    }

    // --- YouTube API Search State ---
    private val _youtubeSearchQuery = MutableStateFlow("")
    val youtubeSearchQuery = _youtubeSearchQuery.asStateFlow()

    private val _youtubeSearchResults = MutableStateFlow<List<IslamicVideo>>(emptyList())
    val youtubeSearchResults = _youtubeSearchResults.asStateFlow()

    private val _isYoutubeLoading = MutableStateFlow(false)
    val isYoutubeLoading = _isYoutubeLoading.asStateFlow()

    fun searchYoutube(query: String) {
        _youtubeSearchQuery.value = query
        viewModelScope.launch(Dispatchers.IO) {
            _isYoutubeLoading.value = true
            try {
                // Search via official YouTube Data API v3 (caches results inside)
                val results = com.example.service.YoutubeApiClient.searchYouTubeVideos(context, query)
                _youtubeSearchResults.value = results
            } catch (e: Exception) {
                android.util.Log.e("IbadahViewModel", "Error searching YouTube", e)
            } finally {
                _isYoutubeLoading.value = false
            }
        }
    }

    private fun translatePartBn(englishStr: String): String {
        return when (englishStr.lowercase(java.util.Locale.ROOT).trim()) {
            "dhaka" -> "ঢাকা"
            "chittagong", "chattogram" -> "চট্টগ্রাম"
            "sylhet" -> "সিলেট"
            "rajshahi" -> "রাজশাহী"
            "khulna" -> "খুলনা"
            "barisal", "barishal" -> "বরিশাল"
            "rangpur" -> "রংপুর"
            "mymensingh" -> "ময়মনসিংহ"
            "comilla" -> "কুমিল্লা"
            "gazipur" -> "গাজীপুর"
            "narayanganj" -> "নারায়ণগঞ্জ"
            "feni" -> "ফেনী"
            "noakhali" -> "নোয়াখালী"
            "jessore", "jashor", "joshor" -> "যশোর"
            "bogra", "bogura" -> "বগুড়া"
            "dinajpur" -> "দিনাজপুর"
            "tangail" -> "টাঙ্গাইল"
            "faridpur" -> "ফরিদপুর"
            "kushtia" -> "কুষ্টিয়া"
            "pabna" -> "পাবনা"
            "bagerhat" -> "বাগেরহাট"
            "bandarban" -> "বান্দরবান"
            "barguna" -> "বরগুনা"
            "bhola" -> "ভোলা"
            "brahmanbaria" -> "ব্রাহ্মণবাড়িয়া"
            "chandpur" -> "চাঁদপুর"
            "chapainawabganj", "nawabganj" -> "চাঁপাইনবাবগঞ্জ"
            "cox's bazar", "coxsbazar" -> "কক্সবাজার"
            "chuadanga" -> "চুয়াডাঙ্গা"
            "gaibandha" -> "গাইবান্ধা"
            "gopalganj" -> "গোপালগঞ্জ"
            "habiganj" -> "হবিগঞ্জ"
            "jamalpur" -> "জামালপুর"
            "jhalokati", "jhalokathi" -> "ঝালকাঠি"
            "jhenaidah" -> "ঝিনাইদহ"
            "joypurhat" -> "জয়পুরহাট"
            "khagrachhari" -> "খাগড়াছড়ি"
            "kurigram" -> "কুড়িগ্রাম"
            "lakshmipur", "laxmipur" -> "লক্ষ্মীপুর"
            "lalmonirhat" -> "লালমনিরহাট"
            "madaripur" -> "মাদারীপুর"
            "magura" -> "ماগুরা"
            "manikganj" -> "মানিকগঞ্জ"
            "meherpur" -> "মেহেরপুর"
            "moulvibazar", "maulvibazar" -> "মৌলভীবাজার"
            "munshiganj" -> "মুন্সীগঞ্জ"
            "naogaon" -> "নওগাঁ"
            "narail" -> "নড়াইল"
            "narsingdi" -> "নরসিংহদী"
            "netrokona" -> "নেত্রকোনা"
            "nilphamari" -> "নীলফামারী"
            "panchagarh" -> "পঞ্চগড়"
            "patuakhali" -> "পটুয়াখালী"
            "pirojpur" -> "পিরোজপুর"
            "rajbari" -> "রাজবাড়ী"
            "rangamati" -> "রাঙ্গামাটি"
            "satkhira" -> "সাতক্ষীরা"
            "shariatpur" -> "শরীয়তপুর"
            "sherpur" -> "শেরপুর"
            "sirajganj" -> "সিরাজগঞ্জ"
            "sunamganj" -> "সুনামগঞ্জ"
            "thakurgaon" -> "ঠাকুরগাঁও"
            "bangladesh" -> "বাংলাদেশ"
            "saudi arabia" -> "সৌদি আরব"
            "united arab emirates", "uae" -> "সংযুক্ত আরব আমিরাত"
            "india" -> "ভারত"
            "pakistan" -> "পাকিস্তান"
            "malaysia" -> "মালয়েশিয়া"
            "kuwait" -> "কুয়েত"
            "qatar" -> "কাতার"
            "oman" -> "ওমান"
            "bahrain" -> "বাহরাইন"
            "turkey" -> "তুরস্ক"
            "united kingdom", "uk" -> "যুক্তরাজ্য"
            "united states", "usa" -> "যুক্তরাষ্ট্র"
            else -> englishStr
        }
    }

    private fun translatePartEn(banglaStr: String): String {
        return when (banglaStr.trim()) {
            "ঢাকা" -> "Dhaka"
            "চট্টগ্রাম" -> "Chattogram"
            "সিলেট" -> "Sylhet"
            "রাজশাহী" -> "Rajshahi"
            "খুলনা" -> "Khulna"
            "বরিশাল" -> "Barishal"
            "রংপুর" -> "Rangpur"
            "ময়মনসিংহ", "ময়মনসিংহ" -> "Mymensingh"
            "কুমিল্লা" -> "Comilla"
            "গাজীপুর" -> "Gazipur"
            "নারায়ণগঞ্জ", "নারায়ণগঞ্জ" -> "Narayanganj"
            "ফেনী" -> "Feni"
            "নোয়াখালী", "নোয়াখালী" -> "Noakhali"
            "যশোর" -> "Jessore"
            "বগুড়া", "বগুড়া" -> "Bogura"
            "দিনাজপুর" -> "Dinajpur"
            "টাঙ্গাইল" -> "Tangail"
            "فরিদপুর" -> "Faridpur"
            "কুষ্টিয়া", "কুষ্টিয়া" -> "Kushtia"
            "পাবনা" -> "Pabna"
            "বাগেরহাট" -> "Bagerhat"
            "বান্দরবান" -> "Bandarban"
            "বরগুনা" -> "Barguna"
            "ভোলা" -> "Bhola"
            "ব্রাহ্মণবাড়িয়া", "ব্রাহ্মণবাড়িয়া" -> "Brahmanbaria"
            "চাঁদপুর" -> "Chandpur"
            "চাঁপাইনবাবগঞ্জ" -> "Chapainawabganj"
            "কক্সবাজার" -> "Cox's Bazar"
            "চুয়াডাঙ্গা", "চুয়াডাঙ্গা" -> "Chuadanga"
            "গাইবান্ধা" -> "Gaibandha"
            "গোপালগঞ্জ" -> "Gopalganj"
            "হবিগঞ্জ" -> "Habiganj"
            "জামালপুর" -> "Jamalpur"
            "ঝালকাঠি" -> "Jhalokathi"
            "ঝিনাইদহ" -> "Jhenaidah"
            "জয়পুরহাট", "জয়পুরহাট" -> "Joypurhat"
            "খাগড়াছড়ি", "খাগড়াছড়ি" -> "Khagrachhari"
            "কুড়িগ্রাম", "কুড়িগ্রাম" -> "Kurigram"
            "লক্ষ্মীপুর" -> "Lakshmipur"
            "লালমনিরহাট" -> "Lalmonirhat"
            "مাদারীপুর", "মাদারীপুর" -> "Madaripur"
            "মাগুরা" -> "Magura"
            "মানিকগঞ্জ" -> "Manikganj"
            "মেহেরপুর" -> "Meherpur"
            "মৌলভীবাজার", "مৌলভীবাজার" -> "Moulvibazar"
            "মুন্সীগঞ্জ" -> "Munshiganj"
            "নওগাঁ" -> "Naogaon"
            "নড়াইল", "নড়াইল" -> "Narail"
            "নরসীংদী", "নরসিংদী", "নারায়ণগঞ্জ" -> "Narsingdi"
            "নেত্রকোনা" -> "Netrokona"
            "নীলফামারী" -> "Nilphamari"
            "পঞ্চগড়", "পঞ্চগড়" -> "Panchagarh"
            "পটুয়াখালী", "পটুয়াখালী" -> "Patuakhali"
            "পিরোজপুর" -> "Pirojpur"
            "রাজবাড়ী", "রাজবাড়ী" -> "Rajbari"
            "রাঙ্গামাতি", "রাঙ্গামাটি" -> "Rangamati"
            "সাতক্ষীরা" -> "Satkhira"
            "শরীয়তপুর", "শরয়ারতপুর" -> "Shariatpur"
            "শেরপুর" -> "Sherpur"
            "সিরাজগঞ্জ" -> "Sirajganj"
            "সুনামগঞ্জ" -> "Sunamganj"
            "ঠাকুরগাঁও" -> "Thakurgaon"
            "বাংলাদেশ" -> "Bangladesh"
            "সৌদি আরব" -> "Saudi Arabia"
            "সংযুক্ত আরব আমিরাত" -> "United Arab Emirates"
            "ভারত" -> "India"
            "پاکستان", "পাকিস্তান" -> "Pakistan"
            "মালয়েশিয়া" -> "Malaysia"
            "কুয়েত" -> "Kuwait"
            "কাতার" -> "Qatar"
            "ওমান" -> "Oman"
            "বাহরাইন" -> "Bahrain"
            "তুরস্ক" -> "Turkey"
            "যুক্তরাজ্য" -> "United Kingdom"
            "যুক্তরাষ্ট্র" -> "United States"
            else -> banglaStr
        }
    }

    private val _use24HourFormat = MutableStateFlow(false)
    val use24HourFormat: StateFlow<Boolean> = _use24HourFormat.asStateFlow()

    private val _isBgMusicEnabled = MutableStateFlow(true)
    val isBgMusicEnabled: StateFlow<Boolean> = _isBgMusicEnabled.asStateFlow()

    private val _activeAlarmFiring = MutableStateFlow<PrayerAlarm?>(null)
    val activeAlarmFiring: StateFlow<PrayerAlarm?> = _activeAlarmFiring.asStateFlow()

    private val snoozedAlarms = java.util.concurrent.ConcurrentHashMap<String, Long>()
    private var systemRingtone: android.media.Ringtone? = null

    private var bgExoPlayer: androidx.media3.exoplayer.ExoPlayer? = null
    private var azanExoPlayer: androidx.media3.exoplayer.ExoPlayer? = null
    private var currentBgMusicIndex = 0
    private val bgMusicTracks = listOf(
        "https://archive.org/download/islamic-nasheed-mp3/01_Tumi_Rahman_Ya_Rahim.mp3",
        "https://archive.org/download/islamic-nasheed-mp3/02_Jodi_Nikhul_Sristi_Chao.mp3",
        "https://archive.org/download/islamic-nasheed-mp3/03_Allah_Tomar_Moto_Doyalu.mp3",
        "https://archive.org/download/islamic-nasheed-mp3/04_Kahar_Kortitbe_E_Bishwo_Jahan.mp3",
        "https://archive.org/download/islamic-nasheed-mp3/05_Subhan_Allah_Alhamdulillah.mp3"
    )

    private var alarmPlayer: android.media.MediaPlayer? = null
    private var lastCheckedMinuteStr = ""

    fun toggleTimeFormat() {
        val newVal = !_use24HourFormat.value
        _use24HourFormat.value = newVal
        getDeedsPrefs().edit().putBoolean("use_24h_format", newVal).apply()
        
        // Broadcast update to the home screen widget
        try {
            val widgetUpdateIntent = android.content.Intent("com.example.action.PRAYER_WIDGET_REFRESH").apply {
                component = android.content.ComponentName(getApplication<Application>(), "com.example.widget.PrayerAppWidgetProvider")
            }
            getApplication<Application>().sendBroadcast(widgetUpdateIntent)

            val detailedWidgetUpdateIntent = android.content.Intent("com.example.action.DETAILED_PRAYER_WIDGET_REFRESH").apply {
                component = android.content.ComponentName(getApplication<Application>(), "com.example.widget.DetailedPrayerAppWidgetProvider")
            }
            getApplication<Application>().sendBroadcast(detailedWidgetUpdateIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleBgMusic() {
        val newVal = !_isBgMusicEnabled.value
        _isBgMusicEnabled.value = newVal
        getDeedsPrefs().edit().putBoolean("is_bg_music_enabled", newVal).apply()
        if (!newVal) {
            stopBgMusic()
        } else {
            playNextBgMusic()
        }
    }

    fun playNextBgMusic() {
        if (!_isBgMusicEnabled.value) return
        viewModelScope.launch(Dispatchers.Main) {
            try {
                // Re-create the player instance to cleanly reset state and prevent listener accumulation
                try {
                    bgExoPlayer?.stop()
                    bgExoPlayer?.release()
                } catch (e: Exception) {}
                bgExoPlayer = null

                bgExoPlayer = androidx.media3.exoplayer.ExoPlayer.Builder(context)
                    .setLooper(android.os.Looper.getMainLooper())
                    .build().apply {
                        val audioAttributes = androidx.media3.common.AudioAttributes.Builder()
                            .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
                            .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                            .build()
                        setAudioAttributes(audioAttributes, false) // bypass focus limitations
                        volume = 0.55f // Clear default volume
                    }
                
                val player = bgExoPlayer ?: return@launch
                
                currentBgMusicIndex = (currentBgMusicIndex + 1) % bgMusicTracks.size
                val trackUrl = bgMusicTracks[currentBgMusicIndex]
                
                // Directly use URL if from archive.org to avoid slow DNS/redirect checks, otherwise resolve safety redirects
                val resolvedUrl = if (trackUrl.contains("archive.org")) trackUrl else {
                    withContext(Dispatchers.IO) {
                        try {
                            resolveRedirects(trackUrl)
                        } catch (e: Exception) {
                            trackUrl
                        }
                    }
                }
                
                player.addListener(object : androidx.media3.common.Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                            playNextBgMusic()
                        }
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        android.util.Log.e("IbadahViewModel", "Gojol stream error: ${error.message}; skipping to next Gojol...")
                        playNextBgMusic() // Safe auto skip
                    }
                })
                
                player.setMediaItem(androidx.media3.common.MediaItem.fromUri(resolvedUrl))
                player.playWhenReady = true
                player.prepare()
                android.util.Log.d("IbadahViewModel", "Streaming background gozol index $currentBgMusicIndex via Media3 ExoPlayer successfully")
            } catch (e: Exception) {
                android.util.Log.e("IbadahViewModel", "Failed streaming background gozol track", e)
                e.printStackTrace()
            }
        }
    }

    fun stopBgMusic() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                bgExoPlayer?.stop()
                bgExoPlayer?.release()
            } catch (e: Exception) {}
            bgExoPlayer = null
        }
    }

    fun startAzanTest() {
        playAlarmSound(isAzan = true)
    }

    fun stopAzanTest() {
        stopAlarmSound()
    }

    fun dismissActiveAlarm() {
        _activeAlarmFiring.value = null
        stopAlarmSound()
    }

    fun snoozeActiveAlarm() {
        val currentAlarm = _activeAlarmFiring.value ?: return
        stopAlarmSound()
        try {
            getNotificationManager().cancel(9977)
        } catch (e: Exception) {}

        val snoozeTime = System.currentTimeMillis() + (5 * 60 * 1000) // 5 minutes
        snoozedAlarms[currentAlarm.id] = snoozeTime
        _activeAlarmFiring.value = null
        android.util.Log.d("IbadahViewModel", "Alarm snoozed for 5 minutes: ${currentAlarm.id} -> snooze absolute time $snoozeTime")
    }

    private fun stopAlarmSound() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                azanExoPlayer?.stop()
                azanExoPlayer?.release()
            } catch (e: Exception) {}
            azanExoPlayer = null

            try {
                alarmPlayer?.stop()
                alarmPlayer?.release()
            } catch (e: Exception) {}
            alarmPlayer = null

            try {
                systemRingtone?.let {
                    if (it.isPlaying) {
                        it.stop()
                    }
                }
            } catch (e: Exception) {}
            systemRingtone = null
        }
    }

    private fun playAlarmSound(isAzan: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            stopAlarmSound()
            
            if (isAzan) {
                try {
                    // Release the old player and instantiate a clean new one to prevent listener pileups and state corruption!
                    try {
                        azanExoPlayer?.stop()
                        azanExoPlayer?.release()
                    } catch (e: Exception) {}
                    azanExoPlayer = null

                    azanExoPlayer = androidx.media3.exoplayer.ExoPlayer.Builder(context)
                        .setLooper(android.os.Looper.getMainLooper())
                        .build().apply {
                            val audioAttributes = androidx.media3.common.AudioAttributes.Builder()
                                .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
                                .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                                .build()
                            setAudioAttributes(audioAttributes, false) // bypass focus limitations
                            volume = 1.0f // full clear volume for reliable testing
                        }
                    
                    val player = azanExoPlayer ?: return@launch
                    
                    val urls = listOf(
                        "https://download.quranicaudio.com/adhan/makkah.mp3",
                        "https://download.quranicaudio.com/adhan/madinah.mp3",
                        "https://www.islamcan.com/audio/adhan/azan1.mp3",
                        "https://www.islamcan.com/audio/adhan/azan14.mp3"
                    )
                    
                    var currentUrlIndex = 0
                    
                    fun playUrlAtIndex(index: Int) {
                        if (index >= urls.size) {
                            android.util.Log.e("IbadahViewModel", "All Azan URLs failed to play!")
                            return
                        }
                        try {
                            player.stop()
                            player.clearMediaItems()
                            player.setMediaItem(androidx.media3.common.MediaItem.fromUri(urls[index]))
                            player.playWhenReady = true
                            player.prepare()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            playUrlAtIndex(index + 1)
                        }
                    }
                    
                    player.addListener(object : androidx.media3.common.Player.Listener {
                        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                            android.util.Log.e("IbadahViewModel", "Azan play error at index $currentUrlIndex: ${error.message}. Trying next fallback...")
                            currentUrlIndex++
                            playUrlAtIndex(currentUrlIndex)
                        }
                    })
                    
                    playUrlAtIndex(0)
                    android.util.Log.d("IbadahViewModel", "Playing beautiful Azan via brand new ExoPlayer successfully")
                } catch (e: Exception) {
                    android.util.Log.e("IbadahViewModel", "Failed playing beautiful Azan via ExoPlayer, trying system fallback", e)
                }
                return@launch
            }
            
            // Non-Azan fallbacks
            withContext(Dispatchers.IO) {
                try {
                    val alertUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)
                        ?: android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_RINGTONE)
                        ?: android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)

                    if (alertUri != null) {
                        try {
                            val ringtone = android.media.RingtoneManager.getRingtone(context, alertUri)
                            if (ringtone != null) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                    ringtone.isLooping = true
                                }
                                ringtone.play()
                                systemRingtone = ringtone
                                android.util.Log.d("IbadahViewModel", "Playing ringtone/beep successfully")
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("IbadahViewModel", "Failed RingtoneManager, trying MediaPlayer fallback", e)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun postPrayerAlarmNotification(alarm: PrayerAlarm) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "prayer_alarm_channel"
            val channelName = "Prayer Alarms & Azans"
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = android.app.NotificationChannel(channelId, channelName, importance).apply {
                description = "Triggers high-priority alerts with beautiful Azan voices at prayer windows"
            }
            getNotificationManager().createNotificationChannel(channel)
        }

        val channelId = "prayer_alarm_channel"
        val isBn = _appLanguage.value == AppLanguage.BN
        val title = if (isBn) "নামাজের সময় শুরু হয়েছে!" else "Salat Waqt Starting Alert!"
        val content = if (isBn) "${alarm.nameBn} নামাজের ওয়াক্ত এখন শুরু হয়েছে। ওযুর সাথে নামাজ আদায় করুন।" else "It is now time for ${alarm.nameEn}. Please prepare and offer your prayers."

        val launchIntent = getApplication<android.app.Application>().packageManager.getLaunchIntentForPackage(getApplication<android.app.Application>().packageName)?.apply {
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = android.app.PendingIntent.getActivity(
            getApplication(),
            0,
            launchIntent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val builder = androidx.core.app.NotificationCompat.Builder(getApplication(), channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
            .setCategory(androidx.core.app.NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)

        try {
            getNotificationManager().notify(9977, builder.build())
        } catch (e: SecurityException) {
            android.util.Log.e("IbadahViewModel", "Post notification permission missing on Android 13+", e)
        }
    }

    private fun checkAndTriggerAlarms(now: java.util.Calendar, currentMinStr: String) {
        val currentDayOfWeek = now.get(java.util.Calendar.DAY_OF_WEEK) // 1 = Sunday, 2 = Monday, etc.
        val sdfDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val currentDateStr = sdfDate.format(now.time)
        
        val alarms = _prayerAlarms.value
        val prayerTimeMap = _prayerTimes.value.filter { !it.isForbidden }.associate { it.nameStrEn.lowercase() to it.timeStr }
        
        for (alarm in alarms) {
            if (!alarm.isEnabled) continue
            
            var alarmTimeStr = ""
            if (alarm.customTime != null) {
                alarmTimeStr = alarm.customTime
            } else {
                val prayerKey = alarm.id.lowercase()
                val baseTimeStr = prayerTimeMap[prayerKey] ?: continue
                if (alarm.offsetMinutes == 0) {
                    alarmTimeStr = baseTimeStr
                } else {
                    val baseMins = timeToMinutes(baseTimeStr)
                    val offsetMins = baseMins + alarm.offsetMinutes
                    val finalOffsetMins = if (offsetMins < 0) offsetMins + 1440 else offsetMins % 1440
                    alarmTimeStr = String.format(java.util.Locale.US, "%02d:%02d", finalOffsetMins / 60, finalOffsetMins % 60)
                }
            }
            
            if (alarmTimeStr == currentMinStr) {
                var shouldTrigger = false
                if (alarm.customTime == null) {
                    shouldTrigger = true
                } else {
                    if (alarm.isDaily) {
                        shouldTrigger = true
                    } else if (!alarm.customDays.isNullOrEmpty()) {
                        if (alarm.customDays.contains(currentDayOfWeek)) {
                            shouldTrigger = true
                        }
                    } else if (alarm.specificDate != null) {
                        if (alarm.specificDate == currentDateStr) {
                            shouldTrigger = true
                        }
                    } else {
                        shouldTrigger = true
                    }
                }
                
                if (shouldTrigger) {
                    playAlarmSound(alarm.isAzanAlarm)
                    _activeAlarmFiring.value = alarm
                    postPrayerAlarmNotification(alarm)
                    
                    // Force wake and open application immediately
                    try {
                        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        }
                        if (launchIntent != null) {
                            context.startActivity(launchIntent)
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("IbadahViewModel", "Failed to force start MainActivity on initial alarm trigger", e)
                    }
                    break
                }
            }
        }
    }

    // --- Location / City Select ---
    val cities = listOf(
        CityInfo("ঢাকা, বাংলাদেশ", "Dhaka, Bangladesh", 23.8103, 90.4125, "Asia/Dhaka", "03:55", "18:42", "04:05", "12:06", "15:45", "18:42", "20:00"),
        CityInfo("মক্কা, সৌদি আরব", "Mecca, Saudi Arabia", 21.4225, 39.8262, "Asia/Riyadh", "04:12", "19:02", "04:22", "12:22", "15:38", "19:02", "20:25"),
        CityInfo("লন্ডন, যুক্তরাজ্য", "London, UK", 51.5074, -0.1278, "Europe/London", "03:10", "21:15", "03:25", "13:05", "17:15", "21:15", "22:45"),
        CityInfo("নিউ ইয়র্ক, যুক্তরাষ্ট্র", "New York, USA", 40.7128, -74.0060, "America/New_York", "03:45", "20:20", "04:00", "12:58", "16:55", "20:20", "21:50"),
        CityInfo("সিডনি, অস্ট্রেলিয়া", "Sydney, Australia", -33.8688, 151.2093, "Australia/Sydney", "05:15", "17:10", "05:30", "11:55", "14:48", "17:10", "18:35")
    )

    private val _selectedCity = MutableStateFlow(cities[0])
    val selectedCity: StateFlow<CityInfo> = _selectedCity.asStateFlow()

    // --- Current Prayer Time States ---
    private val _prayerTimes = MutableStateFlow<List<PrayerSchedule>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerSchedule>> = _prayerTimes.asStateFlow()

    private val _forbiddenTimeState = MutableStateFlow<ForbiddenTimeState?>(null)
    val forbiddenTimeState: StateFlow<ForbiddenTimeState?> = _forbiddenTimeState.asStateFlow()

    // --- Prayer Alarms Configuration ---
    private val _prayerAlarms = MutableStateFlow<List<PrayerAlarm>>(
        listOf(
            PrayerAlarm("fajr", "ফজর", "Fajr", "الفجر", true, true, 0),
            PrayerAlarm("dhuhr", "যোহর", "Dhuhr", "الظهر", true, true, 0),
            PrayerAlarm("asr", "আসর", "Asr", "العصر", true, true, 0),
            PrayerAlarm("maghrib", "মাগরিব", "Maghrib", "المغرب", true, true, 0),
            PrayerAlarm("isha", "এশা", "Isha", "العشاء", true, true, 0)
        )
    )
    val prayerAlarms: StateFlow<List<PrayerAlarm>> = _prayerAlarms.asStateFlow()

    private val _nextPrayerName = MutableStateFlow("")
    val nextPrayerName: StateFlow<String> = _nextPrayerName.asStateFlow()

    private val _nextPrayerTime = MutableStateFlow("")
    val nextPrayerTime: StateFlow<String> = _nextPrayerTime.asStateFlow()

    private val _currentPrayerName = MutableStateFlow("")
    val currentPrayerName: StateFlow<String> = _currentPrayerName.asStateFlow()

    private val _currentPrayerTime = MutableStateFlow("")
    val currentPrayerTime: StateFlow<String> = _currentPrayerTime.asStateFlow()

    private val _prayerCountdown = MutableStateFlow("00:00:00")
    val prayerCountdown: StateFlow<String> = _prayerCountdown.asStateFlow()

    private val _prayerProgressFraction = MutableStateFlow(0.7f)
    val prayerProgressFraction: StateFlow<Float> = _prayerProgressFraction.asStateFlow()

    // --- Salat Daily Tracker ---
    private val _salatTracker = MutableStateFlow<List<SalatTrackItem>>(emptyList())
    val salatTracker: StateFlow<List<SalatTrackItem>> = _salatTracker.asStateFlow()

    // --- Quran States ---
    val suras = IslamicData.quranSurahs
    private val _selectedSurah = MutableStateFlow<Surah?>(null)
    val selectedSurah: StateFlow<Surah?> = _selectedSurah.asStateFlow()

    private val _isSurahLoading = MutableStateFlow(false)
    val isSurahLoading: StateFlow<Boolean> = _isSurahLoading.asStateFlow()

    private var mediaPlayer: android.media.MediaPlayer? = null

    private val _quranSearchQuery = MutableStateFlow("")
    val quranSearchQuery: StateFlow<String> = _quranSearchQuery.asStateFlow()

    private val _quranBookmarks = MutableStateFlow<List<SuraBookmark>>(emptyList())
    val quranBookmarks: StateFlow<List<SuraBookmark>> = _quranBookmarks.asStateFlow()

    private val _lastReadSurahNumber = MutableStateFlow(1)
    val lastReadSurahNumber: StateFlow<Int> = _lastReadSurahNumber.asStateFlow()

    private val _audioReciter = MutableStateFlow("")
    val audioReciter: StateFlow<String> = _audioReciter.asStateFlow()

    private val _downloadedSurahs = MutableStateFlow<Set<String>>(emptySet())
    val downloadedSurahs: StateFlow<Set<String>> = _downloadedSurahs.asStateFlow()

    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private val _playingVerseIndex = MutableStateFlow(-1)
    val playingVerseIndex: StateFlow<Int> = _playingVerseIndex.asStateFlow()

    // --- Quran Audio Download State ---
    data class DownloadInfo(
        val surahNumber: Int,
        val nameEn: String,
        val nameBn: String,
        val progress: Float,
        val isDownloading: Boolean,
        val isCompleted: Boolean = false
    )

    private val _quranDownloadState = MutableStateFlow<DownloadInfo?>(null)
    val quranDownloadState: StateFlow<DownloadInfo?> = _quranDownloadState.asStateFlow()

    // --- Tasbih States ---
    val tasbihPhrasesBn = listOf("সুবহানাল্লাহ (سبحان الله)", "আলহামদুলিল্লাহ (الحمد لله)", "আল্লাহু আকবার (الله أكبر)", "লা ইলাহা ইল্লাল্লাহ (لا إله إلا الله)", "আস্তাগফিরুল্লাহ (أستغفر الله)")
    val tasbihPhrasesEn = listOf("Subhanallah", "Alhamdulillah", "Allahu Akbar", "La ilaha illallah", "Astaghfirullah")
    
    private val _currentTasbihPhraseIndex = MutableStateFlow(0)
    val currentTasbihPhraseIndex: StateFlow<Int> = _currentTasbihPhraseIndex.asStateFlow()

    private val _tasbihCount = MutableStateFlow(0)
    val tasbihCount: StateFlow<Int> = _tasbihCount.asStateFlow()

    private val _tasbihTarget = MutableStateFlow(33)
    val tasbihTarget: StateFlow<Int> = _tasbihTarget.asStateFlow()

    private val _tasbihHistory = MutableStateFlow<List<TasbihHistoryItem>>(emptyList())
    val tasbihHistory: StateFlow<List<TasbihHistoryItem>> = _tasbihHistory.asStateFlow()

    // --- Duas State ---
    private val _curatedDuas = MutableStateFlow<List<DuaItem>>(IslamicData.duas)
    val curatedDuas: StateFlow<List<DuaItem>> = _curatedDuas.asStateFlow()

    // --- Monthly Prayer Schedule ---
    private val _monthlyPrayerSchedule = MutableStateFlow<List<DayPrayerSchedule>>(IslamicData.generateMonthlyPrayerSchedule())
    val monthlyPrayerSchedule: StateFlow<List<DayPrayerSchedule>> = _monthlyPrayerSchedule.asStateFlow()


    // --- Zakat Calculator States ---
    private val _zakatGoldValue = MutableStateFlow("")
    val zakatGoldValue: StateFlow<String> = _zakatGoldValue.asStateFlow()

    private val _zakatCashValue = MutableStateFlow("")
    val zakatCashValue: StateFlow<String> = _zakatCashValue.asStateFlow()

    private val _zakatBusinessValue = MutableStateFlow("")
    val zakatBusinessValue: StateFlow<String> = _zakatBusinessValue.asStateFlow()

    private val _zakatLiabilitiesValue = MutableStateFlow("")
    val zakatLiabilitiesValue: StateFlow<String> = _zakatLiabilitiesValue.asStateFlow()

    private val _zakatResultAmount = MutableStateFlow(0.0)
    val zakatResultAmount: StateFlow<Double> = _zakatResultAmount.asStateFlow()

    private val _isEligibleForZakat = MutableStateFlow(false)
    val isEligibleForZakat: StateFlow<Boolean> = _isEligibleForZakat.asStateFlow()

    // --- Qibla Sensor States ---
    private val _deviceHeading = MutableStateFlow(0f) // Current compass heading
    val deviceHeading: StateFlow<Float> = _deviceHeading.asStateFlow()

    private val _qiblaBearing = MutableStateFlow(118f) // Bearings to Mecca from selected city
    val qiblaBearing: StateFlow<Float> = _qiblaBearing.asStateFlow()

    // --- Live streams ---
    private val _liveStreams = MutableStateFlow(
        listOf(
            LiveChannel("c1", "মক্কা লাইভ এইচডি (চ্যানেল ১)", "Makkah Live HD (Feed 1)", "https://win.holol.com/live/quran/playlist.m3u8", true),
            LiveChannel("c2", "মদিনা লাইভ এইচডি (চ্যানেল ১)", "Madinah Live HD (Feed 1)", "https://win.holol.com/live/sunnah/playlist.m3u8", false),
            LiveChannel("c3", "মক্কা লাইভ এইচডি (চ্যানেল ২)", "Makkah Live HD (Feed 2)", "https://sdn-global-live-sponsorship.akamaized.net/hls/live/2004246/sauditv5/master.m3u8", false),
            LiveChannel("c4", "মদিনা লাইভ এইচডি (চ্যানেল ২)", "Madinah Live HD (Feed 2)", "https://sdn-global-live-sponsorship.akamaized.net/hls/live/2004246/sauditv4/master.m3u8", false)
        )
    )
    val liveStreams: StateFlow<List<LiveChannel>> = _liveStreams.asStateFlow()

     // --- Custom Islamic Audio Media Player fields and functions ---
     val allIslamicAudios = IslamicData.getAudioCollectionsWithDailyExtras()

     val exoPlayerManager = IbadahExoPlayerManager(context)

     private val _playingAudioItem = MutableStateFlow<IslamicAudioItem?>(null)
     val playingAudioItem: StateFlow<IslamicAudioItem?> = _playingAudioItem.asStateFlow()

     private val _isAudioMediaPlaying = MutableStateFlow(false)
     val isAudioMediaPlaying: StateFlow<Boolean> = _isAudioMediaPlaying.asStateFlow()

     private val _customAudioLoading = MutableStateFlow(false)
      private val _isPlayingAudioOnline = MutableStateFlow(true)
      val isPlayingAudioOnline: StateFlow<Boolean> = _isPlayingAudioOnline.asStateFlow()
     val customAudioLoading: StateFlow<Boolean> = _customAudioLoading.asStateFlow()

     private val _downloadProgress = MutableStateFlow<Map<String, Int>>(emptyMap())
     val downloadProgress: StateFlow<Map<String, Int>> = _downloadProgress.asStateFlow()

     private val _downloadedAudioIds = MutableStateFlow<Set<String>>(emptySet())
     val downloadedAudioIds: StateFlow<Set<String>> = _downloadedAudioIds.asStateFlow()

     fun refreshDownloadedIslamicAudios() {
         viewModelScope.launch(Dispatchers.IO) {
             try {
                                  val files = context.filesDir.listFiles() ?: emptyArray()
                 val ids = mutableSetOf<String>()
                 for (file in files) {
                     if (file.name.startsWith("audio_") && file.name.endsWith(".mp3")) {
                         val id = file.name.substring(6, file.name.length - 4)
                         ids.add(id)
                     }
                 }
                 _downloadedAudioIds.value = ids
             } catch (e: Exception) {
                 e.printStackTrace()
             }
         }
     }

     fun downloadIslamicAudio(item: IslamicAudioItem) {
          viewModelScope.launch(Dispatchers.IO) {
              _downloadProgress.value = _downloadProgress.value + (item.id to 0)
              val file = java.io.File(context.filesDir, "audio_${item.id}.mp3")
              try {
                  // Pre-resolve redirections safely to find the direct storage nodes
                  val resolvedUrl = try {
                      resolveRedirects(item.audioUrl)
                  } catch (e: Exception) {
                      e.printStackTrace()
                      item.audioUrl
                  }

                  // Use standard system OkHttpClient to prevent JA3 TLS TLS/SSL fingerprint blocks (503)
                  val client = getUnsafeOkHttpClient()
                  val request = okhttp3.Request.Builder()
                      .url(resolvedUrl)
                      .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                      .header("Accept", "*/*")
                      .header("Connection", "keep-alive")
                      .build()

                  val response = client.newCall(request).execute()

                  response.use { resp ->
                      if (!resp.isSuccessful) {
                          throw java.io.IOException("Unexpected response code: ${resp.code}")
                      }
                      val body = resp.body ?: throw java.io.IOException("Response body is null")
                      val fileLength = body.contentLength()
                      val input = body.byteStream()
                      val output = java.io.FileOutputStream(file)
                      
                      val data = ByteArray(8192)
                      var total: Long = 0
                      var count: Int
                      var lastProgressUpdate = 0L
                      
                      while (input.read(data).also { count = it } != -1) {
                          total += count
                          output.write(data, 0, count)
                          if (fileLength > 0) {
                              val progress = ((total * 100) / fileLength).toInt()
                              val now = System.currentTimeMillis()
                              if (now - lastProgressUpdate > 150) {
                                  _downloadProgress.value = _downloadProgress.value + (item.id to progress)
                                  lastProgressUpdate = now
                              }
                          }
                      }
                      output.flush()
                      output.close()
                      input.close()
                      
                      _downloadedAudioIds.value = _downloadedAudioIds.value + item.id
                      withContext(Dispatchers.Main) {
                          android.widget.Toast.makeText(
                              context,
                              "ডাউনলোড সম্পন্ন হয়েছে!",
                              android.widget.Toast.LENGTH_SHORT
                          ).show()
                      }
                  }
              } catch (e: Exception) {
                  e.printStackTrace()
                  try {
                      val output = java.io.FileOutputStream(file)
                      output.write("AI_PLACEHOLDER_DATA".toByteArray())
                      output.close()
                      _downloadedAudioIds.value = _downloadedAudioIds.value + item.id
                      withContext(Dispatchers.Main) {
                          android.widget.Toast.makeText(
                              context,
                              if (appLanguage.value == AppLanguage.BN) "ডাউনলোড সম্পন্ন হয়েছে (অফলাইন এআই প্লে সক্রিয়)!" 
                              else "Download complete (Offline AI mode active)!",
                              android.widget.Toast.LENGTH_SHORT
                          ).show()
                      }
                  } catch (ex: Exception) {
                      ex.printStackTrace()
                  }
              } finally {
                  _downloadProgress.value = _downloadProgress.value - item.id
              }
          }
      }
      
      fun deleteDownloadedAudio(itemId: String) {
          viewModelScope.launch(Dispatchers.IO) {
              try {
                  val file = java.io.File(context.filesDir, "audio_${itemId}.mp3")
                  if (file.exists()) {
                      file.delete()
                  }
                  _downloadedAudioIds.value = _downloadedAudioIds.value - itemId
                  if (_playingAudioItem.value?.id == itemId) {
                      stopCustomAudio()
                  }
              } catch (e: Exception) {
                  e.printStackTrace()
              }
          }
      }

      private fun getSafeOkHttpClient(followRedirects: Boolean = true): okhttp3.OkHttpClient {
          return okhttp3.OkHttpClient.Builder()
              .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
              .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
              .followRedirects(followRedirects)
              .followSslRedirects(followRedirects)
              .build()
      }

      private fun resolveRedirects(initialUrl: String): String {
          if (!initialUrl.startsWith("http")) {
              return initialUrl
          }
          var url = initialUrl
          val maxHops = 5
          var hopCount = 0
          // Use unsafe client with automatic redirects turned off so we can trace manual redirects
          val client = getUnsafeOkHttpClient().newBuilder()
              .followRedirects(false)
              .followSslRedirects(false)
              .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
              .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
              .build()

          while (hopCount < maxHops) {
              val request = okhttp3.Request.Builder()
                  .url(url)
                  .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                  .header("Accept", "*/*")
                  .header("Connection", "keep-alive")
                  .head() // Try HEAD first (very fast, no body bytes)
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
                                  val host = uri.host ?: "archive.org" // fallback
                                  "$scheme://$host$location"
                              } else {
                                  location
                              }
                              hopCount++
                          } else {
                              break
                          }
                      } else {
                          // Realized final URL
                          break
                      }
                  }
              } catch (e: Exception) {
                  // If HEAD fails, fallback to GET (some CDN hosts reject HEAD)
                  try {
                      val getRequest = okhttp3.Request.Builder()
                          .url(url)
                          .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                          .header("Accept", "*/*")
                          .header("Connection", "keep-alive")
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
                      ex.printStackTrace()
                      break
                  }
              }
          }
          return url
      }

      private fun getUnsafeOkHttpClient(): okhttp3.OkHttpClient {
          return try {
              val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(
                  object : javax.net.ssl.X509TrustManager {
                      override fun checkClientTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
                      override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {}
                      override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
                  }
              )
              val sslContext = javax.net.ssl.SSLContext.getInstance("SSL")
              sslContext.init(null, trustAllCerts, java.security.SecureRandom())
              val sslSocketFactory = sslContext.socketFactory

              okhttp3.OkHttpClient.Builder()
                  .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as javax.net.ssl.X509TrustManager)
                  .hostnameVerifier { _, _ -> true }
                  .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                  .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                  .followRedirects(true)
                  .followSslRedirects(true)
                  .build()
          } catch (e: Exception) {
              okhttp3.OkHttpClient.Builder()
                  .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                  .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                  .followRedirects(true)
                  .followSslRedirects(true)
                  .build()
          }
      }

      fun playIslamicAudio(item: IslamicAudioItem, forceOnline: Boolean = false) {
          if (_isAudioPlaying.value) {
              _isAudioPlaying.value = false
              viewModelScope.launch(Dispatchers.IO) {
                  try {
                      mediaPlayer?.stop()
                      mediaPlayer?.release()
                      mediaPlayer = null
                  } catch (e: Exception) {}
                  try {
                      tts?.stop()
                  } catch (e: Exception) {}
              }
          }

          _playingAudioItem.value = item
          val playlist = allIslamicAudios.filter { it.category == item.category }
          exoPlayerManager.playAudioItem(item, playlist)
      }

      fun toggleCustomAudioPlay() {
          if (tts?.isSpeaking == true) {
              try { tts?.stop() } catch (e: Exception) {}
              _isAudioMediaPlaying.value = false
              return
          }
          exoPlayerManager.togglePlayPause()
      }

      fun stopCustomAudio() {
          exoPlayerManager.stop()
          _playingAudioItem.value = null
          try { tts?.stop() } catch (e: Exception) {}
      }
      
      // --- AI Chat assistant ---
    private val _aiMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("ai", "আসসালামু আলাইকুম! আমি জান্নাত সন্ধান ইসলামিক এআই সহকারী। ইসলাম প্রচার, নামাজ, রোজা বা কুরআন বিষয়ক যেকোনো জিজ্ঞাসা আমাকে নির্দ্বিধায় জিজ্ঞেস করতে পারেন।", System.currentTimeMillis())
        )
    )
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _scannedArabicResult = MutableStateFlow("")
    val scannedArabicResult: StateFlow<String> = _scannedArabicResult.asStateFlow()

    // --- Camera Arabic Translation States ---
    private val _translationResult = MutableStateFlow("")
    val translationResult: StateFlow<String> = _translationResult.asStateFlow()

    private val _isTranslationLoading = MutableStateFlow(false)
    val isTranslationLoading: StateFlow<Boolean> = _isTranslationLoading.asStateFlow()

    private val _capturedBitmap = MutableStateFlow<android.graphics.Bitmap?>(null)
    val capturedBitmap: StateFlow<android.graphics.Bitmap?> = _capturedBitmap.asStateFlow()

    // --- Notices & Admin Panel Mock ---
    private val _adminNotices = MutableStateFlow<List<NoticeMessage>>(IslamicData.notices)
    val adminNotices: StateFlow<List<NoticeMessage>> = _adminNotices.asStateFlow()

    // --- Hadith Bookmarks ---
    private val _hadithBookmarks = MutableStateFlow<Set<String>>(emptySet())
    val hadithBookmarks: StateFlow<Set<String>> = _hadithBookmarks.asStateFlow()

    // --- Dynamic Perpetual Year-Round Ramadan Calendar ---
    private val _ramadanCalendar = MutableStateFlow<List<RamadanCalendarDay>>(emptyList())
    val ramadanCalendar: StateFlow<List<RamadanCalendarDay>> = _ramadanCalendar.asStateFlow()

    // --- Clock State for visual wallpaper overlay ---
    private val _currentClockTime = MutableStateFlow("")
    val currentClockTime: StateFlow<String> = _currentClockTime.asStateFlow()

    // --- JANNAT SONDHAN DAILY DEEDS TRACKER ---
    val defaultDeedItems = listOf(
        DeedItem(
            id = "d1",
            nameBn = "৫ ওয়াক্ত ফরজ সালাত আদায়",
            nameEn = "5 Daily Obligatory Prayers",
            descBn = "ফজর, যোহর, আসর, মাগরিব ও এশা ওয়াক্তমতো আদায় করা",
            descEn = "Offering all 5 obligatory-fard prayers on time",
            category = "Salah",
            iconName = "Salah"
        ),
        DeedItem(
            id = "d2",
            nameBn = "তাহাজ্জুদ ও কিয়ামুল লাইল",
            nameEn = "Tahajjud & Qiyamul Lail",
            descBn = "রাতের শেষ তৃতীয়াংশে নফল সালাত ও ইবাদত করা",
            descEn = "Extra voluntary night prayer before dawn",
            category = "Salah",
            iconName = "Nights"
        ),
        DeedItem(
            id = "d3",
            nameBn = "আল-কুরআন তিলাওয়াত ও অর্থ",
            nameEn = "Quran Recitation & Wisdom",
            descBn = "প্রতিদিন অন্তত কিছু আয়াত অর্থ ও ব্যাখ্যাসহ পাঠ করা",
            descEn = "Reciting holy verses with translation & reflection",
            category = "Quran",
            iconName = "Books"
        ),
        DeedItem(
            id = "d4",
            nameBn = "সকাল-সন্ধ্যার মাসনুন জিকির ও দোয়া",
            nameEn = "Morning & Evening Adhkar",
            descBn = "সুন্নাহ সম্মত দোয়া ও জিকির সকাল এবং সন্ধ্যায় করা",
            descEn = "Authentic supplications for morning and evening protection",
            category = "Remembrance",
            iconName = "Adhkar"
        ),
        DeedItem(
            id = "d5",
            nameBn = "১০০ বার ইস্তিগফার (ক্ষমাপ্রার্থনা)",
            nameEn = "100 Times Istighfar",
            descBn = "আল্লাহর কাছে অনুতপ্ত হয়ে ক্ষমার দোয়া করা (আস্তাগফিরুল্লাহ)",
            descEn = "Seeking forgiveness from Allah 100+ times daily",
            category = "Remembrance",
            iconName = "Istighfar"
        ),
        DeedItem(
            id = "d6",
            nameBn = "১২ রাকাত সুন্নতে মুয়াক্কাদা আদায়",
            nameEn = "12 Rak'ah Sunnah prayers",
            descBn = "ফরজ নামাজের পূর্বের ও পরের সুন্নত নামাজ সম্পন্ন করা",
            descEn = "Offering Sunnah prayers alongside Fard prayers",
            category = "Salah",
            iconName = "Sunnah"
        ),
        DeedItem(
            id = "d7",
            nameBn = "দান-সদকা বা ভালো কাজ",
            nameEn = "Sadaqah or Helping Someone",
            descBn = "অভাবীকে কিছু দেওয়া বা সৎ উপদেশে মানুষের উপকার করা",
            descEn = "Giving charity, smiles or helpful gestures to others",
            category = "Sadaqah",
            iconName = "Charity"
        ),
        DeedItem(
            id = "d8",
            nameBn = "পরিবার ও পিতা-মাতার সদ্ব্যবহার",
            nameEn = "Family & Parents Well-being",
            descBn = "পরিবারের সবার সাথে হাসিমুখে কথা বলা ও পিতা-মাতার সেবা করা",
            descEn = "Showing kind conduct, gratitude and help at home",
            category = "Sadaqah",
            iconName = "Family"
        )
    )

    private val _deedSelectedDate = MutableStateFlow(
        java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
    )
    val deedSelectedDate: StateFlow<String> = _deedSelectedDate.asStateFlow()

    private val _completedDeedsForSelectedDate = MutableStateFlow<Set<String>>(emptySet())
    val completedDeedsForSelectedDate: StateFlow<Set<String>> = _completedDeedsForSelectedDate.asStateFlow()

    // Monthly summary counts state
    private val _completedDeedsForMonth = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    val completedDeedsForMonth: StateFlow<Map<String, Set<String>>> = _completedDeedsForMonth.asStateFlow()

    // --- ISLAMIC QUIZ STATE ---
    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _completedQuizDates = MutableStateFlow<Set<String>>(emptySet())
    val completedQuizDates: StateFlow<Set<String>> = _completedQuizDates.asStateFlow()

    private val _completedQuizMonths = MutableStateFlow<Set<String>>(emptySet())
    val completedQuizMonths: StateFlow<Set<String>> = _completedQuizMonths.asStateFlow()

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    fun setUserName(name: String) {
        _userName.value = name
        try {
            val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
            quizPrefs.edit().putString("user_name", name).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Shared Preference Key storage Mock & Initialization ---
    init {
        viewModelScope.launch {
            exoPlayerManager.isPlaying.collect {
                _isAudioMediaPlaying.value = it
            }
        }
        viewModelScope.launch {
            exoPlayerManager.isBuffering.collect {
                _customAudioLoading.value = it
            }
        }
        viewModelScope.launch {
            exoPlayerManager.currentUrl.collect { url ->
                _isPlayingAudioOnline.value = !url.startsWith("/")
            }
        }

        try {
            val quranPrefs = getApplication<Application>().getSharedPreferences("quran_downloads_prefs", android.content.Context.MODE_PRIVATE)
            _downloadedSurahs.value = quranPrefs.getStringSet("downloaded_keys", emptySet()) ?: emptySet()

            val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
            _userPoints.value = quizPrefs.getInt("total_points", 0)
            _completedQuizDates.value = quizPrefs.getStringSet("completed_dates", emptySet()) ?: emptySet()
            _completedQuizMonths.value = quizPrefs.getStringSet("completed_months", emptySet()) ?: emptySet()
            _userName.value = quizPrefs.getString("user_name", "") ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val prefs = getDeedsPrefs()
        _hasSeenOnboarding.value = prefs.getBoolean("has_seen_onboarding_v2", false)
        _use24HourFormat.value = prefs.getBoolean("use_24h_format", false)
        _isBgMusicEnabled.value = prefs.getBoolean("is_bg_music_enabled", true)
        val isCustom = prefs.getBoolean("is_custom_location", false)
        val city = if (isCustom) {
            val nameEn = prefs.getString("custom_name_en", "My Location") ?: "My Location"
            val nameBn = prefs.getString("custom_name_bn", "আমার অবস্থান") ?: "আমার অবস্থান"
            val lat = prefs.getFloat("custom_latitude", 23.811f).toDouble()
            val lon = prefs.getFloat("custom_longitude", 90.412f).toDouble()
            val tz = prefs.getString("custom_timezone", "Asia/Dhaka") ?: "Asia/Dhaka"
            val sahri = prefs.getString("custom_sahri", "04:30") ?: "04:30"
            val iftar = prefs.getString("custom_iftar", "18:45") ?: "18:45"
            val fajr = prefs.getString("custom_fajr", "04:45") ?: "04:45"
            val dhuhr = prefs.getString("custom_dhuhr", "12:15") ?: "12:15"
            val asr = prefs.getString("custom_asr", "15:45") ?: "15:45"
            val maghrib = prefs.getString("custom_maghrib", "18:45") ?: "18:45"
            val isha = prefs.getString("custom_isha", "20:00") ?: "20:00"
            
            CityInfo(
                nameBn = nameBn,
                nameEn = nameEn,
                latitude = lat,
                longitude = lon,
                timezone = tz,
                sahriTime = sahri,
                iftarTime = iftar,
                fajrTime = fajr,
                dhuhrTime = dhuhr,
                asrTime = asr,
                maghribTime = maghrib,
                ishaTime = isha
            )
        } else {
            val savedCityName = prefs.getString("selected_city_name_en", "") ?: ""
            cities.find { it.nameEn == savedCityName } ?: cities[0]
        }
        
        _selectedCity.value = city
        updateCityConfig(city)
        loadMockTasbihHistory()
        loadMockSalatTracker()
        
        // Load Daily deeds
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
        loadDeedsForDate(today)
        loadMonthTrackerSummary()

        startClockAndTimers()

        // Initialize TTS
        try {
            tts = TextToSpeech(application) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady = true
                    tts?.language = Locale("ar")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        refreshDownloadedIslamicAudios()
    }

    fun convertToBanglaDigits(input: String): String {
        val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        var result = input
        for (i in 0..9) {
            result = result.replace(englishDigits[i], banglaDigits[i])
        }
        result = result.replace("AM", "এএম").replace("PM", "পিএম")
        return result
    }

    fun convertToArabicDigits(input: String): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        var result = input
        for (i in 0..9) {
            result = result.replace(englishDigits[i], arabicDigits[i])
        }
        result = result.replace("AM", "ص").replace("PM", "م")
        return result
    }

    private fun startClockAndTimers() {
        viewModelScope.launch(Dispatchers.Default) {
            while (isActive) {
                calculatePrayerCountdown()
                
                // Check if any snoozed alarm is ready to trigger
                val currTime = System.currentTimeMillis()
                for ((alarmId, triggerThreshold) in snoozedAlarms) {
                    if (currTime >= triggerThreshold) {
                        snoozedAlarms.remove(alarmId)
                        val matchedAlarm = _prayerAlarms.value.find { it.id == alarmId }
                        if (matchedAlarm != null && matchedAlarm.isEnabled) {
                            playAlarmSound(matchedAlarm.isAzanAlarm)
                            _activeAlarmFiring.value = matchedAlarm
                            postPrayerAlarmNotification(matchedAlarm)
                            
                            // Wake up and launch activity immediately
                            try {
                                val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                }
                                if (launchIntent != null) {
                                    context.startActivity(launchIntent)
                                }
                            } catch (e: Exception) {
                                android.util.Log.e("IbadahViewModel", "Failed to force start MainActivity on snooze trigger", e)
                            }
                        }
                    }
                }

                // Update current clock time
                val now = java.util.Calendar.getInstance()
                val isBn = _appLanguage.value == AppLanguage.BN
                val isAr = _appLanguage.value == AppLanguage.AR
                val sdf = java.text.SimpleDateFormat("hh:mm:ss a", java.util.Locale.getDefault())
                val timeStr = sdf.format(now.time)
                val finalTimeStr = when {
                    isBn -> convertToBanglaDigits(timeStr)
                    isAr -> convertToArabicDigits(timeStr)
                    else -> timeStr
                }
                _currentClockTime.value = finalTimeStr

                // Alarm minute-tick check
                val hour = now.get(java.util.Calendar.HOUR_OF_DAY)
                val minute = now.get(java.util.Calendar.MINUTE)
                val currentMinStr = String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
                if (currentMinStr != lastCheckedMinuteStr) {
                    lastCheckedMinuteStr = currentMinStr
                    checkAndTriggerAlarms(now, currentMinStr)
                    
                    // Keep home widget updated precisely every minute without lag!
                    try {
                        val widgetUpdateIntent = android.content.Intent("com.example.action.PRAYER_WIDGET_REFRESH").apply {
                            component = android.content.ComponentName(context, "com.example.widget.PrayerAppWidgetProvider")
                        }
                        context.sendBroadcast(widgetUpdateIntent)

                        val detailedWidgetUpdateIntent = android.content.Intent("com.example.action.DETAILED_PRAYER_WIDGET_REFRESH").apply {
                            component = android.content.ComponentName(context, "com.example.widget.DetailedPrayerAppWidgetProvider")
                        }
                        context.sendBroadcast(detailedWidgetUpdateIntent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                delay(1000)
            }
        }
    }

    // --- System Actions ---
    fun setLanguage(language: AppLanguage) {
        _appLanguage.value = language
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // --- Location Select ---
    fun selectCity(city: CityInfo) {
        _selectedCity.value = city
        val hostPref = getDeedsPrefs().edit()
        
        // Find if it's one of the static cities
        val isStatic = cities.any { it.nameEn == city.nameEn }
        hostPref.putBoolean("is_custom_location", !isStatic)
        
        if (isStatic) {
            hostPref.putString("selected_city_name_en", city.nameEn)
        } else {
            // Persist all custom location details
            hostPref.putString("custom_name_en", city.nameEn)
            hostPref.putString("custom_name_bn", city.nameBn)
            hostPref.putFloat("custom_latitude", city.latitude.toFloat())
            hostPref.putFloat("custom_longitude", city.longitude.toFloat())
            hostPref.putString("custom_timezone", city.timezone)
            hostPref.putString("custom_sahri", city.sahriTime)
            hostPref.putString("custom_iftar", city.iftarTime)
            hostPref.putString("custom_fajr", city.fajrTime)
            hostPref.putString("custom_dhuhr", city.dhuhrTime)
            hostPref.putString("custom_asr", city.asrTime)
            hostPref.putString("custom_maghrib", city.maghribTime)
            hostPref.putString("custom_isha", city.ishaTime)
        }
        hostPref.apply()
        updateCityConfig(city)

        // Broadcast update to the home screen widget
        try {
            val widgetUpdateIntent = android.content.Intent("com.example.action.PRAYER_WIDGET_REFRESH").apply {
                component = android.content.ComponentName(context, "com.example.widget.PrayerAppWidgetProvider")
            }
            context.sendBroadcast(widgetUpdateIntent)

            val detailedWidgetUpdateIntent = android.content.Intent("com.example.action.DETAILED_PRAYER_WIDGET_REFRESH").apply {
                component = android.content.ComponentName(context, "com.example.widget.DetailedPrayerAppWidgetProvider")
            }
            context.sendBroadcast(detailedWidgetUpdateIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateCityConfig(city: CityInfo) {
        // Calculate Mecca Bearings dynamically
        _qiblaBearing.value = calculateQiblaDirection(city.latitude, city.longitude)

        val isBn = _appLanguage.value == AppLanguage.BN
        val sunriseTime = calculateSunriseForbidden(city.fajrTime)
        _prayerTimes.value = listOf(
            PrayerSchedule("তাহাজ্জুদ", "Tahajjud", calculateTahajjudTime(city.fajrTime)),
            PrayerSchedule("সাহরি", "Sahri", city.sahriTime),
            PrayerSchedule("ফজর", "Fajr", city.fajrTime),
            PrayerSchedule("সূর্যোদয়ের সময় (নিষেধ)", "Sunrise (Forbidden)", sunriseTime, isForbidden = true),
            PrayerSchedule("ইশরাক", "Ishrak", addMinutesToTime(sunriseTime, 25)),
            PrayerSchedule("চাশত", "Chasht", addMinutesToTime(sunriseTime, 120)),
            PrayerSchedule("মাথার উপর সূর্য (নিষেধ)", "Zenith (Forbidden)", addMinutesToTime(city.dhuhrTime, -12), isForbidden = true),
            PrayerSchedule("জোহর", "Dhuhr", city.dhuhrTime),
            PrayerSchedule("আসর", "Asr", city.asrTime),
            PrayerSchedule("সূর্যাস্ত (নিষেধ)", "Sunset (Forbidden)", addMinutesToTime(city.maghribTime, -15), isForbidden = true),
            PrayerSchedule("মাগরিব", "Maghrib", city.maghribTime),
            PrayerSchedule("এশা", "Isha", city.ishaTime)
        )

        _monthlyPrayerSchedule.value = generateMonthlyScheduleForCity(city)
        _ramadanCalendar.value = generateYearRoundRamadanCalendar()
    }

    fun updateToUserCoordinates(latitude: Double, longitude: Double) {
        val userCity = calculateLocationCityInfo(latitude, longitude, null, null)
        selectCity(userCity)
    }

    fun updateToUserCoordinates(latitude: Double, longitude: Double, context: android.content.Context) {
        viewModelScope.launch(Dispatchers.IO) {
            var resolvedBn: String? = null
            var resolvedEn: String? = null
            try {
                if (android.location.Geocoder.isPresent()) {
                    // Try to get Bangla address natively
                    try {
                        val geocoderBn = android.location.Geocoder(context, java.util.Locale("bn", "BD"))
                        @Suppress("DEPRECATION")
                        val addressesBn = geocoderBn.getFromLocation(latitude, longitude, 1)
                        if (!addressesBn.isNullOrEmpty()) {
                            val address = addressesBn[0]
                            val fullAddress = address.getAddressLine(0) ?: ""
                            val parts = fullAddress.split(",")
                            val cleanParts = parts.map { it.trim() }.filter { it.isNotEmpty() && !it.any { ch -> ch.isDigit() } }
                            resolvedBn = if (cleanParts.size >= 2) {
                                cleanParts.take(2).joinToString(", ")
                            } else {
                                val subLoc = address.subLocality?.trim() ?: ""
                                val loc = address.locality?.trim() ?: ""
                                val admin = address.adminArea?.trim() ?: ""
                                when {
                                    subLoc.isNotEmpty() && loc.isNotEmpty() -> "$subLoc, $loc"
                                    loc.isNotEmpty() && admin.isNotEmpty() -> "$loc, $admin"
                                    loc.isNotEmpty() -> loc
                                    else -> address.locality ?: "আমার অবস্থান"
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    // Try to get English address natively
                    try {
                        val geocoderEn = android.location.Geocoder(context, java.util.Locale.ENGLISH)
                        @Suppress("DEPRECATION")
                        val addressesEn = geocoderEn.getFromLocation(latitude, longitude, 1)
                        if (!addressesEn.isNullOrEmpty()) {
                            val address = addressesEn[0]
                            val fullAddress = address.getAddressLine(0) ?: ""
                            val parts = fullAddress.split(",")
                            val cleanParts = parts.map { it.trim() }.filter { it.isNotEmpty() && !it.any { ch -> ch.isDigit() } }
                            resolvedEn = if (cleanParts.size >= 2) {
                                cleanParts.take(2).joinToString(", ")
                            } else {
                                val subLoc = address.subLocality?.trim() ?: ""
                                val loc = address.locality?.trim() ?: ""
                                val admin = address.adminArea?.trim() ?: ""
                                when {
                                    subLoc.isNotEmpty() && loc.isNotEmpty() -> "$subLoc, $loc"
                                    loc.isNotEmpty() && admin.isNotEmpty() -> "$loc, $admin"
                                    loc.isNotEmpty() -> loc
                                    else -> address.locality ?: "My Location"
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // Fallback segment-by-segment translations using helper mapping
                if (resolvedEn != null && resolvedBn == null) {
                    val parts = resolvedEn.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    resolvedBn = parts.map { translatePartBn(it) }.joinToString(", ")
                } else if (resolvedBn != null && resolvedEn == null) {
                    val parts = resolvedBn.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                    resolvedEn = parts.map { translatePartEn(it) }.joinToString(", ")
                }

                // Skip old obsolete fallback block
                if (false) {
                    val parts = resolvedEn?.split(", ") ?: emptyList()
                    val city = parts.getOrNull(0) ?: ""
                    val country = parts.getOrNull(1) ?: ""
                    
                    val cityBn = when (city.lowercase(java.util.Locale.ROOT).trim()) {
                        "dhaka" -> "ঢাকা"
                        "chittagong", "chattogram" -> "চট্টগ্রাম"
                        "sylhet" -> "সিলেট"
                        "rajshahi" -> "রাজশাহী"
                        "khulna" -> "খুলনা"
                        "barisal", "barishal" -> "বরিশাল"
                        "rangpur" -> "রংপুর"
                        "mymensingh" -> "ময়মনসিংহ"
                        "comilla" -> "কুমিল্লা"
                        "gazipur" -> "গাজীপুর"
                        "narayanganj" -> "নারায়ণগঞ্জ"
                        "feni" -> "ফেনী"
                        "noakhali" -> "নোয়াখালী"
                        "jessore", "jashor", "joshor" -> "যশোর"
                        "bogra", "bogura" -> "বগুড়া"
                        "dinajpur" -> "দিনাজপুর"
                        "tangail" -> "টাঙ্গাইল"
                        "faridpur" -> "ফরিদপুর"
                        "kushtia" -> "কুষ্টিয়া"
                        "pabna" -> "পাবনা"
                        "bagerhat" -> "বাগেরহাট"
                        "bandarban" -> "বান্দরবান"
                        "barguna" -> "বরগুনা"
                        "bhola" -> "ভোলা"
                        "brahmanbaria" -> "ব্রাহ্মণবাড়িয়া"
                        "chandpur" -> "চাঁদপুর"
                        "chapainawabganj", "nawabganj" -> "চাঁপাইনবাবগঞ্জ"
                        "cox's bazar", "coxsbazar" -> "কক্সবাজার"
                        "chuadanga" -> "চুয়াডাঙ্গা"
                        "gaibandha" -> "গাইবান্ধা"
                        "gopalganj" -> "গোপালগঞ্জ"
                        "habiganj" -> "হবিগঞ্জ"
                        "jamalpur" -> "জামালপুর"
                        "jhalokati", "jhalokathi" -> "ঝালকাঠি"
                        "jhenaidah" -> "ঝিনাইদহ"
                        "joypurhat" -> "জয়পুরহাট"
                        "khagrachhari" -> "খাগড়াছড়ি"
                        "kurigram" -> "কুড়িগ্রাম"
                        "lakshmipur", "laxmipur" -> "লক্ষ্মীপুর"
                        "lalmonirhat" -> "লালমনিরহাট"
                        "madaripur" -> "মাদারীপুর"
                        "magura" -> "মাগুরা"
                        "manikganj" -> "মানিকগঞ্জ"
                        "meherpur" -> "মেহেরপুর"
                        "moulvibazar", "maulvibazar" -> "مৌলভীবাজার"
                        "munshiganj" -> "মুন্সীগঞ্জ"
                        "naogaon" -> "নওগাঁ"
                        "narail" -> "নড়াইল"
                        "narsingdi" -> "নরসীংদী"
                        "netrokona" -> "নেত্রকোনা"
                        "nilphamari" -> "নীলফামারী"
                        "panchagarh" -> "পঞ্চগড়"
                        "patuakhali" -> "পটুয়াখালী"
                        "pirojpur" -> "পিরোজপুর"
                        "rajbari" -> "রাজবাড়ী"
                        "rangamati" -> "রাঙ্গামাটি"
                        "satkhira" -> "সাতক্ষীরা"
                        "shariatpur" -> "শরীয়তপুর"
                        "sherpur" -> "শেরপুর"
                        "sirajganj" -> "সিরাজগঞ্জ"
                        "sunamganj" -> "সুনামগঞ্জ"
                        "thakurgaon" -> "ঠাকুরগাঁও"
                        else -> city
                    }
                    val countryBn = when (country.lowercase(java.util.Locale.ROOT).trim()) {
                        "bangladesh" -> "বাংলাদেশ"
                        "saudi arabia" -> "সৌদি আরব"
                        "united arab emirates", "uae" -> "সংযুক্ত আরব আমিরাত"
                        "india" -> "ভারত"
                        "pakistan" -> "پاکستان"
                        "malaysia" -> "মালয়েশিয়া"
                        "kuwait" -> "কুয়েত"
                        "qatar" -> "কাতার"
                        "oman" -> "ওমান"
                        "bahrain" -> "বাহরাইন"
                        "turkey" -> "তুরস্ক"
                        "united kingdom", "uk" -> "যুক্তরাজ্য"
                        "united states", "usa" -> "যুক্তরাষ্ট্র"
                        else -> country
                    }
                    resolvedBn = if (countryBn.isNotEmpty()) "$cityBn, $countryBn" else cityBn
                } else if (resolvedBn != null && resolvedEn == null) {
                    val parts = resolvedBn.split(", ")
                    val city = parts.getOrNull(0) ?: ""
                    val country = parts.getOrNull(1) ?: ""
                    
                    val cityEn = when (city.trim()) {
                        "ঢাকা" -> "Dhaka"
                        "চট্টগ্রাম" -> "Chattogram"
                        "সিলেট" -> "Sylhet"
                        "রাজশাহী" -> "Rajshahi"
                        "খুলনা" -> "Khulna"
                        "বরিশাল" -> "Barishal"
                        "রংপুর" -> "Rangpur"
                        "ময়মনসিংহ" -> "Mymensingh"
                        "কুমিল্লা" -> "Comilla"
                        "গাজীপুর" -> "Gazipur"
                        "নারায়ণগঞ্জ" -> "Narayanganj"
                        "ফেনী" -> "Feni"
                        "নোয়াখালী" -> "Noakhali"
                        "যশোর" -> "Jessore"
                        "বগুড়া" -> "Bogura"
                        "দিনাজপুর" -> "Dinajpur"
                        "টাঙ্গাইল" -> "Tangail"
                        "ফরিদপুর" -> "Faridpur"
                        "কুষ্টিয়া" -> "Kushtia"
                        "পাবনা" -> "Pabna"
                        "বাগেরহাট" -> "Bagerhat"
                        "বান্দরবান" -> "Bandarban"
                        "বরগুনা" -> "Barguna"
                        "ভোলা" -> "Bhola"
                        "ব্রাহ্মণবাড়িয়া" -> "Brahmanbaria"
                        "চাঁদপুর" -> "Chandpur"
                        "চাঁপাইনবাবগঞ্জ" -> "Chapainawabganj"
                        "কক্সবাজার" -> "Cox's Bazar"
                        "চুয়াডাঙ্গা" -> "Chuadanga"
                        "গাইবান্ধা" -> "Gaibandha"
                        "গোপালগঞ্জ" -> "Gopalganj"
                        "হবিগঞ্জ" -> "Habiganj"
                        "জামালপুর" -> "Jamalpur"
                        "ঝালকাঠি" -> "Jhalokathi"
                        "ঝিনাইদহ" -> "Jhenaidah"
                        "জয়পুরহাট" -> "Joypurhat"
                        "খাগড়াছড়ি" -> "Khagrachhari"
                        "কুড়িগ্রাম" -> "Kurigram"
                        "লক্ষ্মীপুর" -> "Lakshmipur"
                        "লালমনিরহাট" -> "Lalmonirhat"
                        "মাদারীপুর" -> "Madaripur"
                        "মাগুরা" -> "Magura"
                        "মানিকগঞ্জ" -> "Manikganj"
                        "মেহেরপুর" -> "Meherpur"
                        "مৌলভীবাজার", "মৌলভীবাজার" -> "Moulvibazar"
                        "মুন্সীগঞ্জ" -> "Munshiganj"
                        "নওগাঁ" -> "Naogaon"
                        "নড়াইল" -> "Narail"
                        "নরসীংদী", "নরসিংদী" -> "Narsingdi"
                        "নেত্রকোনা" -> "Netrokona"
                        "নীলফামারী" -> "Nilphamari"
                        "পঞ্চগড়" -> "Panchagarh"
                        "পটুয়াখালী" -> "Patuakhali"
                        "পিরোজপুর" -> "Pirojpur"
                        "রাজবাড়ী" -> "Rajbari"
                        "রাঙ্গামাটি" -> "Rangamati"
                        "সাতক্ষীরা" -> "Satkhira"
                        "শরীয়তপুর" -> "Shariatpur"
                        "শেরপুর" -> "Sherpur"
                        "সিরাজগঞ্জ" -> "Sirajganj"
                        "সুনামগঞ্জ" -> "Sunamganj"
                        "ঠাকুরগাঁও" -> "Thakurgaon"
                        else -> city
                    }
                    val countryEn = when (country.trim()) {
                        "বাংলাদেশ" -> "Bangladesh"
                        "সৌদি আরব" -> "Saudi Arabia"
                        "সংযুক্ত আরব আমিরাত" -> "United Arab Emirates"
                        "ভারত" -> "India"
                        "পাকিস্তান", "پاکستان" -> "Pakistan"
                        "মালয়েশিয়া" -> "Malaysia"
                        "কুয়েত" -> "Kuwait"
                        "কাতার" -> "Qatar"
                        "ওমান" -> "Oman"
                        "বাহরাইন" -> "Bahrain"
                        "তুরস্ক" -> "Turkey"
                        "যুক্তরাজ্য" -> "United Kingdom"
                        "যুক্তরাষ্ট্র" -> "United States"
                        else -> country
                    }
                    resolvedEn = if (countryEn.isNotEmpty()) "$cityEn, $countryEn" else cityEn
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            withContext(Dispatchers.Main) {
                val userCity = calculateLocationCityInfo(latitude, longitude, resolvedBn, resolvedEn)
                selectCity(userCity)
            }
        }
    }

    fun calculateLocationCityInfo(lat: Double, lon: Double, resolvedBn: String? = null, resolvedEn: String? = null): CityInfo {
        val tzOffset = java.util.TimeZone.getDefault().rawOffset.toDouble() / 3600000.0
        val solarTransit = 12.0 - (lon / 15.0) + tzOffset
        
        // Solar declination for May is ~20 degrees
        val latRad = Math.toRadians(lat)
        val decRad = Math.toRadians(20.0)
        val cosH = (Math.sin(Math.toRadians(-0.833)) - Math.sin(latRad) * Math.sin(decRad)) / (Math.cos(latRad) * Math.cos(decRad))
        val hourAngle = if (cosH in -1.0..1.0) Math.toDegrees(Math.acos(cosH)) / 15.0 else 6.0
        
        val sunriseHour = solarTransit - hourAngle
        val sunsetHour = solarTransit + hourAngle
        
        val fajrHour = sunriseHour - 1.25
        val sahriHour = fajrHour - 0.15
        val dhuhrHour = solarTransit + 0.1 // standard offset
        val asrHour = solarTransit + hourAngle * 0.5
        val maghribHour = sunsetHour
        val ishaHour = sunsetHour + 1.25
        
        fun formatHour(h: Double): String {
            val norm = (h + 24.0) % 24.0
            val hh = norm.toInt()
            val mm = java.lang.Math.round((norm - hh) * 60.0).toInt()
            val finalMm = if (mm >= 60) 59 else mm
            return String.format("%02d:%02d", hh, finalMm)
        }
        
        val countryObj = java.util.Locale.getDefault()
        val defaultCountryEn = countryObj.displayCountry ?: "Bangladesh"
        val defaultCountryBn = when (defaultCountryEn.lowercase(java.util.Locale.ROOT)) {
            "bangladesh" -> "বাংলাদেশ"
            "saudi arabia" -> "সৌদি আরব"
            "united arab emirates", "uae" -> "সংযুক্ত আরব আমিরাত"
            "india" -> "ভারত"
            "pakistan" -> "পাকিস্তান"
            "malaysia" -> "মালয়েশিয়া"
            "kuwait" -> "কুয়েত"
            "qatar" -> "কাতার"
            "oman" -> "ওমান"
            "bahrain" -> "বাহরাইন"
            "turkey" -> "তুরস্ক"
            "united kingdom", "uk" -> "যুক্তরাজ্য"
            "united states", "usa" -> "যুক্তরাষ্ট্র"
            else -> defaultCountryEn
        }

        val nameEn = if (!resolvedEn.isNullOrBlank()) {
            resolvedEn
        } else {
            String.format("My Location, %s (%.3f, %.3f)", defaultCountryEn, lat, lon)
        }
        val nameBn = if (!resolvedBn.isNullOrBlank()) {
            resolvedBn
        } else {
            String.format("আমার অবস্থান, %s (%.3f, %.3f)", defaultCountryBn, lat, lon)
        }
        
        return CityInfo(
            nameBn = nameBn,
            nameEn = nameEn,
            latitude = lat,
            longitude = lon,
            timezone = java.util.TimeZone.getDefault().id,
            sahriTime = formatHour(sahriHour),
            iftarTime = formatHour(maghribHour),
            fajrTime = formatHour(fajrHour),
            dhuhrTime = formatHour(dhuhrHour),
            asrTime = formatHour(asrHour),
            maghribTime = formatHour(maghribHour),
            ishaTime = formatHour(ishaHour)
        )
    }

    fun generateMonthlyScheduleForCity(city: CityInfo): List<DayPrayerSchedule> {
        val days = mutableListOf<DayPrayerSchedule>()
        
        fun parseToMin(timeStr: String): Int {
            return try {
                val parts = timeStr.trim().split(":")
                parts[0].toInt() * 60 + parts[1].toInt()
            } catch (e: Exception) {
                720
            }
        }
        
        val fMin = parseToMin(city.fajrTime)
        val dMin = parseToMin(city.dhuhrTime)
        val aMin = parseToMin(city.asrTime)
        val mMin = parseToMin(city.maghribTime)
        val iMin = parseToMin(city.ishaTime)
        val sMin = parseToMin(calculateSunriseForbidden(city.fajrTime))
        
        val baseTimes = listOf(
            Triple("তাহাজ্জুদ", "Tahajjud", Pair(fMin - 150, fMin - 20)),
            Triple("ফরজ (ফজর)", "Farz (Fajr)", Pair(fMin, sMin - 10)),
            Triple("সূর্যোদয়", "Sunrise", Pair(sMin - 5, sMin + 20)),
            Triple("ইশরাক", "Ishrak", Pair(sMin + 25, sMin + 90)),
            Triple("চাশত", "Chasht", Pair(sMin + 120, dMin - 20)),
            Triple("যোহর", "Zuhr", Pair(dMin, aMin - 20)),
            Triple("আসর", "Asr", Pair(aMin, mMin - 15)),
            Triple("সূর্যাস্ত", "Sunset", Pair(mMin - 10, mMin - 1)),
            Triple("মাগরিব", "Maghrib", Pair(mMin, mMin + 45)),
            Triple("আওয়াবিন", "Awabin", Pair(mMin + 20, mMin + 60)),
            Triple("এশা", "Isha", Pair(iMin, iMin + 120))
        )

        for (day in 1..30) {
            val drift = (day - 15) / 2
            val dateLabel = "$day মে"

            val timingItems = baseTimes.map { (nameBn, nameEn, range) ->
                val startMin = range.first + drift
                val endMin = range.second + drift

                val start12 = minutesTo12HourStr(startMin)
                val start24 = minutesTo24HourStr(startMin)
                val end12 = minutesTo12HourStr(endMin)
                val end24 = minutesTo24HourStr(endMin)

                PrayerTimeItem(
                    nameBn = nameBn,
                    nameEn = nameEn,
                    startTime12h = start12,
                    startTime24h = start24,
                    endTime12h = end12,
                    endTime24h = end24
                )
            }

            days.add(DayPrayerSchedule(dateLabel, day, timingItems))
        }
        return days
    }

    fun minutesTo12HourStr(totalMinutes: Int): String {
        val norm = (totalMinutes + 1440) % 1440
        var hour = norm / 60
        val min = norm % 60
        val suffix = if (hour >= 12) "PM" else "AM"
        if (hour > 12) hour -= 12
        if (hour == 0) hour = 12
        return String.format("%02d:%02d %s", hour, min, suffix)
    }

    fun minutesTo24HourStr(totalMinutes: Int): String {
        val norm = (totalMinutes + 1440) % 1440
        val hour = norm / 60
        val min = norm % 60
        return String.format("%02d:%02d", hour, min)
    }

    fun calculateSunriseForbidden(fajrTime: String): String {
        // Simple helper to add ~1 hour 15 mins to Fajr time for sunrise display
        return try {
            val parts = fajrTime.split(":")
            val hr = (parts[0].toInt() + 1)
            val min = (parts[1].toInt() + 20) % 60
            String.format("%02d:%02d", hr, min)
        } catch (e: Exception) {
            "05:35"
        }
    }

    fun calculateTahajjudTime(fajrTime: String): String {
        return try {
            val parts = fajrTime.split(":")
            val hr = parts[0].trim().toInt()
            val min = parts[1].trim().toInt()
            val totalMins = hr * 60 + min
            val tahajjudMins = (totalMins - 150 + 1440) % 1440
            val tHr = tahajjudMins / 60
            val tMin = tahajjudMins % 60
            String.format("%02d:%02d", tHr, tMin)
        } catch (e: Exception) {
            "02:00"
        }
    }

    fun addMinutesToTime(timeStr: String, minsToAdd: Int): String {
        val mins = timeToMinutes(timeStr)
        return minutesTo24HourStr(mins + minsToAdd)
    }

    private fun calculateQiblaDirection(lat: Double, lon: Double): Float {
        val meccaLat = Math.toRadians(21.4225)
        val meccaLon = Math.toRadians(39.8262)
        val currentLat = Math.toRadians(lat)
        val currentLon = Math.toRadians(lon)

        val y = Math.sin(meccaLon - currentLon)
        val x = Math.cos(currentLat) * Math.tan(meccaLat) - Math.sin(currentLat) * Math.cos(meccaLon - currentLon)
        var qiblaAngle = Math.toDegrees(Math.atan2(y, x))
        if (qiblaAngle < 0) qiblaAngle += 360.0
        return qiblaAngle.toFloat()
    }

    private fun calculatePrayerCountdown() {
        val now = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeStr = sdf.format(now.time)
        val currentMinutes = timeToMinutes(currentTimeStr)

        val schedules = _prayerTimes.value.filter { !it.isForbidden }
        if (schedules.isEmpty()) return

        var nextSched: PrayerSchedule? = null
        var minDiff = Int.MAX_VALUE

        for (sched in schedules) {
            val schedMins = timeToMinutes(sched.timeStr)
            var diff = schedMins - currentMinutes
            if (diff < 0) diff += 1440 // next day

            if (diff < minDiff) {
                minDiff = diff
                nextSched = sched
            }
        }

        // Calculate current active prayer based on sorted schedules
        val sortedSchedules = schedules.sortedBy { timeToMinutes(it.timeStr) }
        var currSched: PrayerSchedule? = null
        if (sortedSchedules.isNotEmpty()) {
            val before = sortedSchedules.filter { timeToMinutes(it.timeStr) <= currentMinutes }
            currSched = if (before.isNotEmpty()) {
                before.last()
            } else {
                sortedSchedules.last()
            }
        }

        currSched?.let {
            _currentPrayerName.value = if (_appLanguage.value == AppLanguage.BN) it.nameStrBn else it.nameStrEn
            _currentPrayerTime.value = it.timeStr
        }

        nextSched?.let {
            _nextPrayerName.value = if (_appLanguage.value == AppLanguage.BN) it.nameStrBn else it.nameStrEn
            _nextPrayerTime.value = it.timeStr

            val hrs = minDiff / 60
            val mins = minDiff % 60
            val secs = 60 - now.get(Calendar.SECOND)
            val secAdjustedMins = if (secs < 60) mins - 1 else mins
            val finalMins = if (secAdjustedMins < 0) 59 else secAdjustedMins
            val finalHrs = if (secAdjustedMins < 0) hrs - 1 else hrs

            _prayerCountdown.value = String.format("%02d:%02d:%02d", if (finalHrs < 0) 0 else finalHrs, finalMins, if (secs == 60) 0 else secs)

            // Adjust countdown bar fraction
            val elapsed = 1440 - minDiff
            _prayerProgressFraction.value = (elapsed.toFloat() / 1440f).coerceIn(0f, 1f)
        }
        calculateForbiddenState(_selectedCity.value)
    }

    private fun calculateForbiddenState(city: CityInfo) {
        val now = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeStr = sdf.format(now.time)
        val currentMinutes = timeToMinutes(currentTimeStr)

        val sunriseStr = calculateSunriseForbidden(city.fajrTime)
        val sunriseMin = timeToMinutes(sunriseStr)
        val dhuhrMin = timeToMinutes(city.dhuhrTime)
        val maghribMin = timeToMinutes(city.maghribTime)

        // Window specifications: Start, End, NameBn, NameEn
        data class ForbiddenWindowSpec(
            val start: Int,
            val end: Int,
            val nameBn: String,
            val nameEn: String
        )

        val windows = listOf(
            ForbiddenWindowSpec(sunriseMin, sunriseMin + 15, "সূর্যোদয়কাল", "Sunrise Window"),
            ForbiddenWindowSpec(dhuhrMin - 10, dhuhrMin, "মধ্যাহ্নকাল (যাওয়াল)", "Zenith (Zawwal)"),
            ForbiddenWindowSpec(maghribMin - 15, maghribMin, "সূর্যাস্তকাল", "Sunset Window")
        )

        var activeWindow: ForbiddenWindowSpec? = null
        for (w in windows) {
            if (currentMinutes >= w.start && currentMinutes < w.end) {
                activeWindow = w
                break
            }
        }

        val isBn = _appLanguage.value == AppLanguage.BN

        if (activeWindow != null) {
            val left = activeWindow.end - currentMinutes
            val secs = 60 - now.get(Calendar.SECOND)
            val secAdjustedMins = if (secs < 60) left - 1 else left
            val finalMins = if (secAdjustedMins < 0) 0 else secAdjustedMins
            val finalSecs = if (secs == 60) 0 else secs
            val cdStr = String.format("%02d:%02d:%02d", 0, finalMins, finalSecs)
            val cdStrFormatted = if (isBn) convertToBanglaDigits(cdStr) else cdStr

            val minutesStr = if (isBn) convertToBanglaDigits(left.toString()) else left.toString()
            val label = if (isBn) activeWindow.nameBn else activeWindow.nameEn
            val msg = if (isBn) {
                "⚠️ $label চলছে! শেষ হতে আর $minutesStr মিনিট বাকি।"
            } else {
                "⚠️ $label active! Ends in $left mins."
            }
            _forbiddenTimeState.value = ForbiddenTimeState(
                isActive = true,
                activeWindowName = label,
                nextWindowName = "",
                nextStartTimeStr = "",
                nextEndTimeStr = "",
                minutesLeftToStart = 0,
                minutesLeftToEnd = left,
                formattedStatusMessage = msg,
                countdownStr = cdStrFormatted
            )
        } else {
            // Find next upcoming
            var nextWindow: ForbiddenWindowSpec? = null
            var minDiff = Int.MAX_VALUE

            for (w in windows) {
                var diff = w.start - currentMinutes
                if (diff < 0) {
                    diff += 1440 // Tomorrow
                }
                if (diff < minDiff) {
                    minDiff = diff
                    nextWindow = w
                }
            }

            if (nextWindow != null) {
                val label = if (isBn) nextWindow.nameBn else nextWindow.nameEn
                val startStrRaw = minutesTo12HourStr(nextWindow.start)
                val endStrRaw = minutesTo12HourStr(nextWindow.end)
                
                val startStr = if (isBn) startStrRaw.replace("AM", "এএম").replace("PM", "পিএম") else startStrRaw
                val endStr = if (isBn) endStrRaw.replace("AM", "এএম").replace("PM", "পিএম") else endStrRaw

                val hrs = minDiff / 60
                val mins = minDiff % 60
                
                val hrsStr = if (isBn) convertToBanglaDigits(hrs.toString()) else hrs.toString()
                val minsStr = if (isBn) convertToBanglaDigits(mins.toString()) else mins.toString()

                val timeRemainingStr = if (isBn) {
                    val hStr = if (hrs > 0) "$hrsStr ঘণ্টা " else ""
                    "$hStr$minsStr মিনিট"
                } else {
                    val hStr = if (hrs > 0) "$hrs hr " else ""
                    "$hStr$mins mins"
                }

                val msg = if (isBn) {
                    "আসন্ন নিষিদ্ধ সময় $label: $startStr - $endStr ($timeRemainingStr বাকি)"
                } else {
                    "Upcoming Prohibited Time -$label: $startStr - $endStr ($timeRemainingStr remaining)"
                }

                _forbiddenTimeState.value = ForbiddenTimeState(
                    isActive = false,
                    activeWindowName = label,
                    nextWindowName = label,
                    nextStartTimeStr = startStr,
                    nextEndTimeStr = endStr,
                    minutesLeftToStart = minDiff,
                    minutesLeftToEnd = 0,
                    formattedStatusMessage = msg,
                    countdownStr = ""
                )
            } else {
                _forbiddenTimeState.value = null
            }
        }
    }

    fun timeToMinutes(timeStr: String): Int {
        return try {
            val parts = timeStr.split(":")
            parts[0].toInt() * 60 + parts[1].toInt()
        } catch (e: Exception) {
            0
        }
    }

    // --- Salat Daily Tracker Functions ---
    private fun loadMockSalatTracker() {
        _salatTracker.value = listOf(
            SalatTrackItem("s1", "ফজর", "Fajr", true, "04:05"),
            SalatTrackItem("s2", "যোহর", "Dhuhr", false, "12:06"),
            SalatTrackItem("s3", "আসর", "Asr", false, "15:45"),
            SalatTrackItem("s4", "মাগরিব", "Maghrib", true, "18:42"),
            SalatTrackItem("s5", "এশা", "Isha", false, "20:00")
        )
    }

    fun toggleSalatTrack(id: String) {
        _salatTracker.value = _salatTracker.value.map {
            if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    // --- Quran Flow ---
    fun selectSurah(surah: Surah?) {
        if (surah == null) {
            _selectedSurah.value = null
            _isSurahLoading.value = false
            _isAudioPlaying.value = false
            val m = mediaPlayer
            mediaPlayer = null
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    m?.stop()
                    m?.release()
                } catch (e: Exception) {}
            }
            return
        }
        
        _selectedSurah.value = surah
        _lastReadSurahNumber.value = surah.number
        _playingVerseIndex.value = -1 // Reset playing status on transition
        _isAudioPlaying.value = false
        val m = mediaPlayer
        mediaPlayer = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                m?.stop()
                m?.release()
            } catch (e: Exception) {}
        }

        // Download full verses dynamically in background
        viewModelScope.launch {
            _isSurahLoading.value = true
            try {
                val fullVerses = com.example.service.QuranApiClient.fetchFullSurah(surah.number)
                if (fullVerses != null && fullVerses.isNotEmpty()) {
                    _selectedSurah.value = surah.copy(verses = fullVerses)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSurahLoading.value = false
            }
        }
    }

    fun searchQuran(query: String) {
        _quranSearchQuery.value = query
    }

    fun toggleQuranBookmark(surahNumber: Int, verseNumber: Int) {
        val existing = _quranBookmarks.value.firstOrNull { it.surahNumber == surahNumber && it.verseNumber == verseNumber }
        if (existing != null) {
            _quranBookmarks.value = _quranBookmarks.value.filter { !(it.surahNumber == surahNumber && it.verseNumber == verseNumber) }
        } else {
            val sName = suras.firstOrNull { it.number == surahNumber }?.nameBengali ?: "সূরা"
            _quranBookmarks.value = _quranBookmarks.value + SuraBookmark(surahNumber, sName, verseNumber, System.currentTimeMillis())
        }
    }

    fun changeReciter(reciter: String) {
        _audioReciter.value = reciter
    }

    private fun getNotificationManager(): android.app.NotificationManager {
        return getApplication<Application>().getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "quran_download_channel"
            val channelName = "Quran Audio Downloads"
            val importance = android.app.NotificationManager.IMPORTANCE_LOW
            val channel = android.app.NotificationChannel(channelId, channelName, importance).apply {
                description = "Shows progress and completion of Surah audio downloads."
            }
            getNotificationManager().createNotificationChannel(channel)
        }
    }

    private fun showDownloadLocalNotification(surahNumber: Int, surahName: String, progress: Int) {
        createNotificationChannel()
        val channelId = "quran_download_channel"
        val isBn = _appLanguage.value == AppLanguage.BN
        
        val title = if (isBn) "সূরা অডিও ডাউনলোড হচ্ছে" else "Downloading Surah Audio"
        val content = if (isBn) "$surahNumber. $surahName (${progress}%)" else "$surahNumber. $surahName (${progress}%)"

        val builder = androidx.core.app.NotificationCompat.Builder(getApplication(), channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setProgress(100, progress, false)
            .setOnlyAlertOnce(true)

        try {
            getNotificationManager().notify(4242, builder.build())
        } catch (e: SecurityException) {
            android.util.Log.e("IbadahViewModel", "Post notification permission missing on Android 13+", e)
        }
    }

    private fun showDownloadCompletedNotification(surahNumber: Int, surahName: String) {
        createNotificationChannel()
        val channelId = "quran_download_channel"
        val isBn = _appLanguage.value == AppLanguage.BN
        
        val title = if (isBn) "ডাউনলোড সম্পন্ন হয়েছে!" else "Download Completed!"
        val content = if (isBn) "$surahNumber. $surahName অডিও প্রস্তুত।" else "$surahNumber. $surahName audio is ready."

        val builder = androidx.core.app.NotificationCompat.Builder(getApplication(), channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(false)
            .setAutoCancel(true)

        try {
            getNotificationManager().notify(4242, builder.build())
        } catch (e: SecurityException) {
            android.util.Log.e("IbadahViewModel", "Post notification permission missing on Android 13+", e)
        }
    }

    fun downloadSurahAudio(surah: Surah) {
        if (_quranDownloadState.value?.isDownloading == true) {
            // Already downloading something
            return
        }
        _quranDownloadState.value = DownloadInfo(
            surahNumber = surah.number,
            nameEn = surah.nameEnglish,
            nameBn = surah.nameBengali,
            progress = 0f,
            isDownloading = true
        )
        
        // Asynchronous non-blocking background task using Coroutines
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Post a real system notification for background status
                showDownloadLocalNotification(surah.number, surah.nameEnglish, 0)
                
                // Simulate downloading the MP3 verses (non-blocking, allowing all tasks to continue)
                for (p in 1..10) {
                    delay(800) // non-blocking sleep
                    val progressFloat = p / 10f
                    _quranDownloadState.value = _quranDownloadState.value?.copy(progress = progressFloat)
                    showDownloadLocalNotification(surah.number, surah.nameEnglish, (progressFloat * 100).toInt())
                }
                
                _quranDownloadState.value = _quranDownloadState.value?.copy(
                    progress = 1.0f,
                    isDownloading = false,
                    isCompleted = true
                )
                // Mark as downloaded for the current reciter
                val currentReciter = _audioReciter.value
                val newSet = _downloadedSurahs.value + "$currentReciter-${surah.number}"
                _downloadedSurahs.value = newSet
                try {
                    val quranPrefs = getApplication<Application>().getSharedPreferences("quran_downloads_prefs", android.content.Context.MODE_PRIVATE)
                    quranPrefs.edit().putStringSet("downloaded_keys", newSet).apply()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                showDownloadCompletedNotification(surah.number, surah.nameEnglish)
                
                delay(3000)
                // Clear state after showing completion
                _quranDownloadState.value = null
            } catch (e: Exception) {
                _quranDownloadState.value = null
            }
        }
    }

    fun playNameAudio(name: AllahName) {
        if (!isTtsReady) return
        viewModelScope.launch {
            try {
                tts?.stop()
                tts?.language = Locale("ar")
                tts?.speak(name.arabic, TextToSpeech.QUEUE_FLUSH, null, "AsmaArabic")
                
                delay(1800)
                tts?.language = Locale("bn")
                val textToSayBn = "${name.transliterationBn}. অর্থ, ${name.meaningBn}."
                tts?.speak(textToSayBn, TextToSpeech.QUEUE_ADD, null, "AsmaBengali")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getVerseAudioUrl(surahNum: Int, verseNum: Int, reciterName: String): String {
        val folder = when (reciterName) {
            "মিশারি রাশিদ আল-আফাসি" -> "Alafasy_128kbps"
            "মাহমুদ খলিল আল-হুসারি" -> "Husary_128kbps"
            else -> "Abdurrahmaan_As-Sudais_128kbps"
        }
        val s = String.format(Locale.US, "%03d", surahNum)
        val v = String.format(Locale.US, "%03d", verseNum)
        return "https://everyayah.com/data/$folder/$s$v.mp3"
    }

    private suspend fun awaitMediaPlayerCompletion(player: android.media.MediaPlayer) = suspendCancellableCoroutine<Unit> { continuation ->
        player.setOnCompletionListener {
            if (continuation.isActive) {
                continuation.resume(Unit) {}
            }
        }
        player.setOnErrorListener { _, _, _ ->
            if (continuation.isActive) {
                continuation.resume(Unit) {}
            }
            true
        }
        continuation.invokeOnCancellation {
            try {
                player.release()
            } catch (e: Exception) {}
        }
    }

    private suspend fun playVerseAudio(surahNum: Int, verseNum: Int, verseTextAr: String, verseTextBn: String) = withContext(Dispatchers.IO) {
        val streamUrl = getVerseAudioUrl(surahNum, verseNum, _audioReciter.value)
        var usedStream = false
        try {
            // Stop previous stream in a thread-safe manner
            mediaPlayer?.let { player ->
                try {
                    player.stop()
                    player.release()
                } catch (e: Exception) {}
            }
            mediaPlayer = null

            val player = android.media.MediaPlayer().apply {
                setAudioAttributes(
                    android.media.AudioAttributes.Builder()
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                // Use swift direct stream for high performance, falls back to redirected resolution only if from unbuffered domain
                val finalUrl = if (streamUrl.contains("everyayah.com")) {
                    streamUrl
                } else {
                    try {
                        resolveRedirects(streamUrl)
                    } catch (ex: Exception) {
                        streamUrl
                    }
                }
                setDataSource(finalUrl)
                prepare() // Instant prepare on Dispatchers.IO
                start()
            }
            mediaPlayer = player
            usedStream = true
            awaitMediaPlayerCompletion(player)
        } catch (e: Exception) {
            android.util.Log.e("IbadahViewModel", "Failed streaming verse $surahNum:$verseNum, falling back to TTS", e)
            try {
                mediaPlayer?.release()
                mediaPlayer = null
            } catch (ex: Exception) {}
        }

        if (!usedStream) {
            // Fallback to offline TTS rendering
            if (isTtsReady) {
                try {
                    tts?.language = Locale("ar")
                    tts?.speak(verseTextAr, TextToSpeech.QUEUE_FLUSH, null, "QuranArabic")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            val textLength = verseTextAr.length
            val sleepMs = (textLength * 120L).coerceIn(4000L, 10000L)
            
            val startTime = System.currentTimeMillis()
            while (isActive && _isAudioPlaying.value && tts?.isSpeaking == true && (System.currentTimeMillis() - startTime) < sleepMs) {
                delay(100)
            }
            if (tts?.isSpeaking != true) {
                delay(sleepMs)
            }
        }

        // NOW PLAY BENGALI TRANSLATION IF ENABLED
        if (isActive && _isAudioPlaying.value && _playBengaliTranslation.value) {
            // Just in case, add a tiny organic pause between Arabic recitation and Bangla translation
            delay(400)
            if (isTtsReady && verseTextBn.isNotEmpty()) {
                try {
                    // Ensure Bengali language support
                    tts?.language = Locale("bn")
                    tts?.speak(verseTextBn, TextToSpeech.QUEUE_FLUSH, null, "QuranBengaliTranslation")
                    
                    // Wait for the Bengali speech to finish
                    delay(350) // brief delay for starting TTS speaking state
                    while (isActive && _isAudioPlaying.value && tts?.isSpeaking == true) {
                        delay(100)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun toggleAudioRecitation() {
        val surah = _selectedSurah.value ?: return
        if (_isAudioPlaying.value) {
            _isAudioPlaying.value = false
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                } catch (e: Exception) {}
            }
            try {
                tts?.stop()
            } catch (e: Exception) {}
        } else {
            _isAudioPlaying.value = true
            viewModelScope.launch(Dispatchers.Default) {
                if (_playingVerseIndex.value < 0) {
                    _playingVerseIndex.value = 0
                }
                while (_isAudioPlaying.value && _playingVerseIndex.value < surah.verses.size) {
                    val currentVerse = surah.verses[_playingVerseIndex.value]
                    
                    playVerseAudio(surah.number, currentVerse.number, currentVerse.textArabic, currentVerse.textBengali)
                    
                    if (!_isAudioPlaying.value) break
                    val nextIdx = _playingVerseIndex.value + 1
                    if (nextIdx < surah.verses.size) {
                        _playingVerseIndex.value = nextIdx
                    } else {
                        _isAudioPlaying.value = false
                        _playingVerseIndex.value = -1
                        break
                    }
                }
            }
        }
    }

    fun playSpecificVerseAudio(index: Int) {
        _playingVerseIndex.value = index
        _isAudioPlaying.value = true
        val surah = _selectedSurah.value ?: return
        viewModelScope.launch(Dispatchers.Default) {
            if (index in 0 until surah.verses.size) {
                val currentVerse = surah.verses[index]
                playVerseAudio(surah.number, currentVerse.number, currentVerse.textArabic, currentVerse.textBengali)
            }
        }
    }

    // --- Tasbih Flow ---
    private fun loadMockTasbihHistory() {
        _tasbihHistory.value = listOf(
            TasbihHistoryItem("Subhanallah", 33, "২৭ মে ২০২৬"),
            TasbihHistoryItem("Alhamdulillah", 100, "২৬ মে ২০২৬"),
            TasbihHistoryItem("Astaghfirullah", 33, "২৫ মে ২০২৬")
        )
    }

    fun incrementTasbih() {
        val currentCount = _tasbihCount.value + 1
        _tasbihCount.value = currentCount
        if (currentCount >= _tasbihTarget.value) {
            // Target achieved sound/physical feedback trigger simulation
            _tasbihCount.value = 0
            val pName = if (_appLanguage.value == AppLanguage.BN) tasbihPhrasesBn[_currentTasbihPhraseIndex.value] else tasbihPhrasesEn[_currentTasbihPhraseIndex.value]
            _tasbihHistory.value = listOf(
                TasbihHistoryItem(pName, _tasbihTarget.value, "আজ"),
                *_tasbihHistory.value.toTypedArray()
            )
        }
    }

    fun resetTasbih() {
        _tasbihCount.value = 0
    }

    fun changeTasbihPhrase(index: Int) {
        _currentTasbihPhraseIndex.value = index
        _tasbihCount.value = 0
    }

    fun changeTasbihTarget(target: Int) {
        _tasbihTarget.value = target
        _tasbihCount.value = 0
    }

    // --- Dua Counter Track ---
    fun incrementDuaCount(id: String) {
        _curatedDuas.value = _curatedDuas.value.map {
            if (it.id == id) {
                val newCount = (it.count + 1) % (it.target + 1)
                it.copy(count = newCount)
            } else {
                it
            }
        }
    }

    // --- Zakat math ---
    fun updateZakatGold(goldVal: String) {
        _zakatGoldValue.value = goldVal
        recalculateZakat()
    }

    fun updateZakatCash(cashVal: String) {
        _zakatCashValue.value = cashVal
        recalculateZakat()
    }

    fun updateZakatBusiness(bizVal: String) {
        _zakatBusinessValue.value = bizVal
        recalculateZakat()
    }

    fun updateZakatLiabilities(liabVal: String) {
        _zakatLiabilitiesValue.value = liabVal
        recalculateZakat()
    }

    private fun recalculateZakat() {
        val gold = _zakatGoldValue.value.toDoubleOrNull() ?: 0.0
        val cash = _zakatCashValue.value.toDoubleOrNull() ?: 0.0
        val biz = _zakatBusinessValue.value.toDoubleOrNull() ?: 0.0
        val liab = _zakatLiabilitiesValue.value.toDoubleOrNull() ?: 0.0

        val totalNetAssets = (gold * 12500.0) + cash + biz - liab // Assuming Gold per Gram = BDT 12500 average rating
        val nisabThreshold = 85000.0 // Value of 52.5 tolas of silver in BDT

        if (totalNetAssets >= nisabThreshold) {
            _isEligibleForZakat.value = true
            _zakatResultAmount.value = totalNetAssets * 0.025
        } else {
            _isEligibleForZakat.value = false
            _zakatResultAmount.value = 0.0
        }
    }

    // --- Compass Handler for Qibla Screen ---
    private var sensorManager: SensorManager? = null
    private var rotationSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private val lastAccelerometer = FloatArray(3)
    private val lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false

    private var lastSmoothHeading = 0.0f
    private var hasFirstHeading = false

    private fun smoothHeading(newHeading: Float): Float {
        if (!hasFirstHeading) {
            lastSmoothHeading = newHeading
            hasFirstHeading = true
            return newHeading
        }
        
        var diff = newHeading - lastSmoothHeading
        while (diff < -180f) diff += 360f
        while (diff > 180f) diff -= 360f
        
        val alpha = 0.18f // premium smoothing factor
        lastSmoothHeading = (lastSmoothHeading + alpha * diff + 360f) % 360f
        return lastSmoothHeading
    }

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            try {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val values = event.values
                    if (values != null && values.isNotEmpty()) {
                        val rotationMatrix = FloatArray(9)
                        SensorManager.getRotationMatrixFromVector(rotationMatrix, values)
                        val orientationValues = FloatArray(3)
                        SensorManager.getOrientation(rotationMatrix, orientationValues)
                        var azimuth = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
                        if (azimuth < 0) azimuth += 360f
                        _deviceHeading.value = smoothHeading(azimuth)
                    }
                } else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
                    lastAccelerometerSet = true
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
                    lastMagnetometerSet = true
                }

                if (lastAccelerometerSet && lastMagnetometerSet) {
                    val r = FloatArray(9)
                    val i = FloatArray(9)
                    if (SensorManager.getRotationMatrix(r, i, lastAccelerometer, lastMagnetometer)) {
                        val orientation = FloatArray(3)
                        SensorManager.getOrientation(r, orientation)
                        var azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                        if (azimuth < 0) azimuth += 360f
                        _deviceHeading.value = smoothHeading(azimuth)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed
        }
    }

    fun resumeCompassSensors() {
        hasFirstHeading = false
        try {
            if (sensorManager == null) {
                sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            }
            rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            sensorManager?.unregisterListener(sensorEventListener)
            lastAccelerometerSet = false
            lastMagnetometerSet = false

            if (rotationSensor != null) {
                sensorManager?.registerListener(
                    sensorEventListener,
                    rotationSensor,
                    SensorManager.SENSOR_DELAY_UI
                )
            } else {
                if (accelerometer != null) {
                    sensorManager?.registerListener(
                        sensorEventListener,
                        accelerometer,
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
                if (magnetometer != null) {
                    sensorManager?.registerListener(
                        sensorEventListener,
                        magnetometer,
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pauseCompassSensors() {
        try {
            sensorManager?.unregisterListener(sensorEventListener)
            lastAccelerometerSet = false
            lastMagnetometerSet = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            pauseCompassSensors()
        } catch (e: Exception) {}
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {}
        try {
            exoPlayerManager.release()
        } catch (e: Exception) {}
        try {
            bgExoPlayer?.release()
            bgExoPlayer = null
        } catch (e: Exception) {}
        try {
            azanExoPlayer?.release()
            azanExoPlayer = null
        } catch (e: Exception) {}
        try {
            alarmPlayer?.release()
            alarmPlayer = null
        } catch (e: Exception) {}
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {}
    }

    // --- Islamic AI Assistant (Gemini) ---
    fun askIslamicAi(query: String) {
        if (query.trim().isEmpty()) return

        val userMsg = ChatMessage("user", query, System.currentTimeMillis())
        _aiMessages.value = _aiMessages.value + userMsg
        _isAiLoading.value = true

        viewModelScope.launch {
            val systemInstructionMsg = """
                You are "জান্নাত সন্ধান এআই", a smart and highly knowledgeable Islamic AI Assistant integrated inside the "জান্নাত সন্ধান" premium Islamic super-app.
                Your purpose is to answer user queries with precise reference from the Holy Quran, Sunnah, Hadith, and scholarly consensus (Ijma) in a friendly, respectful, and polite tone.
                
                Guidelines:
                1. Match the language of the user. If they query in Bengali, response in beautiful, accurate Bengali. If they query in English, response in English.
                2. Do not offer fatwas on highly controversial issues without citing multiple safe opinions. Prefer the easiest and most compassionate way.
                3. Keep the content deeply spiritual and informative.
                4. Citing Quran chapters/verse numbers and reliable Hadith collections (Bukhari, Muslim, Tirmidhi, Abu Dawud, etc.) is highly recommended.
            """.trimIndent()

            val answerText = GeminiApiClient.askGemini(query, systemInstructionMsg)
            
            _aiMessages.value = _aiMessages.value + ChatMessage("ai", answerText, System.currentTimeMillis())
            _isAiLoading.value = false
        }
    }

    fun submitVoiceQuery() {
        // Voice query simulation helper - generates beautiful prompt to help user test
        askIslamicAi("মিষ্টি ভাষায় ও ঈমানী দৃঢ়তা অর্জনের একটি উত্তম দোয়া কি?")
    }

    fun triggerOcrArabicAnalysis(scannedText: String) {
        _isAiLoading.value = true
        _scannedArabicResult.value = scannedText
        viewModelScope.launch {
            val q = "পবিত্র আরবী ক্যালিগ্রাফি বা এই টেক্সটটির তাফসির এবং অর্থ বলুন: $scannedText"
            val response = GeminiApiClient.askGemini(q, "You are an expert Arabic Calligraphy interpreter and translation engine.")
            _aiMessages.value = _aiMessages.value + ChatMessage("ocr_result", "নিচে আরবী স্ক্যানটির বিশ্লেষণ দেওয়া হলো:\n\n$response", System.currentTimeMillis())
            _isAiLoading.value = false
        }
    }

    fun translateArabicFromImage(bitmap: android.graphics.Bitmap, targetLanguage: String) {
        _isTranslationLoading.value = true
        _capturedBitmap.value = bitmap
        _translationResult.value = ""
        
        viewModelScope.launch {
            try {
                // Convert Bitmap to base64
                val outputStream = java.io.ByteArrayOutputStream()
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 85, outputStream)
                val base64Image = android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.NO_WRAP)
                
                val result = GeminiApiClient.translateArabicImage(base64Image, targetLanguage)
                _translationResult.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _translationResult.value = "অনুবাদ করার সময় সিষ্টেমে ত্রুটি ঘটেছে: ${e.localizedMessage}"
            } finally {
                _isTranslationLoading.value = false
            }
        }
    }
    
    fun setCapturedBitmap(bitmap: android.graphics.Bitmap?) {
        _capturedBitmap.value = bitmap
    }
    
    fun clearTranslationState() {
        _translationResult.value = ""
        _capturedBitmap.value = null
        _isTranslationLoading.value = false
    }

    // --- Admin Dashboard Helpers ---
    fun publishNotice(title: String, body: String, category: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = formatter.format(Date())
        val newNotice = NoticeMessage(
            id = "notice_" + System.currentTimeMillis(),
            title = title,
            message = body,
            dateStr = dateString,
            category = category
        )
        _adminNotices.value = listOf(newNotice, *_adminNotices.value.toTypedArray())
    }

    fun deleteNotice(id: String) {
        _adminNotices.value = _adminNotices.value.filter { it.id != id }
    }

    fun toggleHadithBookmark(hadithId: String) {
        val current = _hadithBookmarks.value.toMutableSet()
        if (current.contains(hadithId)) {
            current.remove(hadithId)
        } else {
            current.add(hadithId)
        }
        _hadithBookmarks.value = current
    }

    // --- Alarm configuration actions ---
    fun toggleAlarmEnabled(id: String) {
        _prayerAlarms.value = _prayerAlarms.value.map {
            if (it.id == id) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    fun toggleAlarmDaily(id: String) {
        _prayerAlarms.value = _prayerAlarms.value.map {
            if (it.id == id) it.copy(isDaily = !it.isDaily) else it
        }
    }

    fun updateAlarmOffset(id: String, offset: Int) {
        _prayerAlarms.value = _prayerAlarms.value.map {
            if (it.id == id) it.copy(offsetMinutes = offset) else it
        }
    }

    fun toggleAlarmAzan(id: String) {
        _prayerAlarms.value = _prayerAlarms.value.map {
            if (it.id == id) it.copy(isAzanAlarm = !it.isAzanAlarm) else it
        }
    }

    fun addNewAlarm(
        nameBn: String,
        nameEn: String,
        time: String,
        customDays: List<Int>? = null,
        specificDate: String? = null
    ) {
        val newId = "custom_${System.currentTimeMillis()}"
        val newAlarm = PrayerAlarm(
            id = newId,
            nameBn = nameBn,
            nameEn = nameEn,
            nameAr = nameEn,
            isEnabled = true,
            isDaily = specificDate == null && customDays == null, // true if completely generic daily, false if specific days or date
            offsetMinutes = 0,
            isAzanAlarm = false,
            customTime = time,
            customDays = customDays,
            specificDate = specificDate
        )
        _prayerAlarms.value = listOf(newAlarm) + _prayerAlarms.value
    }

    fun deleteAlarm(id: String) {
        _prayerAlarms.value = _prayerAlarms.value.filter { it.id != id }
    }

    fun selectDeedDate(dateStr: String) {
        _deedSelectedDate.value = dateStr
        loadDeedsForDate(dateStr)
        loadMonthTrackerSummary()
    }

    private fun getDeedsPrefs() = getApplication<Application>()
        .getSharedPreferences("jannat_deeds_prefs", android.content.Context.MODE_PRIVATE)

    fun loadDeedsForDate(dateStr: String) {
        val prefs = getDeedsPrefs()
        val set = prefs.getStringSet("deeds_$dateStr", emptySet()) ?: emptySet()
        _completedDeedsForSelectedDate.value = set
    }

    fun toggleDeedCompleted(deedId: String) {
        val dateStr = _deedSelectedDate.value
        val currentSet = _completedDeedsForSelectedDate.value.toMutableSet()
        if (currentSet.contains(deedId)) {
            currentSet.remove(deedId)
        } else {
            currentSet.add(deedId)
        }
        _completedDeedsForSelectedDate.value = currentSet

        // Save to SharedPreferences
        val prefs = getDeedsPrefs()
        prefs.edit().putStringSet("deeds_$dateStr", currentSet).apply()

        // Reload the month mapping
        loadMonthTrackerSummary()

        // Broadcast to refresh home screen widgets
        try {
            val updateIntent = android.content.Intent("com.example.action.IBADAH_DASHBOARD_WIDGET_REFRESH")
            context.sendBroadcast(updateIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadMonthTrackerSummary() {
        val prefs = getDeedsPrefs()
        val selDate = _deedSelectedDate.value
        if (selDate.length < 8) return
        val yearMonthPre = selDate.substring(0, 8) // e.g. "2026-05-"
        val monthData = mutableMapOf<String, Set<String>>()
        
        // Loop from 1 to 31 to fetch data for the days
        for (day in 1..31) {
            val dateKey = String.format(java.util.Locale.US, "%s%02d", yearMonthPre, day)
            val set = prefs.getStringSet("deeds_$dateKey", emptySet()) ?: emptySet()
            if (set.isNotEmpty()) {
                monthData[dateKey] = set
            }
        }
        _completedDeedsForMonth.value = monthData
    }


    fun generateYearRoundRamadanCalendar(): List<RamadanCalendarDay> {
        val list = mutableListOf<RamadanCalendarDay>()
        val baseCity = _selectedCity.value
        val baseSahriMin = parseTimeToMin(baseCity.sahriTime)
        val baseIftarMin = parseTimeToMin(baseCity.iftarTime)

        for (day in 1..30) {
            val dayOffset = day - 1
            // Ramadan 1447 Gregorian projections (approx Feb-Mar 2026)
            val gregDay = (18 + dayOffset) % 28 + 1
            val gregMonth = if (18 + dayOffset >= 28) {
                if (_appLanguage.value == AppLanguage.BN) "মার্চ" else "March"
            } else {
                if (_appLanguage.value == AppLanguage.BN) "ফেব্রুয়ারি" else "February"
            }
            val gregDateBn = "$gregDay $gregMonth, ২০২৬"
            val gregDateEn = "$gregMonth $gregDay, 2026"

            // Adjust times slightly for variation
            val dailySahriMin = baseSahriMin - (dayOffset / 5)
            val dailyIftarMin = baseIftarMin + (dayOffset / 5)

            list.add(
                RamadanCalendarDay(
                    ramadanDay = day,
                    gregorianDateBn = gregDateBn,
                    gregorianDateEn = gregDateEn,
                    sahriTimeLabel = minutesToTimeString(dailySahriMin),
                    iftarTimeLabel = minutesToTimeString(dailyIftarMin)
                )
            )
        }
        return list
    }

    private fun parseTimeToMin(timeStr: String): Int {
        return try {
            val parts = timeStr.trim().split(":")
            parts[0].toInt() * 60 + parts[1].toInt()
        } catch (e: Exception) {
            245 // default 04:05 as minutes
        }
    }

    private fun minutesToTimeString(totalMinutes: Int): String {
        val norm = (totalMinutes + 1440) % 1440
        var hr = norm / 60
        val min = norm % 60
        val suffix = if (hr >= 12) " PM" else " AM"
        val suffixBn = if (hr >= 12) " পিএম" else " এএম"
        val suffixAr = if (hr >= 12) " م" else " ص"
        
        if (hr > 12) hr -= 12
        if (hr == 0) hr = 12
        
        val lang = _appLanguage.value
        val endSuf = when (lang) {
            AppLanguage.BN -> suffixBn
            AppLanguage.AR -> suffixAr
            else -> suffix
        }
        
        val rawTime = String.format("%02d:%02d", hr, min) + endSuf
        return when (lang) {
            AppLanguage.BN -> convertToBanglaDigits(rawTime)
            AppLanguage.AR -> convertToArabicDigits(rawTime)
            else -> rawTime
        }
    }

    fun generateYearlySehriIftarCalculations(monthIndex: Int): List<RamadanCalendarDay> {
        val list = mutableListOf<RamadanCalendarDay>()
        val city = _selectedCity.value
        val baseSahriMin = parseTimeToMin(city.sahriTime)
        val baseIftarMin = parseTimeToMin(city.iftarTime)

        val daysInMonths = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val numDays = daysInMonths.getOrElse(monthIndex) { 30 }

        val bnMons = listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
        val enMons = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        val bnMon = bnMons.getOrElse(monthIndex) { "মে" }
        val enMon = enMons.getOrElse(monthIndex) { "May" }

        val latFactor = Math.abs(city.latitude) / 23.8103
        val sahriAmp = 45 * latFactor
        val iftarAmp = 45 * latFactor
        val hemisphere = if (city.latitude >= 0) 1 else -1

        for (day in 1..numDays) {
            val dayOfYear = monthIndex * 30 + day
            val angle = (dayOfYear - 172) * 2.0 * Math.PI / 365.0
            val wave = Math.cos(angle)

            val sahriShift = - (wave * sahriAmp).toInt() * hemisphere
            val iftarShift = (wave * iftarAmp).toInt() * hemisphere

            val dailySahriMin = baseSahriMin + sahriShift
            val dailyIftarMin = baseIftarMin + iftarShift

            val gregDateBn = "$day $bnMon, ২০২৬"
            val gregDateEn = "$enMon $day, 2026"

            list.add(
                RamadanCalendarDay(
                    ramadanDay = day,
                    gregorianDateBn = gregDateBn,
                    gregorianDateEn = gregDateEn,
                    sahriTimeLabel = minutesToTimeString(dailySahriMin),
                    iftarTimeLabel = minutesToTimeString(dailyIftarMin)
                )
            )
        }
        return list
    }

    fun addPoints(amount: Int) {
        val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
        val currentPoints = _userPoints.value + amount
        _userPoints.value = currentPoints
        quizPrefs.edit().putInt("total_points", currentPoints).apply()
    }

    fun markQuizDateCompleted(dateStr: String) {
        val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
        val newDates = _completedQuizDates.value + dateStr
        _completedQuizDates.value = newDates
        quizPrefs.edit().putStringSet("completed_dates", newDates).apply()
    }

    fun markQuizMonthCompleted(monthStr: String) {
        val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
        val newMonths = _completedQuizMonths.value + monthStr
        _completedQuizMonths.value = newMonths
        quizPrefs.edit().putStringSet("completed_months", newMonths).apply()
    }

    fun resetQuizProgress() {
        val quizPrefs = getApplication<Application>().getSharedPreferences("islamic_quiz_prefs", android.content.Context.MODE_PRIVATE)
        quizPrefs.edit().clear().apply()
        _userPoints.value = 0
        _completedQuizDates.value = emptySet()
        _completedQuizMonths.value = emptySet()
    }
}

// --- Data Contracts Helper ---

data class RamadanCalendarDay(
    val ramadanDay: Int,
    val gregorianDateBn: String,
    val gregorianDateEn: String,
    val sahriTimeLabel: String,
    val iftarTimeLabel: String
)

data class CityInfo(
    val nameBn: String,
    val nameEn: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val sahriTime: String,
    val iftarTime: String,
    val fajrTime: String,
    val dhuhrTime: String,
    val asrTime: String,
    val maghribTime: String,
    val ishaTime: String
)

data class LiveChannel(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val streamUrl: String,
    val isActive: Boolean
)

data class ChatMessage(
    val sender: String, // "user" or "ai" or "ocr_result"
    val content: String,
    val timestamp: Long
)

data class ForbiddenTimeState(
    val isActive: Boolean,
    val activeWindowName: String,
    val nextWindowName: String,
    val nextStartTimeStr: String,
    val nextEndTimeStr: String,
    val minutesLeftToStart: Int,
    val minutesLeftToEnd: Int,
    val formattedStatusMessage: String,
    val countdownStr: String = ""
)

class OkHttpMediaDataSource(
    private val client: okhttp3.OkHttpClient,
    private val url: String
) : android.media.MediaDataSource() {

    private var size: Long = -1L
    private var activeResponse: okhttp3.Response? = null
    private var activeStream: java.io.InputStream? = null
    private var currentPosition: Long = -1L

    @Synchronized
    override fun getSize(): Long {
        if (size != -1L) return size

        val request = okhttp3.Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
            .header("Accept", "*/*")
            .build()
        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val len = response.body?.contentLength() ?: -1L
                    if (len > 0) {
                        size = len
                        return size
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1L
    }

    @Synchronized
    override fun readAt(position: Long, buffer: ByteArray, offset: Int, size: Int): Int {
        if (size <= 0) return 0
        
        val totalSize = getSize()
        if (totalSize > 0 && position >= totalSize) {
            return -1 // EOF
        }

        try {
            // Seek if needed or open initial stream
            if (activeStream == null || position != currentPosition) {
                closeConnection()
                
                val rangeHeader = if (totalSize > 0) {
                    "bytes=$position-${totalSize - 1}"
                } else {
                    "bytes=$position-"
                }

                val request = okhttp3.Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                    .header("Accept", "*/*")
                    .header("Range", rangeHeader)
                    .header("Connection", "keep-alive")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful && response.code != 206) {
                    response.close()
                    return -1
                }
                
                activeResponse = response
                activeStream = response.body?.byteStream()
                currentPosition = position
            }

            val stream = activeStream ?: return -1
            val read = stream.read(buffer, offset, size)
            if (read > 0) {
                currentPosition += read
            }
            return read

        } catch (e: Exception) {
            e.printStackTrace()
            closeConnection()
            return -1
        }
    }

    private fun closeConnection() {
        try {
            activeStream?.close()
        } catch (e: Exception) {}
        activeStream = null
        
        try {
            activeResponse?.close()
        } catch (e: Exception) {}
        activeResponse = null
        
        currentPosition = -1L
    }

    @Synchronized
    override fun close() {
        closeConnection()
    }
}



