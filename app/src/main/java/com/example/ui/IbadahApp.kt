package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import java.util.Calendar
import java.util.Date

val dailyHadiths = listOf(
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'উত্তম মানুষ সে-ই যার চরিত্র সবচেয়ে সুন্দর।' (বুখারী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'নামাজ হচ্ছে জান্নাতের চাবিকাঠি।' (আহমাদ)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা আল্লাহর ইবাদত করো এবং তাঁর সাথে কাউকে শরিক কোরো না।' (বুখারী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'মুচকি হাসাও একটি সাদকা।' (তিরমিযী)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি আল্লাহর প্রতি ও আখেরাতের প্রতি ঈমান রাখে, সে যেন ভালো কথা বলে অথবা নীরব থাকে।' (বুখারী)",
        "1. The Prophet (PBUH) said: 'The best of you are those who have the best character.' (Bukhari)  ★  2. The Prophet (PBUH) said: 'Prayer is the key to Paradise.' (Ahmad)  ★  3. The Prophet (PBUH) said: 'Worship Allah and associate nothing with Him.' (Bukhari)  ★  4. The Prophet (PBUH) said: 'Smiling is a charity.' (Tirmidhi)  ★  5. The Prophet (PBUH) said: 'He who believes in Allah and the Last Day must either speak good or remain silent.' (Bukhari)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যে কুরআন শেখে এবং অন্যকে শেখায়।' (বুখারী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'নিশ্চয়ই আল্লাহ সুন্দর, তিনি সৌন্দর্য পছন্দ করেন।' (মুসলিম)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'সবচেয়ে ভারী জিনিস যা কিয়ামতের দিন মুমিনের পাল্লায় রাখা হবে তা হলো উত্তম চরিত্র।' (তিরমিযী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'ধৈর্য আলোস্বরূপ।' (মুসলিম)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'দোয়া হলো ইবাদতের মগজ।' (তিরমিযী)",
        "1. The Prophet (PBUH) said: 'The best among you are those who learn the Quran and teach it.' (Bukhari)  ★  2. The Prophet (PBUH) said: 'Indeed, Allah is Beautiful and He loves beauty.' (Muslim)  ★  3. The Prophet (PBUH) said: 'The heaviest thing on the Scale of the believer on Resurrection Day will be good character.' (Tirmidhi)  ★  4. The Prophet (PBUH) said: 'Patience is a shining light.' (Muslim)  ★  5. The Prophet (PBUH) said: 'Supplication is the essence of worship.' (Tirmidhi)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'সহজ করো, কঠিন করো না; সুসংবাদ দাও, দূরে সরিয়ে দিও না।' (বুখারী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'পবিত্রতা ঈমানের অর্ধেক।' (মুসলিম)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি মানুষের শুকরিয়া আদায় করে না, সে আল্লাহরও শুকরিয়া আদায় করে না।' (আবু দাউদ)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'প্রতিটি ভালো কাজই একটি সদকা।' (বুখারী)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'প্রকৃত মুসলিম সে, যার জিহ্বা ও হাত থেকে অন্য মুসলিম নিরাপদ থাকে।' (বুখারী)",
        "1. The Prophet (PBUH) said: 'Facilitate things and do not make them difficult; give glad tidings and do not scare people away.' (Bukhari)  ★  2. The Prophet (PBUH) said: 'Purity is half of faith.' (Muslim)  ★  3. The Prophet (PBUH) said: 'He who does not thank people does not thank Allah.' (Abu Dawud)  ★  4. The Prophet (PBUH) said: 'Every good deed is charity.' (Bukhari)  ★  5. The Prophet (PBUH) said: 'A Muslim is the one from whose tongue and hand other Muslims are safe.' (Bukhari)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি জ্ঞানার্জনের উদ্দেশ্যে বের হয়, সে ফিরে আসা পর্যন্ত আল্লাহর রাস্তায় থাকে।' (তিরমিযী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা একে অপরকে উপহার দাও, ভালোবাসা বৃদ্ধি পাবে।' (আল-আদাবুল মুফরাদ)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'ক্রোধের সময় নিজেকে নিয়ন্ত্রণকারী ব্যক্তিই প্রকৃত শক্তিশালী।' (বুখারী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'লোভ-লালসা থেকে বেঁচে থাকো, কারণ তা তোমাদের পূর্বসূরিদের ধ্বংস করেছে।' (মুসলিম)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমাদের কেউ ঈমানদার হতে পারবে না যতক্ষণ না সে তার ভাইয়ের জন্য তা পছন্দ করে যা নিজের জন্য করে।' (বুখারী)",
        "1. The Prophet (PBUH) said: 'Whoever goes out seeking knowledge is in the path of Allah until he returns.' (Tirmidhi)  ★  2. The Prophet (PBUH) said: 'Give gifts to each other, and you will love each other.' (Al-Adab Al-Mufrad)  ★  3. The Prophet (PBUH) said: 'The strong is not the one who overcomes people, but the one who controls himself in anger.' (Bukhari)  ★  4. The Prophet (PBUH) said: 'Avoid greed/covetousness, for it destroyed those before you.' (Muslim)  ★  5. The Prophet (PBUH) said: 'None of you believes until he loves for his brother what he loves for himself.' (Bukhari)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'যে দয়াবান নয়, তার ওপর দয়া করা হয় না।' (বুখারী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'ভালো কাজের পথপ্রদর্শনকারী ব্যক্তি আমলকারীর সমপরিমাণ সওয়াব পায়।' (মুসলিম)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'লজ্জাশীলতা ঈমানের একটি শাখা।' (বুখারী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা হিংসা থেকে বেঁচে থাকো, কেননা হিংসা নেক আমল খেয়ে ফেলে যেমন আগুন কাঠ খেয়ে ফেলে।' (আবু দাউদ)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'নিশ্চয়ই শেষ রাতের দোয়া কবুল হয়।' (মুসলিম)",
        "1. The Prophet (PBUH) said: 'He who is not merciful will not be shown mercy.' (Bukhari)  ★  2. The Prophet (PBUH) said: 'The one who guides to good is like the one who does it.' (Muslim)  ★  3. The Prophet (PBUH) said: 'Modesty is a branch of faith.' (Bukhari)  ★  4. The Prophet (PBUH) said: 'Avoid jealousy, for jealousy consumes good deeds as fire consumes wood.' (Abu Dawud)  ★  5. The Prophet (PBUH) said: 'Verily, prayers in the last part of the night are accepted.' (Muslim)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'দান-সদকা কখনো সম্পদ হ্রাস করে না।' (মুসলিম)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা সহজ করো এবং সহজ আচরণ করো।' (বুখারী)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'সবচেয়ে প্রিয় আমল হলো যা নিয়মিত করা হয়, তা অল্প হলেও।' (বুখারী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'দুনিয়া মুমিনের জন্য কারাগার এবং কাফেরের জন্য জান্নাত।' (মুসলিম)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'মুমিন ব্যক্তি একই গর্ত থেকে দুবার দংশিত হয় না।' (বুখারী)",
        "1. The Prophet (PBUH) said: 'Charity does not decrease wealth.' (Muslim)  ★  2. The Prophet (PBUH) said: 'Be gentle and easy-going with people.' (Bukhari)  ★  3. The Prophet (PBUH) said: 'The most beloved deed to Allah is what is done regularly, even if it is small.' (Bukhari)  ★  4. The Prophet (PBUH) said: 'The world is a prison for the believer and a paradise for the disbeliever.' (Muslim)  ★  5. The Prophet (PBUH) said: 'A believer is not stung from the same hole twice.' (Bukhari)"
    ),
    Pair(
        "১. রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি নীরবতা অবলম্বন করে, সে মুক্তি পায়।' (তিরমিযী)  ★  ২. রাসূলুল্লাহ (সা.) বলেছেন: 'প্রত্যেক ভালো কাজই একটি সদকা।' (বুখারী)  ★  ৩. রাসূলুল্লাহ (সা.) বলেছেন: 'খাদ্য খাওয়াও এবং চেনা-অচেনা সবাইকে সালাম দাও।' (বুখারী)  ★  ৪. রাসূলুল্লাহ (সা.) বলেছেন: 'মায়ের পায়ের নিচে সন্তানের জান্নাত।' (নাসায়ী)  ★  ৫. রাসূলুল্লাহ (সা.) বলেছেন: 'ভালো কথা বলাও একটি সদকা।' (বুখারী)",
        "1. The Prophet (PBUH) said: 'Whoever remains silent is saved.' (Tirmidhi)  ★  2. The Prophet (PBUH) said: 'Every good deed is charity.' (Bukhari)  ★  3. The Prophet (PBUH) said: 'Feed the hungry and spread peace (Salams) to those you know and those you do not know.' (Bukhari)  ★  4. The Prophet (PBUH) said: 'Paradise lies under the feet of mothers.' (Nasai)  ★  5. The Prophet (PBUH) said: 'A good word is a charity.' (Bukhari)"
    )
)

@Composable
fun HadithScrollingTicker(text: String, modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    
    LaunchedEffect(text) {
        while (true) {
            if (scrollState.maxValue > 0) {
                scrollState.animateScrollTo(
                    value = scrollState.maxValue,
                    animationSpec = tween<Float>(
                        durationMillis = (scrollState.maxValue * 18).coerceAtLeast(8000),
                        easing = LinearEasing
                    )
                )
                delay(1500)
                scrollState.scrollTo(0)
                delay(1000)
            } else {
                delay(1000)
            }
        }
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState, false),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontStyle = FontStyle.Italic,
            softWrap = false,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

fun getCurrentDates(language: AppLanguage, timezoneId: String = ""): Triple<String, String, String> {
    val isBn = language == AppLanguage.BN
    val isAr = language == AppLanguage.AR
    
    val zone = if (timezoneId.isNotEmpty()) java.util.TimeZone.getTimeZone(timezoneId) else java.util.TimeZone.getDefault()
    val calendar = java.util.Calendar.getInstance(zone)
    val year = calendar.get(java.util.Calendar.YEAR)
    val month = calendar.get(java.util.Calendar.MONTH) // 0-indexed (0 = Jan, 5 = Jun)
    val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
    val dayOfYear = calendar.get(java.util.Calendar.DAY_OF_YEAR)
    
    // 1. Gregorian (English) Date representation
    val monthNamesEn = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val monthNamesBn = listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
    val monthNamesAr = listOf("يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر")
    
    val englishDateStr = when {
        isBn -> "${convertToBanglaDigits(day.toString())} ${monthNamesBn[month]} ${convertToBanglaDigits(year.toString())} খ্রিষ্টাব্দ"
        isAr -> "${convertToArabicDigits(day.toString())} ${monthNamesAr[month]} ${convertToArabicDigits(year.toString())} م"
        else -> "${monthNamesEn[month]} $day, $year AD"
    }
    
    // 2. Hijri (Arabic) Date calculation
    var hDay = 12
    var hMonthIndex = 8 // Default to Ramadan (0-indexed) or Zul-Hijjah
    var hYear = 1447
    
    // Standard Java HijrahDate can calculate it dynamically!
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        try {
            val zoneId = if (timezoneId.isNotEmpty()) java.time.ZoneId.of(timezoneId) else java.time.ZoneId.systemDefault()
            val today = java.time.LocalDate.now(zoneId)
            val hijriDate = java.time.chrono.HijrahDate.from(today)
            hDay = hijriDate.get(java.time.temporal.ChronoField.DAY_OF_MONTH)
            hMonthIndex = hijriDate.get(java.time.temporal.ChronoField.MONTH_OF_YEAR) - 1
            hYear = hijriDate.get(java.time.temporal.ChronoField.YEAR)
        } catch (e: Exception) {
            // Fallback math-based calculation if API fails
            val jd = dayOfYear + 365 * (year - 2026) // Simple rough estimate
            hDay = ((jd + 11) % 30) + 1
            hMonthIndex = ((jd / 30) + 11) % 12
            hYear = 1447 + (jd / 355)
        }
    } else {
        val jd = dayOfYear + 365 * (year - 2026)
        hDay = ((jd + 11) % 30) + 1
        hMonthIndex = ((jd / 30) + 11) % 12
        hYear = 1447 + (jd / 355)
    }
    
    val hijriMonthsBn = listOf("মহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি", "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান", "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ্জ")
    val hijriMonthsEn = listOf("Muharram", "Safar", "Rabi' al-Awwal", "Rabi' ath-Thani", "Jumada al-Awwal", "Jumada ath-Thani", "Rajab", "Sha'ban", "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhul-Hijjah")
    val hijriMonthsAr = listOf("محرم", "صفر", "ربيع الأول", "ربيع الثاني", "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة")
    
    val hMonthIndexCoerced = hMonthIndex.coerceIn(0, 11)
    val hijriDateStr = when {
        isBn -> "${convertToBanglaDigits(hDay.toString())} ${hijriMonthsBn[hMonthIndexCoerced]}, ${convertToBanglaDigits(hYear.toString())} হিজরি"
        isAr -> "${convertToArabicDigits(hDay.toString())} ${hijriMonthsAr[hMonthIndexCoerced]} ${convertToArabicDigits(hYear.toString())} هـ"
        else -> "$hDay ${hijriMonthsEn[hMonthIndexCoerced]} $hYear AH"
    }
    
    // 3. Bangla Date calculation
    // Pohela Boishakh (1st Boishakh) is April 14th standard of the modern Gregorian calendar
    // Let's calculate the Bangla year:
    // The Bangla year is Gregorian year - 593 (from April 14 to Dec 31) and - 594 (from Jan 1 to April 13)
    val isAfterApril14 = (month > 3) || (month == 3 && day >= 14)
    val banglaYear = if (isAfterApril14) year - 593 else year - 594
    
    // Get day of month in Bangla calendar:
    // Simple estimation:
    val monthsDaysInGregorian = listOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    // Let's accumulate days since April 14
    var daysSinceApril14 = 0
    if (isAfterApril14) {
        // Inside the same Gregorian year
        var currentMonth = 3 // April
        var currentDay = 14
        while (currentMonth < month) {
            daysSinceApril14 += (monthsDaysInGregorian[currentMonth] - currentDay) + 1
            currentMonth++
            currentDay = 1
        }
        daysSinceApril14 += (day - currentDay)
    } else {
        // Crossed greg year boundary. Days from April 14 of old year to Dec 31
        var currentMonth = 3
        var currentDay = 14
        while (currentMonth < 12) {
            val daysInThisMonth = if (currentMonth == 1 && ((year-1)%4 == 0)) 29 else monthsDaysInGregorian[currentMonth]
            daysSinceApril14 += (daysInThisMonth - currentDay) + 1
            currentMonth++
            currentDay = 1
        }
        // Days of new year up to today
        currentMonth = 0
        while (currentMonth < month) {
            val daysInThisMonth = if (currentMonth == 1 && (year%4 == 0)) 29 else monthsDaysInGregorian[currentMonth]
            daysSinceApril14 += daysInThisMonth
            currentMonth++
        }
        daysSinceApril14 += day
    }
    
    // Map daysSinceApril14 to Bangla Month
    val banglaMonthLengths = listOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30) // Revised Bangladesh structure
    val banglaMonthsEn = listOf("Boishakh", "Jaistha", "Ashar", "Shravan", "Bhadra", "Ashwin", "Kartik", "Agrahayan", "Poush", "Magh", "Falgun", "Chaitra")
    val banglaMonthsBn = listOf("বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন", "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র")
    
    var bMonthIndex = 0
    var bDay = daysSinceApril14 + 1
    while (bMonthIndex < 12 && bDay > banglaMonthLengths[bMonthIndex]) {
        bDay -= banglaMonthLengths[bMonthIndex]
        bMonthIndex++
    }
    
    val bMonthIndexCoerced = bMonthIndex.coerceIn(0, 11)
    val banglaDateStr = when {
        isBn -> "${convertToBanglaDigits(bDay.toString())} ${banglaMonthsBn[bMonthIndexCoerced]}, ${convertToBanglaDigits(banglaYear.toString())} বঙ্গাব্দ"
        else -> "$bDay ${banglaMonthsEn[bMonthIndexCoerced]}, $banglaYear BS"
    }
    
    return Triple(hijriDateStr, banglaDateStr, englishDateStr)
}

fun convertToBanglaDigits(input: String): String {
    val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    var result = input
    for (i in 0..9) {
        result = result.replace(englishDigits[i], banglaDigits[i])
    }
    return result
}

fun convertToArabicDigits(input: String): String {
    val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
    val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    var result = input
    for (i in 0..9) {
        result = result.replace(englishDigits[i], arabicDigits[i])
    }
    return result
}

fun formatTo12Hour(time24h: String, language: AppLanguage): String {
    return try {
        val parts = time24h.split(":")
        var hr = parts[0].trim().toInt()
        val min = parts[1].trim().toInt()
        val suffix = if (hr >= 12) " PM" else " AM"
        val suffixBn = if (hr >= 12) " পিএম" else " এএম"
        val suffixAr = if (hr >= 12) " م" else " ص"
        
        if (hr > 12) hr -= 12
        if (hr == 0) hr = 12
        
        val endSuf = when (language) {
            AppLanguage.BN -> suffixBn
            AppLanguage.AR -> suffixAr
            else -> suffix
        }
        
        val rawTime = String.format("%02d:%02d", hr, min) + endSuf
        when (language) {
            AppLanguage.BN -> {
                val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
                val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
                var result = rawTime
                for (i in 0..9) {
                    result = result.replace(englishDigits[i], banglaDigits[i])
                }
                result
            }
            AppLanguage.AR -> {
                val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
                val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
                var result = rawTime
                for (i in 0..9) {
                    result = result.replace(englishDigits[i], arabicDigits[i])
                }
                result
            }
            else -> rawTime
        }
    } catch (e: Exception) {
        time24h
    }
}

