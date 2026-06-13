package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.IbadahMediaStream
import com.example.service.FirebaseMediaCenterBackend
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Color Scheme Essentials for Cosmic Islamic design
val DeepEmerald = Color(0xFF042F1A)
val SoftEmerald = Color(0xFF0C4A2B)
val LightMint = Color(0xFFC7F9CC)
val CoralGold = Color(0xFFEAA15F)
val DarkCardBg = Color(0xFF131D18)
val CleanWhite = Color(0xFFF7FFF7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicLiveMediaCenter(viewModel: IbadahViewModel, isBn: Boolean) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Collect States
    val favorites by viewModel.firebaseMediaBackend.dbFavorites.collectAsState()
    val recentlyPlayed by viewModel.firebaseMediaBackend.recentlyPlayed.collectAsState()
    val syncState by viewModel.firebaseMediaBackend.syncState.collectAsState()

    // UI States
    var selectedCategory by remember { mutableStateOf("all") } // all, live_tv, tv_channels, quran_radio, bangla_radio, arabic_radio, int_radio, favorites, recent
    var searchQuery by remember { mutableStateOf("") }
    var activeStream by remember { mutableStateOf<IbadahMediaStream?>(null) }
    var isMiniPlayerActive by remember { mutableStateOf(false) }
    var showFullscreenDialog by remember { mutableStateOf(false) }

    // Sleep Timer states
    var showTimerDialog by remember { mutableStateOf(false) }

    // Filter Streams based on Category and Search
    val filteredStreams = remember(selectedCategory, searchQuery, favorites, recentlyPlayed) {
        val baseList = when (selectedCategory) {
            "all" -> viewModel.customIslamicLiveStreams
            "favorites" -> favorites
            "recent" -> recentlyPlayed
            else -> viewModel.customIslamicLiveStreams.filter { it.category == selectedCategory }
        }
        if (searchQuery.isBlank()) {
            baseList
        } else {
            baseList.filter {
                it.titleBn.contains(searchQuery, ignoreCase = true) ||
                        it.titleEn.contains(searchQuery, ignoreCase = true) ||
                        it.subtitleBn.contains(searchQuery, ignoreCase = true) ||
                        it.subtitleEn.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // Categories definition
    val categories = listOf(
        CategoryInfo("all", "🔍 সব চ্যানেল", "🔍 All Feeds"),
        CategoryInfo("favorites", "⭐ প্রিয় তালিকা", "⭐ Favorites"),
        CategoryInfo("recent", "🕒 সম্প্রতি চালিত", "🕒 Recently Played"),
        CategoryInfo("live_tv", "📺 প্রধান লাইভ টিভি", "📺 Main Live TV"),
        CategoryInfo("tv_channels", "📡 ইসলামিক টিভি", "📡 Islamic TV"),
        CategoryInfo("quran_radio", "🕌 কুরআন রেডিও", "🕌 Quran Radios"),
        CategoryInfo("bangla_radio", "🇧🇩 বাংলা রেডিও", "🇧🇩 Bangla Radios"),
        CategoryInfo("arabic_radio", "🇸🇦 আরবি রেডিও", "🇸🇦 Arabic Radios"),
        CategoryInfo("int_radio", "🌐 আন্তর্জাতিক রেডিও", "🌐 Int'l Radios")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DeepEmerald, DarkCardBg)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            // Header Section with Title and Cloud status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBn) "ইসলামিক লাইভ মিডিয়া সেন্টার" else "Islamic Live Media Center",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralGold,
                        modifier = Modifier.testTag("media_center_title")
                    )
                    Text(
                        text = if (isBn) "Media3 ExoPlayer দ্বারা পরিচালিত" else "Powered by Media3 ExoPlayer Engine",
                        fontSize = 11.sp,
                        color = LightMint.copy(alpha = 0.8f)
                    )
                }

                // Cloud Status Badge
                Card(
                    onClick = {
                        viewModel.firebaseMediaBackend.forceSync()
                    },
                    colors = CardDefaults.cardColors(containerColor = SoftEmerald),
                    border = BorderStroke(1.dp, CoralGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val pulseColor = remember { mutableStateOf(Color.Green) }
                        LaunchedEffect(syncState) {
                            if (syncState == FirebaseMediaCenterBackend.SyncState.SYNCING) {
                                while (true) {
                                    pulseColor.value = Color.Yellow
                                    delay(400)
                                    pulseColor.value = Color.Transparent
                                    delay(400)
                                }
                            } else if (syncState == FirebaseMediaCenterBackend.SyncState.ERROR) {
                                pulseColor.value = Color.Red
                            } else {
                                pulseColor.value = Color.Green
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(pulseColor.value)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = when (syncState) {
                                FirebaseMediaCenterBackend.SyncState.SYNCING -> if (isBn) "ব্যাকআপ হচ্ছে" else "Syncing..."
                                FirebaseMediaCenterBackend.SyncState.ERROR -> if (isBn) "অফলাইন" else "Error"
                                else -> if (isBn) "ক্লাউড সংরক্ষিত" else "Cloud Synced"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CleanWhite
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = if (isBn) "চ্যানেল বা রেডিওর নাম খুজুন..." else "Search live stream or radio station...",
                        color = CleanWhite.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = CoralGold
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear search", tint = CleanWhite)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CoralGold,
                    unfocusedBorderColor = CoralGold.copy(alpha = 0.3f),
                    focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                ),
                textStyle = androidx.compose.ui.text.TextStyle(color = CleanWhite),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("media_center_search")
            )

            // Category horizontally scrollable list
            LazyRow(
                modifier = Modifier.padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val selected = selectedCategory == category.id
                    FilterChip(
                        selected = selected,
                        onClick = { selectedCategory = category.id },
                        label = {
                            Text(
                                text = if (isBn) category.titleBn else category.titleEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralGold,
                            selectedLabelColor = DeepEmerald,
                            containerColor = SoftEmerald.copy(alpha = 0.6f),
                            labelColor = CleanWhite
                        ),
                        border = BorderStroke(1.dp, if (selected) CoralGold else CoralGold.copy(alpha = 0.15f))
                    )
                }
            }

            // Sleep Timer and Playback Control Quick Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.Timer, contentDescription = "Sleep timer", tint = LightMint, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBn) "স্লিপ টাইমার অটো ফিনিশ" else "Sleep Auto Timer Close",
                        fontSize = 11.sp,
                        color = CleanWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = { showTimerDialog = true },
                    colors = ButtonDefaults.textButtonColors(contentColor = CoralGold)
                ) {
                    val sleepTimerMinutes by viewModel.exoPlayerManager.sleepTimerMinutes.collectAsState()
                    val timerStatusText = if (sleepTimerMinutes > 0) {
                        if (isBn) "${sleepTimerMinutes} মিনিট বাকি" else "${sleepTimerMinutes}m left"
                    } else {
                        if (isBn) "টাইমার সেট করুন" else "Set Sleep Timer"
                    }
                    Text(
                        text = timerStatusText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Main feeds List
            if (filteredStreams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = CoralGold.copy(alpha = 0.6f), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) "এই ক্যাটাগরিতে কোনো চ্যানেল খুঁজে পাওয়া যায়নি।" else "No stations found in this category.",
                            color = CleanWhite.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredStreams, key = { it.id }) { stream ->
                        val isPlaying = activeStream?.id == stream.id && !isMiniPlayerActive
                        val isFav = viewModel.firebaseMediaBackend.isFavorite(stream.id)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stream_card_${stream.id}")
                                .clickable {
                                    activeStream = stream
                                    isMiniPlayerActive = false
                                    showFullscreenDialog = true

                                    // Add to recently played
                                    viewModel.firebaseMediaBackend.addToRecent(stream)

                                    // Play the stream via manager
                                    viewModel.exoPlayerManager.playStream(
                                        url = stream.streamUrl,
                                        title = if (isBn) stream.titleBn else stream.titleEn,
                                        subtitle = if (isBn) stream.subtitleBn else stream.subtitleEn,
                                        isVideo = stream.isVideo
                                    )
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isPlaying) SoftEmerald else DarkCardBg
                            ),
                            border = BorderStroke(1.dp, if (isPlaying) CoralGold else CoralGold.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thumbnail
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(stream.thumbnail)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = stream.titleEn,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Live Badge or Media Type Badge
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(4.dp)
                                            .background(
                                                if (stream.isVideo) Color.Red else CoralGold,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (stream.isVideo) "LIVE TV" else "RADIO",
                                            color = Color.White,
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                // Content Details
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isBn) stream.titleBn else stream.titleEn,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = CleanWhite,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isBn) stream.subtitleBn else stream.subtitleEn,
                                        fontSize = 11.sp,
                                        color = LightMint.copy(alpha = 0.7f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (stream.isVideo) Icons.Default.Tv else Icons.Default.Radio,
                                            contentDescription = null,
                                            tint = CoralGold,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isBn) {
                                                if (stream.isVideo) "ভিডিও সরাসরি সম্প্রচার" else "অডিও সরাসরি সম্প্রচার"
                                            } else {
                                                if (stream.isVideo) "Video Live Stream" else "Audio Stream Feed"
                                            },
                                            fontSize = 9.sp,
                                            color = CoralGold,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                // Favorite Toggle & Action Play icon
                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(
                                        onClick = {
                                            viewModel.firebaseMediaBackend.toggleFavorite(stream)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorite Toggle",
                                            tint = if (isFav) Color.Red else CleanWhite.copy(alpha = 0.5f)
                                        )
                                    }

                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = "Status indicator",
                                        tint = if (isPlaying) CoralGold else LightMint.copy(alpha = 0.8f),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mini Floating In-App Simulated Picture-In-Picture Player
        if (isMiniPlayerActive && activeStream != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 90.dp, end = 16.dp)
                        .width(180.dp)
                        .shadow(12.dp, RoundedCornerShape(12.dp))
                        .background(Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, CoralGold)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            // Mini Player View
                            if (activeStream?.isVideo == true) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .background(Color.Black)
                                ) {
                                    AndroidView(
                                        factory = { ctx ->
                                            androidx.media3.ui.PlayerView(ctx).apply {
                                                player = viewModel.exoPlayerManager.getPlayerInstance()
                                                useController = false
                                                resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                            }
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            } else {
                                // Beautiful Audio Mini Visualizer
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .background(SoftEmerald),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        // Dynamic animated Equalizer bars
                                        repeat(5) {
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(24.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                                    .background(CoralGold)
                                            )
                                        }
                                    }
                                }
                            }

                            // Info details
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(DarkCardBg)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isBn) activeStream!!.titleBn else activeStream!!.titleEn,
                                        color = CleanWhite,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            showFullscreenDialog = true
                                            isMiniPlayerActive = false
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Fullscreen, contentDescription = "Maximize", tint = CoralGold, modifier = Modifier.size(16.dp))
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = {
                                            viewModel.exoPlayerManager.stop()
                                            isMiniPlayerActive = false
                                            activeStream = null
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Red, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Sleep Timer selection Dialog
        if (showTimerDialog) {
            AlertDialog(
                onDismissRequest = { showTimerDialog = false },
                title = { Text(if (isBn) "স্লিপ টাইমার অটো অফ" else "Sleep Timer Control", color = CoralGold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (isBn) "কয় মিনিট পর ব্রডকাস্ট বন্ধ করতে চান?" else "Choose duration to automatically pause stream:")
                        val options = listOf(
                            TimerOption(10, if (isBn) "১০ মিনিট" else "10 Minutes"),
                            TimerOption(15, if (isBn) "১৫ মিনিট" else "15 Minutes"),
                            TimerOption(30, if (isBn) "৩০ মিনিট" else "30 Minutes"),
                            TimerOption(60, if (isBn) "১ ঘণ্টা" else "1 Hour"),
                            TimerOption(120, if (isBn) "২ ঘণ্টা" else "2 Hours")
                        )
                        options.forEach { opt ->
                            Button(
                                onClick = {
                                    viewModel.exoPlayerManager.setSleepTimer(opt.minutes)
                                    showTimerDialog = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = SoftEmerald)
                            ) {
                                Text(opt.label, color = CleanWhite)
                            }
                        }
                        Button(
                            onClick = {
                                viewModel.exoPlayerManager.setSleepTimer(0)
                                showTimerDialog = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                        ) {
                            Text(if (isBn) "টাইমার বন্ধ করুন" else "Cancel Timer", color = Color.White)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTimerDialog = false }) {
                        Text(if (isBn) "বন্ধ করুন" else "Close", color = CoralGold)
                    }
                },
                containerColor = DarkCardBg
            )
        }

        // Stunning Native aspect-ratio Fullscreen floating player Dialog
        if (showFullscreenDialog && activeStream != null) {
            val isBuffering by viewModel.exoPlayerManager.isBuffering.collectAsState()
            val isPlaying by viewModel.exoPlayerManager.isPlaying.collectAsState()
            val playbackSpeed by viewModel.exoPlayerManager.playbackSpeed.collectAsState()

            Dialog(
                onDismissRequest = {
                    // Back to floating picture-in-picture mode so it continues in background!
                    showFullscreenDialog = false
                    isMiniPlayerActive = true
                },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    androidx.compose.ui.viewinterop.AndroidView(
                        factory = { ctx ->
                            try {
                                androidx.media3.ui.PlayerView(ctx).apply {
                                    player = viewModel.exoPlayerManager.getPlayerInstance()
                                    useController = true
                                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                androidx.media3.ui.PlayerView(ctx)
                            }
                        },
                        update = { playerView ->
                            try {
                                playerView.player = viewModel.exoPlayerManager.getPlayerInstance()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        onRelease = { playerView ->
                            try {
                                playerView.player = null
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay loading / Buffering with auto reconnect indicator
                    if (isBuffering) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = CoralGold, modifier = Modifier.size(44.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = if (isBn) "লোড হচ্ছে ও রিকানেক্ট চেষ্টা করা হচ্ছে..." else "Reconnecting stream cleanly...",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (isBn) "নেটওয়ার্ক ড্রপ বা বাফার স্পিড রিকভারি একটিভ" else "Auto recover & network loss guard active",
                                    color = LightMint,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Elegant translucent Control header overlay with rounded bounds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(14.dp)
                            .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            FilledIconButton(
                                onClick = {
                                    // Minimize to Picture in Picture floating view
                                    showFullscreenDialog = false
                                    isMiniPlayerActive = true
                                },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Minimize")
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isBn) activeStream!!.titleBn else activeStream!!.titleEn,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isBn) activeStream!!.subtitleBn else activeStream!!.subtitleEn,
                                    color = LightMint,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Close completely button
                        IconButton(
                            onClick = {
                                viewModel.exoPlayerManager.stop()
                                showFullscreenDialog = false
                                activeStream = null
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close stream", tint = Color.Red)
                        }
                    }

                    // Player Control Speed Modifier (0.5x - 3x) Overlay at Bottom left (Elevated above navigation bars and ExoPlayer controller)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .navigationBarsPadding()
                            .padding(bottom = 140.dp, start = 16.dp, end = 16.dp)
                            .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = CoralGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "গতি: ${playbackSpeed}x" else "Speed: ${playbackSpeed}x",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        // Circular Slow Trigger Button (-)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val current = playbackSpeed
                                    if (current > 0.5f) {
                                        viewModel.exoPlayerManager.setSpeed(current - 0.25f)
                                    }
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "−",
                                color = CoralGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Circular Fast Trigger Button (+)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val current = playbackSpeed
                                    if (current < 3.0f) {
                                        viewModel.exoPlayerManager.setSpeed(current + 0.25f)
                                    }
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                color = CoralGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CategoryInfo(
    val id: String,
    val titleBn: String,
    val titleEn: String
)

data class TimerOption(
    val minutes: Int,
    val label: String
)
