package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.ComponentName
import android.widget.RemoteViews
import com.example.R
import java.util.Calendar
import java.util.Locale

class IbadahCurrentPrayerWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE || 
            intent.action == "com.example.action.CURRENT_PRAYER_WIDGET_REFRESH") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, IbadahCurrentPrayerWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleNextUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? android.app.AlarmManager
            if (alarmManager != null) {
                val intent = Intent(context, IbadahCurrentPrayerWidgetProvider::class.java).apply {
                    action = "com.example.action.CURRENT_PRAYER_WIDGET_REFRESH"
                }
                val pendingIntent = android.app.PendingIntent.getBroadcast(
                    context, 1004, intent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private fun scheduleNextUpdate(context: Context) {
            try {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? android.app.AlarmManager ?: return
                val intent = Intent(context, IbadahCurrentPrayerWidgetProvider::class.java).apply {
                    action = "com.example.action.CURRENT_PRAYER_WIDGET_REFRESH"
                }
                val pendingIntent = android.app.PendingIntent.getBroadcast(
                    context, 1004, intent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MINUTE, 1)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        android.app.AlarmManager.RTC,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        android.app.AlarmManager.RTC,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun toBengaliDigits(input: String): String {
            val bDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
            val eDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
            var result = input
            for (i in 0..9) {
                result = result.replace(eDigits[i], bDigits[i])
            }
            result = result.replace("AM", "এএম").replace("PM", "পিএম")
                           .replace("am", "এএম").replace("pm", "পিএম")
            return result
        }

        private fun formatWaqtTime(time24: String, use24h: Boolean): String {
            if (use24h) {
                return toBengaliDigits(time24)
            }
            return try {
                val parts = time24.split(":")
                var hr = parts[0].trim().toInt()
                val min = parts[1].trim().toInt()
                val isPm = hr >= 12
                if (hr > 12) hr -= 12
                if (hr == 0) hr = 12
                val suffix = if (isPm) " পি" else " এ"
                toBengaliDigits(String.format("%02d:%02d", hr, min) + suffix)
            } catch (e: Exception) {
                toBengaliDigits(time24)
            }
        }

        private fun formatRemainingText(mins: Int): String {
            if (mins <= 0) return "০ মিনিট"
            val h = mins / 60
            val m = mins % 60
            return if (h > 0) {
                "${h} ঘণ্টা ${m} মিনিট"
            } else {
                "${m} মিনিট"
            }
        }

        private fun getUpcomingForbiddenText(
            currentMins: Int,
            fajrParts: List<String>,
            dhuhrMins: Int,
            sunsetMins: Int,
            use24h: Boolean
        ): Pair<String, String> {
            try {
                val sunriseHour = (fajrParts[0].toInt() + 1)
                val sunriseMin = (fajrParts[1].toInt() + 20) % 60
                val sunriseMins = sunriseHour * 60 + sunriseMin
                val sunriseEndMins = sunriseMins + 15

                val zawalStartMins = dhuhrMins - 10
                val sunsetForbiddenStartMins = sunsetMins - 15

                val sunriseStartStr = String.format("%02d:%02d", sunriseHour, sunriseMin)
                val sunriseEndStr = String.format("%02d:%02d", sunriseEndMins / 60, sunriseEndMins % 60)

                val zawalStartStr = String.format("%02d:%02d", zawalStartMins / 60, zawalStartMins % 60)
                val dhuhrStr = String.format("%02d:%02d", dhuhrMins / 60, dhuhrMins % 60)

                val sunsetStartStr = String.format("%02d:%02d", sunsetForbiddenStartMins / 60, sunsetForbiddenStartMins % 60)
                val sunsetStr = String.format("%02d:%02d", sunsetMins / 60, sunsetMins % 60)

                return when {
                    currentMins in sunriseMins until sunriseEndMins -> {
                        Pair("চলমান সূর্যোদয়", formatWaqtTime(sunriseEndStr, use24h))
                    }
                    currentMins in zawalStartMins until dhuhrMins -> {
                        Pair("চলমান দ্বিপ্রহর", formatWaqtTime(dhuhrStr, use24h))
                    }
                    currentMins in sunsetForbiddenStartMins until sunsetMins -> {
                        Pair("চলমান সূর্যাস্ত", formatWaqtTime(sunsetStr, use24h))
                    }
                    currentMins < sunriseMins || currentMins >= sunsetMins -> {
                        val range = "${formatWaqtTime(sunriseStartStr, use24h)}-${formatWaqtTime(sunriseEndStr, use24h)}"
                        Pair("আসন্ন সূর্যোদয়", range)
                    }
                    currentMins in sunriseEndMins until zawalStartMins -> {
                        val range = "${formatWaqtTime(zawalStartStr, use24h)}-${formatWaqtTime(dhuhrStr, use24h)}"
                        Pair("আসন্ন দ্বিপ্রহর", range)
                    }
                    else -> {
                        val range = "${formatWaqtTime(sunsetStartStr, use24h)}-${formatWaqtTime(sunsetStr, use24h)}"
                        Pair("আসন্ন সূর্যাস্ত", range)
                    }
                }
            } catch (e: Exception) {
                return Pair("আসন্ন নিষিদ্ধ", "--:--")
            }
        }

        data class CityInfoBase(
            val nameBn: String,
            val nameEn: String,
            val sahriTime: String,
            val iftarTime: String,
            val fajrTime: String,
            val dhuhrTime: String,
            val asrTime: String,
            val maghribTime: String,
            val ishaTime: String
        )

        private val staticCities = listOf(
            CityInfoBase("ঢাকা", "Dhaka", "03:55", "18:42", "04:05", "12:06", "15:45", "18:42", "20:00"),
            CityInfoBase("মক্কা", "Mecca", "04:12", "19:02", "04:22", "12:22", "15:38", "19:02", "20:25"),
            CityInfoBase("লন্ডন", "London", "03:10", "21:15", "03:25", "13:05", "17:15", "21:15", "22:45"),
            CityInfoBase("নিউ ইয়র্ক", "New York", "03:45", "20:20", "04:00", "12:58", "16:55", "20:20", "21:50"),
            CityInfoBase("সিডনি", "Sydney", "05:15", "17:10", "05:30", "11:55", "14:48", "17:10", "18:35")
        )

        private fun getSelectedCity(context: Context): CityInfoBase {
            val prefs = context.getSharedPreferences("jannat_deeds_prefs", Context.MODE_PRIVATE)
            val isCustom = prefs.getBoolean("is_custom_location", false)
            if (isCustom) {
                val nameBn = prefs.getString("custom_name_bn", "ঢাকা") ?: "ঢাকা"
                val cleanBn = nameBn.replace(", বাংলাদেশ", "").replace("বাংলাদেশ", "").trim()
                val nameEn = prefs.getString("custom_name_en", "Dhaka") ?: "Dhaka"
                val sahri = prefs.getString("custom_sahri", "03:55") ?: "03:55"
                val iftar = prefs.getString("custom_iftar", "18:42") ?: "18:42"
                val fajr = prefs.getString("custom_fajr", "04:05") ?: "04:05"
                val dhuhr = prefs.getString("custom_dhuhr", "12:06") ?: "12:06"
                val asr = prefs.getString("custom_asr", "15:45") ?: "15:45"
                val maghrib = prefs.getString("custom_maghrib", "18:42") ?: "18:42"
                val isha = prefs.getString("custom_isha", "20:00") ?: "20:00"
                return CityInfoBase(cleanBn, nameEn, sahri, iftar, fajr, dhuhr, asr, maghrib, isha)
            } else {
                val savedName = prefs.getString("selected_city_name_en", "Dhaka, Bangladesh") ?: "Dhaka"
                val cleanSaved = savedName.split(",")[0].trim()
                val matched = staticCities.find { it.nameEn.equals(cleanSaved, ignoreCase = true) || it.nameEn.contains(cleanSaved, ignoreCase = true) }
                return matched ?: staticCities[0]
            }
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val city = getSelectedCity(context)
                val prefs = context.getSharedPreferences("jannat_deeds_prefs", Context.MODE_PRIVATE)
                val use24HourFormat = prefs.getBoolean("use_24h_format", false)

                val views = RemoteViews(context.packageName, R.layout.ibadah_current_prayer_widget)

                // 1. Set City Label
                views.setTextViewText(R.id.current_widget_city_text, city.nameBn)

                // 2. Set static inputs
                views.setTextViewText(R.id.current_widget_sehri_time, formatWaqtTime(city.sahriTime, use24HourFormat))
                views.setTextViewText(R.id.current_widget_iftar_time, formatWaqtTime(city.iftarTime, use24HourFormat))

                // 3. Setup timings boundaries
                val calNow = Calendar.getInstance()
                val currentMins = calNow.get(Calendar.HOUR_OF_DAY) * 60 + calNow.get(Calendar.MINUTE)

                val fajrParts = city.fajrTime.split(":")
                val fajrMins = if (fajrParts.size == 2) fajrParts[0].toInt() * 60 + fajrParts[1].toInt() else 240

                val dhuhrParts = city.dhuhrTime.split(":")
                val dhuhrMins = if (dhuhrParts.size == 2) dhuhrParts[0].toInt() * 60 + dhuhrParts[1].toInt() else 720

                val asrParts = city.asrTime.split(":")
                val asrMins = if (asrParts.size == 2) asrParts[0].toInt() * 60 + asrParts[1].toInt() else 945

                val sunsetParts = city.maghribTime.split(":")
                val sunsetMins = if (sunsetParts.size == 2) sunsetParts[0].toInt() * 60 + sunsetParts[1].toInt() else 1110

                val ishaParts = city.ishaTime.split(":")
                val ishaMins = if (ishaParts.size == 2) ishaParts[0].toInt() * 60 + ishaParts[1].toInt() else 1200

                // Sunrise Hour Details
                val sunriseHour = (fajrParts[0].toInt() + 1)
                val sunriseMin = (fajrParts[1].toInt() + 20) % 60
                val sunriseMins = sunriseHour * 60 + sunriseMin
                val sunriseEndMins = sunriseMins + 15

                val zawalStartMins = dhuhrMins - 10
                val sunsetForbiddenStartMins = sunsetMins - 15

                // 4. Solve active waqt state
                var activeName = "ফজর"
                var activeTime = city.fajrTime
                var nextName = "জোহর"
                var nextTime = city.dhuhrTime
                
                var activeStartMins = fajrMins
                var nextStartMins = dhuhrMins

                var isForbiddenActive = false

                when {
                    currentMins in fajrMins until sunriseMins -> {
                        activeName = "ফজর"
                        activeTime = city.fajrTime
                        nextName = "যোহর"
                        nextTime = city.dhuhrTime
                        activeStartMins = fajrMins
                        nextStartMins = dhuhrMins
                    }
                    currentMins in sunriseMins until sunriseEndMins -> {
                        activeName = "সূর্যোদয়"
                        activeTime = String.format("%02d:%02d", sunriseHour, sunriseMin)
                        nextName = "যোহর"
                        nextTime = city.dhuhrTime
                        activeStartMins = sunriseMins
                        nextStartMins = dhuhrMins
                        isForbiddenActive = true
                    }
                    currentMins in sunriseEndMins until zawalStartMins -> {
                        activeName = "ইশরাক/চাশত"
                        activeTime = String.format("%02d:%02d", sunriseHour, (sunriseMin + 25) % 60)
                        nextName = "যোহর"
                        nextTime = city.dhuhrTime
                        activeStartMins = sunriseEndMins
                        nextStartMins = dhuhrMins
                    }
                    currentMins in zawalStartMins until dhuhrMins -> {
                        activeName = "দ্বিপ্রহর"
                        activeTime = String.format("%02d:%02d", zawalStartMins / 60, zawalStartMins % 60)
                        nextName = "যোহর"
                        nextTime = city.dhuhrTime
                        activeStartMins = zawalStartMins
                        nextStartMins = dhuhrMins
                        isForbiddenActive = true
                    }
                    currentMins in dhuhrMins until asrMins -> {
                        activeName = "যোহর"
                        activeTime = city.dhuhrTime
                        nextName = "আসর"
                        nextTime = city.asrTime
                        activeStartMins = dhuhrMins
                        nextStartMins = asrMins
                    }
                    currentMins in asrMins until sunsetForbiddenStartMins -> {
                        activeName = "আসর"
                        activeTime = city.asrTime
                        nextName = "মাগরিব"
                        nextTime = city.maghribTime
                        activeStartMins = asrMins
                        nextStartMins = sunsetMins
                    }
                    currentMins in sunsetForbiddenStartMins until sunsetMins -> {
                        activeName = "সূর্যাস্ত"
                        activeTime = String.format("%02d:%02d", sunsetForbiddenStartMins / 60, sunsetForbiddenStartMins % 60)
                        nextName = "মাগরিব"
                        nextTime = city.maghribTime
                        activeStartMins = sunsetForbiddenStartMins
                        nextStartMins = sunsetMins
                        isForbiddenActive = true
                    }
                    currentMins in sunsetMins until ishaMins -> {
                        activeName = "মাগরিব"
                        activeTime = city.maghribTime
                        nextName = "এশা"
                        nextTime = city.ishaTime
                        activeStartMins = sunsetMins
                        nextStartMins = ishaMins
                    }
                    else -> {
                        activeName = "এশা"
                        activeTime = city.ishaTime
                        nextName = "ফজর"
                        nextTime = city.fajrTime
                        activeStartMins = ishaMins
                        nextStartMins = fajrMins
                    }
                }

                // Apply Active and Next Labels in Widget
                views.setTextViewText(R.id.current_widget_curr_name, activeName)
                views.setTextViewText(R.id.current_widget_curr_time, formatWaqtTime(activeTime, use24HourFormat))
                views.setTextViewText(R.id.current_widget_next_name, nextName)
                views.setTextViewText(R.id.current_widget_next_time, formatWaqtTime(nextTime, use24HourFormat))

                // Solve exact countdown remaining time
                val diffMins = if (nextStartMins >= currentMins) {
                    nextStartMins - currentMins
                } else {
                    (1440 - currentMins) + nextStartMins
                }
                views.setTextViewText(R.id.current_widget_countdown_text, toBengaliDigits(formatRemainingText(diffMins)))

                // Calculate progress bar fraction percentage
                val totalWindow = if (nextStartMins >= activeStartMins) {
                    nextStartMins - activeStartMins
                } else {
                    (1440 - activeStartMins) + nextStartMins
                }
                val elapsed = if (currentMins >= activeStartMins) {
                    currentMins - activeStartMins
                } else {
                    (1440 - activeStartMins) + currentMins
                }
                val progressFraction = if (totalWindow > 0) {
                    (elapsed * 100) / totalWindow
                } else {
                    0
                }
                views.setProgressBar(R.id.current_widget_progress_bar, 100, progressFraction.coerceIn(0, 100), false)

                // Forbidden Banner status calculations
                val (upcomingLabel, upcomingRange) = getUpcomingForbiddenText(
                    currentMins = currentMins,
                    fajrParts = fajrParts,
                    dhuhrMins = dhuhrMins,
                    sunsetMins = sunsetMins,
                    use24h = use24HourFormat
                )
                views.setTextViewText(R.id.current_widget_upcoming_label, upcomingLabel)
                views.setTextViewText(R.id.current_widget_upcoming_forbidden, toBengaliDigits(upcomingRange))

                if (isForbiddenActive) {
                    views.setTextViewText(R.id.current_widget_status_sub, "⚠️ নামাজ আদায় নিষিদ্ধ!")
                    views.setTextColor(R.id.current_widget_status_sub, 0xFFFF5252.toInt())
                    views.setInt(R.id.current_widget_forbidden_banner_box, "setBackgroundResource", R.drawable.widget_forbidden_bg)
                    
                    views.setTextViewText(R.id.current_widget_forbidden_banner_title, "⚠️ তীব্রভাবে হারাম সময় চলছে!")
                    views.setTextColor(R.id.current_widget_forbidden_banner_title, 0xFFFF8A80.toInt())
                    views.setTextViewText(R.id.current_widget_forbidden_banner_desc, "এই সময়টিতে যেকোনো ফরজ বা নফল নামাজ পড়া হারাম")
                } else {
                    views.setTextViewText(R.id.current_widget_status_sub, "পবিত্র $activeName ওয়াক্ত চলমান")
                    views.setTextColor(R.id.current_widget_status_sub, 0xFF81C784.toInt())
                    views.setInt(R.id.current_widget_forbidden_banner_box, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                    
                    views.setTextViewText(R.id.current_widget_forbidden_banner_title, "আসন্ন নিষিদ্ধ নামাজের সময়সূচী")
                    views.setTextColor(R.id.current_widget_forbidden_banner_title, 0xFFD4AF37.toInt())
                    views.setTextViewText(R.id.current_widget_forbidden_banner_desc, "পরবর্তী নিষিদ্ধ সময়ে সালাত ও সেজদা পরিহার করুন")
                }

                // Click Intent to Open Main App
                val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                if (launchIntent != null) {
                    val pendingIntent = android.app.PendingIntent.getActivity(
                        context, 0, launchIntent, 
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.current_prayer_widget_root, pendingIntent)
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
