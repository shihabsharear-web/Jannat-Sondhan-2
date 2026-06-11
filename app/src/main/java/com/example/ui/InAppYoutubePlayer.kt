package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.IslamicVideo

@Composable
fun InAppYoutubePlayerDialog(
    videoId: String,
    videoTitle: String,
    videoSpeaker: String,
    relatedVideos: List<IslamicVideo>,
    isBn: Boolean,
    onDismiss: () -> Unit
) {
    var activeVideoId by remember(videoId) { mutableStateOf(videoId) }
    var activeTitle by remember(videoId) { mutableStateOf(videoTitle) }
    var activeSpeaker by remember(videoId) { mutableStateOf(videoSpeaker) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        InAppYoutubePlayerScreen(
            videoId = activeVideoId,
            videoTitle = activeTitle,
            videoSpeaker = activeSpeaker,
            relatedVideos = relatedVideos,
            isBn = isBn,
            onClose = onDismiss,
            onSelectVideo = { nextVideo ->
                activeVideoId = nextVideo.youtubeId
                activeTitle = nextVideo.title
                activeSpeaker = nextVideo.speaker
            }
        )
    }
}

@Composable
fun InAppYoutubePlayerScreen(
    videoId: String,
    videoTitle: String,
    videoSpeaker: String,
    relatedVideos: List<IslamicVideo>,
    isBn: Boolean,
    onClose: () -> Unit,
    onSelectVideo: (IslamicVideo) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    var isPlayerLoading by remember(videoId) { mutableStateOf(true) }
    var embeddingError by remember(videoId) { mutableStateOf<Int?>(null) } // null = no error, 101/150 = cannot embed

    // Watch for back button or exit and release landscape lock if present
    BackHandler {
        if (isLandscape) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            onClose()
        }
    }

    val openInYtAction = {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            context.startActivity(intent)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (isLandscape) {
            // Unrestricted Fullscreen Mode
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                YoutubeImePlayerView(
                    videoId = videoId,
                    onReady = { isPlayerLoading = false },
                    onError = { code -> embeddingError = code },
                    modifier = Modifier.fillMaxSize()
                )

                // Landscape Overlays
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FullscreenExit,
                            contentDescription = "Portrait",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = videoTitle,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Error Overlay
                if (embeddingError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = if (isBn) "ভিডিওটি এই অ্যাপ্লিকেশনে প্লে করা সম্ভব হচ্ছে না।" else "This video cannot be played inside the app.",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = openInYtAction,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "Open in YT")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (isBn) "ইউটিউবে দেখুন" else "Open in YouTube")
                            }
                        }
                    }
                }
            }
        } else {
            // Portrait Details player + related feed
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Header Options
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF151515))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = onClose) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text(
                            text = if (isBn) "ইবাদত ইউটিউব প্লেয়ার" else "Ibadah YouTube Player",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = openInYtAction) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = "External Play", tint = Color.White)
                        }
                    }
                }

                // 16:9 Video Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black)
                ) {
                    YoutubeImePlayerView(
                        videoId = videoId,
                        onReady = { isPlayerLoading = false },
                        onError = { code -> embeddingError = code },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (isPlayerLoading && embeddingError == null) {
                        CircularProgressIndicator(
                            color = Color(0xFFFFD700),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(36.dp)
                        )
                    }

                    // Floating Fullscreen toggle over video bottom right
                    IconButton(
                        onClick = {
                            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Fullscreen",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Scrollable details and related lists
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFF101010))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Title Details & Info
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = videoTitle,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = videoSpeaker,
                                    color = Color(0xFFFFD700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isBn) "• অটো প্লে সক্রিয় • আইফ্রেম মডিউলে চালিত" else "• Autoplay Active • Powered by YouTube IFrame API",
                                    color = Color.LightGray.copy(alpha = 0.6f),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Warning card when embedding block triggers
                    if (embeddingError != null) {
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF3A1010)),
                                border = BorderStroke(1.dp, Color(0xFF9E2A2B)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Notice",
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isBn) 
                                            "প্রকাশক কর্তৃক এই ভিডিওটি সরাসরি অ্যাপ্লিকেশনে প্লে করতে বাধার সম্মুখীন হয়েছে (Error $embeddingError)। অনুগ্রহ করে নিচের বোতামে চাপ দিয়ে সরাসরি ইউটিউবে দেখুন।"
                                            else "The publisher of this video does not allow embedding playback (Error $embeddingError). Please watch it directly on YouTube.",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = openInYtAction,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = if (isBn) "ইউটিউব অ্যাপে দেখুন" else "Watch on YouTube", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Related videos section title
                    item {
                        Text(
                            text = if (isBn) "সহযোগী ভিডিওসমূহ (Related Videos)" else "Related Videos",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    // Related list items
                    val filteredRelated = relatedVideos.filter { it.youtubeId != videoId }
                    if (filteredRelated.isEmpty()) {
                        item {
                            Text(
                                text = if (isBn) "কোনো সহযোগী ভিডিও পাওয়া যায়নি।" else "No related videos found.",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        items(filteredRelated) { it ->
                            RelatedVideoItemCard(video = it, isBn = isBn, onClick = { onSelectVideo(it) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RelatedVideoItemCard(video: IslamicVideo, isBn: Boolean, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 55.dp)
                    .clip(RoundedCornerShape(6.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(video.thumbnail)
                        .crossfade(true)
                        .placeholder(com.example.R.drawable.islamic_mecca_wallpaper)
                        .error(com.example.R.drawable.islamic_mecca_wallpaper)
                        .build(),
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp)
                        .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(2.dp))
                        .padding(horizontal = 3.dp, vertical = 0.5.dp)
                ) {
                    Text(
                        text = video.duration,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = video.speaker,
                    color = Color.Gray,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun YoutubeImePlayerView(
    videoId: String,
    onReady: () -> Unit,
    onError: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    key(videoId) {
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        databaseEnabled = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Mobile Safari/537.36"
                    }
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()

                    val bridge = object {
                        @JavascriptInterface
                        fun onPlayerReady() {
                            post { onReady() }
                        }

                        @JavascriptInterface
                        fun onPlayerStateChange(state: Int) {
                            // Can track logs if needed
                        }

                        @JavascriptInterface
                        fun onPlayerError(errCode: Int) {
                            post { onError(errCode) }
                        }
                    }

                    addJavascriptInterface(bridge, "AndroidInterface")

                    val htmlContent = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                            <style>
                                body, html { margin:0; padding:0; width:100%; height:100%; background-color:#000; overflow:hidden; }
                                #player { width:100%; height:100%; border:none; }
                            </style>
                        </head>
                        <body>
                            <div id="player"></div>
                            <script>
                                var tag = document.createElement('script');
                                tag.src = "https://www.youtube.com/iframe_api";
                                var firstScriptTag = document.getElementsByTagName('script')[0];
                                firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                                var player;
                                function onYouTubeIframeAPIReady() {
                                    player = new YT.Player('player', {
                                        height: '100%',
                                        width: '100%',
                                        videoId: '$videoId',
                                        playerVars: {
                                            'autoplay': 1,
                                            'controls': 1,
                                            'rel': 0,
                                            'showinfo': 0,
                                            'iv_load_policy': 3,
                                            'modestbranding': 1,
                                            'origin': 'https://www.youtube.com'
                                        },
                                        events: {
                                            'onReady': onPlayerReady,
                                            'onStateChange': onPlayerStateChange,
                                            'onError': onPlayerError
                                        }
                                    });
                                }

                                function onPlayerReady(event) {
                                    AndroidInterface.onPlayerReady();
                                }

                                function onPlayerStateChange(event) {
                                    AndroidInterface.onPlayerStateChange(event.data);
                                }

                                function onPlayerError(event) {
                                    AndroidInterface.onPlayerError(event.data);
                                }
                            </script>
                        </body>
                        </html>
                    """.trimIndent()

                    loadDataWithBaseURL("https://www.youtube.com", htmlContent, "text/html", "UTF-8", null)
                }
            },
            onRelease = { webView ->
                try {
                    webView.stopLoading()
                    webView.clearHistory()
                    webView.removeAllViews()
                    webView.destroy()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        )
    }
}