// Primary structure
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IbadahApp(viewModel: IbadahViewModel) {
    val isDark by viewModel.isDarkMode.collectAsState()
    val language by viewModel.appLanguage.collectAsState()
    
    val selectedCity by viewModel.selectedCity.collectAsState()
    val currentClockTime by viewModel.currentClockTime.collectAsState()
    val isBn = language == AppLanguage.BN

    val hasSeenOnboarding by viewModel.hasSeenOnboarding.collectAsState()
    val showOnboarding = !hasSeenOnboarding
    var selectedTab by remember { mutableStateOf(0) } // 0: Home/Prayer, 1: Schedule, 2: Quran, 3: Compass & Dhikr, 4: Tools & Ramadan, 5: Live & AI, 6: Settings / Notice
    var homeSubView by remember { mutableStateOf("dashboard") }

    val selectedSurah by viewModel.selectedSurah.collectAsState()

    // Natural Back Navigation: If reading a Quran Surah, close the Surah first.
    // If in any Home subview, return to main dashboard.
    // If on any tab other than Home, return to Home.
    BackHandler(enabled = !showOnboarding && (selectedSurah != null || homeSubView != "dashboard" || selectedTab != 0)) {
        if (selectedSurah != null) {
            viewModel.selectSurah(null)
        } else if (homeSubView != "dashboard") {
            homeSubView = "dashboard"
        } else if (selectedTab != 0) {
            selectedTab = 0
        }
    }

    val context = LocalContext.current
    
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            fetchDeviceLocation(context, viewModel)
        } else {
            val msg = if (isBn) "লোকেশন পারমিশন মঞ্জুর করা হয়নি" else "Location permission denied"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.playNextBgMusic()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val hasNotificationPermission = androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!hasNotificationPermission) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val currentDeedDate by viewModel.deedSelectedDate.collectAsState()
    androidx.compose.runtime.DisposableEffect(context, currentDeedDate) {
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(ctx: android.content.Context?, intent: android.content.Intent?) {
                viewModel.loadDeedsForDate(currentDeedDate)
                viewModel.loadMonthTrackerSummary()
            }
        }
        val filter = android.content.IntentFilter("com.example.action.IBADAH_DEEDS_DATA_CHANGED")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.registerReceiver(receiver, filter, android.content.Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }
        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    val activeAlarmFiring by viewModel.activeAlarmFiring.collectAsState()
    activeAlarmFiring?.let { alarm ->
        AlertDialog(
            onDismissRequest = { viewModel.dismissActiveAlarm() },
            icon = {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
            },
            title = {
                Text(
                    text = if (isBn) "নামাজের সময় শুরু হয়েছে!" else "Prayer Time Alert!",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text(
                        text = if (isBn) "ওয়াক্ত: ${alarm.nameBn}" else "Waqt: ${alarm.nameEn}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (alarm.isAzanAlarm) {
                            if (isBn) "মুয়াযযিনের কণ্ঠে আজান বাজছে..." else "Muezzin is calling Azan..."
                        } else {
                            if (isBn) "এলার্মের টোন বাজছে..." else "Ringtone is playing..."
                        },
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissActiveAlarm() }
                ) {
                    Text(if (isBn) "বন্ধ করুন" else "Stop/Dismiss")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { viewModel.snoozeActiveAlarm() }
                ) {
                    Text(if (isBn) "৫ মিনিট পর বাজুন" else "Snooze 5 Mins")
                }
            }
        )
    }

    MyApplicationTheme(darkTheme = isDark) {
        if (showOnboarding) {
            OnboardingScreen(
                onFinished = { viewModel.setHasSeenOnboarding(true) },
                isBn = isBn
            )
        } else {
            Scaffold(
            topBar = {
                if (selectedTab == 0 && homeSubView == "dashboard") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp) // Generous panoramic wallpaper view with extra clearance
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                    // Underlay the magnificent custom generated 3D Mecca Desert road wallpaper!
                    Image(
                        painter = painterResource(id = com.example.R.drawable.islamic_mecca_wallpaper),
                        contentDescription = "Makkah Road Wallpaper",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        alpha = 1.0f, // 100% full clear view with crisp sharpness
                        modifier = Modifier.fillMaxSize()
                    )

                    // Completely clear and crisp full view wallpaper without any blurring or dark gradient overlays!

                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = SoftGoldBorder.copy(alpha = 0.8f),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = com.example.R.drawable.dome_mimbar_logo),
                                    contentDescription = "Jannat Sondhan Dome & Mimbar Logo",
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                        .border(
                                            width = 1.dp,
                                            color = SoftGoldBorder,
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        ),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "জান্নাত সন্ধান" else "Jannat Sondhan",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = if (isBn) "ইসলামিক সুপার অ্যাপ" else "Islamic Super App",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        },
                        navigationIcon = {}, // Removed toggle dark mode button
                        actions = {
                            // Location Picker directly in Toolbar
                            var cityMenuExpanded by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.25f),
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                            ) {
                                IconButton(onClick = { cityMenuExpanded = true }) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Select City/Country",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                DropdownMenu(
                                    expanded = cityMenuExpanded,
                                    onDismissRequest = { cityMenuExpanded = false }
                                ) {
                                    DropdownMenuItem(
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.LocationSearching,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        text = {
                                            Text(
                                                text = if (isBn) "আমার অবস্থান" else "My Location",
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        },
                                        onClick = {
                                            cityMenuExpanded = false
                                            val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                            val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
                                            
                                            if (hasFine || hasCoarse) {
                                                fetchDeviceLocation(context, viewModel)
                                            } else {
                                                locationPermissionLauncher.launch(
                                                    arrayOf(
                                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                                    )
                                                )
                                            }
                                        }
                                    )
                                    HorizontalDivider()
                                    viewModel.cities.forEach { city ->
                                        DropdownMenuItem(
                                            text = { Text(if (isBn) city.nameBn else city.nameEn) },
                                            onClick = {
                                                viewModel.selectCity(city)
                                                cityMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )

                    // Unified GPS Location display and 3-Date Panel overlay on top of wallpaper (Time removed)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(bottom = 6.dp, start = 12.dp, end = 12.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // GPS Location name on top of wallpaper (Stacked with Area, District, Country as requested)
                        val fullLocation = if (language == AppLanguage.BN) selectedCity.nameBn else if (language == AppLanguage.AR) selectedCity.nameBn else selectedCity.nameEn
                        val locationParts = fullLocation.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        
                        val areaStr = if (locationParts.isNotEmpty()) locationParts[0] else ""
                        val districtStr = if (locationParts.size >= 3) {
                            locationParts[1]
                        } else if (locationParts.size == 2) {
                            locationParts[0] // Fallback same as area
                        } else {
                            ""
                        }
                        val countryStr = if (locationParts.size >= 2) locationParts.last() else ""

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.weight(1.4f) // Give location display a robust portion of row
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Active Location",
                                tint = SoftGoldBorder,
                                modifier = Modifier.size(18.dp).padding(top = 2.dp)
                            )
                            Column(
                                verticalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                if (areaStr.isNotEmpty()) {
                                    Text(
                                        text = areaStr,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontSize = if (areaStr.length > 15) 10.sp else 12.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (districtStr.isNotEmpty()) {
                                    Text(
                                        text = districtStr,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = if (districtStr.length > 15) 9.sp else 11.sp,
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                if (countryStr.isNotEmpty()) {
                                    Text(
                                        text = countryStr,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = if (countryStr.length > 15) 8.sp else 10.sp,
                                        color = Color.White.copy(alpha = 0.72f),
                                        fontWeight = FontWeight.Normal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // Date panel containing Hijri, Bangla, and English dates in neat alignment
                        val dateTriple = getCurrentDates(language, selectedCity.timezone)
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.weight(1f) // Balance row layout weight safely
                        ) {
                            // Arabic (Hijri) Date
                            Text(
                                text = dateTriple.first,
                                style = MaterialTheme.typography.labelSmall,
                                color = SoftGoldBorder,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                            // Bangla Date
                            Text(
                                text = dateTriple.second,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )
                            // English Date
                            Text(
                                text = dateTriple.third,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.82f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val items = listOf(
                        NavigationItem(if (isBn) "হোম" else "Home", Icons.Default.Home, 0),
                        NavigationItem(if (isBn) "সময়সূচী" else "Schedule", Icons.Default.AccessTime, 1),
                        NavigationItem(if (isBn) "কুরআন" else "Quran", Icons.Default.MenuBook, 2),
                        NavigationItem(if (isBn) "কিবলা" else "Qibla", Icons.Default.CompassCalibration, 3),
                        NavigationItem(if (isBn) "টুলস" else "Tools", Icons.Default.Build, 4),
                        NavigationItem(if (isBn) "মিডিয়া" else "Media", Icons.Default.OnlinePrediction, 5),
                        NavigationItem(if (isBn) "সেটিংস" else "Settings", Icons.Default.Settings, 6)
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            selected = selectedTab == item.index,
                            onClick = { 
                                if (item.index == 0) {
                                    homeSubView = "dashboard"
                                } else if (item.index == 2) {
                                    viewModel.selectSurah(null)
                                }
                                if (selectedTab != item.index) {
                                    viewModel.playNextBgMusic()
                                }
                                selectedTab = item.index 
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                            label = { Text(text = item.label, fontSize = 9.sp, overflow = TextOverflow.Ellipsis, maxLines = 1) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets.systemBars
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Full Screen background wallpaper with 100% elegant visibility so it looks completely clear & crisp!
                Image(
                    painter = painterResource(id = com.example.R.drawable.islamic_mecca_wallpaper),
                    contentDescription = "Full Background Wallpaper",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    alpha = 0.15f, // Beautiful subtle backdrop so text remains highly readable but the Mecca 3D road is clearly visible across all pages!
                    modifier = Modifier.fillMaxSize()
                )
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                    },
                    label = "TabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> HomeTabScreen(
                            viewModel = viewModel,
                            isBn = isBn,
                            homeSubView = homeSubView,
                            onHomeSubViewChange = { subView ->
                                if (subView == "quran") {
                                    viewModel.selectSurah(null)
                                    selectedTab = 2
                                    homeSubView = "dashboard"
                                } else if (subView == "qibla") {
                                    selectedTab = 3
                                    homeSubView = "dashboard"
                                } else {
                                    homeSubView = subView
                                }
                            },
                            onTabSelect = { selectedTab = it }
                        )
                        1 -> PrayerScheduleTabScreen(viewModel, isBn)
                        2 -> QuranTabScreen(viewModel, isBn)
                        3 -> CompassDhikrTabScreen(viewModel, isBn)
                        4 -> ToolsRamadanTabScreen(viewModel, isBn)
                        5 -> LiveAiTabScreen(viewModel, isBn)
                        6 -> SettingsNoticeTabScreen(viewModel, isBn)
                    }
                }

                // Beautiful, floating, non-blocking in-app notification banner for Surah downloads
                val quranDownloadState by viewModel.quranDownloadState.collectAsState()
                quranDownloadState?.let { download ->
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        border = BorderStroke(1.dp, SoftGoldBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (download.isCompleted) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                                contentDescription = null,
                                tint = if (download.isCompleted) EmeraldDeep else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (download.isCompleted) {
                                        if (isBn) "অডিও ডাউনলোড সম্পন্ন হয়েছে!" else "Audio Download Completed!"
                                    } else {
                                        if (isBn) "সূরা অডিও ডাউনলোড হচ্ছে..." else "Downloading Surah Audio..."
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${download.surahNumber}. ${if (isBn) download.nameBn else download.nameEn}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.82f)
                                )
                                if (!download.isCompleted) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { download.progress },
                                        modifier = Modifier.fillMaxWidth().height(4.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${(download.progress * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

// Data holder for bottom navigations
data class NavigationItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val index: Int)

private fun fetchDeviceLocation(context: android.content.Context, viewModel: IbadahViewModel) {
    val locationManager = context.getSystemService(android.content.Context.LOCATION_SERVICE) as? android.location.LocationManager
    if (locationManager == null) {
        android.widget.Toast.makeText(context, "Location service not available", android.widget.Toast.LENGTH_SHORT).show()
        return
    }
    try {
        val hasFine = androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarse = androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
        
        if (hasFine || hasCoarse) {
            val isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
            
            val provider = when {
                isGpsEnabled -> android.location.LocationManager.GPS_PROVIDER
                isNetworkEnabled -> android.location.LocationManager.NETWORK_PROVIDER
                else -> null
            }
            
            if (provider != null) {
                val loc = locationManager.getLastKnownLocation(provider) ?: locationManager.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER)
                if (loc != null) {
                    viewModel.updateToUserCoordinates(loc.latitude, loc.longitude, context)
                    val msg = if (viewModel.appLanguage.value == AppLanguage.BN) "জিপিএস লোকেশন আপডেট করা হয়েছে!" else "GPS Location updated successfully!"
                    android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
                } else {
                    locationManager.requestSingleUpdate(provider, object : android.location.LocationListener {
                        override fun onLocationChanged(location: android.location.Location) {
                            viewModel.updateToUserCoordinates(location.latitude, location.longitude, context)
                            val msg = if (viewModel.appLanguage.value == AppLanguage.BN) "জিপিএস লোকেশন সেট করা হয়েছে!" else "GPS Location updated successfully!"
                            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {}
                    }, android.os.Looper.getMainLooper())
                }
            } else {
                val msg = if (viewModel.appLanguage.value == AppLanguage.BN) "অনুগ্রহ করে মোবাইল জিপিএস চালু করুন" else "Please enable location services / GPS"
                android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// ===================================================================================
// 1. HOME & PRAYER TAB
// ===================================================================================
@Composable
fun QuickActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .themeOutlineShadow()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftMintBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = EmeraldMedium,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldDeep,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Extension placeholder helper for standard modifier styling
fun Modifier.themeOutlineShadow(): Modifier = this

@Composable
fun HomeTabScreen(
    viewModel: IbadahViewModel,
    isBn: Boolean,
    homeSubView: String,
    onHomeSubViewChange: (String) -> Unit,
    onTabSelect: (Int) -> Unit
) {
    when (homeSubView) {
        "hadith" -> HadithReaderScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "ramadan_cal" -> RamadanCalendarScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "tasbih" -> TasbihScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "zakat" -> ZakatScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "ai_chat" -> AiChatScreen(viewModel, isBn, onBack = { 
            viewModel.clearTranslationState()
            onHomeSubViewChange("dashboard") 
        })
        "allah_names" -> AllahNamesScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "duas" -> DuasScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "ramadan_eid" -> RamadanEidScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "prayer_education" -> PrayerEducationScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        "islamic_quiz" -> IslamicQuizScreen(viewModel, isBn, onBack = { onHomeSubViewChange("dashboard") })
        else -> HomeDashboardView(viewModel, isBn, onTabSelect, onSubViewSelected = { onHomeSubViewChange(it) })
    }
}

@Composable
fun DuasScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "দোয়া ও জিকির" else "Duas & Supplications",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "প্রতিদিনের দোয়া ও সকাল-সন্ধ্যার আমল" else "Daily prayers and morning-evening remembrance",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        DuasAndSupplicationSub(viewModel, isBn)
    }
}

@Composable
fun RamadanEidScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "রমজান ও ঈদ ট্র্যাকার" else "Ramadan & Eid Tracker",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "রোজা ট্র্যাকিং ও ঈদের আনন্দ কাউন্টডাউন" else "Fasting stats & Eid celebrations countdown",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        RamadanAndEidSub(viewModel, isBn)
    }
}

// ===================================================================================
// IN-HOME FEATURE 1: HADITH READER SCREEN (Serial-wise Bukhari & Others with Bookmarks)
// ===================================================================================
@Composable
fun HadithReaderScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    var selectedBookId by remember { mutableStateOf("bukhari") }
    var searchQuery by remember { mutableStateOf("") }
    var showBookmarksOnly by remember { mutableStateOf(false) }

    val hadithBookmarks by viewModel.hadithBookmarks.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Simple Back Bar
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "সহিহ হাদিস শরিফ" else "Authentic Hadith Reader",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "বুখারি, মুসলিম ও তিরমিযী শরিফ" else "Sahih Bukhari, Muslim & Tirmidhi",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search and Bookmarks Toggle Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(if (isBn) "শব্দ দিয়ে হাদিস খুঁজুন..." else "Search Hadith...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldMedium,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Bookmark Filter Button
            IconToggleButton(
                checked = showBookmarksOnly,
                onCheckedChange = { showBookmarksOnly = it },
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        if (showBookmarksOnly) IslamicGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Icon(
                    imageVector = if (showBookmarksOnly) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Filter Bookmarks",
                    tint = if (showBookmarksOnly) IslamicGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Book Tabs Selection (Only shown when Show Bookmarks is false)
        if (!showBookmarksOnly) {
            ScrollableTabRow(
                selectedTabIndex = HadithData.books.indexOfFirst { it.id == selectedBookId }.coerceAtLeast(0),
                containerColor = Color.Transparent,
                contentColor = EmeraldMedium,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                HadithData.books.forEach { book ->
                    Tab(
                        selected = selectedBookId == book.id,
                        onClick = { selectedBookId = book.id },
                        text = {
                            Text(
                                text = if (isBn) book.nameBn.split(" ")[0] else book.nameEn,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        } else {
            // Bookmarked view banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = IslamicGold.copy(alpha = 0.08f)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = IslamicGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "আপনার বুকমার্ক করা হাদিসসমূহ" else "Your Starred Bookmarked Hadiths",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Filtering Hadiths List
        val filteredHadiths = HadithData.hadiths.filter { hadith ->
            val matchesBook = showBookmarksOnly || hadith.bookId == selectedBookId
            val matchesStarred = !showBookmarksOnly || hadithBookmarks.contains(hadith.id)
            val matchesQuery = searchQuery.isEmpty() ||
                    hadith.translationBn.contains(searchQuery, ignoreCase = true) ||
                    hadith.translationEn.contains(searchQuery, ignoreCase = true) ||
                    hadith.chapterBn.contains(searchQuery, ignoreCase = true) ||
                    hadith.narratorBn.contains(searchQuery, ignoreCase = true)

            matchesBook && matchesStarred && matchesQuery
        }

        if (filteredHadiths.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBn) "কোনো হাদিস পাওয়া যায়নি" else "No Hadiths found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredHadiths) { hadith ->
                    val isStarred = hadithBookmarks.contains(hadith.id)
                    val bookName = HadithData.books.find { it.id == hadith.bookId }?.let {
                        if (isBn) it.nameBn else it.nameEn
                    } ?: "Bukhari"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Hadith Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "$bookName - " + (if (isBn) "হাদিস নং ${hadith.number}" else "Hadith #${hadith.number}"),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldMedium
                                    )
                                    Text(
                                        text = if (isBn) "অধ্যায়: ${hadith.chapterBn}" else "Chapter: ${hadith.chapterEn}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                                
                                Row {
                                    // Bookmark Button (Stars)
                                    IconButton(
                                        onClick = { viewModel.toggleHadithBookmark(hadith.id) },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isStarred) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Bookmark Star",
                                            tint = if (isStarred) IslamicGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // Clipboard Copy button
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val fullClipText = "${hadith.arabic}\n\n${hadith.translationBn}\n\n— ${hadith.narratorBn} ($bookName)"
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Hadith", fullClipText))
                                            Toast.makeText(context, if (isBn) "হাদিস টেক্সট অনুলিপি করা হয়েছে!" else "Hadith copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy to clipboard",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))

                            // Arabic text
                            Text(
                                text = hadith.arabic,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                textAlign = TextAlign.End,
                                fontFamily = FontFamily.Serif,
                                lineHeight = 30.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Bangla Translation
                            Text(
                                text = hadith.translationBn,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 21.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // English Translation
                            Text(
                                text = hadith.translationEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                fontStyle = FontStyle.Italic,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Narrator & Authenticity Grade
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isBn) "সূত্র: ${hadith.narratorBn}" else "Source: ${hadith.narratorEn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    modifier = Modifier.weight(1f)
                                )

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = EmeraldMedium.copy(alpha = 0.12f)),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (isBn) hadith.gradeBn else hadith.gradeEn,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldDeep,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE 2: RAMADAN CALENDAR SCREEN (Perpetual year-round 30 days)
// ===================================================================================
@Composable
fun RamadanCalendarScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    val ramadanCalendar by viewModel.ramadanCalendar.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Back Header
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "রমজান ক্যালেন্ডার" else "Ramadan Calendar",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "সারা বছরের ৩০ দিনের সময়সূচী (${selectedCity.nameBn})" else "Perpetual 30-Day Timings (${selectedCity.nameEn})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Table Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = EmeraldDeep),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = if (isBn) "রমজান" else "Ramadan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1f))
                Text(text = if (isBn) "তারিখ (ইং)" else "Date", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center)
                Text(text = if (isBn) "সেহরির শেষ" else "Sehri End", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
                Text(text = if (isBn) "ইফতার" else "Iftar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Infinite calendar rows
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(ramadanCalendar) { item ->
                // Highlight mock "Today" (say Ramadan 12 is today)
                val isToday = item.ramadanDay == 12

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isToday) EmeraldMedium.copy(alpha = 0.08f) 
                                         else MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isToday) EmeraldMedium else MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Day badge
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = if (isBn) "${item.ramadanDay} রমজান" else "Ramadan ${item.ramadanDay}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) EmeraldMedium else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Gregorian Date label
                        Text(
                            text = if (isBn) item.gregorianDateBn.split(",")[0] else item.gregorianDateEn.split(",")[0],
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            modifier = Modifier.weight(1.5f),
                            textAlign = TextAlign.Center
                        )

                        // Sehri Time
                        Row(
                            modifier = Modifier.weight(1.2f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NightsStay,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.sahriTimeLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Iftar Time
                        Row(
                            modifier = Modifier.weight(1.2f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.iftarTimeLabel,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) EmeraldMedium else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE 3: DEDICATED FULL DIGITAL TASBIH SCREEN
// ===================================================================================
@Composable
fun TasbihScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    val currentTasbihIndex by viewModel.currentTasbihPhraseIndex.collectAsState()
    val tasbihCount by viewModel.tasbihCount.collectAsState()
    val tasbihPhrases = if (isBn) viewModel.tasbihPhrasesBn else viewModel.tasbihPhrasesEn

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "ডিজিটাল তসবিহ" else "Digital Tasbih Counter",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "জিকিরের আমল ও গণনা" else "Dhikir & Prayers Counter",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBn) "জিকিরের বাক্য পরিবর্তন করতে চাপুন" else "Tap phrase to select next",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = tasbihPhrases[currentTasbihIndex],
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDeep,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(EmeraldMedium.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .clickable { viewModel.changeTasbihPhrase((currentTasbihIndex + 1) % tasbihPhrases.size) }
                        .padding(vertical = 16.dp, horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Huge Count Bubble
                Card(
                    modifier = Modifier.size(160.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = EmeraldMedium.copy(alpha = 0.05f)),
                    border = BorderStroke(3.dp, EmeraldMedium.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$tasbihCount",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDeep,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = if (isBn) "বার" else "Times",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Click and Reset
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Reset Button
                    OutlinedButton(
                        onClick = { viewModel.resetTasbih() },
                        shape = CircleShape,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                        modifier = Modifier.size(64.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Count", tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(if (isBn) "রিসেট" else "Reset", fontSize = 8.sp, color = MaterialTheme.colorScheme.error)
                        }
                    }

                    // Tapper
                    Button(
                        onClick = { viewModel.incrementTasbih() },
                        modifier = Modifier
                            .size(96.dp)
                            .testTag("full_tasbih_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMedium),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Fingerprint, contentDescription = "Tap to count", tint = Color.White, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(if (isBn) "জিকির করুন" else "COUNT", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE 4: DEDICATED FULL ZAKAT CALCULATOR SCREEN
// ===================================================================================
@Composable
fun ZakatScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    val zakatGold by viewModel.zakatGoldValue.collectAsState()
    val zakatCash by viewModel.zakatCashValue.collectAsState()
    val zakatBusiness by viewModel.zakatBusinessValue.collectAsState()
    val zakatLiabilities by viewModel.zakatLiabilitiesValue.collectAsState()
    val zakatPayableAmount by viewModel.zakatResultAmount.collectAsState()
    val zakatEligible by viewModel.isEligibleForZakat.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "যাকাত ক্যালকুলেটর" else "Zakat Calculator",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "স্বর্ণ, রূপা ও নগদ সম্পদের হিসাব" else "Estimate Gold, Silver & Savings",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isBn) "ইসলামিক যাকাত নির্দেশনাবলী" else "Zakat Rules & Guidelines",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isBn) 
                                "যাকাত ইসলামের অন্যতম একটি ফরজ স্তম্ভ। এক বছর নিসাব পরিমাণ সম্পদ (৭.৫ তোলা স্বর্ণ বা ৫২.৫ তোলা রূপা অথবা এর সমমূল্য) মালিকানায় থাকলে সম্পদ মূল্যের ২.৫% দরিদ্রদের যাকাত হিসেবে প্রদান করতে হবে।" 
                                else "Zakat is a mandatory pillar of Islam. It is obligatory on wealth (cash, gold, business values) held for a lunar year that exceeds Nisab threshold (equivalent to 7.5 tola/87.48g of gold or 52.5 tola/612.36g of silver). The rate of Zakat is 2.5%.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = if (isBn) "সম্পদের বিবরণী" else "Statement of Wealth",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )

                        // Cash
                        OutlinedTextField(
                            value = zakatCash,
                            onValueChange = { viewModel.updateZakatCash(it) },
                            label = { Text(if (isBn) "নগদ টাকা ও ব্যাংক আমানত (৳)" else "Cash & Savings (৳)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldMedium)
                        )

                        // Gold value
                        OutlinedTextField(
                            value = zakatGold,
                            onValueChange = { viewModel.updateZakatGold(it) },
                            label = { Text(if (isBn) "স্বর্ণ ও রূপার বাজার মূল্য (৳)" else "Gold & Silver Value (৳)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldMedium)
                        )

                        // Business Value
                        OutlinedTextField(
                            value = zakatBusiness,
                            onValueChange = { viewModel.updateZakatBusiness(it) },
                            label = { Text(if (isBn) "ব্যবসার মালামাল ও বিনিয়োগ (৳)" else "Business Assets (৳)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldMedium)
                        )

                        // Liabilities
                        OutlinedTextField(
                            value = zakatLiabilities,
                            onValueChange = { viewModel.updateZakatLiabilities(it) },
                            label = { Text(if (isBn) "ব্যক্তিগত ঋণ ও অন্যান্য দায় বাদ (৳)" else "Minus Debt/Liabilities (৳)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldMedium)
                        )
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (zakatEligible) EmeraldMedium.copy(alpha = 0.08f) 
                                         else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, if (zakatEligible) EmeraldMedium.copy(alpha = 0.2f) else Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isBn) "পরিশোধযোগ্য যাকাত (২.৫%)" else "Payable Zakat (2.5%)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "৳ ${zakatPayableAmount.toInt()}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (zakatEligible) EmeraldMedium else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (zakatEligible) (if (isBn) "নেসাব পূর্ণ হয়েছে" else "Nisab Succeeded")
                                           else (if (isBn) "যាកাত ফরজ নয়" else "Nisab Pending"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        if (zakatEligible) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (isBn) 
                                    "আপনার মোট সম্পদ যাকাতের নেসাব সীমাকে অতিক্রম করেছে। দরিদ্রের হক অনুযায়ী দ্রুত এই যাকাত পরিশোধ করা আবশ্যক।" 
                                    else "Your net positive assets exceed the Nisab limit. Fast payment of this amount with intention is recommended.",
                                fontSize = 11.sp,
                                color = EmeraldMedium,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE 5: DEDICATED FULL ISLAMIC AI ASSISTANT CHAT SCREEN (WITH CAMERA ARABIC TRANSLATION)
// ===================================================================================
@Composable
fun AiChatScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    var queryInput by remember { mutableStateOf("") }
    val aiMessages by viewModel.aiMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val listState = rememberLazyListState()

    var activeAiTab by remember { mutableStateOf(0) } // 0: Quran/Islamic QA Chat, 1: Camera Arabic Translator

    // Trigger scroll to bottom when messages list size changes
    LaunchedEffect(aiMessages.size) {
        if (aiMessages.isNotEmpty()) {
            listState.animateScrollToItem(aiMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "জান্নাত সন্ধান দ্বীনি এআই" else "Jannat Sondhan Islamic AI Chat",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "বিশ্বস্ত শরিয়াহ নির্দেশিকা ও আরবী অনুবাদক" else "Shariah QA & Arabic Camera Translator",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // M3 style segment switcher tab
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeAiTab == 0) EmeraldMedium else Color.Transparent)
                    .clickable { activeAiTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBn) "দ্বীনি জিজ্ঞাসা (AI QA)" else "Islamic QA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (activeAiTab == 0) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeAiTab == 1) EmeraldMedium else Color.Transparent)
                    .clickable { activeAiTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isBn) "আরবি ক্যামেরা অনুবাদ" else "Camera Translator",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (activeAiTab == 1) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (activeAiTab == 0) {
            // --- TAB 0: QA ASSISTANT CHAT ---
            // Question suggestions chips
            val suggestions = if (isBn) {
                listOf(
                    "নামাজের ফজিলত কি?",
                    "যাকাতের নেসাব কত?",
                    "সেহরির নিয়ত কি?",
                    "কোরআন পড়ার হুকুম কি?"
                )
            } else {
                listOf(
                    "Virtues of Salat?",
                    "Gold Nisab threshold?",
                    "Niyyah for Fasting?",
                    "Importance of Quran recitation?"
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(suggestions) { keyword ->
                    SuggestionChip(
                        onClick = {
                            viewModel.askIslamicAi(keyword)
                        },
                        label = { Text(keyword, fontSize = 11.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = EmeraldMedium.copy(alpha = 0.08f),
                            labelColor = EmeraldDeep
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            borderColor = EmeraldMedium.copy(alpha = 0.15f),
                            enabled = true
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chat conversation stack
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (aiMessages.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isBn) "আস-সালামু আলাইকুম" else "As-Salamu Alaykum",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBn) 
                                "আমি আপনার দ্বীনি এআই সহকারী। কোরআন, হাদিস, সালাত, রোজা বা শরিয়তের যেকোনো বিষয়ে প্রশ্ন জিজ্ঞেস করতে পারেন।" 
                                else "I'm your Islamic AI assistant. Type any questions about Quran, Hadith, Salah, Ramadan, or Islamic jurisprudence.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 12.dp)
                    ) {
                        items(aiMessages) { msg ->
                            val isUser = msg.sender == "user"
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                            ) {
                                Card(
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isUser) 16.dp else 4.dp,
                                        bottomEnd = if (isUser) 4.dp else 16.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isUser) EmeraldMedium else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    ),
                                    modifier = Modifier.fillMaxWidth(0.85f),
                                    border = if (!isUser) BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)) else null
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isUser) Icons.Default.Person else Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = if (isUser) Color.White.copy(alpha = 0.9f) else IslamicGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = if (isUser) (if (isBn) "আপনি" else "You") 
                                                       else (if (isBn) "দ্বীনি AI উপদেষ্টা" else "Islamic AI Guide"),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUser) Color.White.copy(alpha = 0.9f) else EmeraldMedium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = msg.content,
                                            fontSize = 13.sp,
                                            lineHeight = 20.sp,
                                            color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Search panel / Text input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = queryInput,
                    onValueChange = { queryInput = it },
                    placeholder = { Text(if (isBn) "দ্বীনি প্রশ্ন জিজ্ঞাসা করুন..." else "Query about Islam...") },
                    modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldMedium,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    )
                )

                Button(
                    onClick = {
                        if (queryInput.isNotBlank()) {
                            viewModel.askIslamicAi(queryInput)
                            queryInput = ""
                        }
                    },
                    enabled = !isAiLoading && queryInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldMedium),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    if (isAiLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        } else {
            // --- TAB 1: ARABIC CAMERA TRANSLATOR ---
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                CameraTranslatorTab(viewModel, isBn)
            }
        }
    }
}

@Composable
fun CameraTranslatorTab(
    viewModel: IbadahViewModel,
    isBn: Boolean
) {
    val context = LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    
    val languages = listOf(
        "Bengali" to (if (isBn) "বাংলা (Bengali)" else "Bengali"),
        "English" to (if (isBn) "ইংরেজি (English)" else "English"),
        "Urdu" to (if (isBn) "উর্দু (Urdu)" else "Urdu"),
        "Hindi" to (if (isBn) "হিন্দি (Hindi)" else "Hindi"),
        "Indonesian" to (if (isBn) "বাহাসা (Indonesian)" else "Indonesian"),
        "Turkish" to (if (isBn) "তুর্কি (Turkish)" else "Turkish")
    )
    
    var selectedLangPair by remember { mutableStateOf(languages[0]) }
    var showLangDropdown by remember { mutableStateOf(false) }
    
    // States from viewmodel
    val capturedBitmap by viewModel.capturedBitmap.collectAsState()
    val translationResult by viewModel.translationResult.collectAsState()
    val isTranslationLoading by viewModel.isTranslationLoading.collectAsState()
    
    // Permission state
    var hasCameraPermission by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, if (isBn) "ক্যামেরা পারমিশন ছাড়া ছবি তোলা সম্ভব নয়!" else "Camera permission is required to capture photos!", Toast.LENGTH_LONG).show()
        }
    }
    
    // Launchers
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            viewModel.translateArabicFromImage(bitmap, selectedLangPair.first)
        }
    }
    
    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val bitmap = if (android.os.Build.VERSION.SDK_INT >= 28) {
                    val source = android.graphics.ImageDecoder.createSource(context.contentResolver, uri)
                    android.graphics.ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                } else {
                    @Suppress("DEPRECATION")
                    android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                viewModel.translateArabicFromImage(bitmap, selectedLangPair.first)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error loading image: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Feature description card
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(EmeraldMedium.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = EmeraldDeep,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isBn) "স্মার্ট এআই অনুবাদক" else "Smart AI Camera Translator",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldDeep
                    )
                    Text(
                        text = if (isBn) "যেকোনো আরবি ক্যালিগ্রাফি বা বইয়ের পাতার ছবি তুলে সাথে সাথে অনুবাদ ও তাফসির লাভ করুন" 
                               else "Take an image of any Arabic calligraphy or page to translate and get explanation instantly.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Language selector & Camera/Gallery actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Selector
                Text(
                    text = if (isBn) "অনূদিত ভাষা নির্বাচন করুন:" else "Select Target Language:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, EmeraldMedium.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .clickable { showLangDropdown = true }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = selectedLangPair.second, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = EmeraldDeep)
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = EmeraldMedium)
                        }
                    }
                    DropdownMenu(
                        expanded = showLangDropdown,
                        onDismissRequest = { showLangDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.85f).background(MaterialTheme.colorScheme.surface)
                    ) {
                        languages.forEach { pair ->
                            DropdownMenuItem(
                                text = { Text(pair.second, fontWeight = FontWeight.Medium) },
                                onClick = {
                                    selectedLangPair = pair
                                    showLangDropdown = false
                                    // If a bitmap already exists, run re-translation in real-time!
                                    capturedBitmap?.let {
                                        viewModel.translateArabicFromImage(it, pair.first)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons side-by-side
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // camera capture button
                    Button(
                        onClick = {
                            if (hasCameraPermission) {
                                takePictureLauncher.launch(null)
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMedium),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Camera", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isBn) "ক্যামেরা" else "Camera", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    // gallery import button
                    OutlinedButton(
                        onClick = {
                            getContentLauncher.launch("image/*")
                        },
                        border = BorderStroke(1.dp, EmeraldMedium),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = "Gallery", tint = EmeraldMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isBn) "গ্যালারি" else "Gallery", color = EmeraldMedium, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Show loading progress
        if (isTranslationLoading) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = EmeraldMedium, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBn) "ছবি এনালাইটিক্স ও আরবী টেক্সট অনুবাদ হচ্ছে..." 
                               else "Analyzing image and translating Arabic text...",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Translation response card
        if (capturedBitmap != null || translationResult.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Headline and Clear action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "অনুবাদ ফাইল ও ফলাফল" else "Translation Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep
                        )
                        IconButton(
                            onClick = { viewModel.clearTranslationState() },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Clear", tint = Color.Red.copy(alpha = 0.7f))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Thumbnail image preview (highly accessible & polished)
                    capturedBitmap?.let { bitmap ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.05f))
                        ) {
                            androidx.compose.foundation.Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Captured Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Markdown-style translation results text
                    if (translationResult.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = translationResult,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action rows: Copy and Google Search (as user specified!)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Copy Action
                            Button(
                                onClick = {
                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(translationResult))
                                    Toast.makeText(context, if (isBn) "ফলাফল কপি করা হয়েছে" else "Copied translation results", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldMedium),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isBn) "কপি করুন" else "Copy Results", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            // Google Search Action
                            OutlinedButton(
                                onClick = {
                                    try {
                                        // strip Markdown hashes & layout symbols to search clean sentences
                                        val cleanQuery = translationResult
                                            .replace("#", "")
                                            .replace("*", "")
                                            .take(150) // short query friendly for search engine
                                        val intent = android.content.Intent(
                                            android.content.Intent.ACTION_VIEW,
                                            android.net.Uri.parse("https://www.google.com/search?q=" + android.net.Uri.encode(cleanQuery))
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Could not launch web browser", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                border = BorderStroke(1.dp, EmeraldMedium),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = EmeraldDeep, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = if (isBn) "গুগল সার্চ" else "Google Search", color = EmeraldDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE 6: DEDICATED FULL ALLAH'S 99 NAMES SCREEN
// ===================================================================================
@Composable
fun AllahNamesScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isBn) "আল্লাহ তাআলার ৯৯টি নাম" else "99 Beautiful Names of Allah",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "অর্থ, ফজিলত ও জিকিরের আমল" else "With Meanings, Benefits & Recitation",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = IslamicGold.copy(alpha = 0.08f))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBn)
                        "রাসূলুল্লাহ (সাঃ) বলেছেন: 'নিশ্চয়ই আল্লাহর ৯৯টি নাম রয়েছে, যে ব্যক্তি সেগুলো মুখস্থ করবে বা মনে রাখবে, সে জান্নাতে প্রবেশ করবে।' (বুখারী)"
                        else "The Prophet (PBUH) said: 'Indeed, Allah has ninety-nine names, anyone who commits them to memory will enter Paradise.' (Bukhari)",
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDeep
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid list of all names
        val items = IslamicData.asmaUlHusna
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { name ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, EmeraldMedium.copy(alpha = 0.12f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.playNameAudio(name) }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(EmeraldMedium.copy(alpha = 0.06f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${name.id}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = name.arabic,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = name.transliterationBn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (isBn) name.meaningBn else name.meaningEn,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME DASHBOARD (Islamic Hub with quick inline tools and cards)
// ===================================================================================
@Composable
fun HomeDashboardView(
    viewModel: IbadahViewModel,
    isBn: Boolean,
    onTabSelect: (Int) -> Unit,
    onSubViewSelected: (String) -> Unit
) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
    val countdown by viewModel.prayerCountdown.collectAsState()
    val language by viewModel.appLanguage.collectAsState()
    val currentPrayerNameVal by viewModel.currentPrayerName.collectAsState()
    val currentPrayerTimeVal by viewModel.currentPrayerTime.collectAsState()
    val use24HourFormat by viewModel.use24HourFormat.collectAsState()

    var showForbiddenDialog by remember { mutableStateOf(false) }
    var homeActiveTab by remember { mutableIntStateOf(0) } // 0 = Dashboard, 1 = Daily Tracker

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Hadith Ticker: Sticky/Always visible at the top!
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(vertical = 6.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoStories,
                    contentDescription = "Hadith Logo",
                    tint = IslamicGold,
                    modifier = Modifier.size(16.dp)
                )
                
                val calendar = Calendar.getInstance()
                val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
                val index = dayOfYear % dailyHadiths.size
                val activeHadithPair = dailyHadiths[index]
                val hadithText = if (isBn) activeHadithPair.first else activeHadithPair.second
                
                HadithScrollingTicker(
                    text = hadithText,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Premium Custom Segmented Control / Pill Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tabLabelBn = listOf("ইবাদত ড্যাশবোর্ড", "জান্নাত ট্র্যাকার (আমলনামা)")
            val tabLabelEn = listOf("Ibadah Dashboard", "Jannat Tracker (Deeds)")
            val labels = if (isBn) tabLabelBn else tabLabelEn

            labels.forEachIndexed { idx, label ->
                val selected = homeActiveTab == idx
                val icon = if (idx == 0) Icons.Default.Dashboard else Icons.Default.Verified
                
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) EmeraldDeep else Color.Transparent)
                        .clickable { homeActiveTab = idx }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selected) Color.White else EmeraldDeep,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (homeActiveTab == 0) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Beautiful Combined Prayer Dashboard Card
                item {
                    val progressFraction by viewModel.prayerProgressFraction.collectAsState()
                    val nextPrayerTimeVal by viewModel.nextPrayerTime.collectAsState()
                    val forbiddenState by viewModel.forbiddenTimeState.collectAsState()
                    val currentPrayerNameVal by viewModel.currentPrayerName.collectAsState()
                    val currentPrayerTimeVal by viewModel.currentPrayerTime.collectAsState()
                    val use24HourFormat by viewModel.use24HourFormat.collectAsState()
                    val selectedCity by viewModel.selectedCity.collectAsState()
                    val nextPrayerName by viewModel.nextPrayerName.collectAsState()
                    val countdown by viewModel.prayerCountdown.collectAsState()
                    val language by viewModel.appLanguage.collectAsState()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldDeep),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // Row of Current & Next Prayers Side-by-side
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val displayCurrentTime = if (use24HourFormat) currentPrayerTimeVal else formatTo12Hour(currentPrayerTimeVal, language)
                                val displayNextTime = if (use24HourFormat) nextPrayerTimeVal else formatTo12Hour(nextPrayerTimeVal, language)
                                val currentHeader = when (language) {
                                    AppLanguage.BN -> "বর্তমান নামাজ / সময়"
                                    AppLanguage.AR -> "الصلاة الحالية / الوقت"
                                    else -> "Current Prayer / Time"
                                }
                                val nextHeader = when (language) {
                                    AppLanguage.BN -> "পরবর্তী নামাজ / সময়"
                                    AppLanguage.AR -> "الصلاة القادمة / الوقت"
                                    else -> "Next Prayer / Time"
                                }

                                // 1. Current Prayer Column (Left)
                                Column(
                                    modifier = Modifier.weight(1.1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF4CAF50))
                                        )
                                        Text(
                                            text = currentHeader,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.75f),
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = currentPrayerNameVal,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = displayCurrentTime,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                }
                                
                                // Vertical Divider
                                Box(
                                    modifier = Modifier
                                        .height(55.dp)
                                        .width(1.dp)
                                        .background(Color.White.copy(alpha = 0.15f))
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                // 2. Next Prayer Column (Right)
                                Column(
                                    modifier = Modifier.weight(1.1f),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = nextHeader,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold.copy(alpha = 0.9f),
                                        letterSpacing = 0.5.sp
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text(
                                            text = nextPrayerName,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = displayNextTime,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(18.dp))
                            
                            // Row 3: Countdown and gold progress track
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = if (isBn) "পরবর্তী নামাজে বাকি" else "Time to Next",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                                
                                LinearProgressIndicator(
                                    progress = { progressFraction },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = IslamicGold,
                                    trackColor = Color.White.copy(alpha = 0.15f),
                                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                
                                Text(
                                    text = countdown,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Row 4: Two small sub-cards side-by-side
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Sehri End Time Card
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .padding(horizontal = 12.dp, vertical = 12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bedtime,
                                            contentDescription = null,
                                            tint = IslamicGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Column {
                                            Text(
                                                text = if (isBn) "সেহরির শেষ সময়" else "Sehri Ends",
                                                fontSize = 10.sp,
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (use24HourFormat) selectedCity.sahriTime else formatTo12Hour(selectedCity.sahriTime, language),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                
                                // Iftar Time Card
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White.copy(alpha = 0.08f))
                                        .padding(horizontal = 12.dp, vertical = 12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WbSunny,
                                            contentDescription = null,
                                            tint = IslamicGold,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Column {
                                            Text(
                                                text = if (isBn) "ইফতারের সময়সূচী" else "Iftar Time",
                                                fontSize = 10.sp,
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (use24HourFormat) selectedCity.iftarTime else formatTo12Hour(selectedCity.iftarTime, language),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                            
                            // Row 5: Forbidden/Upcoming Forbidden times Card
                            forbiddenState?.let { fState ->
                                Spacer(modifier = Modifier.height(14.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (fState.isActive) Color(0xFF9E2A2B).copy(alpha = 0.3f) else Color.White.copy(alpha = 0.06f)
                                        )
                                        .border(
                                            1.dp,
                                            if (fState.isActive) Color(0xFFC93B3B).copy(alpha = 0.4f) else Color.White.copy(alpha = 0.08f),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { showForbiddenDialog = true }
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = IslamicGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = fState.formattedStatusMessage,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                lineHeight = 15.sp
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (isBn) "সবগুলো নিষিদ্ধ সময় দেখতে এখানে চাপুন ↗" else "Tap here to see all forbidden times ↗",
                                                fontSize = 9.sp,
                                                color = Color.White.copy(alpha = 0.65f),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Special Premium Card: নামাজ শিক্ষা (Prayer Education)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubViewSelected("prayer_education") },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EmeraldDeep.copy(alpha = 0.95f),
                            contentColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.White, CircleShape)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val w = size.width
                                        val h = size.height
                                        val primaryGreen = Color(0xFF0F5A47)
                                        
                                        val domePath = androidx.compose.ui.graphics.Path().apply {
                                            moveTo(w * 0.15f, h * 0.85f)
                                            lineTo(w * 0.85f, h * 0.85f)
                                            lineTo(w * 0.85f, h * 0.60f)
                                            cubicTo(w * 0.85f, h * 0.35f, w * 0.68f, h * 0.18f, w * 0.50f, h * 0.12f)
                                            cubicTo(w * 0.32f, h * 0.18f, w * 0.15f, h * 0.35f, w * 0.15f, h * 0.60f)
                                            close()
                                        }
                                        drawPath(path = domePath, color = primaryGreen)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "সবচেয়ে সহজ নামাজ শিক্ষা" else "Easiest Way to Learn Prayer",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (isBn) "কিভাবে নামাজ পড়বেন ও প্রয়োজনীয় ১০টি সূরা বিস্তারিত" else "How to pray & 10 essential suras with translation",
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                            
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // 4b. Special Premium Card: ইসলামিক কুইজ (Islamic Quiz)
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSubViewSelected("islamic_quiz") },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EmeraldDeep.copy(alpha = 0.95f),
                            contentColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(Color.White, CircleShape)
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Help,
                                        contentDescription = null,
                                        tint = EmeraldDeep,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "ইসলামিক কুইজ" else "Islamic Quiz",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (isBn) "ডেইলি কুইজ খেলুন এবং পয়েন্ট অর্জন করুন" else "Play daily quizzes to earn reward points",
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                            
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // 5. Islamic Super Features Blocks
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = if (isBn) "ইসলামিক ফিচার সমূহ" else "Islamic Core Features",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            
                            // Row 1
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickActionItem(
                                    label = if (isBn) "আল-কুরআন" else "Al-Quran",
                                    icon = Icons.Default.Book,
                                    onClick = { onSubViewSelected("quran") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "আল-হাদিস" else "Hadith Core",
                                    icon = Icons.Default.LibraryBooks,
                                    onClick = { onSubViewSelected("hadith") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "নামাজের সময়" else "Prayer Times",
                                    icon = Icons.Default.Schedule,
                                    onClick = { onTabSelect(1) }, // Switch to Prayer Tab
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            // Row 2
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickActionItem(
                                    label = if (isBn) "কিবলা কম্পাস" else "Qibla Finder",
                                    icon = Icons.Default.CompassCalibration,
                                    onClick = { onSubViewSelected("qibla") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "ডিজিটাল তাসবিহ" else "Digital Tasbih",
                                    icon = Icons.Default.Fingerprint,
                                    onClick = { onSubViewSelected("tasbih") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "যাকাত ক্যালকুলেটর" else "Zakat Calc",
                                    icon = Icons.Default.Calculate,
                                    onClick = { onSubViewSelected("zakat") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            // Row 3
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickActionItem(
                                    label = if (isBn) "ইসলামিক এআই" else "Islamic AI",
                                    icon = Icons.Default.AutoAwesome,
                                    onClick = { onSubViewSelected("ai_chat") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "আল্লাহর ৯৯ নাম" else "99 Names",
                                    icon = Icons.Default.Star,
                                    onClick = { onSubViewSelected("allah_names") },
                                    modifier = Modifier.weight(1f)
                                )
                                QuickActionItem(
                                    label = if (isBn) "দোয়া ও জিকির" else "Duas & Supps",
                                    icon = Icons.Default.Bookmark,
                                    onClick = { onSubViewSelected("duas") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            DailyDeedsTrackerView(
                viewModel = viewModel,
                isBn = isBn,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
        
        if (showForbiddenDialog) {
            AlertDialog(
                onDismissRequest = { showForbiddenDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFC93B3B),
                        modifier = Modifier.size(36.dp)
                    )
                },
                title = {
                    Text(
                        text = if (language == AppLanguage.BN) "পবিত্র হাদিস অনুসারে নিষিদ্ধ ৩ সময়" else "3 Forbidden Prayer Times",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.BN) 
                                "সূর্যোদয়, দুপুর ও সূর্যাস্তের নির্দিষ্ট সময়গুলোতে যেকোনো সালাত আদায় করা সম্পূর্ণ হারাম/নিষিদ্ধ।" 
                                else "It is completely forbidden / Makruh Tahrimi to perform any salah during sunrise, solar meridian (midday zawaal), and sunset.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        
                        // Window 1: Sunrise
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent, RoundedCornerShape(8.dp))
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.BN) "১. সূর্যোদয়কালীন নিষিদ্ধ সময়" else "1. Sunrise Window",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = if (language == AppLanguage.BN) "সূর্য ওঠার সময় থেকে সম্পূর্ণ বলয় উজ্জ্বল হওয়া পর্যন্ত (প্রায় ১৫-২০ মিনিট)" else "From sunrise until fully elevated (~15-20 mins)",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // Window 2: Midday
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent, RoundedCornerShape(8.dp))
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.BN) "২. ঠিক মধ্যাহ্ন (মাথা বরাবর সূর্য)" else "2. Solar Meridian Window",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = if (language == AppLanguage.BN) "সূর্য ঠিক মাথার উপরে অবস্থানকালে (যোহর নামাজের ৫-১০ মিনিট পূর্ব মুহূর্ত)" else "When sun is at its absolute zenith (~10 mins before Dhuhr)",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        // Window 3: Sunset
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent, RoundedCornerShape(8.dp))
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.BN) "৩. সূর্যাস্তকালীন নিষিদ্ধ সময়" else "3. Sunset Window",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = if (language == AppLanguage.BN) "সূর্য লালচে হয়ে অস্ত যাওয়া শুরু হতে সম্পূর্ণ অস্ত যাওয়া পর্যন্ত" else "From sun becoming reddish until fully set (around 15 mins before Maghrib)",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showForbiddenDialog = false }) {
                        Text(text = if (language == AppLanguage.BN) "ঠিক আছে" else "Okay")
                    }
                }
            )
        }
    }
}
// ===================================================================================
// 2. DAILY & MONTHLY PRAYER TIMETABLE TAB SCREEN
// ===================================================================================
@Composable
fun PrayerScheduleTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    var activeSubTab by remember { mutableStateOf(0) } // 0: Prayer, 1: Year-Round Sehri/Iftar

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(14.dp))
        
        Text(
            text = if (isBn) "নামাজ ও সেহরি ইফতারের সময়" else "Prayer, Sehri & Iftar Times",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = if (isBn) "রোজার মাস এবং সাধারণ সময়ের তারিখ ভিত্তিক সকল সময়সূচী" else "Date-specific prayer, sahri, and iftar schedules all year round.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Modern Tab Switch Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { activeSubTab = 0 },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (activeSubTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isBn) "নামাজের সময়সূচী" else "Prayer Timetable",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { activeSubTab = 1 },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeSubTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (activeSubTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isBn) "সেহরি ও ইফতার" else "Sahri & Iftar",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (activeSubTab == 0) {
            MonthlyPrayerScheduleTable(viewModel, isBn)
        } else {
            YearlySehriIftarTable(viewModel, isBn)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun YearlySehriIftarTable(viewModel: IbadahViewModel, isBn: Boolean) {
    var selectedMonthIndex by remember { mutableStateOf(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH)) }
    val yearlyData = viewModel.generateYearlySehriIftarCalculations(selectedMonthIndex)
    val city by viewModel.selectedCity.collectAsState()

    val bnMonths = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    val enMonths = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBn) "স্থান: ${city.nameBn}" else "Location: ${city.nameEn}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (isBn) "সারা বছর ২০২৬" else "Year-Round 2026",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Horizontal Month selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(12) { monthIdx ->
                val isSelected = monthIdx == selectedMonthIndex
                val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                val fg = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                val border = if (isSelected) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                Card(
                    modifier = Modifier
                        .clickable { selectedMonthIndex = monthIdx },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = bg),
                    border = border
                ) {
                    Text(
                        text = if (isBn) bnMonths[monthIdx] else enMonths[monthIdx],
                        fontWeight = FontWeight.Bold,
                        color = fg,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        // Table List
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Table Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) "তারিখ ( গ্রেগোরিয়ান )" else "Date (Gregorian)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1.5f)
                    )
                    Text(
                        text = if (isBn) "সেহরি শেষ" else "Sehri Ends",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1.2f)
                    )
                    Text(
                        text = if (isBn) "ইফতার শুরু" else "Iftar Starts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1.2f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                yearlyData.forEachIndexed { rowIdx, dayData ->
                    val rowBg = if (rowIdx % 2 == 0) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(rowBg, RoundedCornerShape(6.dp))
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) dayData.gregorianDateBn else dayData.gregorianDateEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1.5f)
                        )

                        Text(
                            text = dayData.sahriTimeLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDeep,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1.2f)
                        )

                        Text(
                            text = dayData.iftarTimeLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1.2f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MonthlyPrayerScheduleTable(viewModel: IbadahViewModel, isBn: Boolean) {
    val monthlySchedule by viewModel.monthlyPrayerSchedule.collectAsState()
    var selectedDayIndex by remember { mutableStateOf(0) }
    val use24HourFormat by viewModel.use24HourFormat.collectAsState()

    val activeDayData = monthlySchedule.getOrNull(selectedDayIndex)

    if (activeDayData != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // Heading & Format Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "তারিখ ভিত্তিক সময়সূচী (এক মাস)" else "Date Wise Timetable (One Month)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                // Toggle Button/Row for Time format
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                        .clickable { viewModel.toggleTimeFormat() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (use24HourFormat) "২৪ ঘণ্টা" else "১২ ঘণ্টা",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Horizontal Scroll of Dates (May 1 to May 30)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                itemsIndexed(monthlySchedule) { idx, day ->
                    val isSelected = idx == selectedDayIndex
                    val bg = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    val fg = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    val border = if (isSelected) BorderStroke(0.dp, Color.Transparent) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Card(
                        modifier = Modifier
                            .clickable { selectedDayIndex = idx },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = bg),
                        border = border
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day.dateStr,
                                fontWeight = FontWeight.Bold,
                                color = fg,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isBn) "দিন ${day.dayIndex}" else "Day ${day.dayIndex}",
                                color = fg.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Beautiful Table Card containing the 11 times
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Table Header Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isBn) "নামাজের শুরু ও শেষ" else "Prayer Sequence / Span",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1.5f)
                        )
                        Text(
                            text = if (isBn) "শুরুর সময়" else "Start Time",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = if (isBn) "শেষ সময়" else "End Time",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 11 Rows
                    activeDayData.timings.forEachIndexed { rowIdx, item ->
                        val icon = when (rowIdx) {
                            0, 9 -> Icons.Default.NightsStay // Tahajjud, Awabin
                            1 -> Icons.Default.Brightness3 // Fajr
                            2 -> Icons.Default.WbSunny // Sunrise
                            3, 4 -> Icons.Default.LightMode // Ishrak, Chasht
                            5 -> Icons.Default.WbSunny // Zuhr
                            6 -> Icons.Default.WbTwilight // Asr
                            7 -> Icons.Default.WbSunny // Sunset
                            8 -> Icons.Default.Bedtime // Maghrib
                            else -> Icons.Default.NightsStay // Isha
                        }

                        val rowBg = if (rowIdx % 2 == 0) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(rowBg, RoundedCornerShape(6.dp))
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Column 1: Icon and Name
                            Row(
                                modifier = Modifier.weight(1.5f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (rowIdx == 1 || rowIdx == 5 || rowIdx == 6 || rowIdx == 8 || rowIdx == 10) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) item.nameBn else item.nameEn,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            // Column 2: Start Time
                            Text(
                                text = if (use24HourFormat) item.startTime24h else item.startTime12h,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1.2f)
                            )

                            // Column 3: End Time
                            Text(
                                text = if (use24HourFormat) item.endTime24h else item.endTime12h,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                textAlign = TextAlign.End,
                                modifier = Modifier.weight(1.2f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    val quranSearchQuery by viewModel.quranSearchQuery.collectAsState()
    val selectedSurah by viewModel.selectedSurah.collectAsState()
    val bookmarks by viewModel.quranBookmarks.collectAsState()
    val playingIndex by viewModel.playingVerseIndex.collectAsState()
    val isPlaying by viewModel.isAudioPlaying.collectAsState()
    val lastReadNumber by viewModel.lastReadSurahNumber.collectAsState()
    val isSurahLoading by viewModel.isSurahLoading.collectAsState()
    val downloadedSurahs by viewModel.downloadedSurahs.collectAsState()
    val audioReciter by viewModel.audioReciter.collectAsState()
    val playBengaliTranslation by viewModel.playBengaliTranslation.collectAsState()

    val filteredSuras = viewModel.suras.filter {
        it.nameBengali.contains(quranSearchQuery, true) ||
                it.nameEnglish.contains(quranSearchQuery, true) ||
                it.number.toString().contains(quranSearchQuery)
    }

    val currentSurah = selectedSurah
    if (currentSurah == null) {
        // Quran List with integrated 3-tab layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = quranSearchQuery,
                onValueChange = { viewModel.searchQuran(it) },
                placeholder = { Text(if (isBn) "সূরা নাম্বার বা নাম দিয়ে খুঁজুন..." else "Search by Sura or index...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // Last Read Section Shortcut
            var quranTabSelected by remember { mutableStateOf(0) } // 0: Surah list, 1: Bookmarks

            val lastReadSuraData = viewModel.suras.firstOrNull { it.number == lastReadNumber }
            if (lastReadSuraData != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clickable { viewModel.selectSurah(lastReadSuraData) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    border = BorderStroke(1.dp, SoftGoldBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isBn) "সর্বশেষ পাঠ" else "Last Read Sura",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${lastReadSuraData.number}. ${if (isBn) lastReadSuraData.nameBengali else lastReadSuraData.nameEnglish} (${lastReadSuraData.nameArabic})",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Tab selection inside Quran Screen (Surah List -> Bookmarks)
            TabRow(
                selectedTabIndex = quranTabSelected,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Tab(
                    selected = quranTabSelected == 0,
                    onClick = { quranTabSelected = 0 },
                    text = { Text(if (isBn) "সূরা তালিকা" else "Surah List", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
                Tab(
                    selected = quranTabSelected == 1,
                    onClick = { quranTabSelected = 1 },
                    text = { Text(if (isBn) "বুকমার্ক (${bookmarks.size})" else "Bookmarks (${bookmarks.size})", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                )
            }

            when (quranTabSelected) {
                0 -> {
                    // Sura List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(filteredSuras) { sura ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectSurah(sura) },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.primaryContainer,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = sura.number.toString(),
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column {
                                            Text(
                                                text = if (isBn) sura.nameBengali else sura.nameEnglish,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Text(
                                                text = "${sura.totalVerses} " + (if (isBn) "টি আয়াত" else "verses"),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                        }
                                    }

                                    Text(
                                        text = sura.nameArabic,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    if (bookmarks.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (isBn) "কোনো বুকমার্ক খুঁজে পাওয়া যায়নি!" else "No Bookmarks Saved Yet",
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(bookmarks) { b ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val realSura =
                                                viewModel.suras.find { it.number == b.surahNumber }
                                            viewModel.selectSurah(realSura)
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = BorderStroke(1.dp, SoftGoldBorder)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${b.surahNumber}. ${b.surahName}",
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Text(
                                                text = if (isBn) "আয়াত নং: ${b.verseNumber}" else "Verse: ${b.verseNumber}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        IconButton(onClick = { viewModel.toggleQuranBookmark(b.surahNumber, b.verseNumber) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Remove",
                                                tint = AccentRed
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    } else {
        // Detailed Surah Reader screen
        val context = LocalContext.current
        val listState = rememberLazyListState()
        
        LaunchedEffect(playingIndex) {
            if (playingIndex >= 0) {
                listState.animateScrollToItem(playingIndex)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.selectSurah(null) }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = if (isBn) currentSurah.nameBengali else currentSurah.nameEnglish,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var menuReciterShow by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuReciterShow = true }) {
                        Icon(
                            imageVector = Icons.Default.Audiotrack, 
                            contentDescription = "Select Reciter",
                            tint = if (audioReciter.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                    DropdownMenu(expanded = menuReciterShow, onDismissRequest = { menuReciterShow = false }) {
                        listOf("হাফেজ আব্দুর রহমান আল-সুদাইস", "মিশারি রাশিদ আল-আফাসি", "মাহমুদ খলিল আল-হুসারি").forEach { reciter ->
                            DropdownMenuItem(
                                text = { Text(reciter) },
                                onClick = {
                                    viewModel.changeReciter(reciter)
                                    menuReciterShow = false
                                }
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = { 
                            viewModel.togglePlayBengaliTranslation()
                            val msg = if (playBengaliTranslation) {
                                if (isBn) "বাংলা অনুবাদ আবৃত্তি বন্ধ করা হয়েছে" else "Bangla translation voice disabled"
                            } else {
                                if (isBn) "বাংলা অনুবাদ আবৃত্তি চালু করা হয়েছে" else "Bangla translation voice enabled"
                            }
                            android.widget.Toast.makeText(context, msg, android.widget.Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Toggle Translation Audio",
                            tint = if (playBengaliTranslation) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    
                    val isReciterSelected = audioReciter.isNotEmpty()
                    val isSurahDownloaded = isReciterSelected && downloadedSurahs.contains("$audioReciter-${currentSurah.number}")
                    
                    IconButton(
                        onClick = { 
                            if (!isReciterSelected) {
                                android.widget.Toast.makeText(context, if (isBn) "দয়া করে প্রথমে ওপরে ডান পাশে আলেম/কারী সিলেক্ট করুন!" else "Please select an Alem/Reciter first!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.downloadSurahAudio(currentSurah)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSurahDownloaded) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                            contentDescription = "Download Surah Audio",
                            tint = if (isReciterSelected) {
                                if (isSurahDownloaded) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            },
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { 
                            if (!isReciterSelected) {
                                android.widget.Toast.makeText(context, if (isBn) "দয়া করে প্রথমে ওপরে ডান পাশে আলেম/কারী সিলেক্ট করুন!" else "Please select an Alem/Reciter first!", android.widget.Toast.LENGTH_SHORT).show()
                            } else if (!isSurahDownloaded) {
                                android.widget.Toast.makeText(context, if (isBn) "অডিও শুনতে প্রথমে ওপরে ক্লাউড ডাউনলোড বাটনে চাপুন!" else "Please cloud-download the audio first before playing!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.toggleAudioRecitation()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                            contentDescription = "Play/Pause recitation",
                            tint = if (isSurahDownloaded) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            },
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Subtitle metadata
            val reciterDisplay = if (audioReciter.isEmpty()) {
                if (isBn) "সিলেক্ট করা হয়নি" else "None Selected"
            } else {
                audioReciter
            }
            Text(
                text = "${currentSurah.nameArabic} • ${currentSurah.totalVerses} verses • Reciter: $reciterDisplay",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))
            
            // Helpful responsive step-by-step status banners
            if (audioReciter.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.85f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Audiotrack, 
                            contentDescription = "Select Reciter Info", 
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "ধাপ ১: ওপরে ডান পাশের আলেম আইকন (🎵) টিপে কারী/আলেম নির্বাচন করুন।" else "Step 1: Please select an Alem/Reciter using the music icon (🎵) in the top-right.",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            } else {
                val isSurahDownloaded = downloadedSurahs.contains("$audioReciter-${currentSurah.number}")
                if (!isSurahDownloaded) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload, 
                                contentDescription = "Download Info", 
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "ধাপ ২: $audioReciter এর সম্পূর্ণ অডিও অয়েটিং এড়াতে ওপরের ডাওনলোড (☁️) বাটনে চাপুন।" else "Step 2: Tap the download icon (☁️) above to download the full audio for $audioReciter.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle, 
                                contentDescription = "Ready Info", 
                                tint = Color(0xFF2E7D32)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "ধাপ ৩: ডাউনলোড সফল! অডিও চালু করতে প্লে (▶️) বাটনে চাপ দিন।" else "Step 3: Download Complete! Press the Play (▶️) button to start listening.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B5E20)
                            )
                        }
                    }
                }
            }

            if (isSurahLoading) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) "সম্পূর্ণ সূরা ডাউনলোড হচ্ছে..." else "Downloading full verses...",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bismillah representation (except surah Fatihah starts with it directly)
            if (currentSurah.number != 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            // Verse lists items
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(currentSurah.verses) { index, verse ->
                    val isVersePlaying = playingIndex == index
                    val isBookmarked = bookmarks.any { it.surahNumber == currentSurah.number && it.verseNumber == verse.number }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isVersePlaying) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isVersePlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Row indices + Actions buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = verse.number.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Row {
                                    // Tafsir Button
                                    var showTafsirDialog by remember { mutableStateOf(false) }
                                    IconButton(onClick = { showTafsirDialog = true }) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Tafsir",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    if (showTafsirDialog) {
                                        AlertDialog(
                                            onDismissRequest = { showTafsirDialog = false },
                                            title = { Text("তাফসীর (আয়াত নং ${verse.number})") },
                                            text = { Text(verse.tafsirBengali, style = MaterialTheme.typography.bodyMedium) },
                                            confirmButton = {
                                                TextButton(onClick = { showTafsirDialog = false }) {
                                                    Text("ঠিক আছে")
                                                }
                                            }
                                        )
                                    }

                                    // Bookmark list icon
                                    IconButton(onClick = { viewModel.toggleQuranBookmark(currentSurah.number, verse.number) }) {
                                        Icon(
                                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "Bookmark",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Verse play audio
                                    IconButton(onClick = { viewModel.playSpecificVerseAudio(index) }) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Audio Play",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    // Copy Verse clipboard
                                    IconButton(onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Verse", "${verse.textArabic}\n${verse.textBengali}")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, "আয়াত কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy Verse",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Arabic scripture
                            Text(
                                text = verse.textArabic,
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                textAlign = TextAlign.End,
                                fontFamily = FontFamily.Serif,
                                lineHeight = 38.sp
                            )

                            // Translate Bengali
                            Text(
                                text = verse.textBengali,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Translate English
                            Text(
                                text = verse.textEnglish,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// 3. COMPASS & DHIKR TAB SCREEN
// ===================================================================================
@Composable
fun CompassDhikrTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    var isDhikrActiveTab by remember { mutableStateOf(false) } // False: Compass, True: Dhikr

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tab Headers
        TabRow(
            selectedTabIndex = if (isDhikrActiveTab) 1 else 0,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Tab(
                selected = !isDhikrActiveTab,
                onClick = { isDhikrActiveTab = false },
                text = { Text(if (isBn) "কিবলা কম্পাস" else "Qibla Compass", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = isDhikrActiveTab,
                onClick = { isDhikrActiveTab = true },
                text = { Text(if (isBn) "ডিজিটাল তাসবিহ" else "Digital Tasbih", fontWeight = FontWeight.Bold) }
            )
        }

        if (!isDhikrActiveTab) {
            // Animated Qibla compass UI
            DisposableEffect(Unit) {
                viewModel.resumeCompassSensors()
                onDispose {
                    viewModel.pauseCompassSensors()
                }
            }

            val deviceHeading by viewModel.deviceHeading.collectAsState()
            val qiblaBearing by viewModel.qiblaBearing.collectAsState()
            val selectedCity by viewModel.selectedCity.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isBn) "ইসলামিক কিবলা কম্পাস" else "Precision Qibla Compass",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                     text = if (isBn) "আপনার অবস্থান: ${selectedCity.nameBn}" else "Your Location: ${selectedCity.nameEn}",
                     fontSize = 12.sp,
                     color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Compass Graphical Rotator representation
                // Combining device orientation offset and standard Mecca bearing
                val relativeRotationAngle = (qiblaBearing - deviceHeading)
                val normalizedAngle = ((relativeRotationAngle + 180) % 360 + 360) % 360 - 180
                val isAligned = kotlin.math.abs(normalizedAngle) < 8f

                val animatedRelativeAngle by animateFloatAsState(
                    targetValue = relativeRotationAngle,
                    animationSpec = tween(150),
                    label = "RelativeCompassRotation"
                )

                val animatedDialAngle by animateFloatAsState(
                    targetValue = -deviceHeading,
                    animationSpec = tween(150),
                    label = "DialCompassRotation"
                )

                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .background(
                            color = if (isAligned) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                        .border(
                            width = 4.dp,
                            color = if (isAligned) Color(0xFF059669) else SoftGoldBorder,
                            shape = CircleShape
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // ROTATING DIAL (Rotates by -deviceHeading to keep N facing North)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(animatedDialAngle),
                        contentAlignment = Alignment.Center
                    ) {
                        // Cardinal direction markings
                        Text(
                            text = "N",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFFDC2626), // North is historically red on compasses!
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                        Text(
                            text = "E",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                        Text(
                            text = "S",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                        Text(
                            text = "W",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.align(Alignment.CenterStart)
                        )

                        // Delicate astrolabe ticks reference overlay inside the rotating dial
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val r = size.minDimension / 2f
                            for (i in 0 until 360 step 30) {
                                val angleRad = Math.toRadians(i.toDouble())
                                val length = if (i % 90 == 0) 10.dp.toPx() else 5.dp.toPx()
                                val color = if (isAligned) Color(0xFF059669).copy(alpha = 0.25f) else SoftGoldBorder.copy(alpha = 0.4f)
                                
                                val startX = (r + (r - length) * Math.sin(angleRad)).toFloat()
                                val startY = (r - (r - length) * Math.cos(angleRad)).toFloat()
                                val endX = (r + r * Math.sin(angleRad)).toFloat()
                                val endY = (r - r * Math.cos(angleRad)).toFloat()
                                
                                drawLine(
                                    color = color,
                                    start = androidx.compose.ui.geometry.Offset(startX, startY),
                                    end = androidx.compose.ui.geometry.Offset(endX, endY),
                                    strokeWidth = if (i % 90 == 0) 2.dp.toPx() else 1.dp.toPx()
                                )
                            }
                        }
                    }

                    // ROTATING QIBLA NEEDLE (Rotates by qiblaBearing - deviceHeading to point to Mecca)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(animatedRelativeAngle),
                        contentAlignment = Alignment.Center
                    ) {
                        // Sits on top of the dial, pointing towards Mecca (Qibla Direction)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Arrow pointing to Mecca
                            Icon(
                                imageVector = Icons.Default.KeyboardDoubleArrowUp,
                                contentDescription = "Mecca Path",
                                tint = if (isAligned) Color(0xFF059669) else IslamicGold,
                                modifier = Modifier
                                    .size(52.dp)
                                    .padding(bottom = 2.dp)
                            )
                            
                            // Kaaba centerpiece
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                    .border(1.5.dp, if (isAligned) Color(0xFF059669) else SoftGoldBorder, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Kaaba's golden belt
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .align(Alignment.TopCenter)
                                        .padding(top = 4.dp)
                                        .background(if (isAligned) Color(0xFF34D399) else SoftGoldBorder)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = if (isBn) "কিবলা" else "QIBLA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (isAligned) Color(0xFF059669) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Alignment Guidance Indicator Bubble Panel
                if (isAligned) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                        border = BorderStroke(1.5.dp, Color(0xFF059669)),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "✨ কিবলা সঠিক দিকে রয়েছে! ✨" else "✨ Qibla is perfectly aligned! ✨",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(if (normalizedAngle > 0) 180f else 0f) // Rotate 180 to show arrow right
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) {
                                    if (normalizedAngle > 0) "মোবাইলটি ডানে ঘোরান (→)" else "মোবাইলটি বামে ঘোরান (←)"
                                } else {
                                    if (normalizedAngle > 0) "Rotate Right (→)" else "Rotate Left (←)"
                                },
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Accurate metadata panels display
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isBn) "ডিভাইস হেডিং" else "Device Angle", fontSize = 11.sp, color = Color.Gray)
                                Text("${deviceHeading.toInt()}°", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(if (isBn) "মক্কার দিক" else "Mecca Heading", fontSize = 11.sp, color = Color.Gray)
                                Text("${qiblaBearing.toInt()}°", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBn) "ফোনের সেন্সরের সক্রিয়তার উপর ভিত্তি করে ডায়ালটি ঘোরে। সঠিকভাবে দিক জানতে ডিভাইসটি মাটির সমান্তরালে রাখুন।"
                                else "Rotate the phone parallel to earth for accurate direction sensor metrics.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            // Digital Tasbih tracker layout with dynamic circle clicks
            val count by viewModel.tasbihCount.collectAsState()
            val target by viewModel.tasbihTarget.collectAsState()
            val history by viewModel.tasbihHistory.collectAsState()
            val phraseIndex by viewModel.currentTasbihPhraseIndex.collectAsState()

            val selectedPhrase = if (isBn) viewModel.tasbihPhrasesBn[phraseIndex] else viewModel.tasbihPhrasesEn[phraseIndex]

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Selector Header
                item {
                    var menuPhrasesShow by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { menuPhrasesShow = true }
                            .border(1.dp, SoftGoldBorder, RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedPhrase,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 15.sp
                        )
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        DropdownMenu(expanded = menuPhrasesShow, onDismissRequest = { menuPhrasesShow = false }) {
                            (if (isBn) viewModel.tasbihPhrasesBn else viewModel.tasbihPhrasesEn).forEachIndexed { i, item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        viewModel.changeTasbihPhrase(i)
                                        menuPhrasesShow = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Target Selector Buttons Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf(33, 100, 1000).forEach { t ->
                            FilterChip(
                                selected = target == t,
                                onClick = { viewModel.changeTasbihTarget(t) },
                                label = { Text("$t বার") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Tactile Counting Core Trigger
                item {
                    val angleOffsetFraction = count.toFloat() / target.toFloat()
                    val scoreFraction by animateFloatAsState(targetValue = angleOffsetFraction, label = "TasbihCircle")

                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .clickable { viewModel.incrementTasbih() }
                            .border(2.dp, SoftGoldBorder.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing live progression arc
                        Canvas(modifier = Modifier.size(190.dp)) {
                            drawArc(
                                color = SoftGoldBorder,
                                startAngle = -90f,
                                sweepAngle = scoreFraction * 360f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx())
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = count.toString(),
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (isBn) "লক্ষ্য: $target" else "Target: $target",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Control Toggles Reset
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { viewModel.resetTasbih() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.outline)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isBn) "পুনরায় শুরু" else "Reset Counter")
                        }
                    }
                }

                // Historics Header
                item {
                     Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                         Text(
                             text = if (isBn) "জিকর সম্পন্ন করার লোগসমূহ" else "Dhikr Session History",
                             style = MaterialTheme.typography.titleMedium,
                             fontWeight = FontWeight.Bold,
                             color = MaterialTheme.colorScheme.primary,
                             modifier = Modifier.padding(top = 8.dp)
                         )
                     }
                }

                // Detailed Session Records
                items(history) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(record.phrase, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Text(record.dateString, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Text("+${record.count}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// 4. TOOLS & RAMADAN TAB SCREEN
// ===================================================================================
@Composable
fun ToolsRamadanTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    var toolSelectedTab by remember { mutableStateOf(0) } // 0: Hisnul Muslim / Duas, 1: Zakat. 2: 99 Names

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Toggle Slider Menu
        ScrollableTabRow(
            selectedTabIndex = toolSelectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 0.dp
        ) {
            listOf("দোয়া ও জিকির", "যাকাত", "আল্লাহর ৯৯ নাম").forEachIndexed { i, tag ->
                Tab(
                    selected = toolSelectedTab == i,
                    onClick = { toolSelectedTab = i },
                    text = { Text(tag, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (toolSelectedTab) {
            0 -> DuasAndSupplicationSub(viewModel, isBn)
            1 -> ZakatCalculatorSub(viewModel, isBn)
            2 -> AsmaUlHusnaSub(viewModel)
        }
    }
}

// Sub section 0: Hisnul Muslim & Morning Evening Duas
@Composable
fun DuasAndSupplicationSub(viewModel: IbadahViewModel, isBn: Boolean) {
    val curatedDuas by viewModel.curatedDuas.collectAsState()
    val context = LocalContext.current
    var tts by remember { mutableStateOf<android.speech.tts.TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var activePlayingId by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        var speech: android.speech.tts.TextToSpeech? = null
        try {
            val s = android.speech.tts.TextToSpeech(context.applicationContext) { status ->
                try {
                    if (status == android.speech.tts.TextToSpeech.SUCCESS) {
                        val localeResult = speech?.setLanguage(java.util.Locale("bn", "BD"))
                        if (localeResult == android.speech.tts.TextToSpeech.LANG_MISSING_DATA || localeResult == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                            speech?.setLanguage(java.util.Locale("bn"))
                        }
                        isTtsReady = true
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            speech = s
            speech.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    activePlayingId = null
                }
                @Deprecated("Deprecated in Java", ReplaceWith("activePlayingId = null"))
                override fun onError(utteranceId: String?) {
                    activePlayingId = null
                }
            })
            tts = speech
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        onDispose {
            try {
                tts?.stop()
                tts?.shutdown()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            tts = null
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(curatedDuas) { dua ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isBn) dua.title else dua.titleEn,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = dua.reference,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dua.arabic,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isBn) "উচ্চারণ: ${dua.transliterationBn}" else "Read: ${dua.transliteration}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isBn) "ইংরেজি হরফে: ${dua.transliteration}" else "Latin Transliteration: ${dua.transliteration}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isBn) "অনুবাদ: ${dua.translation}" else "Translation: ${dua.translation}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val isPlaying = activePlayingId == dua.id
                            
                            OutlinedButton(
                                onClick = {
                                    try {
                                        if (isPlaying) {
                                            tts?.stop()
                                            activePlayingId = null
                                        } else {
                                            activePlayingId = dua.id
                                            val textToSpeak = if (isBn) {
                                                if (dua.transliterationBn.isNotEmpty()) {
                                                    "${dua.title}। উচ্চারণ: ${dua.transliterationBn}। অনুবাদ: ${dua.translation}"
                                                } else {
                                                    "${dua.title}। অনুবাদ: ${dua.translation}"
                                                }
                                            } else {
                                                "${dua.titleEn}. Transliteration: ${dua.transliteration}. Translation: ${dua.translation}"
                                            }
                                            
                                            val params = android.os.Bundle()
                                            params.putString(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, dua.id)
                                            tts?.speak(textToSpeak, android.speech.tts.TextToSpeech.QUEUE_FLUSH, params, dua.id)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        activePlayingId = null
                                    }
                                },
                                enabled = isTtsReady,
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (isPlaying) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
                                    contentDescription = if (isPlaying) "Stop" else "Listen",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPlaying) {
                                        if (isBn) "থামুন" else "Stop"
                                    } else {
                                        if (isBn) "শুনুন" else "Listen"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            if (isPlaying) {
                                Text(
                                    text = if (isBn) "চলছে..." else "Playing...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isBn) "পাঠ: ${dua.count}/${dua.target}" else "Count: ${dua.count}/${dua.target}",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            FilledTonalButton(
                                onClick = { viewModel.incrementDuaCount(dua.id) },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (dua.count >= dua.target) SoftGoldBorder else MaterialTheme.colorScheme.primaryContainer
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (dua.count >= dua.target) (if (isBn) "সম্পন্ন" else "Finished") else (if (isBn) "পড়ুন" else "Count"),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Sub section 1: Zakat calculator interactive dashboard
@Composable
fun ZakatCalculatorSub(viewModel: IbadahViewModel, isBn: Boolean) {
    val gold by viewModel.zakatGoldValue.collectAsState()
    val cash by viewModel.zakatCashValue.collectAsState()
    val business by viewModel.zakatBusinessValue.collectAsState()
    val liabilities by viewModel.zakatLiabilitiesValue.collectAsState()
    val isEligible by viewModel.isEligibleForZakat.collectAsState()
    val resultAmount by viewModel.zakatResultAmount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (isBn) "যাকাত ক্যালকুলেটর (স্বর্ণ ও অন্যান্য নিসাব হিসাব)" else "Zakat Calculator Engine",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = if (isBn) "রূপার হিসাব ভিত্তিক নিসাব সীমা: ৮৫,০০০ টাকা (৫২.৫ তোলা)"
                        else "Silver Nisab Benchmark: BDT 85,000",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (isBn) "আপনার বার্ষিক অতিরিক্ত সম্পদ যদি এই সীমার সমান বা বেশি হয় তবে ২.৫% হারে যাকাত দেওয়া ফরজ।"
                        else "If your net yearly wealth overrides this limit, then 2.5% is due.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        OutlinedTextField(
            value = gold,
            onValueChange = { viewModel.updateZakatGold(it) },
            label = { Text(if (isBn) "স্বর্ণের পরিমাণ (ভরি বা গ্রাম হিসাবে)" else "Gold Weight (Grams)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = cash,
            onValueChange = { viewModel.updateZakatCash(it) },
            label = { Text(if (isBn) "নগদ টাকা (ব্যাংক ও পকেটে থাকা)" else "Cash in Hand & Bank") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = business,
            onValueChange = { viewModel.updateZakatBusiness(it) },
            label = { Text(if (isBn) "ব্যবসায়িক পণ্য বা স্টক সম্পদ" else "Business Property & Assets") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        OutlinedTextField(
            value = liabilities,
            onValueChange = { viewModel.updateZakatLiabilities(it) },
            label = { Text(if (isBn) "ঋণ ও দায়সমূহ (বিয়োগ হবে)" else "Liabilities & Family Debt") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // Outcome Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isEligible) SoftMintBg else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.5.dp, if (isEligible) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isEligible) (if (isBn) "আপনার ওপর যাকাত ফরজ হয়েছে!" else "Zakat is obligatory on your net wealth!")
                        else (if (isBn) "আপনার যাকাত প্রযোজ্য সীমার নিচে" else "Zakat limits not reached yet"),
                    fontWeight = FontWeight.Bold,
                    color = if (isEligible) EmeraldDeep else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isBn) "প্রদেয় যাকাত (BDT)" else "Net Zakat Amount (BDT)",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = String.format("৳ %,.2f", resultAmount),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black,
                    color = if (isEligible) EmeraldDeep else Color.DarkGray
                )
            }
        }
    }
}

// Sub section 2: Asma Ul Husna 99 names
@Composable
fun AsmaUlHusnaSub(viewModel: IbadahViewModel) {
    val items = IslamicData.asmaUlHusna

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(items) { name ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, SoftGoldBorder),
                modifier = Modifier.clickable { viewModel.playNameAudio(name) }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Audio",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(16.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = name.arabic,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = name.transliterationBn,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = name.meaningBn,
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// Sub section 3: Ramadan tracking days and Eid Countdown cards
@Composable
fun RamadanAndEidSub(viewModel: IbadahViewModel, isBn: Boolean) {
    var fastStreakCount by remember { mutableStateOf(16) }
    var eidCountdownDays by remember { mutableStateOf(2) } // Mock 2 days left for Eid-ul-Fitr

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Eid Card Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            border = BorderStroke(1.dp, SoftGoldBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isBn) "পবিত্র ঈদুল ফিতর কাউন্টডাউন" else "Eid-ul-Fitr Celebrations Countdown",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$eidCountdownDays",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isBn) "দিন বাকি" else "Days Left",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // Fasting Logger Calendar Simulator
        Text(
            text = if (isBn) "রমজান রোজা ট্র্যাকার" else "Fasting Keeper Checklist",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isBn) "মোট পূর্ণ রোজা: $fastStreakCount টি" else "Kept Fasts Count: $fastStreakCount",
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { fastStreakCount++ }) {
                Text(if (isBn) "আজকের রোজা যুক্ত করুন" else "Log Fast Kept")
            }
        }

        // Fast grid simulation list calendar
        LazyHorizontalGrid()
    }
}

@Composable
fun LazyHorizontalGrid() {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            (1..5).forEach { d ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(EmeraldDeep, RoundedCornerShape(8.dp))
                        .border(1.dp, SoftGoldBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$d", color = Color.White, fontSize = 11.sp)
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SoftGoldBorder, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            (6..10).forEach { d ->
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(EmeraldDeep, RoundedCornerShape(8.dp))
                        .border(1.dp, SoftGoldBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$d", color = Color.White, fontSize = 11.sp)
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SoftGoldBorder, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun IslamicAudioCard(
    item: IslamicAudioItem,
    viewModel: IbadahViewModel,
    isBn: Boolean,
    playingAudioItem: IslamicAudioItem?,
    isAudioPlaying: Boolean,
    isCustomAudioLoading: Boolean,
    isPlayingAudioOnline: Boolean,
    downloadProgress: Map<String, Int>,
    downloadedAudioIds: Set<String>
) {
    val progress = downloadProgress[item.id]
    val isDownloading = progress != null
    val isDownloaded = downloadedAudioIds.contains(item.id)
    val isCurrentPlaying = playingAudioItem?.id == item.id
    val isCurrentPlayingOnline = isCurrentPlaying && isPlayingAudioOnline
    val isCurrentPlayingOffline = isCurrentPlaying && !isPlayingAudioOnline
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("islamic_audio_card_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentPlaying) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isCurrentPlaying) {
                MaterialTheme.colorScheme.primary
            } else if (isDownloading) {
                SoftGoldBorder.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            // Title & Speaker Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when (item.category) {
                        "waz" -> Icons.Default.RecordVoiceOver
                        "quran_only", "quran_translation" -> Icons.Default.Book
                        "nasheed" -> Icons.Default.Audiotrack
                        "dua" -> Icons.Default.Favorite
                        else -> Icons.Default.RecordVoiceOver
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isBn) item.titleBn else item.titleEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isCurrentPlaying) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (isBn) item.speakerBn else item.speakerEn,
                            fontSize = 11.sp,
                            color = if (isCurrentPlaying) {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(text = "•", fontSize = 11.sp, color = Color.Gray)
                        
                        Text(text = item.duration, fontSize = 11.sp, color = Color.Gray)
                        
                        if (isDownloaded) {
                            Spacer(modifier = Modifier.width(2.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF2E7D32).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isBn) "মেমোরিতে সংরক্ষিত" else "Downloaded",
                                    color = Color(0xFF4CAF50),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(10.dp))
            
            // Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button 1: Online Play
                Button(
                    onClick = {
                        if (isCurrentPlayingOnline) {
                            viewModel.toggleCustomAudioPlay()
                        } else {
                            viewModel.playIslamicAudio(item, forceOnline = true)
                        }
                    },
                    modifier = Modifier.weight(1.0f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentPlayingOnline) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                        },
                        contentColor = if (isCurrentPlayingOnline) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    if (isCurrentPlayingOnline && isCustomAudioLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = if (isCurrentPlayingOnline && isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Online Play",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBn) "অনলাইন প্লে" else "Play Online",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Button 2: Download / Local Play
                if (isDownloading) {
                    Row(
                        modifier = Modifier
                            .weight(1.2f)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            progress = (progress ?: 0) / 100f,
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "ডাউনলোড: ${progress ?: 0}%" else "Progress: ${progress ?: 0}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else if (isDownloaded) {
                    Button(
                        onClick = {
                            if (isCurrentPlayingOffline) {
                                viewModel.toggleCustomAudioPlay()
                            } else {
                                viewModel.playIslamicAudio(item, forceOnline = false)
                            }
                        },
                        modifier = Modifier.weight(1.0f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrentPlayingOffline) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                Color(0xFF2E7D32)
                            },
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        if (isCurrentPlayingOffline && isCustomAudioLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Icon(
                                imageVector = if (isCurrentPlayingOffline && isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play Offline",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "অফলাইন প্লে" else "Play Offline",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    IconButton(
                        onClick = { viewModel.deleteDownloadedAudio(item.id) },
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Download",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = { viewModel.downloadIslamicAudio(item) },
                        modifier = Modifier.weight(1.0f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "Download to Storage",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "ডাউনলোড" else "Download",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun YoutubePlayer(videoId: String, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current

    androidx.compose.runtime.key(videoId) {
        androidx.compose.ui.viewinterop.AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView(ctx).apply {
                    lifecycleOwner.lifecycle.addObserver(this)
                    
                    addYouTubePlayerListener(object : com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                            try {
                                youTubePlayer.loadVideo(videoId, 0f)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    })
                }
            },
            update = {
                // Managed by key(videoId) resetting design
            },
            onRelease = { playerView ->
                try {
                    lifecycleOwner.lifecycle.removeObserver(playerView)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    playerView.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        )
    }
}

@Composable
fun BrowserVideoPlayer(urlOrVideoId: String, isYoutube: Boolean, modifier: Modifier = Modifier) {
    androidx.compose.runtime.key(urlOrVideoId) {
        androidx.compose.ui.viewinterop.AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = { ctx ->
                android.webkit.WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        databaseEnabled = true
                        userAgentString = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Mobile Safari/537.36"
                    }
                    webViewClient = android.webkit.WebViewClient()
                    webChromeClient = android.webkit.WebChromeClient()
                    
                    if (isYoutube) {
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
                                <iframe id="ytplayer" type="text/html" width="100%" height="100%"
                                    src="https://www.youtube.com/embed/$urlOrVideoId?autoplay=1&controls=1&fs=1&rel=0&showinfo=0&iv_load_policy=3&modestbranding=1&origin=https://www.youtube.com"
                                    allowfullscreen allow="autoplay; encrypted-media">
                                </iframe>
                            </body>
                            </html>
                        """.trimIndent()
                        loadDataWithBaseURL("https://www.youtube.com", htmlContent, "text/html", "UTF-8", null)
                    } else {
                        val htmlContent = """
                            <!DOCTYPE html>
                            <html>
                            <head>
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <style>
                                    body, html { margin:0; padding:0; width:100%; height:100%; background-color:#000; overflow:hidden; display:flex; justify-content:center; align-items:center; }
                                    video { width:100% !important; height:auto; max-height:100%; outline:none; }
                                </style>
                            </head>
                            <body>
                                <video id="player" controls autoplay playsinline preload="auto">
                                    <source src="$urlOrVideoId" type="application/x-mpegURL">
                                    <source src="$urlOrVideoId" type="video/mp4">
                                    Your browser does not support HTML5 video streaming.
                                </video>
                            </body>
                            </html>
                        """.trimIndent()
                        loadDataWithBaseURL("https://win.makkahlive.net", htmlContent, "text/html", "UTF-8", null)
                    }
                }
            },
            update = {
                // Managed by key
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

@Composable
fun LiveAiTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    var activeTitle by remember { mutableStateOf("") }
    var activeYoutubeId by remember { mutableStateOf("") }
    var activeSpeaker by remember { mutableStateOf("") }
    var useBrowserPlayerForYt by remember { mutableStateOf(true) }
    var activeStreamUrl by remember { mutableStateOf("") }
    var isListLoading by remember { mutableStateOf(false) }

    val youtubeResults by viewModel.youtubeSearchResults.collectAsState()
    val isYoutubeLoading by viewModel.isYoutubeLoading.collectAsState()

    // ==============================================================================
    // DAILYMOTION STATES
    // ==============================================================================
    var activeMediaPlatform by remember { mutableStateOf("youtube") } // "youtube", "dailymotion"
    var activeDailymotionVideoState by remember { mutableStateOf<com.example.data.DailymotionVideo?>(null) }
    var dailymotionSubSection by remember { mutableStateOf("waz") } // "waz", "quran", "nasheed", "dua"
    var dailymotionSearchQuery by remember { mutableStateOf("") }
    var isIslamicFilter by remember { mutableStateOf(true) }
    var dailymotionTab by remember { mutableStateOf("search") } // "search", "bookmarks"

    val dailymotionResults by viewModel.dailymotionSearchResults.collectAsState()
    val isDailymotionLoading by viewModel.isDailymotionLoading.collectAsState()
    val bookmarkedDailymotionVideos by viewModel.dailymotionBookmarkedVideos.collectAsState()

    // Trigger Dailymotion Search dynamically
    LaunchedEffect(activeMediaPlatform, dailymotionSubSection, dailymotionSearchQuery, isIslamicFilter, dailymotionTab) {
        if (activeMediaPlatform == "dailymotion" && dailymotionTab == "search") {
            viewModel.searchDailymotion(
                query = dailymotionSearchQuery,
                category = dailymotionSubSection,
                isIslamicFilter = isIslamicFilter
            )
        }
    }

    if (activeDailymotionVideoState != null) {
        com.example.ui.InAppDailymotionPlayerDialog(
            video = activeDailymotionVideoState!!,
            viewModel = viewModel,
            relatedVideos = emptyList(),
            isBn = isBn,
            onDismiss = { activeDailymotionVideoState = null }
        )
    }

    if (activeYoutubeId.isNotEmpty()) {
        BackHandler {
            activeYoutubeId = ""
        }
    }
    if (activeStreamUrl.isNotEmpty()) {
        BackHandler {
            activeStreamUrl = ""
            viewModel.exoPlayerManager.stop()
        }
    }

    var mediaSubSection by remember { mutableStateOf("waz") } // "waz", "quran", "quran_translation", "nasheed", "video" (Live TV), "dua"
    var searchQuery by remember { mutableStateOf("") }
    var visibleItemCount by remember { mutableStateOf(15) }

    val liveChannels = listOf(
        com.example.data.IslamicVideo(
            id = "live_makkah",
            title = if (isBn) "মক্কা লাইভ সরাসরি (ক্বাবা শরীফ)" else "Makkah Live Stream (Holy Kaaba)",
            speaker = if (isBn) "সরাসরি সম্প্রচার" else "Live Broadcast",
            thumbnail = "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&q=80&w=400",
            duration = "Live",
            youtubeId = "",
            videoUrl = "https://win.makkahlive.net:8443/live/makkah.m3u8"
        ),
        com.example.data.IslamicVideo(
            id = "live_madinah",
            title = if (isBn) "মদিনা লাইভ সরাসরি (মসজিদে নববী)" else "Madinah Live Stream (Masjid Al-Nabawi)",
            speaker = if (isBn) "সরাসরি সম্প্রচার" else "Live Broadcast",
            thumbnail = "https://images.unsplash.com/photo-1597935258735-e254c1839512?auto=format&fit=crop&q=80&w=400",
            duration = "Live",
            youtubeId = "",
            videoUrl = "https://win.madinahlive.net:8443/live/madinah.m3u8"
        ),
        com.example.data.IslamicVideo(
            id = "live_quran",
            title = if (isBn) "আল কুরআন টিভি সরাসরি সম্প্রচার" else "Quran TV Live Stream",
            speaker = if (isBn) "সার্বক্ষণিক তিলাওয়াত" else "24/7 Recitation",
            thumbnail = "https://images.unsplash.com/photo-1609599006353-e629f1d50218?auto=format&fit=crop&q=80&w=400",
            duration = "Live",
            youtubeId = "",
            videoUrl = "https://win.qurantv.net:8443/live/quran.m3u8"
        ),
        com.example.data.IslamicVideo(
            id = "live_islamic",
            title = if (isBn) "ইসলামিক চ্যানেল লাইভ" else "Islamic TV Live Stream",
            speaker = if (isBn) "ইসলামিক আলোচনা" else "Islamic Lectures",
            thumbnail = "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&q=80&w=400",
            duration = "Live",
            youtubeId = "",
            videoUrl = "https://win.islamictv.net:8443/live/islamic.m3u8"
        )
    )

    var selectedTrustedChannelTab by remember { mutableStateOf("all") } // "all" (All Videos), "merciful", "one_islam", "peace"

    // Real-time automatic search observer on category, selected channel tab, or user typing
    LaunchedEffect(selectedTrustedChannelTab, mediaSubSection, searchQuery) {
        if (mediaSubSection != "video") {
            val queryToSearch = if (searchQuery.isNotBlank()) {
                searchQuery
            } else {
                when (selectedTrustedChannelTab) {
                    "merciful" -> "MercifulServant reminders"
                    "one_islam" -> "One Islam Productions lectures dawah"
                    "peace" -> "Peace TV lecture Dr Zakir Naik"
                    else -> {
                        when (mediaSubSection) {
                            "waz" -> "islamic waz bangla"
                            "quran" -> "quran recitation soothing"
                            "quran_translation" -> "quran bangla anubad"
                            "nasheed" -> "beautiful emotional nasheed"
                            else -> "daily duas and azkar"
                        }
                    }
                }
            }
            viewModel.searchYoutube(queryToSearch)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- High-Level Platforms Segment Header ---
        TabRow(
            selectedTabIndex = if (activeMediaPlatform == "youtube") 0 else 1,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Tab(
                selected = activeMediaPlatform == "youtube",
                onClick = { activeMediaPlatform = "youtube" },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "ইউটিউব মিডিয়া" else "YouTube Hub",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            )
            Tab(
                selected = activeMediaPlatform == "dailymotion",
                onClick = { activeMediaPlatform = "dailymotion" },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "ডেইলি-মোশন হাব" else "Dailymotion Hub",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            )
        }

        if (activeMediaPlatform == "youtube") {
            Column(modifier = Modifier.fillMaxSize()) {

            // --- TRUSTED ISLAMIC CHANNELS MODE PORTAL ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, SoftGoldBorder.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = SoftGoldBorder,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "বিশ্বস্ত ইসলামিক চ্যানেল মোড" else "Trusted Islamic Channels Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    ScrollableTabRow(
                        selectedTabIndex = when (selectedTrustedChannelTab) {
                            "all" -> 0
                            "merciful" -> 1
                            "one_islam" -> 2
                            "peace" -> 3
                            else -> 0
                        },
                        containerColor = Color.Transparent,
                        edgePadding = 0.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = selectedTrustedChannelTab == "all",
                            onClick = { selectedTrustedChannelTab = "all" },
                            text = { Text(if (isBn) "সকল ভিডিও" else "All Videos", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTrustedChannelTab == "merciful",
                            onClick = { selectedTrustedChannelTab = "merciful" },
                            text = { Text("MercifulServant", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTrustedChannelTab == "one_islam",
                            onClick = { selectedTrustedChannelTab = "one_islam" },
                            text = { Text("One Islam", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTrustedChannelTab == "peace",
                            onClick = { selectedTrustedChannelTab = "peace" },
                            text = { Text("Peace TV", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }

            // Show general category filter chips ONLY if 'All Videos' tab is selected!
            if (selectedTrustedChannelTab == "all") {
                // 2. CATEGORY HORIZONTAL FILTER CHIPS (ALL UNDER VIDEO PARADIGM!)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    item {
                        FilterChip(
                            selected = mediaSubSection == "waz",
                            onClick = { mediaSubSection = "waz" },
                            label = { Text(if (isBn) "🎙️ ওয়াজ কালেকশন" else "🎙️ Waz Collection", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = mediaSubSection == "quran",
                            onClick = { mediaSubSection = "quran" },
                            label = { Text(if (isBn) "📖 আল-কোরআন তিলাওয়াত" else "📖 Quran Recitation", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = mediaSubSection == "quran_translation",
                            onClick = { mediaSubSection = "quran_translation" },
                            label = { Text(if (isBn) "🔊 কোরআন তিলাওয়াত তরজমা" else "🔊 Quran Sura Translation", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = mediaSubSection == "nasheed",
                            onClick = { mediaSubSection = "nasheed" },
                            label = { Text(if (isBn) "🎵 ইসলামিক গজল" else "🎵 Islamic Gojals", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = mediaSubSection == "video",
                            onClick = { mediaSubSection = "video" },
                            label = { Text(if (isBn) "📺 লাইভ মক্কা/মদিনা ও টিভি" else "📺 Live TV Channels", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    item {
                        FilterChip(
                            selected = mediaSubSection == "dua",
                            onClick = { mediaSubSection = "dua" },
                            label = { Text(if (isBn) "🤲 দোআ ও আমল ভিডিও" else "🤲 Dua & Amal Videos", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // 3. YOUTUBE-STYLE SEARCH BAR
            val placeholderText = if (selectedTrustedChannelTab != "all") {
                if (isBn) "চ্যানেলের বিষয়বস্তু খুঁজুন..." else "Search within this channel..."
            } else {
                when (mediaSubSection) {
                    "waz" -> if (isBn) "ওয়াজের স্পিকার বা বিষয় সার্চ করুন (যেমন: আহমাদুল্লাহ)" else "Search Waz speaker or topic..."
                    "quran" -> if (isBn) "কুরআন তিলাওয়াত কারী বা সূরা সার্চ করুন..." else "Search Quran Reciters or Surah..."
                    "quran_translation" -> if (isBn) "কুরআন বঙ্গানুবাদ বা সূরা খুঁজুন..." else "Search Quran Bengli translation..."
                    "nasheed" -> if (isBn) "ইসলামিক গজলের নাম বা শিল্পী সার্চ করুন..." else "Search Islamic Gojol or Nasheed artist..."
                    "video" -> if (isBn) "লাইভ সম্প্রচার চ্যানেল সার্চ করুন..." else "Search live TV channels..."
                    else -> if (isBn) "প্রয়োজনীয় দোয়া ও আমল ভিডিও সার্চ করুন..." else "Search daily Duas & Azkar..."
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(placeholderText, fontSize = 11.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(24.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            // SMART CHANNELS PORTAL & RECOMMENDATION SECTIONS
            if (selectedTrustedChannelTab == "all" && mediaSubSection != "video" && searchQuery.isBlank()) {
                val recommendedVideos by viewModel.recommendedVideos.collectAsState()
                
                // (A) SMART RECOMMENDATIONS SECTION (Learns user preferences!)
                if (recommendedVideos.isNotEmpty()) {
                    Column(modifier = Modifier.padding(bottom = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = SoftGoldBorder,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBn) "বিশেষ সুপারিশ (স্মার্ট লার্নিং)" else "Recommended for You (Smart)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = if (isBn) "আপনার পছন্দ" else "Based on Clicks",
                                fontSize = 9.sp,
                                color = SoftGoldBorder,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(recommendedVideos) { recItem ->
                                Card(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .clickable {
                                            viewModel.recordVideoClick(recItem)
                                            activeTitle = recItem.title
                                            activeSpeaker = recItem.speaker
                                            activeYoutubeId = recItem.youtubeId
                                            activeStreamUrl = ""
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                                    border = BorderStroke(1.dp, SoftGoldBorder.copy(alpha = 0.25f)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(72.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                        ) {
                                            coil.compose.AsyncImage(
                                                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                                    .data(recItem.thumbnail)
                                                    .crossfade(true)
                                                    .placeholder(com.example.R.drawable.islamic_mecca_wallpaper)
                                                    .error(com.example.R.drawable.islamic_mecca_wallpaper)
                                                    .fallback(com.example.R.drawable.islamic_mecca_wallpaper)
                                                    .build(),
                                                contentDescription = recItem.title,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(3.dp)
                                                    .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                                                    .padding(horizontal = 3.dp, vertical = 0.5.dp)
                                            ) {
                                                Text(
                                                    text = recItem.duration,
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = recItem.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.5.sp,
                                            maxLines = 2,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 12.sp
                                        )
                                        Text(
                                            text = recItem.speaker,
                                            fontSize = 8.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // (B) DEDICATED "TRUSTED CHANNELS" PORTAL LIST
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VideoLibrary,
                            contentDescription = null,
                            tint = SoftGoldBorder,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBn) "বিশ্বস্ত ইসলামিক চ্যানেলসমূহ" else "Explore Trusted Channels Portal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val portalChannels = listOf(
                            Triple("merciful", "MercifulServant", if (isBn) "রিমাইন্ডার ও তিলাওয়াত" else "Quran & Reminders"),
                            Triple("one_islam", "One Islam", if (isBn) "দাওয়া ও লেকচার" else "Lectures & Dawah"),
                            Triple("peace", "Peace TV", if (isBn) "প্রশ্নোত্তর ও আলোচনা" else "Q&A & Discussions")
                        )
                        
                        portalChannels.forEach { (tabKey, chName, chDesc) ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedTrustedChannelTab = tabKey },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                                border = BorderStroke(1.dp, SoftGoldBorder.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = SoftGoldBorder,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = chName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = chDesc,
                                        fontSize = 8.sp,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. GENERATE RELEVANT VIDEO LIST DIRECTLY (ON-DRAG ENDLESS SCROLLING)
            val targetVideos = remember(mediaSubSection, selectedTrustedChannelTab, searchQuery, youtubeResults) {
                if (selectedTrustedChannelTab == "all" && mediaSubSection == "video") {
                    if (searchQuery.isBlank()) liveChannels else {
                        liveChannels.filter {
                            it.title.lowercase().contains(searchQuery.lowercase()) ||
                            it.speaker.lowercase().contains(searchQuery.lowercase())
                        }
                    }
                } else {
                    youtubeResults
                }
            }

            val isListLoadingToShow = if (selectedTrustedChannelTab == "all" && mediaSubSection == "video") false else isYoutubeLoading
            if (activeYoutubeId.isNotEmpty()) {
                InAppYoutubePlayerDialog(
                    videoId = activeYoutubeId,
                    videoTitle = activeTitle,
                    videoSpeaker = activeSpeaker,
                    relatedVideos = youtubeResults,
                    isBn = isBn,
                    onDismiss = { activeYoutubeId = "" },
                    onVideoBlocked = { blockedId ->
                        viewModel.removeVideoFromResults(blockedId)
                    }
                )
            }

            // (B) Live Direct HLS/Exo Player Video Dialog (Media3 Native)
            if (activeStreamUrl.isNotEmpty()) {
                val isBuffering by viewModel.exoPlayerManager.isBuffering.collectAsState()
                
                androidx.compose.ui.window.Dialog(
                    onDismissRequest = { 
                        activeStreamUrl = ""
                        viewModel.exoPlayerManager.stop()
                    },
                    properties = androidx.compose.ui.window.DialogProperties(
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

                        if (isBuffering) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = SoftGoldBorder, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isBn) "বাফারিং হচ্ছে..." else "Buffering video...",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Navigation header overlay - translucent and floating with rounded corners
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .padding(12.dp)
                                .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilledIconButton(
                                onClick = { 
                                    activeStreamUrl = ""
                                    viewModel.exoPlayerManager.stop()
                                },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = Color.White.copy(alpha = 0.15f),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.size(34.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                                    contentDescription = if (isBn) "ফিরে যান" else "Back",
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeTitle,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isBn) "সরাসরি লাইভ টিভি • এক্সো-প্লেয়ার" else "Live Stream • ExoPlayer Full Screen",
                                    color = SoftGoldBorder,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            if (isListLoadingToShow) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(38.dp),
                            strokeWidth = 3.5.dp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (isBn) "ইউটিউব সার্ভার থেকে ভিডিও লোড করা হচ্ছে..." else "Loading latest YouTube feeds...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (targetVideos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBn) "দুঃখিত! অনুসন্ধান অনুযায়ী কোনো ভিডিও পাওয়া যায়নি।" else "No videos found for this search.",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(
                        items = targetVideos,
                        key = { it.id }
                    ) { item ->
                        val isCurrentSelection = if (item.youtubeId.isNotEmpty()) activeYoutubeId == item.youtubeId else activeStreamUrl == item.videoUrl
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.recordVideoClick(item)
                                        activeTitle = item.title
                                        activeSpeaker = item.speaker
                                        if (item.youtubeId.isNotEmpty()) {
                                            activeYoutubeId = item.youtubeId
                                            activeStreamUrl = ""
                                        } else {
                                            activeStreamUrl = item.videoUrl
                                            activeYoutubeId = ""
                                            viewModel.exoPlayerManager.playStream(
                                                url = item.videoUrl,
                                                title = item.title,
                                                subtitle = if (isBn) "লাইভ সম্প্রচার" else "Live Stream",
                                                isVideo = true
                                            )
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCurrentSelection) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    width = if (isCurrentSelection) 1.5.dp else 1.dp,
                                    color = if (isCurrentSelection) SoftGoldBorder else SoftGoldBorder.copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Image Thumbnail with Video Duration Stamp overlay!
                                    Box(
                                        modifier = Modifier
                                            .size(width = 110.dp, height = 70.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    ) {
                                        coil.compose.AsyncImage(
                                            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                                .data(item.thumbnail)
                                                .crossfade(true)
                                                .placeholder(com.example.R.drawable.islamic_mecca_wallpaper)
                                                .error(com.example.R.drawable.islamic_mecca_wallpaper)
                                                .fallback(com.example.R.drawable.islamic_mecca_wallpaper)
                                                .build(),
                                            contentDescription = item.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(4.dp)
                                                .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = item.duration,
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    
                                    Spacer(modifier = Modifier.width(10.dp))
                                    
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.speaker,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        val views = remember(item.id) { 15 + (Math.abs(item.id.hashCode()) % 936) }
                                        Text(
                                            text = if (isBn) "${views}K ভিউ • ৩ দিন আগে (ভিডিও প্লে করতে চাপুন)" else "${views}K views • 3 days ago (Tap to Play)",
                                            fontSize = 8.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    
                                    Icon(
                                        imageVector = if (isCurrentSelection) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                        contentDescription = "Play/Pause Video",
                                        tint = if (isCurrentSelection) MaterialTheme.colorScheme.primary else SoftGoldBorder,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .padding(end = 4.dp)
                                    )
                                }
                            }
                        }

                        // Load More Button directly in YouTube-Style Infinite Feed!
                        if (mediaSubSection != "video") {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Button(
                                        onClick = { visibleItemCount += 15 },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                        border = BorderStroke(1.dp, SoftGoldBorder)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isBn) "অনবরত আরও ওয়াজ ও ভিডিও লোড করুন" else "Load More Videos (Endless Feed)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // ==============================================================================
            // DAILYMOTION MEDIA SYSTEM SECTION
            // ==============================================================================
            Column(modifier = Modifier.fillMaxSize()) {
                // Secondary level Tab row for search explores vs favorite collection
                TabRow(
                    selectedTabIndex = if (dailymotionTab == "search") 0 else 1,
                    containerColor = Color.Transparent,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Tab(
                        selected = dailymotionTab == "search",
                        onClick = { dailymotionTab = "search" },
                        text = { Text(if (isBn) "🔍 নতুন সন্ধান করুন" else "🔍 Explore Videos", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = dailymotionTab == "bookmarks",
                        onClick = { dailymotionTab = "bookmarks" },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFFFD700))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isBn) "⭐ প্রিয় তালিকা (${bookmarkedDailymotionVideos.size})" else "⭐ Favorites (${bookmarkedDailymotionVideos.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }

                if (dailymotionTab == "search") {
                    // Safe filter & category rows
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBn) "ইসলামিক সেফ ফিল্টার" else "Islamic Safe Filter",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        androidx.compose.material3.Switch(
                            checked = isIslamicFilter,
                            onCheckedChange = { isIslamicFilter = it },
                            modifier = Modifier.scale(0.8f)
                        )
                    }

                    // Category horizontal chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = dailymotionSubSection == "waz",
                                onClick = { dailymotionSubSection = "waz" },
                                label = { Text(if (isBn) "🎙️ ওয়াজ কালেকশন" else "🎙️ Waz Collection", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = dailymotionSubSection == "quran",
                                onClick = { dailymotionSubSection = "quran" },
                                label = { Text(if (isBn) "📖 আল-কোরআন তিলাওয়াত" else "📖 Quran Recitation", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = dailymotionSubSection == "nasheed",
                                onClick = { dailymotionSubSection = "nasheed" },
                                label = { Text(if (isBn) "🎵 ইসলামিক গজল" else "🎵 Islamic Gojals", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                        item {
                            FilterChip(
                                selected = dailymotionSubSection == "dua",
                                onClick = { dailymotionSubSection = "dua" },
                                label = { Text(if (isBn) "🤲 দোআ ও আমল" else "🤲 Dua & Amal", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    // Search input text field
                    OutlinedTextField(
                        value = dailymotionSearchQuery,
                        onValueChange = { dailymotionSearchQuery = it },
                        placeholder = { Text(if (isBn) "সার্চ করুন..." else "Search keyword...", fontSize = 11.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = {
                            if (dailymotionSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { dailymotionSearchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(24.dp),
                        textStyle = MaterialTheme.typography.bodySmall,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    // Results content list
                    if (isDailymotionLoading) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else if (dailymotionResults.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = if (isBn) "কোনো সার্চ রেজাল্ট পাওয়া যায়নি। অনুগ্রহ করে অন্য কিছু খুঁজুন। (ডেইলি-মোশন অফলাইনেও পছন্দের তালিকা দেখতে পারবেন)" else "No videos found. Try a different query. (Offline bookmarks supported)",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f)
                        ) {
                            items(dailymotionResults) { item ->
                                DailymotionVideoRowCard(
                                    video = item,
                                    isBn = isBn,
                                    viewModel = viewModel,
                                    onClick = { activeDailymotionVideoState = item }
                                )
                            }
                        }
                    }

                } else {
                    // BOOKMARKS/FAVORITES VIEW
                    if (bookmarkedDailymotionVideos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.Gray.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isBn) "আপনার প্রিয় তালিকায় কোনো ডেইলি-মোশন ভিডিও এখনো যোগ করা হয়নি।" else "No Dailymotion videos bookmarked yet.",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 8.dp)
                        ) {
                            items(bookmarkedDailymotionVideos) { item ->
                                DailymotionVideoRowCard(
                                    video = item,
                                    isBn = isBn,
                                    viewModel = viewModel,
                                    onClick = { activeDailymotionVideoState = item }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DailymotionVideoRowCard(
    video: com.example.data.DailymotionVideo,
    isBn: Boolean,
    viewModel: IbadahViewModel,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val bookmarks by viewModel.dailymotionBookmarks.collectAsState()
    val isCurrBookmarked = bookmarks.contains(video.id)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
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
                        .padding(4.dp)
                        .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = video.durationString,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = video.channelName,
                        color = SoftGoldBorder,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { viewModel.toggleDailymotionBookmark(video) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Bookmark",
                            tint = if (isCurrBookmarked) Color(0xFFFFD700) else Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// standalone generator function for endless videos recommendations
private fun generateIslamicVideos(category: String, query: String, count: Int, isBn: Boolean): List<com.example.data.IslamicVideo> {
    val list = mutableListOf<com.example.data.IslamicVideo>()
    val queryClean = query.trim().lowercase()
    
    val thumbnails = listOf(
        "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?auto=format&fit=crop&q=80&w=400",
        "https://images.unsplash.com/photo-1597935258735-e254c1839512?auto=format&fit=crop&q=80&w=400",
        "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&q=80&w=400",
        "https://images.unsplash.com/photo-1609599006353-e629f1d50218?auto=format&fit=crop&q=80&w=400"
    )
    
    val videoUrls = listOf(
        "https://archive.org/download/Al_Quran_Video/055_Ar_Rahman.mp4",
        "https://archive.org/download/Al_Quran_Video/036_Ya_Sin.mp4",
        "https://archive.org/download/Al_Quran_Video/001_Al_Fatihah.mp4",
        "https://archive.org/download/Al_Quran_Video/067_Al_Mulk.mp4",
        "https://archive.org/download/Al_Quran_Video/018_Al_Kahf.mp4",
        "https://archive.org/download/Al_Quran_Video/056_Al_Waqi_ah.mp4",
        "https://archive.org/download/Al_Quran_Video/078_An_Naba.mp4",
        "https://archive.org/download/Al_Quran_Video/089_Al_Fajr.mp4",
        "https://archive.org/download/Al_Quran_Video/093_Ad_Duha.mp4",
        "https://archive.org/download/Al_Quran_Video/114_An_Nas.mp4"
    )

    when (category) {
        "waz" -> {
            val speakers = listOf(
                Pair("শায়খ আহমাদুল্লাহ", "Shaykh Ahmadullah"),
                Pair("মিজানুর রহমান আজহারী", "Mizanur Rahman Azhari"),
                Pair("ড. খন্দকার আব্দুল্লাহ জাহাঙ্গীর", "Dr. Abdullah Jahangir"),
                Pair("মাওলানা তারেক জামিল", "Maulana Tariq Jamil"),
                Pair("ড. জাকির নায়েক", "Dr. Zakir Naik"),
                Pair("মাওলানা আবরারুল হক আসিফ", "Maulana Abrarul Haq Asif")
            )
            val subjects = listOf(
                Pair("সুখী পরিবার গঠনের সোনালী উপায় ও জীবন ব্যবস্থা", "Golden Rules for a Happy Family & Life Guidelines"),
                Pair("তওবা ও ইস্তিগফারের অসাধারণ শক্তি এবং আল্লাহর দয়া", "Amazing Power of Repentance and Allah's Mercy"),
                Pair("হাশরের ময়দানের ভয়াবহ দৃশ্য ও মুক্তির সহজ আমল", "Frightening Scene of Resurrection & Deliverance"),
                Pair("হজরত ইউসুফ (আঃ) এর জীবনী থেকে আমাদের শিক্ষা", "Lessons from the Life of Prophet Yusuf (AS)"),
                Pair("রমজান মাসের প্রস্তুতি ও বরকতময় আমলসমূহ", "Preparations and Blessed Deeds for Ramadan"),
                Pair("যাকাত কীভাবে সমাজ ও বাস্তব জীবন বদলে দেয়", "How Zakat Transforms Society & Real Life"),
                Pair("মিথ্যার ভয়াবহতা ও সত্যবাদিতার অসামান্য পুরস্কার", "Dangers of Lies & Tremendous Reward of Truthfulness"),
                Pair("হালাল উপার্জনের অলৌকিক বরকত ও সমৃদ্ধি", "Miraculous Blessings of Halal Sustenance & Prosperity"),
                Pair("জান্নাতে যাওয়ার সহজ ১০টি সোনালী আমল ও আমলনামা", "10 Simple Golden Deeds to Enter Paradise"),
                Pair("কবরের প্রথম রাত ও অত্যন্ত জরুরি প্রশ্নের উত্তর", "First Night in the Grave & Urgent Answers to Munkar-Nakir"),
                Pair("মা-বাবার দোয়া কীভাবে মানুষের ভাগ্য পরিবর্তন করে", "How Parents' Prayers Change Destiny & Status"),
                Pair("অহংকার ও হিংসার বিষাক্ত থাবা থেকে সুস্থ থাকার পথ", "Escaping the Toxic Grip of Arrogance & Envy"),
                Pair("নামাজ কায়েমের অপরিহার্য গুরুত্ব ও অলসতার শাস্তি", "Salah: Dynamic Pillar of Faith & Negligence Consequences"),
                Pair("শয়তানের ফাঁদ ও সুন্দর জীবন গড়ার অনন্য সমাধান", "Satan's Traps & Unique Solutions to Build a Pure Life"),
                Pair("উত্তম চরিত্র অর্জনের বাস্তবমুখী গাইডলাইন", "Practical Guidelines for Excelled Manners")
            )

            for (i in 0 until count) {
                val speaker = speakers[i % speakers.size]
                val subject = subjects[i % subjects.size]
                
                val titleBn = "${subject.first} - ${speaker.first} ওয়াজ"
                val titleEn = "${subject.second} - Lecture by ${speaker.second}"
                
                val name = if (isBn) speaker.first else speaker.second
                val title = if (isBn) titleBn else titleEn
                val duration = String.format("%02d:%02d", (10 + i * 3) % 60 + 10, (15 + i * 7) % 60)
                
                val wazYts = listOf("sOnFscD795o", "6g_f3O-46sc", "N80C2U9uI8c", "U_65iMKyX00", "6WvIdMcbo6I")
                val youtubeId = wazYts[i % wazYts.size]
                val thumbnail = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
                
                val item = com.example.data.IslamicVideo(
                    id = "waz_gen_$i",
                    title = title,
                    speaker = name,
                    thumbnail = thumbnail,
                    duration = duration,
                    youtubeId = youtubeId,
                    videoUrl = videoUrls[i % videoUrls.size]
                )
                
                if (queryClean.isEmpty() || 
                    title.lowercase().contains(queryClean) || 
                    name.lowercase().contains(queryClean)) {
                    list.add(item)
                }
            }
        }
        "quran" -> {
            val reciters = listOf(
                Pair("কারী মিশারী রশিদ আল-আফাসী", "Mishary Rashid Alafasy"),
                Pair("শায়খ আব্দুল রহমান আস-সুদাইস", "Abdur Rahman As-Sudais"),
                Pair("শায়খ মাহমুদ খলিল আল-হুসারি", "Mahmoud Khalil Al-Husary"),
                Pair("শায়খ সাউদ আশ-শুরাইম", "Saud Al-Shuraim"),
                Pair("শায়খ ইয়াসির আদ-দাওসারী", "Yasser Al-Dosari"),
                Pair("শায়খ মাহের আল-মুয়াইকিলী", "Maher Al-Muaiqly")
            )
            val surahs = listOf(
                Triple("আল-ফাতিহা", "Al-Fatihah", "001"),
                Triple("আল-বাকারাহ", "Al-Baqarah", "002"),
                Triple("আলে ইমরান", "Ali 'Imran", "003"),
                Triple("ইউসুফ", "Yusuf", "012"),
                Triple("আল-কাহাফ", "Al-Kahf", "018"),
                Triple("ইয়াসিন", "Ya-Sin", "036"),
                Triple("আর-রহমান", "Ar-Rahman", "055"),
                Triple("আল-ওয়াকিয়াহ", "Al-Waqi'ah", "056"),
                Triple("আল-মূলক", "Al-Mulk", "067"),
                Triple("আন-নাবা", "An-Naba", "078"),
                Triple("আল-ফাজর", "Al-Fajr", "089"),
                Triple("আদ-দুহা", "Ad-Duha", "093"),
                Triple("আত-তীন", "At-Tin", "095"),
                Triple("আল-কদর", "Al-Qadr", "097"),
                Triple("আল-আছর", "Al-Asr", "103"),
                Triple("আল-কাওসার", "Al-Kawsar", "108"),
                Triple("আল-ইখলাস", "Al-Ikhlas", "112"),
                Triple("আল-ফালাক", "Al-Falaq", "113"),
                Triple("আন-নাস", "An-Nas", "114")
            )

            for (i in 0 until count) {
                val reciter = reciters[i % reciters.size]
                val surah = surahs[i % surahs.size]
                
                val titleBn = "সূরা ${surah.first} - মনকাড়া সুন্দর তিলাওয়াত ভিডিও"
                val titleEn = "Surah ${surah.second} - Soulful Recitation Video"
                
                val name = if (isBn) reciter.first else reciter.second
                val title = if (isBn) titleBn else titleEn
                val duration = String.format("%02d:%02d", (5 + i * 2) % 45 + 5, (10 + i * 8) % 60)
                
                val quranYts = listOf("Sg_kP23x1Qc", "6WvIdMcbo6I", "X_u-0uR1q-s", "y7u1oF4fQ10", "S20a0Z_eSm0")
                val youtubeId = quranYts[i % quranYts.size]
                val thumbnail = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
                
                val item = com.example.data.IslamicVideo(
                    id = "quran_gen_$i",
                    title = title,
                    speaker = name,
                    thumbnail = thumbnail,
                    duration = duration,
                    youtubeId = youtubeId,
                    videoUrl = videoUrls[i % videoUrls.size]
                )
                
                if (queryClean.isEmpty() || 
                    title.lowercase().contains(queryClean) || 
                    name.lowercase().contains(queryClean)) {
                    list.add(item)
                }
            }
        }
        "quran_translation" -> {
            val reciters = listOf(
                Pair("কারী মিশারী রশিদ আল-আফাসী", "Mishary Rashid Alafasy"),
                Pair("শায়খ আব্দুল রহমান আস-সুদাইস", "Abdur Rahman As-Sudais"),
                Pair("কারী আব্দুল বাসেত আব্দুস সামাদ", "Abdul Basit Abdus Samad"),
                Pair("শায়খ মাহের আল-মুয়াইকিলী", "Maher Al-Muaiqly")
            )
            val surahs = listOf(
                Triple("আল-ফাতিহা", "Al-Fatihah", "001"),
                Triple("আল-মূলক", "Al-Mulk", "067"),
                Triple("আর-রহমান", "Ar-Rahman", "055"),
                Triple("ইয়াসিন", "Ya-Sin", "036"),
                Triple("আল-কাহাফ", "Al-Kahf", "018"),
                Triple("আল-ওয়াকিয়াহ", "Al-Waqi'ah", "056"),
                Triple("সূরা লোকমান", "Surah Luqman", "031"),
                Triple("সূরা আর-রূম", "Surah Ar-Rum", "030"),
                Triple("সূরা আল-হুজুরাত", "Surah Al-Hujurat", "049"),
                Triple("সূরা আত-তারিক", "Surah At-Tariq", "086"),
                Triple("সূরা আল-বালাদ", "Surah Al-Balad", "090")
            )

            for (i in 0 until count) {
                val reciter = reciters[i % reciters.size]
                val surah = surahs[i % surahs.size]
                
                val titleBn = "সূরা ${surah.first} (সহজ বাংলা অনুবাদসহ ভিডিও)"
                val titleEn = "Surah ${surah.second} (Translation and Meaning Video)"
                
                val name = if (isBn) reciter.first else reciter.second
                val title = if (isBn) titleBn else titleEn
                val duration = String.format("%02d:%02d", (8 + i * 4) % 55 + 5, (20 + i * 6) % 60)
                
                val tagYts = listOf("Sg_kP23x1Qc", "6WvIdMcbo6I", "X_u-0uR1q-s", "y7u1oF4fQ10", "S20a0Z_eSm0")
                val youtubeId = tagYts[i % tagYts.size]
                val thumbnail = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
                
                val item = com.example.data.IslamicVideo(
                    id = "quran_trans_gen_$i",
                    title = title,
                    speaker = name,
                    thumbnail = thumbnail,
                    duration = duration,
                    youtubeId = youtubeId,
                    videoUrl = videoUrls[i % videoUrls.size]
                )
                
                if (queryClean.isEmpty() || 
                    title.lowercase().contains(queryClean) || 
                    name.lowercase().contains(queryClean)) {
                    list.add(item)
                }
            }
        }
        "nasheed" -> {
            val artists = listOf(
                Pair("কলরব শিল্পীগোষ্ঠী", "Kalarab Sound"),
                Pair("মুহিব খান", "Muhib Khan"),
                Pair("আবু রায়হান কলরব", "Abu Rayhan"),
                Pair("মিশারী রশিদ গজল", "Mishary Alafasy Nasheed"),
                Pair("ইকবাল এইচ ভুঁইয়া", "Iqbal H Bhuiyan"),
                Pair("সারওয়ার আল হাসানি", "Sarwar Al Hasani")
            )
            val gojols = listOf(
                Pair("আল্লাহ আমার রব, এই রবই আমার সর্বশ্রেষ্ঠ শক্তি", "Allah is My Rabb, My Ultimate Strength"),
                Pair("ত্রিভুবনের প্রিয় মুহাম্মদ এলো রে সোনার মদিনায়", "The Most Beloved Muhammad is Born in Beautiful Madinah"),
                Pair("শোনো গো জান্নাতের হূর-গিলমান সুন্দর বাণী", "Hear the Melodies of the Heavens - Soulful Nasheed"),
                Pair("রাসূল আমার ভালোবাসার ছবি ও মনের ধ্রুবতারা", "The Messenger of Mercy is the Bright Star of My Soul"),
                Pair("হাসবি রাব্বি জাল্লাল্লাহ মা ফি ক্বালবি গাইরুল্লাহ সুর", "Hasbi Rabbi Jallallah, Naught in My Heart but Allah Divine Voice"),
                Pair("রহমতে আলম নূর কা মুজাসসাম পেয়ারা খোদা", "O Mercy of the Realms, Light Embodiment"),
                Pair("মাওলা ইয়া সাল্লি ওয়া সাল্লিম মদিনার গজল", "Mawla Ya Salli Wa Sallim - Madinah Heart touching Gojol"),
                Pair("দিনের শেষে রাতের শেষে ক্ষমা করো দয়াল খোদা", "At the End of Day & Night Forgive Us O Allah"),
                Pair("কোরআন আমার বুকের ভেতরের সেরা উপহার", "Quran: The Best Gift Encased in My Chest"),
                Pair("নামাজকে বলো না কাজ আছে, বরং কাজকে বলো নামাজ আছে", "Do Not Tell Salah You Have Work, Tell Work You Have Salah"),
                Pair("দয়া করো ওগো সর্বশক্তিমান প্রভু মাবুদ", "Have Mercy Upon Us, O Omnipotent Creator")
            )

            for (i in 0 until count) {
                val artist = artists[i % artists.size]
                val gojol = gojols[i % gojols.size]
                
                val titleBn = "${gojol.first} (মধুর গজল)"
                val titleEn = "${gojol.second} - Beautiful Nasheed Recitation"
                
                val name = if (isBn) artist.first else artist.second
                val title = if (isBn) titleBn else titleEn
                val duration = String.format("%02d:%02d", (3 + i) % 10 + 3, (5 + i * 11) % 60)
                
                val nasheedYts = listOf("O_1LgUWeYsw", "Sg_kP23x1Qc", "6WvIdMcbo6I", "X_u-0uR1q-s", "y7u1oF4fQ10")
                val youtubeId = nasheedYts[i % nasheedYts.size]
                val thumbnail = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
                
                val item = com.example.data.IslamicVideo(
                    id = "nasheed_gen_$i",
                    title = title,
                    speaker = name,
                    thumbnail = thumbnail,
                    duration = duration,
                    youtubeId = youtubeId,
                    videoUrl = videoUrls[i % videoUrls.size]
                )
                
                if (queryClean.isEmpty() || 
                    title.lowercase().contains(queryClean) || 
                    name.lowercase().contains(queryClean)) {
                    list.add(item)
                }
            }
        }
        "dua" -> {
            val scholars = listOf(
                Pair("শায়খ মোস্তফা আল-আজহারী", "Shaykh Mostafa Al-Azhari"),
                Pair("শায়খ আহমাদুল্লাহ", "Shaykh Ahmadullah"),
                Pair("মিজানুর রহমান আজহারী", "Mizanur Rahman Azhari"),
                Pair("কারী শাকির কাসমি", "Qari Shakir Qasmi")
            )
            val duas = listOf(
                Pair("সকাল ও সন্ধ্যার অত্যন্ত জরুরি সুন্নাহ ও মাসনুন দোআসমূহ", "Essential Supplications for Morning & Evening Protection"),
                Pair("ঘুমানোর পূর্বে অত্যন্ত মর্যাদাপূর্ণ আমল ও সূরা আযকার", "Glorious Sunnah and Surahs Before Sleeping"),
                Pair("বিপত্তি ও ভয়াবহ রোগ বালাই থেকে বাঁচার কার্যকারী দুয়া", "Powerful Dua Taught by Prophet for Protection Against Crisis"),
                Pair("ঋণ পরিশোধে নবী কারীম (সা.) এর শেখানো আমল ও দোআ", "Dua Taught by Prophet (S) for Quick Debt Elimination"),
                Pair("দুশ্চিন্তা ও অস্থিরতা থেকে চিরতরে মুক্তির সবচেয়ে বড় আমল", "Ultimate Medication & Qur'an Cure for Anxiety"),
                Pair("তওবার সর্বশ্রেষ্ঠ আমল: সাইয়্যেদুল ইস্তিগফার অর্থসহ পাঠ", "Master of Repentance: Sayyidul Istighfar with Translation"),
                Pair("রুকইয়াহ শরীয়াহ: জিন, শয়তান ও নজর থেকে নিরাপদে থাকার আমল", "Ruqyah Shariah: Guard Against Evil Eyes, Jinns & Satin"),
                Pair("রিযিক ও ব্যবসা-বাণিজ্যে বরকত বৃদ্ধির ৩টি সহীহ কোরআনী দুআ", "3 Authentic Quranic Prayers to Boost Halal Sustenance")
            )

            for (i in 0 until count) {
                val scholar = scholars[i % scholars.size]
                val dua = duas[i % duas.size]
                
                val titleBn = "${dua.first} (সহীহ আমল)"
                val titleEn = "${dua.second} - Solacing Daily Prayer"
                
                val name = if (isBn) scholar.first else scholar.second
                val title = if (isBn) titleBn else titleEn
                val duration = String.format("%02d:%02d", (4 + i * 2) % 20 + 3, (15 + i * 9) % 60)
                
                val duaYts = listOf("sOnFscD795o", "Sg_kP23x1Qc", "6WvIdMcbo6I", "X_u-0uR1q-s", "y7u1oF4fQ10")
                val youtubeId = duaYts[i % duaYts.size]
                val thumbnail = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
                
                val item = com.example.data.IslamicVideo(
                    id = "dua_gen_$i",
                    title = title,
                    speaker = name,
                    thumbnail = thumbnail,
                    duration = duration,
                    youtubeId = youtubeId,
                    videoUrl = videoUrls[i % videoUrls.size]
                )
                
                if (queryClean.isEmpty() || 
                    title.lowercase().contains(queryClean) || 
                    name.lowercase().contains(queryClean)) {
                    list.add(item)
                }
            }
        }
    }
    
    if (list.isEmpty() && query.isNotEmpty()) {
        val title = if (isBn) "\"$query\" সম্পর্কিত ইসলামিক ওয়াজ ও تিলাওয়াত ভিডিও" else "Islamic video for \"$query\""
        val fallbackYt = "sOnFscD795o"
        list.add(
            com.example.data.IslamicVideo(
                id = "query_custom_match",
                title = title,
                speaker = if (isBn) "ইসলামিক আলেম ও বক্তা" else "Verified Islamic Scholar",
                thumbnail = "https://img.youtube.com/vi/$fallbackYt/hqdefault.jpg",
                duration = "১১:৪৫",
                youtubeId = fallbackYt,
                videoUrl = videoUrls[0]
            )
        )
    }
    
    return list
}
// ===================================================================================
// 6. SETTINGS & NOTICE BOARD / ADMIN TAB SCREEN
// ===================================================================================
@Composable
fun SettingsNoticeTabScreen(viewModel: IbadahViewModel, isBn: Boolean) {
    var adminModeActive by remember { mutableStateOf(false) } // False: Settings, True: Admin Panel

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = if (adminModeActive) 1 else 0,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Tab(
                selected = !adminModeActive,
                onClick = { adminModeActive = false },
                text = { Text(if (isBn) "সেটিংস ও সাহায্য" else "Settings & Support", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = adminModeActive,
                onClick = { adminModeActive = true },
                text = { Text(if (isBn) "এডমিন নোটিশ বোর্ড" else "Admin Board", fontWeight = FontWeight.Bold) }
            )
        }

        if (!adminModeActive) {
            // General settings panel
            val currentLanguage by viewModel.appLanguage.collectAsState()
            val isBn = currentLanguage == AppLanguage.BN
            val isAr = currentLanguage == AppLanguage.AR
            val isDarkTheme by viewModel.isDarkMode.collectAsState()
            val context = LocalContext.current

            var activeAdjustAlarmId by remember { mutableStateOf<String?>(null) }
            var activeAdjustValue by remember { mutableIntStateOf(0) }
            var showAzanPlayDialog by remember { mutableStateOf(false) }
            var playingAzanTheme by remember { mutableStateOf("") }

            LaunchedEffect(showAzanPlayDialog) {
                if (showAzanPlayDialog) {
                    viewModel.startAzanTest()
                } else {
                    viewModel.stopAzanTest()
                }
            }

            var showNewAlarmDialog by remember { mutableStateOf(false) }
            var showAboutAppDialog by remember { mutableStateOf(false) }
            var isAlarmSettingsExpanded by remember { mutableStateOf(false) }
            var newAlarmLabelBn by remember { mutableStateOf("") }
            var newAlarmLabelEn by remember { mutableStateOf("") }
            var newAlarmHour by remember { mutableIntStateOf(12) }
            var newAlarmMinute by remember { mutableIntStateOf(0) }
            var newAlarmAmPm by remember { mutableStateOf("PM") }
            var customAlarmType by remember { mutableStateOf("daily") } // "daily", "weekdays", "date"
            var selectedCustomDays by remember { mutableStateOf(setOf<Int>()) } // set of 1..7 (1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat)
            var specificYear by remember { mutableIntStateOf(2026) }
            var specificMonth by remember { mutableIntStateOf(6) }
            var specificDay by remember { mutableIntStateOf(1) }

            // Local Helper to parse base 24h string, adjust with offset, and format to localized 12h representation
            fun getAlarmTimeString(baseTimeStr: String, offsetMins: Int, language: AppLanguage): String {
                try {
                    val parts = baseTimeStr.split(":")
                    if (parts.size != 2) return baseTimeStr
                    val hr = parts[0].toIntOrNull() ?: 0
                    val min = parts[1].toIntOrNull() ?: 0
                    val totalMins = (hr * 60 + min + offsetMins + 1440) % 1440
                    val newHr = totalMins / 60
                    val newMin = totalMins % 60
                    val rawTime = String.format("%02d:%02d", newHr, newMin)
                    return formatTo12Hour(rawTime, language)
                } catch (e: Exception) {
                    return baseTimeStr
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Multi language
                item {
                    Text(
                        text = if (isBn) "ভাষা নির্বাচন (Multi-Language)" else if (isAr) "اختر اللغة العامة للمشروع" else "Select App Language", 
                        fontWeight = FontWeight.Bold, 
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.setLanguage(AppLanguage.BN) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentLanguage == AppLanguage.BN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text("বাংলা", fontSize = 11.sp, maxLines = 1)
                        }
                        Button(
                            onClick = { viewModel.setLanguage(AppLanguage.EN) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentLanguage == AppLanguage.EN) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text("English", fontSize = 11.sp, maxLines = 1)
                        }
                        Button(
                            onClick = { viewModel.setLanguage(AppLanguage.AR) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentLanguage == AppLanguage.AR) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        ) {
                            Text("العربية", fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }

                // -------------------------------------------------------------
                // GENERAL APP SETTINGS (Dark Mode, 12h/24h, Bg Music)
                // -------------------------------------------------------------
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBn) "সাধারণ সেটিংস" else if (isAr) "الإعدادات العامة" else "General Settings",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val use24HourFormat by viewModel.use24HourFormat.collectAsState()
                    val isBgMusicEnabled by viewModel.isBgMusicEnabled.collectAsState()
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // 1. Dark Mode Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = if (isBn) "ডার্ক মোড" else "Dark Mode",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = if (isBn) "অন্ধকারে চোখের সুরক্ষার জন্য স্ক্রিন ডার্ক করুন" else "Enable dark canvas for eye protection",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { viewModel.toggleDarkMode() }
                                )
                            }
                            
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            
                            // 2. Time Format Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = if (isBn) "সময় বিন্যাস (২৪ ঘণ্টা সিস্টেম)" else "Time System (24h format)",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = if (isBn) "১২ ঘণ্টার জায়গায় ২৪ ঘণ্টা সময় বিন্যাস ব্যবহার করুন" else "Activate 24-hour military clock display",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                Switch(
                                    checked = use24HourFormat,
                                    onCheckedChange = { viewModel.toggleTimeFormat() }
                                )
                            }
                            
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            
                            // 3. Islamic Background Music Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isBgMusicEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = if (isBn) "ব্যাকগ্রাউন্ড গজল/মিউজিক" else "Background Islamic Tone",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = if (isBn) "ট্যাব পরিবর্তন করলে হালকা টনে ইসলামিক নাশিদ বাজবে" else "Gentle ambient background tracks on tab switch",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                Switch(
                                    checked = isBgMusicEnabled,
                                    onCheckedChange = { viewModel.toggleBgMusic() }
                                )
                            }
                        }
                    }
                }

                // -------------------------------------------------------------
                // PREMIUM PRAYER ALARM ENGINE (M3 Dashboard CARD System)
                // -------------------------------------------------------------
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    // Clickable Expandable Row for Alarm Settings
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isAlarmSettingsExpanded = !isAlarmSettingsExpanded },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isAlarmSettingsExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                                             else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isAlarmSettingsExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                   else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (isBn) "নামাজের সময় এলার্ম সেটিংস" 
                                               else if (isAr) "إعدادات منبه أوقات الصلاة" 
                                               else "Prayer Alarm Configurations",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = if (isBn) "আজান, নোটিফিকেশন, সিঙ্ক কনফিগার করুন" 
                                               else if (isAr) "تعديل وقت تنبيه أوقাত الصلاة" 
                                               else "Configure daily Azan, offsets and alerts",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Icon(
                                imageVector = if (isAlarmSettingsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (isAlarmSettingsExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) "প্রতিটি নামাজের ওয়াক্ত শুরু বা তার পূর্বে আজান ও নোটিফিকেশন এলার্ম দিন। সময় ও ধরন আপনার ইচ্ছামতো পরিবর্তন করতে পারবেন।" 
                                   else if (isAr) "اضبط منبه أذان أو نغمة خاصة لكل صلاة مع إمكانية تعديل وقت التنبيه للصلوات الخمس للتبيه اليومي أو لمرة واحدة."
                                   else "Configure Azan bells or standard beeps for each prayer time. Set specific offsets and recurrence formats easily.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val activeAlarms by viewModel.prayerAlarms.collectAsState()
                        val selectedCity by viewModel.selectedCity.collectAsState()

                    OutlinedButton(
                        onClick = {
                            newAlarmLabelBn = ""
                            newAlarmLabelEn = ""
                            newAlarmHour = 12
                            newAlarmMinute = 0
                            newAlarmAmPm = "PM"
                            customAlarmType = "daily"
                            selectedCustomDays = setOf()
                            specificYear = 2026
                            specificMonth = 6
                            specificDay = 1
                            showNewAlarmDialog = true
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBn) "নতুন এলার্ম যোগ করুন" else "Add New Alarm",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        activeAlarms.forEach { alarm ->
                            val origTime = when (alarm.id) {
                                "fajr" -> selectedCity.fajrTime
                                "dhuhr" -> selectedCity.dhuhrTime
                                "asr" -> selectedCity.asrTime
                                "maghrib" -> selectedCity.maghribTime
                                "isha" -> selectedCity.ishaTime
                                else -> alarm.customTime ?: "12:00"
                            }

                            val alarmTimeLabel = getAlarmTimeString(origTime, alarm.offsetMinutes, currentLanguage)
                            val title = if (currentLanguage == AppLanguage.BN) alarm.nameBn else if (currentLanguage == AppLanguage.AR) alarm.nameAr else alarm.nameEn

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (alarm.isEnabled) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                                                     else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (alarm.isEnabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                           else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    // Top Header Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (alarm.isEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                            contentDescription = null,
                                            tint = if (alarm.isEnabled) MaterialTheme.colorScheme.primary else Color.Gray,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = title,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = if (isBn) "এলার্ম সময়: " else if (isAr) "وقت المنبه: " else "Time: ",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = alarmTimeLabel,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = if (alarm.isEnabled) MaterialTheme.colorScheme.primary else Color.Gray,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                                // Offset details markup
                                                val offsetLabel = when {
                                                    alarm.offsetMinutes == 0 -> if (isBn) "(ওয়াক্ত সুরূতেই)" else if (isAr) "(في الوقت)" else "(At time)"
                                                    alarm.offsetMinutes < 0 -> if (isBn) "(${viewModel.convertToBanglaDigits((-alarm.offsetMinutes).toString())} মি. পূর্বে)" else if (isAr) "(${viewModel.convertToArabicDigits((-alarm.offsetMinutes).toString())} دقيقة قبل)" else "(${(-alarm.offsetMinutes)}m before)"
                                                    else -> if (isBn) "(${viewModel.convertToBanglaDigits(alarm.offsetMinutes.toString())} মি. পরে)" else if (isAr) "(${viewModel.convertToArabicDigits(alarm.offsetMinutes.toString())} دقيقة بعد)" else "(${alarm.offsetMinutes}m after)"
                                                }
                                                Text(
                                                    text = offsetLabel,
                                                    fontSize = 10.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            
                                            if (alarm.customDays != null && alarm.customDays.isNotEmpty()) {
                                                val dayNames = if (isBn) {
                                                    listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্রবার", "শনিবার")
                                                } else {
                                                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                                                }
                                                val activeDayLabels = alarm.customDays.sorted().map { dayIndex ->
                                                    if (dayIndex in 1..7) dayNames[dayIndex - 1] else ""
                                                }.filter { it.isNotEmpty() }.joinToString(", ")
                                                
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.DateRange,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${if (isBn) "সাপ্তাহিক দিনসমূহ: " else "Days: "}$activeDayLabels",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            } else if (alarm.specificDate != null) {
                                                Spacer(modifier = Modifier.height(3.dp))
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.DateRange,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "${if (isBn) "নির্দিষ্ট তারিখ: " else "Date: "}${if (isBn) viewModel.convertToBanglaDigits(alarm.specificDate) else alarm.specificDate}",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                        }
                                        if (alarm.customTime != null) {
                                            IconButton(
                                                onClick = { viewModel.deleteAlarm(alarm.id) },
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete alarm",
                                                    tint = Color.Red.copy(alpha = 0.8f),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                        Switch(
                                            checked = alarm.isEnabled,
                                            onCheckedChange = { viewModel.toggleAlarmEnabled(alarm.id) }
                                        )
                                    }

                                    if (alarm.isEnabled) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Tuning controls row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 1. Recurrence Switch Button (Today only vs Daily)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                                    .clickable { viewModel.toggleAlarmDaily(alarm.id) }
                                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (alarm.isDaily) Icons.Default.Refresh else Icons.Default.DateRange,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (alarm.isDaily) {
                                                        if (isBn) "প্রতিদিন" else if (isAr) "يومياً" else "Daily"
                                                    } else {
                                                        if (isBn) "আজকের জন্য মাত্র" else if (isAr) "اليوم فقط" else "Today Only"
                                                    },
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            // 2. Sound Type Selector Button (Azan vs Beep)
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier
                                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                                    .clickable { viewModel.toggleAlarmAzan(alarm.id) }
                                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (alarm.isAzanAlarm) Icons.Default.MusicNote else Icons.Default.VolumeOff,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(14.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (alarm.isAzanAlarm) {
                                                        if (isBn) "আজান এলার্ম" else if (isAr) "منبه الأذان" else "Azan Bell"
                                                    } else {
                                                        if (isBn) "সাধারণ টোন" else if (isAr) "رنة عادية" else "Beep Alert"
                                                    },
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            // 3. Edit Time Offset Button
                                            OutlinedButton(
                                                onClick = {
                                                    activeAdjustAlarmId = alarm.id
                                                    activeAdjustValue = alarm.offsetMinutes
                                                },
                                                modifier = Modifier.height(28.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                                            ) {
                                                Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isBn) "সময় পরিবর্তন" else if (isAr) "تعديل الوقت" else "Shift Time",
                                                    fontSize = 10.sp
                                                )
                                            }

                                            // 4. Test Azan Sound Button
                                            if (alarm.isAzanAlarm) {
                                                FilledTonalButton(
                                                    onClick = {
                                                        playingAzanTheme = title
                                                        showAzanPlayDialog = true
                                                    },
                                                    modifier = Modifier.height(28.dp),
                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = ButtonDefaults.filledTonalButtonColors(
                                                        containerColor = MaterialTheme.colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                                                    Spacer(modifier = Modifier.width(3.dp))
                                                    Text(
                                                        text = if (isBn) "আজান টেস্ট" else if (isAr) "تجربة الأذان" else "Test Azan",
                                                        fontSize = 10.sp,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }
            }

                // Data Backup simulate options
                item {
                    Text(if (isBn) "মেমোরি ব্যাকআপ ও সিঙ্ক" else "Cloud Sync & Backup", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(if (isBn) "আপনার নামাজ ট্র্যাকার ও জিকির ডেটা ব্যাকআপ ও রিস্টোর করুন।" else "Manage SQLite database backup state securely.")
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                FilledTonalButton(
                                    onClick = { Toast.makeText(context, "ব্যাকআপ সফল হয়েছে!", Toast.LENGTH_SHORT).show() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBn) "ব্যাকআপ" else "Backup DB")
                                }
                                FilledTonalButton(
                                    onClick = { Toast.makeText(context, "রিস্টোর সম্পন্ন হয়েছে!", Toast.LENGTH_SHORT).show() },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBn) "রিস্টোর" else "Restore DB")
                                }
                            }
                        }
                    }
                }

                // About App & Developer Section Card
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBn) "অ্যাপ সম্পর্কে ও নির্মাতা" else "About App & Creator",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAboutAppDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Column {
                                    Text(
                                        text = if (isBn) "জান্নাত সন্ধান অ্যাপের বিস্তারিত" else "Jannat Sondhan App Details",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isBn) "সকল ইভেন্ট, ফিচার, সাহায্য ও নির্মাতা পরিচিতি" else "All events, features, help & builder details",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // App version specs
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 12.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = com.example.R.drawable.dome_mimbar_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .border(
                                        width = 1.5.dp,
                                        color = SoftGoldBorder,
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .padding(4.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (isBn) "জান্নাত সন্ধান" else "Jannat Sondhan",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (isBn) "ইসলামিক সুপার অ্যাপ — Ver. 1.0" else "Islamic Super App — Ver. 1.0",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Designed with Jetpack Compose & M3",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // About App Info Dialog
            if (showAboutAppDialog) {
                AlertDialog(
                    onDismissRequest = { showAboutAppDialog = false },
                    confirmButton = {
                        Button(
                            onClick = { showAboutAppDialog = false },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (isBn) "বন্ধ করুন" else "Close",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.96f),
                    shape = RoundedCornerShape(24.dp),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = if (isBn) "জান্নাত সন্ধান — বিস্তারিত ফিচার" else "Jannat Sondhan — Feature Details",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    },
                    text = {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .heightIn(max = 420.dp)
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Header banner
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = SoftGoldBorder.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = com.example.R.drawable.dome_mimbar_logo),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .border(1.dp, SoftGoldBorder, RoundedCornerShape(12.dp)),
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                    )
                                    Column {
                                        Text(
                                            text = if (isBn) "জান্নাত সন্ধান" else "Jannat Sondhan",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = if (isBn) "ইসলামিক সুপার অ্যাপ — সংস্করণ ১.০" else "Islamic Super App — Version 1.0",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }

                            Text(
                                text = if (isBn) "জান্নাত সন্ধান অ্যাপের সকল অনন্য ও প্রিমিয়াম ফিচারসমূহ:" else "Key Premium Features of Jannat Sondhan App:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            // List of features
                            val featuresList = listOf(
                                Triple(
                                    Icons.Default.NotificationsActive,
                                    if (isBn) "১. নামাজের সময় ও আজান এলার্ম" else "1. Prayer Timer & Azan Alarm",
                                    if (isBn) 
                                        "অ্যাপে প্রতিটি নামাজের ওয়াক্তের সময় সুনির্দিষ্টভাবে গণনা করার পাশাপাশি দৈনিক আজান টেস্ট ও ৫ ওয়াক্তের জন্য কাস্টম অফসেট (সময় তফাত) মিলিয়ে সম্পূর্ণ স্বয়ংক্রিয় নোটিফিকেশন ও আজান এলার্ম সেট করা যায়।"
                                    else 
                                        "Accurate local calculation of Islamic prayer schedules with configurable ringing bells, test simulation playbacks, and manual offset minute tuning filters."
                                ),
                                Triple(
                                    Icons.Default.Language,
                                    if (isBn) "২. জান্নাত সন্ধান দ্বীনি এআই চ্যাট" else "2. Jannat Sondhan Islamic AI Chat",
                                    if (isBn)
                                        "নির্ভরযোগ্য কৃত্রিম বুদ্ধিমত্তা সমৃদ্ধ এআই চ্যাট সিস্টেম, যেখানে কোরআন, হাদিস এবং ইসলামি জীবনবিধানের আলোকে যে কোনো দ্বীনি জিজ্ঞাসার নিখুঁত উত্তর পাওয়া যায়।"
                                    else
                                        "Reliable AI chatbot leveraging neural services to resolve islamic queries, cite texts, or address daily queries safely within native bounds."
                                ),
                                Triple(
                                    Icons.Default.Tv,
                                    if (isBn) "৩. লাইভ সম্প্রচার ও টিভি" else "3. Live Islamic Broadcasting",
                                    if (isBn)
                                        "জান্নাত সন্ধান টিভি সরাসরি প্লেব্যাক সহ চমৎকার ও আধুনিক এইচডি মানের লাইভ ভিডিও সম্প্রচার চ্যানেল দেখার দারুন সুযোগ।"
                                    else
                                        "Dedicated online high-fidelity feed streaming directly for religious and educational transmissions safely within active frames."
                                ),
                                Triple(
                                    Icons.Default.Book,
                                    if (isBn) "৪. আল-কোরআন কারীম" else "4. Al-Quran Kareem with Tajweed",
                                    if (isBn)
                                        "সম্পূর্ণ পবিত্র কোরআন সাবলীল বাংলা অনুবাদ, নির্ভুল আরবি উচ্চারণ ও সহজ সার্চ সুবিধা সহ পড়ার আধুনিক রিডার মডিউল।"
                                    else
                                        "Full interactive Holy Quran text displaying translation, clear phonetics, reading trackers, and interactive surah query mechanisms."
                                ),
                                Triple(
                                    Icons.Default.CheckCircle,
                                    if (isBn) "৫. হিসনুল মুসলিম ও দোয়াসমূহ" else "5. Hisnul Muslim & Supplications",
                                    if (isBn)
                                        "জরুরি দোয়া, প্রতিদিনের সুন্দর অডিও রিডিং সহ (TTS Engine) বাংলা এবং ইংরেজি উচ্চারণ ও অর্থের এক সমৃদ্ধ ভাণ্ডার। সাথে রয়েছে কাস্টম কাউন্টার!"
                                    else
                                        "Beautiful and interactive Supplications database containing everyday dua cards with real-time text-to-speech pronunciations."
                                ),
                                Triple(
                                    Icons.Default.Refresh,
                                    if (isBn) "৬. তাসবীহ ও জিকির ট্র্যাকার" else "6. Digital Tasbih Track Tool",
                                    if (isBn)
                                        "প্রতিদিনের জিকির ও ইবাদত গণনা করার জন্য চমৎকার কম্পোজ রিপল ইফেক্ট এবং কাস্টম রিসেট বাটন যুক্ত ডিজিটাল তাসবীহ কাউন্টার।"
                                    else
                                        "Modern touch counter featuring feedback indicators to track daily dhikrs and help keep daily digital records."
                                ),
                                Triple(
                                    Icons.Default.Navigation,
                                    if (isBn) "৭. কিবলা কম্পাস" else "7. Precise Qibla Compass Direction",
                                    if (isBn)
                                        "যেকোনো স্থান থেকে নির্ভুলভাবে মক্কাতুল মোকাররমার কিবলা দিক খুঁজে পাওয়ার জন্য অত্যন্ত আধুনিক ও সুচারু কিবলা কম্পাস কম্পোনেন্ট।"
                                    else
                                        "Accurate real-time compass utilizing location services to locate Islamic Kaaba direction securely worldwide."
                                ),
                                Triple(
                                    Icons.Default.Calculate,
                                    if (isBn) "৮. যাকাত হিসাব ক্যালকুলেটর" else "8. Zakat Ledger & Calculator",
                                    if (isBn)
                                        "স্বর্ণ, রৌপ্য ও নগদের যাকাত নির্ধারণের হিসাব অত্যন্ত সাশ্রয়ী ও সরল কাঠামোয় করতে সাহায্য করে এই যাকাত ইনপুট গেজেট।"
                                    else
                                        "Seamlessly input gold weights, savings and currency valuation to compute standard obligatory charity."
                                ),
                                Triple(
                                    Icons.Default.Star,
                                    if (isBn) "৯. আসমা-উল-হুসনা" else "9. Beautiful 99 Names of Allah",
                                    if (isBn)
                                        "মহান আল্লাহর পবিত্র ৯৯টি গুণবাচক নামের সুন্দর উপস্থাপন, বিশুদ্ধ আরবির পাশাপাশি বাংলা ও ইংরেজি অর্থ সহ।"
                                    else
                                        "Detailed descriptions showcasing Allah's noble names on stylish cards with easy translation translations."
                                ),
                                Triple(
                                    Icons.Default.List,
                                    if (isBn) "১০. দৈনিক ইবাদত ও আমল ট্র্যাকার" else "10. Daily Good Deeds & Amal Tracker",
                                    if (isBn)
                                        "ফরজ ও নফল ইবাদতের সঠিক রেকর্ড রাখতে দৈনিক আমল লিস্ট ট্র্যাকার, যা আপনার অগ্রগতি ট্র্যাক করতে সহায়তা করে।"
                                    else
                                        "Deeds checker helping users mark off five prayers and and routine religious activities to build habits."
                                )
                            )

                            // Render features
                            featuresList.forEach { (icon, title, desc) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                        .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(6.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }

                            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), modifier = Modifier.padding(vertical = 4.dp))

                            // Developer Info Card inside dialog
                            Text(
                                text = if (isBn) "ডেভেলপার পরিচিতি ও যোগাযোগ:" else "Developer Identity & Contact:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                                border = BorderStroke(1.2.dp, SoftGoldBorder),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(42.dp)
                                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = Color.White
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = if (isBn) "মোঃ রেদওয়ান শিহাব" else "Md Redwan Shihab",
                                                fontWeight = FontWeight.ExtraBold,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = if (isBn) "প্রধান প্রতিষ্ঠাতা ও নির্বাহী নির্মাতা" else "Founder & Lead Developer",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = if (isBn) "মোবাইল: ০১৯৩৪৯৬৪৭৬৩" else "Mobile: 01934964763",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = if (isBn) 
                                            "আপনার যেকোনো প্রশ্ন, পরামর্শ বা সাহায্য পেতে সরাসরি ডেভেলপারের সাথে যোগাযোগ করতে নিচের বোতামগুলোতে ট্যাপ করুন।" 
                                            else "For any queries, customized feature additions or personal assistance, click below to interact directly.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                                        lineHeight = 16.sp
                                    )

                                    // Action buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        // 1. Call Intent
                                        Button(
                                            onClick = {
                                                try {
                                                    val intent = android.content.Intent(
                                                        android.content.Intent.ACTION_DIAL,
                                                        android.net.Uri.parse("tel:01934964763")
                                                    )
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "ফোন ট্রাই করা যায়নি", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(if (isBn) "কল দিন" else "Call", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }

                                        // 2. Clipboard copy
                                        Button(
                                            onClick = {
                                                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                val clip = android.content.ClipData.newPlainText("phone_number", "01934964763")
                                                clipboard.setPrimaryClip(clip)
                                                Toast.makeText(context, if (isBn) "নম্বরটি কপি করা হয়েছে!" else "Number Copied!", Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(11.dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(if (isBn) "কপি করুন" else "Copy", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                        }

                                        // 3. WhatsApp Redirect API
                                        Button(
                                            onClick = {
                                                try {
                                                    val url = "https://api.whatsapp.com/send?phone=+8801934964763&text=আসসালামু আলাইকুম রেদওয়ান ভাই, 'জান্নাত সন্ধান' অ্যাপ সম্পর্কিত..."
                                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "হোয়াটসঅ্যাপ পাওয়া যায়নি", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldMedium),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                            modifier = Modifier.weight(1.1f)
                                        ) {
                                            Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(11.dp), tint = Color.White)
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(if (isBn) "হোয়াটসঅ্যাপ" else "WhatsApp", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }

            // Adjust Offset Time Dialog
            if (activeAdjustAlarmId != null) {
                val activeId = activeAdjustAlarmId!!
                AlertDialog(
                    onDismissRequest = { activeAdjustAlarmId = null },
                    title = {
                        Text(
                            text = if (isBn) "এলার্ম সময় তফাত পরিবর্তন" else if (isAr) "تعديل وقت المنبه" else "Shift Alarm Time Offset",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBn) "ওয়াক্ত শুরুর আসল সময়ের চেয়ে কত মিনিট আগে বা পরে এলার্ম বাজবে তা সেট করুন।" 
                                       else if (isAr) "اختر مقدار الدقائق بالتقديم أو التأخير عن وقت دخول الأذان الفعلي صعوداً ونزولاً."
                                       else "Set the offset adjustments (in minutes) relative to the actual start of the prayer time.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (activeAdjustValue > -60) activeAdjustValue -= 5 },
                                    modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                                ) {
                                    Text("-৫", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }

                                IconButton(
                                    onClick = { if (activeAdjustValue > -60) activeAdjustValue -= 1 },
                                    modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                ) {
                                    Text("-১", fontWeight = FontWeight.Bold)
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.width(100.dp)
                                ) {
                                    val dispVal = if (isBn) viewModel.convertToBanglaDigits(activeAdjustValue.toString()) else if (isAr) viewModel.convertToArabicDigits(activeAdjustValue.toString()) else activeAdjustValue.toString()
                                    val direction = when {
                                        activeAdjustValue == 0 -> if (isBn) "ঠিক সময়ে" else if (isAr) "في الوقت" else "At prayer"
                                        activeAdjustValue < 0 -> if (isBn) "মিনিট আগে" else if (isAr) "دقيقة قبل" else "mins before"
                                        else -> if (isBn) "মিনিট পরে" else if (isAr) "دقيقة بعد" else "mins after"
                                    }

                                    Text(
                                        text = if (activeAdjustValue == 0) "" else dispVal,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = direction,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = { if (activeAdjustValue < 60) activeAdjustValue += 1 },
                                    modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                ) {
                                    Text("+১", fontWeight = FontWeight.Bold)
                                }

                                IconButton(
                                    onClick = { if (activeAdjustValue < 60) activeAdjustValue += 5 },
                                    modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
                                ) {
                                    Text("+৫", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                }
                            }

                            // Slider selector
                            Slider(
                                value = activeAdjustValue.toFloat(),
                                onValueChange = { activeAdjustValue = it.toInt() },
                                valueRange = -60f..60f,
                                steps = 24
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.updateAlarmOffset(activeId, activeAdjustValue)
                                activeAdjustAlarmId = null
                            }
                        ) {
                            Text(text = if (isBn) "সংরক্ষণ করুন" else if (isAr) "حفظ التغييرات" else "Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { activeAdjustAlarmId = null }) {
                            Text(text = if (isBn) "বাতিল" else if (isAr) "إلغاء" else "Cancel")
                        }
                    }
                )
            }

            // Real Sounding Azan Testing Modal Dialog
            if (showAzanPlayDialog) {
                var isPlaying by remember { mutableStateOf(true) }
                var progressFraction by remember { mutableStateOf(0.4f) }

                LaunchedEffect(isPlaying) {
                    if (isPlaying) {
                        while (true) {
                            kotlinx.coroutines.delay(100)
                            progressFraction = progressFraction + 0.015f
                            if (progressFraction > 1.0f) progressFraction = 0.0f
                        }
                    }
                }

                AlertDialog(
                    onDismissRequest = { showAzanPlayDialog = false },
                    title = {
                        Text(
                            text = if (isBn) "আজান টেস্ট প্লেয়ার" else if (isAr) "اختبار صوت الأذان" else "Azan Audio Playback Test",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Visual representation: animated Kaaba illustration
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), RoundedCornerShape(50.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Animated concentric audio rings using scale
                                val infiniteTransition = rememberInfiniteTransition()
                                val waveRadius by infiniteTransition.animateFloat(
                                    initialValue = 40.dp.value,
                                    targetValue = 90.dp.value,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1250, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    )
                                )
                                val waveAlpha by infiniteTransition.animateFloat(
                                    initialValue = 0.6f,
                                    targetValue = 0.0f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1250, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    )
                                )
                                if (isPlaying) {
                                    Box(
                                        modifier = Modifier
                                            .size(waveRadius.dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = waveAlpha), RoundedCornerShape(waveRadius.dp))
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (isBn) "মুয়াযযিনের কণ্ঠে আজান বাজছে..." else if (isAr) "صوت الأذان يرتفع الآن بصوت جميل..." else "Muezzin's beautiful azan voice is playing...",
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = if (isBn) "ওয়াক্ত: $playingAzanTheme" else if (isAr) "الصلاة: $playingAzanTheme" else "Waqt: $playingAzanTheme",
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }

                            // Linear progress bar simulating audio progress
                            LinearProgressIndicator(
                                progress = progressFraction,
                                modifier = Modifier.fillMaxWidth().height(5.dp).background(Color.LightGray, RoundedCornerShape(2.dp)),
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Play / Stop Controls
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { isPlaying = !isPlaying },
                                    modifier = Modifier.size(50.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(25.dp))
                                ) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAzanPlayDialog = false }) {
                            Text(if (isBn) "বন্ধ করুন" else if (isAr) "إيقاف التشغيل" else "Stop Test")
                        }
                    }
                )
            }

            if (showNewAlarmDialog) {
                AlertDialog(
                    onDismissRequest = { showNewAlarmDialog = false },
                    title = {
                        Text(
                            text = if (isBn) "নতুন এলার্ম যোগ করুন" else "Create Custom Alarm",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    text = {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isBn) "অনুকূল সময়ে নিয়মিত বা নির্দিষ্ট দিনে এবং তারিখ অনুযায়ী বাজানোর জন্য এলার্ম সেট করুন।" 
                                       else "Add custom time alarm label and configure schedule type (daily, repeating days, or specific target date).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // 1. Alarm Name in Bengali
                            OutlinedTextField(
                                value = newAlarmLabelBn,
                                onValueChange = { newAlarmLabelBn = it },
                                label = { Text(if (isBn) "এলার্মের নাম (বাংলা)" else "Alarm Label (Bangla)") },
                                placeholder = { Text(if (isBn) "উদা: তাহাজ্জুদ" else "e.g., Tahajjud") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            // 2. Alarm Name in English 
                            OutlinedTextField(
                                value = newAlarmLabelEn,
                                onValueChange = { newAlarmLabelEn = it },
                                label = { Text(if (isBn) "এলার্মের নাম (ইংলিশ)" else "Alarm Label (English)") },
                                placeholder = { Text("e.g. Tahajjud") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            // 2.5 Selection of Alarm Recurrence Type 
                            Text(
                                text = if (isBn) "এলার্মের ধরন নির্ধারণ করুন: " else "Schedule Type:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                val types = listOf(
                                    Triple("daily", if (isBn) "প্রতিদিন" else "Daily", Icons.Default.Refresh),
                                    Triple("weekdays", if (isBn) "বারের নাম" else "Days of Week", Icons.Default.DateRange),
                                    Triple("date", if (isBn) "তারিখভিত্তিক" else "Specific Date", Icons.Default.Event)
                                )
                                types.forEach { (typeKey, typeLabel, typeIcon) ->
                                    val isSelected = customAlarmType == typeKey
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else Color.Transparent
                                            )
                                            .clickable { customAlarmType = typeKey }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = typeIcon,
                                                contentDescription = null,
                                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = typeLabel,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // 2.6 Conditional Schedule Options rendering
                            if (customAlarmType == "weekdays") {
                                // Day of week checkboxes
                                Text(
                                    text = if (isBn) "দিনের নামসমূহ নির্বাচন করুন (এক বা একাধিক):" else "Select Target Days (One or more):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Let's create localized weekday circles starting with Saturday
                                    // Map index as in Java list or standard Calendar 1=Sun, 2=Mon... 7=Sat
                                    val weekdaysList = listOf(
                                        Pair(7, if (isBn) "শ" else "Sa"),
                                        Pair(1, if (isBn) "র" else "Su"),
                                        Pair(2, if (isBn) "সো" else "Mo"),
                                        Pair(3, if (isBn) "ম" else "Tu"),
                                        Pair(4, if (isBn) "বু" else "We"),
                                        Pair(5, if (isBn) "বৃ" else "Th"),
                                        Pair(6, if (isBn) "শু" else "Fr")
                                    )
                                    weekdaysList.forEach { (dayIdx, label) ->
                                        val dayActive = selectedCustomDays.contains(dayIdx)
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (dayActive) MaterialTheme.colorScheme.primary
                                                    else MaterialTheme.colorScheme.surfaceVariant
                                                )
                                                .clickable {
                                                    selectedCustomDays = if (dayActive) {
                                                        selectedCustomDays - dayIdx
                                                    } else {
                                                        selectedCustomDays + dayIdx
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontWeight = FontWeight.Bold,
                                                color = if (dayActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            } else if (customAlarmType == "date") {
                                // Specific date selector with custom +/- spinner adjusters
                                Text(
                                    text = if (isBn) "তারিখ সিলেক্ট করুন (দিন / মাস / বছর):" else "Pick Date (Day / Month / Year):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Day Spinner
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(if (isBn) "দিন" else "Day", fontSize = 10.sp, color = Color.Gray)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificDay > 1) specificDay-- else specificDay = 31 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                            Text(
                                                text = if (isBn) viewModel.convertToBanglaDigits(specificDay.toString()) else specificDay.toString(),
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificDay < 31) specificDay++ else specificDay = 1 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }

                                    // Month Spinner
                                    val monthNamesBn = listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
                                    val monthNamesEn = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(if (isBn) "মাস" else "Month", fontSize = 10.sp, color = Color.Gray)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificMonth > 1) specificMonth-- else specificMonth = 12 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                            Text(
                                                text = if (isBn) monthNamesBn[specificMonth - 1] else monthNamesEn[specificMonth - 1],
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(horizontal = 4.dp).widthIn(min = 45.dp),
                                                textAlign = TextAlign.Center
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificMonth < 12) specificMonth++ else specificMonth = 1 },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }

                                    // Year Spinner
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(if (isBn) "বছর" else "Year", fontSize = 10.sp, color = Color.Gray)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificYear > 2026) specificYear-- },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                            Text(
                                                text = if (isBn) viewModel.convertToBanglaDigits(specificYear.toString()) else specificYear.toString(),
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(horizontal = 6.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f), CircleShape)
                                                    .clickable { if (specificYear < 2035) specificYear++ },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }
                                }
                            }

                            // 3. Time Picker layout (Hour & Minute Selector)
                            Text(
                                text = if (isBn) "সময় নির্ধারণ করুন: " else "Set Alarm Time:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Hour Spinner Column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(if (isBn) "ঘণ্টা" else "Hour", fontSize = 11.sp, color = Color.Gray)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), CircleShape)
                                                .clickable { if (newAlarmHour > 1) newAlarmHour-- else newAlarmHour = 12 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                                        }
                                        Text(
                                            text = if (isBn) viewModel.convertToBanglaDigits(newAlarmHour.toString()) else newAlarmHour.toString(),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), CircleShape)
                                                .clickable { if (newAlarmHour < 12) newAlarmHour++ else newAlarmHour = 1 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                                        }
                                    }
                                }

                                // Colon separator
                                Text(":", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)

                                // Minute Spinner Column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(if (isBn) "মিনিট" else "Minute", fontSize = 11.sp, color = Color.Gray)
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), CircleShape)
                                                .clickable { newAlarmMinute = (newAlarmMinute - 1 + 60) % 60 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                                        }
                                        Text(
                                            text = if (isBn) viewModel.convertToBanglaDigits(String.format("%02d", newAlarmMinute)) else String.format("%02d", newAlarmMinute),
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f), CircleShape)
                                                .clickable { newAlarmMinute = (newAlarmMinute + 1) % 60 },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("+", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                                        }
                                    }
                                }

                                // AM / PM Picker Column
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(if (isBn) "বেলা" else "Period", fontSize = 11.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                            .padding(2.dp)
                                    ) {
                                        arrayOf("AM", "PM").forEach { item ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(
                                                        if (newAlarmAmPm == item) MaterialTheme.colorScheme.primary
                                                        else Color.Transparent
                                                    )
                                                    .clickable { newAlarmAmPm = item }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = item,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (newAlarmAmPm == item) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val hr24 = when {
                                    newAlarmAmPm == "AM" -> if (newAlarmHour == 12) 0 else newAlarmHour
                                    else -> if (newAlarmHour == 12) 12 else newAlarmHour + 12
                                }
                                val formattedTime = String.format("%02d:%02d", hr24, newAlarmMinute)
                                val finalBnName = if (newAlarmLabelBn.isNotBlank()) newAlarmLabelBn else "কাস্টম এলার্ম"
                                val finalEnName = if (newAlarmLabelEn.isNotBlank()) newAlarmLabelEn else "Custom Alarm"
                                
                                val customDaysList = if (customAlarmType == "weekdays") selectedCustomDays.toList() else null
                                val specificDateStr = if (customAlarmType == "date") String.format("%04d-%02d-%02d", specificYear, specificMonth, specificDay) else null
                                
                                viewModel.addNewAlarm(finalBnName, finalEnName, formattedTime, customDaysList, specificDateStr)
                                showNewAlarmDialog = false
                                Toast.makeText(context, if (isBn) "নতুন এলার্ম যোগ করা হয়েছে!" else "New custom alarm set successfully!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Text(text = if (isBn) "যোগ করুন" else "Add Alarm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showNewAlarmDialog = false }) {
                            Text(text = if (isBn) "বাতিল" else "Cancel")
                        }
                    }
                )
            }
        } else {
            // Admin Notice board hub panel
            val notices by viewModel.adminNotices.collectAsState()
            var noticeTitle by remember { mutableStateOf("") }
            var noticeBody by remember { mutableStateOf("") }
            var noticeCat by remember { mutableStateOf("SYSTEM") }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Text(
                        text = if (isBn) "একটি নতুন নোটিশ জারি করুন" else "Broadcast New Notice",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    OutlinedTextField(
                        value = noticeTitle,
                        onValueChange = { noticeTitle = it },
                        label = { Text(if (isBn) "নোটিশের শিরোনাম" else "Notice Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = noticeBody,
                        onValueChange = { noticeBody = it },
                        label = { Text(if (isBn) "নোটিশের বিস্তারিত বার্তা" else "Notice Body message") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (noticeTitle.trim().isNotEmpty() && noticeBody.trim().isNotEmpty()) {
                                viewModel.publishNotice(noticeTitle, noticeBody, noticeCat)
                                noticeTitle = ""
                                noticeBody = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBn) "নোটিশ পাঠান" else "Publish Notice")
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = if (isBn) "এক্টিভ নোটিশ সমূহ" else "Active Notice Board",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Render lists of notice items
                items(notices) { notice ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "[ ${notice.category} ]",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )

                                IconButton(onClick = { viewModel.deleteNotice(notice.id) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = AccentRed, modifier = Modifier.size(18.dp))
                                }
                            }

                            Text(
                                text = notice.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = notice.dateStr,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = notice.message,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// IN-HOME FEATURE: PRAYER EDUCATION (NAMAJ SHIKKHA) SCREEN
// ===================================================================================
@Composable
fun IslamicLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(Color(0xFFE8F5E9), CircleShape) // soft mint green background
            .border(1.5.dp, Color(0xFF2E7D32), CircleShape) // Emerald border
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        // Precise Mosque dome silhouette with crescent
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val goldColor = Color(0xFFD4AF37)
            val deepGreen = Color(0xFF1B5E20)

            // Draw dome base
            val domePath = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.15f, h * 0.85f)
                lineTo(w * 0.85f, h * 0.85f)
                lineTo(w * 0.85f, h * 0.60f)
                cubicTo(w * 0.85f, h * 0.35f, w * 0.68f, h * 0.18f, w * 0.50f, h * 0.12f)
                cubicTo(w * 0.32f, h * 0.18f, w * 0.15f, h * 0.35f, w * 0.15f, h * 0.60f)
                close()
            }
            drawPath(path = domePath, color = deepGreen)

            // Draw gold crescent moon
            val crescentPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.50f, h * 0.08f)
                quadraticTo(w * 0.36f, h * 0.18f, w * 0.40f, h * 0.34f)
                quadraticTo(w * 0.56f, h * 0.18f, w * 0.50f, h * 0.08f)
            }
            drawPath(path = crescentPath, color = goldColor)

            // Dome pinnacle star dot
            drawCircle(color = goldColor, radius = 2.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.50f, h * 0.11f))
        }
    }
}

@Composable
fun WuduLearningTab(isBn: Boolean) {
    var expandedStepId by remember { mutableStateOf<Int?>(1) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SoftMintBg),
                border = BorderStroke(1.dp, EmeraldMedium.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = EmeraldMedium,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isBn) {
                            "রাসূলুল্লাহ (সা.) বলেছেন: 'পবিত্রতা হচ্ছে ঈমানের অর্ধেক।' সহীহ নামাজ আদায়ের জন্য সঠিক নিয়মে ওযু করা আবশ্যক।"
                        } else {
                            "The Messenger of Allah (PBUH) said: 'Purity is half of faith.' Proper Wudu is essential for prayer validity."
                        },
                        fontSize = 11.sp,
                        color = EmeraldDeep,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        items(PrayerEducationData.wuduSteps) { step ->
            val isExpanded = expandedStepId == step.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedStepId = if (isExpanded) null else step.id },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = step.id.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isExpanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isBn) step.nameBn else step.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand info",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(SoftMintBg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Opacity,
                                    contentDescription = null,
                                    tint = Color(0xFF3498DB),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBn) "প্রয়োজনীয় নিয়ম" else "Obligatory Action",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (isBn) "ধাপটি অত্যন্ত মনোযোগ দিয়ে ৩ বার সম্পন্ন করুন।" else "Perform this step with mindfulness exactly 3 times.",
                                    fontSize = 9.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) step.descriptionBn else step.descriptionEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CorrectVsWrongPrayerTab(isBn: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.errorContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = if (isBn) {
                            "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা সেভাবে নামাজ আদায় করো, যেভাবে আমাকে নামাজ আদায় করতে দেখছ।' রুকু, সাজদাহ ও বৈঠকের ক্ষেত্রে আমাদের নামাজের ভুলগুলো সংশোধন করা জরুরি।"
                        } else {
                            "The Prophet (PBUH) said: 'Pray as you have seen me praying.' It is vital to rectify our standing, bowing, and prostration mistakes."
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        items(PrayerEducationData.postureComparisons) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isBn) item.postureNameBn else item.postureNameEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val redClr = Color(0xFFE74C3C)
                            val greenClr = Color(0xFF2ECC71)
                            
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.3f),
                                start = androidx.compose.ui.geometry.Offset(w / 2f, 10f),
                                end = androidx.compose.ui.geometry.Offset(w / 2f, h - 10f),
                                strokeWidth = 2f,
                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            
                            when (item.diagramType) {
                                "qiyam" -> {
                                    // Sloppy crooked head (Wrong)
                                    drawCircle(color = redClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.25f + 12f, h * 0.23f))
                                    // Slanted spine
                                    drawLine(
                                        color = redClr,
                                        start = androidx.compose.ui.geometry.Offset(w * 0.25f + 12f, h * 0.29f),
                                        end = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.62f),
                                        strokeWidth = 3.dp.toPx(),
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    // Scattered legs
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.62f), end = androidx.compose.ui.geometry.Offset(w * 0.16f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.62f), end = androidx.compose.ui.geometry.Offset(w * 0.30f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Dangling/Scratching arms
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.24f, h * 0.35f), end = androidx.compose.ui.geometry.Offset(w * 0.14f, h * 0.44f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.14f, h * 0.44f), end = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.58f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)

                                    // Erect head (Correct)
                                    drawCircle(color = greenClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.22f))
                                    // Flat straight spine
                                    drawLine(
                                        color = greenClr,
                                        start = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.28f),
                                        end = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.65f),
                                        strokeWidth = 3.5.dp.toPx(),
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    // Parallel legs
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.73f, h * 0.65f), end = androidx.compose.ui.geometry.Offset(w * 0.73f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.77f, h * 0.65f), end = androidx.compose.ui.geometry.Offset(w * 0.77f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Elegantly folded arms over chest
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.32f), end = androidx.compose.ui.geometry.Offset(w * 0.71f, h * 0.42f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.71f, h * 0.42f), end = androidx.compose.ui.geometry.Offset(w * 0.79f, h * 0.42f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.32f), end = androidx.compose.ui.geometry.Offset(w * 0.79f, h * 0.42f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                }
                                "ruku" -> {
                                    // Incorrect curved hunch (Wrong)
                                    val rukuWrongBack = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(w * 0.34f, h * 0.60f)
                                        quadraticTo(w * 0.24f, h * 0.32f, w * 0.16f, h * 0.46f)
                                    }
                                    drawPath(path = rukuWrongBack, color = redClr, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                                    
                                    // Hanging head
                                    drawCircle(color = redClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.11f, h * 0.50f))
                                    
                                    // Slanted bent legs
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.34f, h * 0.60f), end = androidx.compose.ui.geometry.Offset(w * 0.37f, h * 0.76f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.37f, h * 0.76f), end = androidx.compose.ui.geometry.Offset(w * 0.33f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    
                                    // Dangling/loose arm
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.46f), end = androidx.compose.ui.geometry.Offset(w * 0.25f, h * 0.75f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    
                                    // Hips to shoulder (completely horizontal flat back) (Correct)
                                    drawLine(
                                        color = greenClr,
                                        start = androidx.compose.ui.geometry.Offset(w * 0.84f, h * 0.50f),
                                        end = androidx.compose.ui.geometry.Offset(w * 0.66f, h * 0.50f),
                                        strokeWidth = 3.5.dp.toPx(),
                                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                                    )
                                    
                                    // Head aligned symmetrically
                                    drawCircle(color = greenClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.60f, h * 0.50f))
                                    
                                    // Vertically straight parallel legs
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.83f, h * 0.50f), end = androidx.compose.ui.geometry.Offset(w * 0.83f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.85f, h * 0.50f), end = androidx.compose.ui.geometry.Offset(w * 0.85f, h * 0.95f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    
                                    // Straight arms locking onto the knees
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.68f, h * 0.50f), end = androidx.compose.ui.geometry.Offset(w * 0.83f, h * 0.72f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                }
                                "sajdah" -> {
                                    // Feet & Knees (Wrong)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.36f, h * 0.92f), end = androidx.compose.ui.geometry.Offset(w * 0.24f, h * 0.92f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Low Hips
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.24f, h * 0.92f), end = androidx.compose.ui.geometry.Offset(w * 0.28f, h * 0.76f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Flat spine
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.28f, h * 0.76f), end = androidx.compose.ui.geometry.Offset(w * 0.16f, h * 0.85f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Hovering head (mistake of not touching)
                                    drawCircle(color = redClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.81f))
                                    // Flat forearm on ground
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.16f, h * 0.85f), end = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.92f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.92f), end = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.92f), strokeWidth = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    
                                    // Ground line (Correct)
                                    drawLine(color = Color.LightGray.copy(alpha = 0.4f), start = androidx.compose.ui.geometry.Offset(w * 0.55f, h * 0.92f), end = androidx.compose.ui.geometry.Offset(w * 0.95f, h * 0.92f), strokeWidth = 1.dp.toPx())
                                    // Upright feet
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.88f, h * 0.80f), end = androidx.compose.ui.geometry.Offset(w * 0.88f, h * 0.92f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Thigh to highly raised hips
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.92f), end = androidx.compose.ui.geometry.Offset(w * 0.83f, h * 0.65f), strokeWidth = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Slanted straight spine down to shoulders
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.83f, h * 0.65f), end = androidx.compose.ui.geometry.Offset(w * 0.68f, h * 0.78f), strokeWidth = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Head firmly on the ground
                                    drawCircle(color = greenClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.62f, h * 0.86f))
                                    // Elevated arching arms (elbows way off floor!)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.68f, h * 0.78f), end = androidx.compose.ui.geometry.Offset(w * 0.73f, h * 0.72f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.73f, h * 0.72f), end = androidx.compose.ui.geometry.Offset(w * 0.67f, h * 0.92f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                }
                                else -> {
                                    // Slonched spine (Wrong)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.26f, h * 0.86f), end = androidx.compose.ui.geometry.Offset(w * 0.20f, h * 0.58f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Slanted head
                                    drawCircle(color = redClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.17f, h * 0.51f))
                                    // Heel-squat legs
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.26f, h * 0.86f), end = androidx.compose.ui.geometry.Offset(w * 0.14f, h * 0.86f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    drawLine(color = redClr, start = androidx.compose.ui.geometry.Offset(w * 0.14f, h * 0.86f), end = androidx.compose.ui.geometry.Offset(w * 0.22f, h * 0.94f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    
                                    // Perfectly upright straight spine (Correct)
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.86f), end = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.54f), strokeWidth = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Erect head
                                    drawCircle(color = greenClr, radius = 6.dp.toPx(), center = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.44f))
                                    // Legs folded flat on ground
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.78f, h * 0.86f), end = androidx.compose.ui.geometry.Offset(w * 0.66f, h * 0.86f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                    // Hands resting nicely on knee
                                    drawLine(color = greenClr, start = androidx.compose.ui.geometry.Offset(w * 0.76f, h * 0.58f), end = androidx.compose.ui.geometry.Offset(w * 0.67f, h * 0.84f), strokeWidth = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF2F2)),
                            border = BorderStroke(1.dp, Color(0xFFF8D7DA))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isBn) item.wrongTitleBn else item.wrongTitleEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC0392B)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                (if (isBn) item.wrongDetailsBn else item.wrongDetailsEn).forEach { detail ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("• ", color = Color(0xFFC0392B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = detail,
                                            fontSize = 9.5.sp,
                                            color = Color(0xFF7F2C22),
                                            lineHeight = 13.sp
                                        )
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F5)),
                            border = BorderStroke(1.dp, Color(0xFFD1F2EB))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isBn) item.correctTitleBn else item.correctTitleEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF16A085)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                (if (isBn) item.correctDetailsBn else item.correctDetailsEn).forEach { detail ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text("• ", color = Color(0xFF16A085), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = detail,
                                            fontSize = 9.5.sp,
                                            color = Color(0xFF0E6251),
                                            lineHeight = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerEducationScreen(viewModel: IbadahViewModel, isBn: Boolean, onBack: () -> Unit) {
    var activeTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isBn) "নামাজ শিক্ষা ও নিয়মাবলী" else "Prayer Education Guide",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBn) "পূর্ণাঙ্গ নামাজ গাইড, সূরা ও জরুরি মাসয়ালা" else "Comprehensive step guide, Suras, and laws of Salat",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IslamicLogo()
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(
                if (isBn) "সহীহ ওযু" else "Ablution (Wudu)",
                if (isBn) "নামাজ শিক্ষা" else "Prayer Steps",
                if (isBn) "সঠিক বনাম ভুল" else "Right vs Wrong",
                if (isBn) "প্রয়োজনীয় সূরা" else "Salat Suras",
                if (isBn) "রাকাত ও মাসয়ালা" else "Rules & Rakats"
            ).forEachIndexed { idx, label ->
                val isSelected = activeTab == idx
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { activeTab = idx }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (activeTab) {
                0 -> WuduLearningTab(isBn)
                1 -> PrayerStepsTab(isBn)
                2 -> CorrectVsWrongPrayerTab(isBn)
                3 -> PrayerSurasTab(isBn, context)
                4 -> PrayerRulesTab(isBn)
            }
        }
    }
}

@Composable
fun PrayerStepsTab(isBn: Boolean) {
    var expandedStepId by remember { mutableStateOf<Int?>(1) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(PrayerEducationData.steps) { step ->
            val isExpanded = expandedStepId == step.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        expandedStepId = if (isExpanded) null else step.id 
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = step.id.toString(),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isExpanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (isBn) step.nameBn else step.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand info",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = if (isBn) step.descriptionBn else step.descriptionEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        if (step.arabicText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = step.arabicText,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        textAlign = TextAlign.Right,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                        color = EmeraldDeep
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = if (isBn) "উচ্চারণ: ${step.transliterationBn}" else "Pronunciation: ${step.transliterationEn}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isBn) "অর্থ: ${step.translationBn}" else "Meaning: ${step.translationEn}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        if (step.postureTipBn.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(IslamicGold.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) step.postureTipBn else step.postureTipEn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerSurasTab(isBn: Boolean, context: Context) {
    var expandedSuraId by remember { mutableStateOf<Int?>(1) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(PrayerEducationData.suras) { sura ->
            val isExpanded = expandedSuraId == sura.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        expandedSuraId = if (isExpanded) null else sura.id 
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) sura.nameBn else sura.nameEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(
                                modifier = Modifier.padding(top = 2.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SuggestionChip(
                                    onClick = { },
                                    label = { Text(text = if (isBn) sura.classificationBn else sura.classificationEn, fontSize = 10.sp) },
                                    modifier = Modifier.height(24.dp)
                                )
                                Text(
                                    text = if (isBn) "আвят: ${sura.totalVerses}" else "Verses: ${sura.totalVerses}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = sura.sNameArabic,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldMedium,
                                fontFamily = FontFamily.Serif
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(14.dp))
                        
                        // Arabic full Box
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = sura.arabicText,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                    lineHeight = 42.sp,
                                    color = EmeraldDeep
                                )

                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))

                                Text(
                                    text = if (isBn) "উচ্চারণ (Pronunciation):" else "Transliteration:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = if (isBn) sura.transliterationBn else sura.transliterationEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = if (isBn) "অনুবাদ (Translation):" else "Meaning / Translation:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = EmeraldMedium
                                )
                                Text(
                                    text = if (isBn) sura.translationBn else sura.translationEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Quick copy
                                OutlinedButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("sura", "${sura.arabicText}\n\n${sura.translationBn}")
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(context, if (isBn) "সূরা কপি করা হয়েছে!" else "Sura copied!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBn) "সূরা কপি করুন" else "Copy Sura", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerRulesTab(isBn: Boolean) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Rakat Structures Row
        item {
            Text(
                text = if (isBn) "৫ ওয়াক্ত নামাজের রাকাত বিভাজন" else "Daily 5 Prayer Rakat Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Header row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(if (isBn) "নামাজ" else "Salat", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f), fontSize = 11.sp)
                        Text(if (isBn) "সুন্নাত" else "Sunnah", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), fontSize = 11.sp, textAlign = TextAlign.Center)
                        Text(if (isBn) "ফরজ" else "Fard", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), fontSize = 11.sp, textAlign = TextAlign.Center)
                        Text(if (isBn) "বিতর" else "Witr", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), fontSize = 11.sp, textAlign = TextAlign.Center)
                        Text(if (isBn) "মোট" else "Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.1f), fontSize = 11.sp, textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    PrayerEducationData.rakatList.forEach { rakat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(if (isBn) rakat.timeBn else rakat.timeEn, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f), fontSize = 12.sp)
                            
                            val totalSunnah = rakat.sunnahBefore + rakat.sunnahAfter
                            Text(text = "${totalSunnah}", modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
                            Text(text = "${rakat.fard}", modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            Text(text = "${rakat.witr}", modifier = Modifier.weight(1f), fontSize = 12.sp, textAlign = TextAlign.Center)
                            
                            Box(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .background(EmeraldMedium.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(vertical = 2.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${rakat.totalRakat} রাকাত",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDeep
                                )
                            }
                        }
                    }
                }
            }
        }

        // 13 Pillars (Farz)
        item {
            Text(
                text = if (isBn) "নামাজের ফরজ সমূহ (আহকাম ও আরকান)" else "Obligatory Acts (14 Fard Pillars)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val rules = if (isBn) PrayerEducationData.rulesBn else PrayerEducationData.rulesEn
                    rules.forEachIndexed { i, rule ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = if (i == 0) "📌 " else "• ",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldMedium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = rule,
                                style = if (i == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (i < rules.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                        }
                    }
                }
            }
        }

        // Wudu Guidelines
        item {
            Text(
                text = if (isBn) "পবিত্র ওযু করার সঠিক নিয়ম" else "Wudu Ritual (How to Perform)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val wuduSteps = if (isBn) listOf(
                        "১. ওযুর শুরুতে মনে মনে নিয়ত করা এবং 'বিসমিল্লাহ' বলা অত্যন্ত আবশ্যক সুন্নত আমল।",
                        "২. দুই হাতের কবজি পর্যন্ত ৩ বার ভালো করে ধৌত করা। প্রতি আঙুল খেলাফ করা।",
                        "৩. ৩ বার গড়গড়া করে বা শান্তভাবে কুলি করা এবং মেসওয়াক বা ব্রাশ করা।",
                        "৪. ৩ বার নাকে পানি দেওয়া এবং বাম হাতের কনিষ্ঠা আঙুল দিয়ে নাক পরিষ্কার করা।",
                        "৫. সম্পূর্ণ মুখমণ্ডল কপাল থেকে থুতনি এবং এক কানের লতি থেকে অন্য লতি পর্যন্ত ৩ বার ধোয়া।",
                        "৬. দুই হাত কনুইসহ ৩ বার ধোয়া (প্রথমে ডান হাত, তারপর বাম হাত)।",
                        "৭. ভেজা হাত দিয়ে একবার পুরো মাথা, দুই কান এবং ঘাড় মাসেহ করা।",
                        "৮. দুই পায়ের টাখনু বা গিরার উপর পর্যন্ত ৩ বার ভালোভাবে ধৌত করা ও আঙুল ঘষে পরিষ্কার রাখা।"
                    ) else listOf(
                        "1. Purify your intention (Niyyah) in heart and say 'Bismillah'.",
                        "2. Wash your hands up to wrist 3 times, rubbing fingers properly.",
                        "3. Rinse your mouth 3 times thoroughly.",
                        "4. Clean your nostrils with left fingers by inhaling water 3 times.",
                        "5. Wash your entire face 3 times completely, from hairline to chin.",
                        "6. Wash both arms up to elbows 3 times, beginning with right arm.",
                        "7. Wet hands and wipe your whole head once, including ears and back neck.",
                        "8. Wash both feet up to ankles 3 times, scrubbing between toes carefully."
                    )

                    wuduSteps.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldMedium,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // 1. Wudu Obligations (ওযুর ফরজসমূহ)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isBn) "ওযুর ফরজ সমূহ" else "Obligatory Acts (4 Fard of Wudu)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val wuduFarz = if (isBn) PrayerEducationData.wuduFarzBn else PrayerEducationData.wuduFarzEn
                    wuduFarz.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = if (i == 0) Icons.Default.Star else Icons.Default.Done,
                                contentDescription = null,
                                tint = if (i == 0) EmeraldMedium else EmeraldDeep,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                style = if (i == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (i < wuduFarz.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
                        }
                    }
                }
            }
        }

        // 2. Wudu Invalidators (ওযু ভঙ্গের কারণসমূহ)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isBn) "ওযু ভঙ্গের কারণ সমূহ" else "Wudu Invalidators (Reasons Wudu Breaks)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val wuduInvalidators = if (isBn) PrayerEducationData.wuduInvalidatorsBn else PrayerEducationData.wuduInvalidatorsEn
                    wuduInvalidators.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = if (i == 0) Icons.Default.Info else Icons.Default.Error,
                                contentDescription = null,
                                tint = if (i == 0) Color(0xFFD97706) else Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                style = if (i == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (i < wuduInvalidators.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
                        }
                    }
                }
            }
        }

        // 3. Prayer Invalidators (নামাজ ভঙ্গের কারণসমূহ)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isBn) "নামাজ ভঙ্গের কারণ সমূহ" else "Prayer Invalidators (Nullifiers of Salat)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val prayerInvalidators = if (isBn) PrayerEducationData.prayerInvalidatorsBn else PrayerEducationData.prayerInvalidatorsEn
                    prayerInvalidators.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                style = if (i == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (i < prayerInvalidators.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
                        }
                    }
                }
            }
        }

        // 4. Sunnah & Etiquettes within Prayer (নামাজের ভেতর ও বাহিরে প্রয়োজনীয় আমল ও সুন্নতসমূহ)
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (isBn) "নামাজের গুরুত্বপূর্ণ সুন্নত ও আমল সমূহ" else "Sunnah & Etiquettes within Prayer",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    val prayerInnerAmals = if (isBn) PrayerEducationData.prayerInnerAmalsBn else PrayerEducationData.prayerInnerAmalsEn
                    prayerInnerAmals.forEachIndexed { i, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Icon(
                                imageVector = if (i == 0) Icons.Default.Star else Icons.Default.Favorite,
                                contentDescription = null,
                                tint = if (i == 0) IslamicGold else EmeraldDeep,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                style = if (i == 0) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (i < prayerInnerAmals.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
                        }
                    }
                }
            }
        }
    }
}

// ===================================================================================
// DAILY DEEDS/AMALS TRACKER VIEWS (Jannat Sondhan Feature)
// ===================================================================================

@Composable
fun DailyDeedsTrackerView(
    viewModel: IbadahViewModel,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedDate by viewModel.deedSelectedDate.collectAsState()
    val completedDeeds by viewModel.completedDeedsForSelectedDate.collectAsState()
    val completedMonthData by viewModel.completedDeedsForMonth.collectAsState()

    // Determine current month details for month view
    val calendar = java.util.Calendar.getInstance()
    try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val date = sdf.parse(selectedDate)
        if (date != null) {
            calendar.time = date
        }
    } catch (_: Exception) {}

    val currentMonthNameBn = listOf("জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর")
    val currentMonthNameEn = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    
    val currentMonthIndex = calendar.get(java.util.Calendar.MONTH) // 0-11
    val currentYear = calendar.get(java.util.Calendar.YEAR)
    val maxDaysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)

    val monthHeaderStr = if (isBn) {
        "${currentMonthNameBn[currentMonthIndex]} ${viewModel.convertToBanglaDigits(currentYear.toString())}"
    } else {
        "${currentMonthNameEn[currentMonthIndex]} $currentYear"
    }

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp, top = 4.dp)
    ) {
        // 1. Target Date Selector Panel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val prevDate = getAdjacentDate(selectedDate, -1)
                            viewModel.selectDeedDate(prevDate)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous Day",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = getDeedHeaderLabel(selectedDate, isBn, viewModel),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable {
                                    val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
                                    viewModel.selectDeedDate(todayStr)
                                }
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Today,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBn) "আজকের দিনে ফিরুন" else "Restore to Today",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val nextDate = getAdjacentDate(selectedDate, 1)
                            viewModel.selectDeedDate(nextDate)
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next Day",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // 2. Beautiful Daily Completion Summary & Progress Card
        val totalDeedsCount = viewModel.defaultDeedItems.size
        val completedCount = completedDeeds.size
        val progressFraction = if (totalDeedsCount > 0) completedCount.toFloat() / totalDeedsCount else 0f
        val pct = (progressFraction * 100).toInt()

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(60.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier.fillMaxSize(),
                            color = EmeraldMedium,
                            strokeWidth = 6.dp,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Text(
                            text = if (isBn) "${viewModel.convertToBanglaDigits(pct.toString())}%" else "$pct%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldMedium
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) "আজকের আমল অগ্রগতির হার" else "Today's Amals Completion",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBn) {
                                "সম্পন্ন হয়েছে: ${viewModel.convertToBanglaDigits(completedCount.toString())} / ${viewModel.convertToBanglaDigits(totalDeedsCount.toString())}টি কাজ"
                            } else {
                                "Completed: $completedCount of $totalDeedsCount amals"
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Encouraging message depending on score
                        val quote = if (isBn) {
                            when {
                                completedCount == totalDeedsCount -> "মাশাআল্লাহ! আপনি সব কাজ করেছেন! জান্নাত আপনার নিকটবর্তী হোক।"
                                completedCount >= 5 -> "চমৎকার! আপনি বেশির ভাগ আমল সম্পন্ন করেছেন।"
                                completedCount >= 1 -> "আলহামদুলিল্লাহ! আমল দিয়ে আপনার দিনটিকে আরও বরকতময় করে তুলুন।"
                                else -> "আজকে একটি আমল দিয়ে জান্নাতের সন্ধান যাত্রা তরান্বিত করুন।"
                            }
                        } else {
                            when {
                                completedCount == totalDeedsCount -> "Masha'Allah! All daily actions fully executed!"
                                completedCount >= 5 -> "Excellence achieved! Most key milestones checked off."
                                completedCount >= 1 -> "Good start! Aim to fulfill other duties to excel."
                                else -> "Click checkbox of any deed below to register your checklist today!"
                            }
                        }
                        Text(
                            text = quote,
                            fontSize = 11.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = EmeraldMedium,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // 3. Checklist of deeds
        item {
            Text(
                text = if (isBn) "সকাল-সন্ধ্যা আমলনামা" else "Daily Islamic Deeds Check",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
            )
        }

        items(viewModel.defaultDeedItems) { deed ->
            val isCompleted = completedDeeds.contains(deed.id)
            val icon = getDeedCategoryIcon(deed.iconName)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleDeedCompleted(deed.id) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) {
                        EmeraldMedium.copy(alpha = 0.06f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (isCompleted) {
                        EmeraldMedium.copy(alpha = 0.3f)
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (isCompleted) EmeraldMedium.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isCompleted) EmeraldMedium else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) deed.nameBn else deed.nameEn,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) EmeraldMedium else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else androidx.compose.ui.text.style.TextDecoration.None
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (isBn) deed.descBn else deed.descEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            lineHeight = 14.sp
                        )
                    }

                    Checkbox(
                        checked = isCompleted,
                        onCheckedChange = { viewModel.toggleDeedCompleted(deed.id) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = EmeraldMedium,
                            uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }

        // 4. Monthly Record Account representation (মাসিক হিসাব ও অগ্রগতি)
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isBn) "মাসিক আমলনামা ও ট্র্যাকার" else "Monthly Analytics & History",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (isBn) "$monthHeaderStr সালের হিসাব" else "Record for $monthHeaderStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .background(EmeraldMedium.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            val activeDaysThisMonth = completedMonthData.keys.filter { it.startsWith(selectedDate.substring(0, 8)) }.size
                            Text(
                                text = if (isBn) {
                                    "সক্রিয়: ${viewModel.convertToBanglaDigits(activeDaysThisMonth.toString())} দিন"
                                } else {
                                    "Active: $activeDaysThisMonth Days"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Heatmap / Calendar-styled grid for 1 to maxDaysInMonth
                    val daysList = (1..maxDaysInMonth).toList()
                    val totalRows = (maxDaysInMonth + 6) / 7

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (r in 0 until totalRows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                for (c in 0 until 7) {
                                    val dayIndex = r * 7 + c
                                    if (dayIndex < maxDaysInMonth) {
                                        val day = daysList[dayIndex]
                                        val dateKey = String.format(java.util.Locale.US, "%s%02d", selectedDate.substring(0, 8), day)
                                        val dayCompletedCount = completedMonthData[dateKey]?.size ?: 0
                                        
                                        // Colors mapping:
                                        val (circleColor, textColor) = when (dayCompletedCount) {
                                            0 -> Pair(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f), MaterialTheme.colorScheme.onSurfaceVariant)
                                            in 1..3 -> Pair(EmeraldMedium.copy(alpha = 0.18f), EmeraldMedium)
                                            in 4..6 -> Pair(EmeraldMedium, Color.White)
                                            else -> Pair(EmeraldMedium.copy(alpha = 0.85f), Color.White)
                                        }

                                        val isCurrentSelectedDay = selectedDate.endsWith(String.format(java.util.Locale.US, "%02d", day))

                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(circleColor)
                                                .border(
                                                    width = if (isCurrentSelectedDay) 2.1.dp else 1.dp,
                                                    color = if (isCurrentSelectedDay) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable {
                                                    val newDate = String.format(java.util.Locale.US, "%s%02d", selectedDate.substring(0, 8), day)
                                                    viewModel.selectDeedDate(newDate)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = if (isBn) viewModel.convertToBanglaDigits(day.toString()) else day.toString(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
                                            )
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(34.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Calendar Color Legend Indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DailyProgressLegendItem(label = if (isBn) "০টি কাজ" else "0 done", color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        DailyProgressLegendItem(label = if (isBn) "১-৩টি" else "1-3 done", color = EmeraldMedium.copy(alpha = 0.25f))
                        DailyProgressLegendItem(label = if (isBn) "৪-৬টি" else "4-6 done", color = EmeraldMedium)
                        DailyProgressLegendItem(label = if (isBn) "৭-৮টি" else "7-8 done", color = EmeraldMedium.copy(alpha = 0.85f))
                    }
                }
            }
        }
    }
}

@Composable
fun DailyProgressLegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(text = label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
    }
}

fun getDeedHeaderLabel(dateStr: String, isBn: Boolean, viewModel: IbadahViewModel): String {
    return try {
        val sdfSource = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val date = sdfSource.parse(dateStr) ?: java.util.Date()
        
        val sdfDestBn = java.text.SimpleDateFormat("dd MMMM, yyyy", java.util.Locale("bn"))
        val sdfDestEn = java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.US)
        
        if (isBn) {
            val formatted = sdfDestBn.format(date)
            viewModel.convertToBanglaDigits(formatted)
        } else {
            sdfDestEn.format(date)
        }
    } catch (e: Exception) {
        dateStr
    }
}

fun getAdjacentDate(dateStr: String, offset: Int): String {
    return try {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        val date = sdf.parse(dateStr) ?: java.util.Date()
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        cal.add(java.util.Calendar.DATE, offset)
        sdf.format(cal.time)
    } catch (e: Exception) {
        dateStr
    }
}

fun getDeedCategoryIcon(iconName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (iconName) {
        "Salah" -> Icons.Default.AccessTime
        "Nights" -> Icons.Default.Brightness2
        "Books" -> Icons.Default.MenuBook
        "Adhkar" -> Icons.Default.WbSunny
        "Istighfar" -> Icons.Default.Fingerprint
        "Sunnah" -> Icons.Default.Spa
        "Charity" -> Icons.Default.Favorite
        "Family" -> Icons.Default.Home
        else -> Icons.Default.Star
    }
}

@Composable
fun OnboardingScreen(onFinished: () -> Unit, isBn: Boolean) {
    var currentPage by remember { mutableStateOf(0) }
    
    val pages = remember {
        listOf(
            OnboardingPageData(
                titleBn = "নামাজ ও ইবাদত",
                titleEn = "Prayer & Worship",
                descBn = "সরাসরি নামাজের সময়সূচী, কিবলা কম্পাস, ডিজিটাল তসবিহ এবং প্রতিদিনের ওয়াক্ত ও ফরজ নামাজ ট্র্যাকার।",
                descEn = "Real-time accurate Prayer Times, Qibla direction, Digital Tasbih counter, and personalized Salat tracking records.",
                imageUrl = "https://images.unsplash.com/photo-1597935258735-e254c1839512?auto=format&fit=crop&w=1200&q=80" // Warm Grand Mosque twilight architecture
            ),
            OnboardingPageData(
                titleBn = "আল-কুরআনুল কারীম",
                titleEn = "The Holy Quran",
                descBn = "বাংলা উচ্চারণ, অর্থ, তাফসীর ও স্পষ্ট অডিও তিলাওয়াতসহ সম্পূর্ণ আল-কুরআন শুনুন এবং পড়ুন।",
                descEn = "Read, learn, and listen to the Holy Quran with phonetics, Bengali translation, script choices, and crystal clear audios.",
                imageUrl = "https://images.unsplash.com/photo-1609599006353-e629e1d55139?auto=format&fit=crop&w=1200&q=80" // Holy Quran under warm spiritual lighting
            ),
            OnboardingPageData(
                titleBn = "লাইভ টিভি ও রেডিও",
                titleEn = "Live TV & Radio",
                descBn = "মক্কা ও মদিনা লাইভ প্রচার, বিভিন্ন ইসলামিক টিভি চ্যানেল এবং সরাসরি জনপ্রিয় ইসলামিক রেডিও শুনুন।",
                descEn = "Watch Holy Makkah & Madinah Live streams, Islamic TV channels, and listen to soul-enriching Islamic Radio broadcasts directly.",
                imageUrl = "https://images.unsplash.com/photo-1564769050039-23113de55513?auto=format&fit=crop&w=1200&q=80" // The Holy Kaaba closeup with gold-wrapped Kiswah
            ),
            OnboardingPageData(
                titleBn = "ওয়াজ ও ইসলামিক ভিডিও",
                titleEn = "Waz & Islamic Video",
                descBn = "জনপ্রিয় বক্তাদের চমৎকার সব ওয়াজ মাহফিল, কুরআন তিলাওয়াতের বঙ্গানুবাদ এবং সুন্দর ইসলামিক গজলের লাইব্রেরী।",
                descEn = "Access professional waz mahfil collection, surah translations, and traditional audio-visual Islamic Nasheeds anytime.",
                imageUrl = "https://images.unsplash.com/photo-1519817650390-64a93db51149?auto=format&fit=crop&w=1200&q=80" // Majestic starry sky and minarets
            ),
            OnboardingPageData(
                titleBn = "প্রতিদিনের দোআ ও আমল",
                titleEn = "Daily Dua & Amal",
                descBn = "প্রতিদিনের প্রয়োজনীয় সহীহ দুআ কালেকশন, উন্নত আমল ট্র্যাকার, হিসনুল মুসলিম ও সুন্দর ইসলামিক কুইজ।",
                descEn = "Follow authentic daily Adhkar, track your habits/good deeds, browse Hisnul Muslim, and test your Islamic knowledge.",
                imageUrl = "https://images.unsplash.com/photo-1584551246679-0daf3d275d0f?auto=format&fit=crop&w=1200&q=80" // Warm hands raised in peaceful Dua
            )
        )
    }
    
    val page = pages[currentPage]
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Beautiful glowing gradient background fallback that shows instantly before image loads
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF031B1B), // Luxurious Dark Teal
                            Color(0xFF063333), // Emerald-shadowed Dark Green
                            Color(0xFF0C1927)  // Deep Midnight Blue
                        )
                    )
                )
        )

        // Background Wallpaper image with crossfade
        coil.compose.AsyncImage(
            model = page.imageUrl,
            contentDescription = page.titleEn,
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        
        // Gradient dark overlay from bottom to top to guarantee readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.92f)
                        )
                    )
                )
        )
        
        // Skip Button position on very top-right
        TextButton(
            onClick = onFinished,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 16.dp, end = 16.dp)
                .testTag("onboarding_skip_button"),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.White.copy(alpha = 0.85f))
        ) {
            Text(
                text = if (isBn) "এড়িয়ে যান ➔" else "Skip ➔",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black,
                        offset = androidx.compose.ui.geometry.Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )
        }
        
        // Content container placed at bottom third
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Heading with Soft Accent Design
            Text(
                text = if (isBn) page.titleBn else page.titleEn,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black,
                        offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Description paragraph with dynamic translation overlay
            Text(
                text = if (isBn) page.descBn else page.descEn,
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Three dot indicator layout
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                pages.forEachIndexed { index, _ ->
                    val isActive = index == currentPage
                    Box(
                        modifier = Modifier
                            .size(
                                width = if (isActive) 24.dp else 8.dp,
                                height = 8.dp
                            )
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isActive) SoftGoldBorder else Color.White.copy(alpha = 0.35f)
                            )
                    )
                }
            }
            
            // Primary Next Button
            Button(
                onClick = {
                    if (currentPage < pages.lastIndex) {
                        currentPage++
                    } else {
                        onFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("onboarding_next_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (currentPage == pages.lastIndex) {
                            if (isBn) "অ্যাপসে প্রবেশ করুন" else "Get Started"
                        } else {
                            if (isBn) "পরবর্তী বিবরণ" else "Next Feature"
                        },
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (currentPage == pages.lastIndex) Icons.Default.CheckCircle else Icons.Default.ArrowForward,
                        contentDescription = "Next Action",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

data class OnboardingPageData(
    val titleBn: String,
    val titleEn: String,
    val descBn: String,
    val descEn: String,
    val imageUrl: String
)

