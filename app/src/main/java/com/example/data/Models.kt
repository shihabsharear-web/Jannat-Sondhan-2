package com.example.data

// Language Preference
enum class AppLanguage {
    BN, EN, AR
}

// Prayer times representation
data class PrayerSchedule(
    val nameStrBn: String,
    val nameStrEn: String,
    val timeStr: String, // HH:mm format
    val isForbidden: Boolean = false,
    val notificationEnabled: Boolean = true
)

// List of prayers for tracking
data class SalatTrackItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val isCompleted: Boolean = false,
    val timeStr: String
)

// Quran Surah representation
data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameBengali: String,
    val nameEnglish: String,
    val totalVerses: Int,
    val verses: List<Verse>
)

data class Verse(
    val number: Int,
    val textArabic: String,
    val textBengali: String,
    val textEnglish: String,
    val tafsirBengali: String
)

// Bookmark entity
data class SuraBookmark(
    val surahNumber: Int,
    val surahName: String,
    val verseNumber: Int,
    val timestamp: Long
)

// Tasbih log history item
data class TasbihHistoryItem(
    val phrase: String,
    val count: Int,
    val dateString: String
)

// Duas with translation and transliteration
data class DuaItem(
    val id: String,
    val title: String,
    val titleEn: String,
    val arabic: String,
    val translation: String,
    val transliteration: String,
    val transliterationBn: String = "",
    val reference: String,
    val category: String, // Morning, Evening, Ramadan, General
    var count: Int = 0,
    val target: Int = 3
)

// Asma-ul-Husna (Allah's names)
data class AllahName(
    val id: Int,
    val arabic: String,
    val transliterationBn: String,
    val meaningBn: String,
    val meaningEn: String
)

// Islamic Daily Quote
data class IslamicQuote(
    val quote: String,
    val source: String
)

// Curated YouTube Videos
data class IslamicVideo(
    val id: String,
    val title: String,
    val speaker: String,
    val thumbnail: String,
    val duration: String,
    val youtubeId: String,
    val videoUrl: String = ""
)

// Notice Board Notification
data class NoticeMessage(
    val id: String,
    val title: String,
    val message: String,
    val dateStr: String,
    val category: String
)

// Day and Time structures for Prayer Schedules
data class DayPrayerSchedule(
    val dateStr: String,
    val dayIndex: Int,
    val timings: List<PrayerTimeItem>
)

data class PrayerTimeItem(
    val nameBn: String,
    val nameEn: String,
    val startTime12h: String,
    val startTime24h: String,
    val endTime12h: String,
    val endTime24h: String
)

data class PrayerAlarm(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val nameAr: String,
    val isEnabled: Boolean,
    val isDaily: Boolean, // True for daily, false for today-only (আজকের দিনে)
    val offsetMinutes: Int, // Shift alarm time in minutes
    val isAzanAlarm: Boolean = true, // Play Azan sound
    val customTime: String? = null, // For user defined alarms (Format "HH:MM" or "HH:MM AM/PM")
    val customDays: List<Int>? = null, // Custom repeating days (1 = Sunday, 2 = Monday, ..., 7 = Saturday)
    val specificDate: String? = null // Specific date-based trigger (Format "YYYY-MM-DD")
)

data class DeedItem(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val descBn: String,
    val descEn: String,
    val category: String, // "Salah" (সালাত), "Quran" (কুরআন), "Remembrance" (জিকির ও দোআ), "Sadaqah" (দান ও সদ্ব্যবহার)
    val iconName: String
)

data class IslamicAudioItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val speakerBn: String,
    val speakerEn: String,
    val category: String, // "waz", "quran_translation", "quran_only", "nasheed", "dua"
    val audioUrl: String,
    val duration: String = ""
)

data class QuizQuestion(
    val id: String,
    val questionBn: String,
    val questionEn: String,
    val optionsBn: List<String>,
    val optionsEn: List<String>,
    val correctIdx: Int,
    val explanationBn: String,
    val explanationEn: String,
    val points: Int = 10
)



