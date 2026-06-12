package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
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
import com.example.data.DailymotionVideo

@Composable
fun InAppDailymotionPlayerDialog(
    video: DailymotionVideo,
    viewModel: IbadahViewModel,
    relatedVideos: List<DailymotionVideo>,
    isBn: Boolean,
    onDismiss: () -> Unit
) {
    var activeVideo by remember(video) { mutableStateOf(video) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        InAppDailymotionPlayerScreen(
            video = activeVideo,
            viewModel = viewModel,
            relatedVideos = relatedVideos,
            isBn = isBn,
            onClose = onDismiss,
            onSelectVideo = { nextVideo ->
                activeVideo = nextVideo
            }
        )
    }
}

@Composable
fun InAppDailymotionPlayerScreen(
    video: DailymotionVideo,
    viewModel: IbadahViewModel,
    relatedVideos: List<DailymotionVideo>,
    isBn: Boolean,
    onClose: () -> Unit,
    onSelectVideo: (DailymotionVideo) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    
    val isBookmarked by viewModel.dailymotionBookmarks.collectAsState()
    val isCurrentBookmarked = isBookmarked.contains(video.id)

    // Watch for back button or exit and release landscape lock if present
    BackHandler {
        if (isLandscape) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            onClose()
        }
    }

    val openInDailymotionAction = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dailymotion.com/video/${video.id}"))
        context.startActivity(intent)
    }

    // Load related videos when the video changes
    LaunchedEffect(video.id) {
        viewModel.loadRelatedDailymotionVideos(video.id)
    }

    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        if (isLandscape) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                DailymotionImePlayerView(
                    videoId = video.id,
                    modifier = Modifier.fillMaxSize()
                )

                // Landscape Overlays (Header overlay details)
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
                        text = video.title,
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
                            text = if (isBn) "ইবাদাত ডেইলি মোশন প্লেয়ার" else "Ibadah Dailymotion Player",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { viewModel.toggleDailymotionBookmark(video) }) {
                            Icon(
                                imageVector = if (isCurrentBookmarked) Icons.Default.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (isCurrentBookmarked) Color(0xFFFFD700) else Color.White
                            )
                        }
                        IconButton(onClick = openInDailymotionAction) {
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
                    DailymotionImePlayerView(
                        videoId = video.id,
                        modifier = Modifier.fillMaxSize()
                    )

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
                val dRelatedList by viewModel.relatedDailymotionVideos.collectAsState()

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
                                    text = video.title,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = video.channelName,
                                        color = Color(0xFFFFD700),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${if (isBn) "সময়কাল:" else "Duration:"} ${video.durationString}",
                                        color = Color.LightGray,
                                        fontSize = 11.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isBn) "• অটো প্লে সক্রিয় • ডেইলি মোশন আইফ্রেম এম্বেড মোড" else "• Autoplay Active • Powered by Dailymotion Embedded Iframe",
                                    color = Color.LightGray.copy(alpha = 0.6f),
                                    fontSize = 10.sp
                                )
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
                    val filteredRelated = dRelatedList.filter { it.id != video.id }
                    if (filteredRelated.isEmpty()) {
                        item {
                            Text(
                                text = if (isBn) "সহযোগী কোনো ভিডিও পাওয়া যায়নি।" else "No related videos found.",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    } else {
                        items(filteredRelated) { item ->
                            DailymotionRelatedCard(video = item, isBn = isBn, onClick = { onSelectVideo(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailymotionRelatedCard(video: DailymotionVideo, isBn: Boolean, onClick: () -> Unit) {
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
                    .size(width = 94.dp, height = 58.dp)
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
                        text = video.durationString,
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
                    text = video.channelName,
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
fun DailymotionImePlayerView(
    videoId: String,
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

                    val htmlContent = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                            <style>
                                body, html { margin:0; padding:0; width:100%; height:100%; background-color:#000; overflow:hidden; }
                                iframe { width:100%; height:100%; border:none; }
                            </style>
                        </head>
                        <body>
                            <iframe src="https://www.dailymotion.com/embed/video/$videoId?autoplay=1&mute=0&ui-start-screen-info=false" allow="autoplay; fullscreen" allowfullscreen></iframe>
                        </body>
                        </html>
                    """.trimIndent()

                    loadDataWithBaseURL("https://www.dailymotion.com", htmlContent, "text/html", "UTF-8", null)
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
