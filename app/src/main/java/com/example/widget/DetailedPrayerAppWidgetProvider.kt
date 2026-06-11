package com.example.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.ComponentName
import android.widget.RemoteViews
import com.example.R
import java.util.Calendar

class DetailedPrayerAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        scheduleNextUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE || 
            intent.action == "com.example.action.DETAILED_PRAYER_WIDGET_REFRESH") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, DetailedPrayerAppWidgetProvider::class.java)
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
                val intent = Intent(context, DetailedPrayerAppWidgetProvider::class.java).apply {
                    action = "com.example.action.DETAILED_PRAYER_WIDGET_REFRESH"
                }
                val pendingIntent = android.app.PendingIntent.getBroadcast(
                    context, 1002, intent,
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
                val intent = Intent(context, DetailedPrayerAppWidgetProvider::class.java).apply {
                    action = "com.example.action.DETAILED_PRAYER_WIDGET_REFRESH"
                }
                val pendingIntent = android.app.PendingIntent.getBroadcast(
                    context, 1002, intent,
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
                val suffix = if (isPm) " পি" else " এ" // Compact alignment
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

        private fun addMinutesToTimeStr(timeStr: String, minsToAdd: Int): String {
            return try {
                val parts = timeStr.split(":")
                val hr = parts[0].trim().toInt()
                val min = parts[1].trim().toInt()
                val totalMins = (hr * 60 + min + minsToAdd + 1440) % 1440
                String.format("%02d:%02d", totalMins / 60, totalMins % 60)
            } catch (e: Exception) {
                timeStr
            }
        }

        private fun calculateTahajjudTime(fajrTime: String): String {
            return try {
                val parts = fajrTime.split(":")
                val hr = parts[0].trim().toInt()
                val min = parts[1].trim().toInt()
                val totalMins = hr * 60 + min
                val tahajjudMins = (totalMins - 150 + 1440) % 1440
                String.format("%02d:%02d", tahajjudMins / 60, tahajjudMins % 60)
            } catch (e: Exception) {
                "02:00"
            }
        }

        private fun calculateSunriseForbidden(fajrTime: String): String {
            return try {
                val parts = fajrTime.split(":")
                val hr = (parts[0].toInt() + 1)
                val min = (parts[1].toInt() + 20) % 60
                String.format("%02d:%02d", hr, min)
            } catch (e: Exception) {
                "05:35"
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

        private fun getSimulatedWeather(cityEn: String, hour: Int): Pair<String, String> {
            val isNight = hour < 6 || hour > 18
            val baseTemp = when {
                cityEn.contains("Dhaka", ignoreCase = true) || cityEn.contains("ঢাকা", ignoreCase = true) -> if (isNight) 27 else 33
                cityEn.contains("Mecca", ignoreCase = true) || cityEn.contains("মক্কা", ignoreCase = true) -> if (isNight) 31 else 39
                cityEn.contains("London", ignoreCase = true) -> if (isNight) 13 else 20
                cityEn.contains("New York", ignoreCase = true) -> if (isNight) 16 else 24
                cityEn.contains("Sydney", ignoreCase = true) -> if (isNight) 14 else 21
                else -> if (isNight) 25 else 31
            }
            
            val minuteFluc = (Calendar.getInstance().get(Calendar.MINUTE) % 3) - 1
            val temp = baseTemp + minuteFluc

            val condition = when {
                isNight -> "পরিষ্কার আকাশ"
                temp >= 38 -> "তীব্র গরম"
                temp >= 32 -> "রৌদ্রোজ্জ্বল"
                temp >= 26 -> "আংশিক মেঘলা"
                else -> "পরিষ্কার ও মনোরম"
            }
            return Pair("${toBengaliDigits(temp.toString())}°সে", condition)
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

                val views = RemoteViews(context.packageName, R.layout.ibadah_detailed_widget)

                // 1. Set City Name In Left Corner
                val displayLabel = "জান্নাত সন্ধান • " + city.nameBn
                views.setTextViewText(R.id.detailed_widget_title, displayLabel)

                // 2. Weather Simulation In Top Right Box
                val calNow = Calendar.getInstance()
                val hourOfDay = calNow.get(Calendar.HOUR_OF_DAY)
                val (tempStr, conditionStr) = getSimulatedWeather(city.nameEn, hourOfDay)
                views.setTextViewText(R.id.detailed_widget_weather_temp, tempStr)
                views.setTextViewText(R.id.detailed_widget_weather_condition, conditionStr)

                // 3. Calculators
                val currentMins = hourOfDay * 60 + calNow.get(Calendar.MINUTE)

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

                // Special detailed times
                val tahajjudTime = calculateTahajjudTime(city.fajrTime)
                val tahajjudParts = tahajjudTime.split(":")
                val tahajjudMins = if (tahajjudParts.size == 2) tahajjudParts[0].toInt() * 60 + tahajjudParts[1].toInt() else 120

                val sahariTime = city.sahriTime
                val sahariParts = sahariTime.split(":")
                val sahariMins = if (sahariParts.size == 2) sahariParts[0].toInt() * 60 + sahariParts[1].toInt() else 235

                val sunriseTime = calculateSunriseForbidden(city.fajrTime)
                val sunriseMins = (fajrParts[0].toInt() + 1) * 60 + (fajrParts[1].toInt() + 20) % 60
                val sunriseEndMins = sunriseMins + 15

                val ishraqTime = addMinutesToTimeStr(sunriseTime, 25)
                val ishraqMins = sunriseMins + 25

                val chashtTime = addMinutesToTimeStr(sunriseTime, 120)
                val chashtMins = sunriseMins + 120

                val sunsetTime = addMinutesToTimeStr(city.maghribTime, -15)
                val sunsetForbiddenStartMins = sunsetMins - 15

                val zawalStartMins = dhuhrMins - 10

                // Set timings in the grid UI
                views.setTextViewText(R.id.detailed_tahajjud_time, formatWaqtTime(tahajjudTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_sahari_time, formatWaqtTime(sahariTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_fajr_time, formatWaqtTime(city.fajrTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_sunrise_time, formatWaqtTime(sunriseTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_ishraq_time, formatWaqtTime(ishraqTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_chasht_time, formatWaqtTime(chashtTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_dhuhr_time, formatWaqtTime(city.dhuhrTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_asr_time, formatWaqtTime(city.asrTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_sunset_time, formatWaqtTime(sunsetTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_maghrib_time, formatWaqtTime(city.maghribTime, use24HourFormat))
                views.setTextViewText(R.id.detailed_isha_time, formatWaqtTime(city.ishaTime, use24HourFormat))

                // Determine active waqt highlighting and countdowns
                var activeWaqt = "জোহর"
                var isForbiddenActive = false
                var countdownTitleValue = "⏳ পরবর্তী ওয়াক্তের বাকি সময়:"
                var countdownDetailsValue = ""

                when {
                    currentMins >= ishaMins || currentMins < tahajjudMins -> {
                        activeWaqt = "ইশা"
                        val diff = if (currentMins >= ishaMins) {
                            (1440 - currentMins) + tahajjudMins
                        } else {
                            tahajjudMins - currentMins
                        }
                        countdownTitleValue = "⏳ তাহাজ্জুদ শুরু হতে বাকি:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in tahajjudMins until sahariMins -> {
                        activeWaqt = "তাহাজ্জুদ"
                        val diff = sahariMins - currentMins
                        countdownTitleValue = "⏳ সাহরি শেষ হতে বাকি:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in sahariMins until fajrMins -> {
                        activeWaqt = "সাহরি"
                        val diff = fajrMins - currentMins
                        countdownTitleValue = "⏳ ফজর ওয়াক্ত শুরু হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in fajrMins until sunriseMins -> {
                        activeWaqt = "ফজর"
                        val diff = sunriseMins - currentMins
                        countdownTitleValue = "⏳ ফজর ওয়াক্তের বাকি সময়:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in sunriseMins until sunriseEndMins -> {
                        activeWaqt = "সূর্যোদয়"
                        isForbiddenActive = true
                        val diff = sunriseEndMins - currentMins
                        countdownTitleValue = "⚠️ নিষিদ্ধ সূর্যোদয় শেষ হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in sunriseEndMins until ishraqMins -> {
                        // Intermediate transition inside Ishraq
                        activeWaqt = "সূর্যোদয়"
                        val diff = ishraqMins - currentMins
                        countdownTitleValue = "⏳ ইশরাক শুরু হতে বাকি:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in ishraqMins until chashtMins -> {
                        activeWaqt = "ইশরাক"
                        val diff = chashtMins - currentMins
                        countdownTitleValue = "⏳ চাশত ওয়াক্ত শুরু হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in chashtMins until zawalStartMins -> {
                        activeWaqt = "চাশত"
                        val diff = dhuhrMins - currentMins
                        countdownTitleValue = "⏳ জোহর ওয়াক্ত শুরু হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in zawalStartMins until dhuhrMins -> {
                        activeWaqt = "জোহর"
                        isForbiddenActive = true
                        val diff = dhuhrMins - currentMins
                        countdownTitleValue = "⚠️ নিষিদ্ধ দ্বিপ্রহর শেষ হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in dhuhrMins until asrMins -> {
                        activeWaqt = "জোহর"
                        val diff = asrMins - currentMins
                        countdownTitleValue = "⏳ জোহর ওয়াক্তের বাকি সময়:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in asrMins until sunsetForbiddenStartMins -> {
                        activeWaqt = "আসর"
                        val diff = sunsetForbiddenStartMins - currentMins
                        countdownTitleValue = "⏳ আসর ওয়াক্তের বাকি সময়:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in sunsetForbiddenStartMins until sunsetMins -> {
                        activeWaqt = "সূর্যাস্ত"
                        isForbiddenActive = true
                        val diff = sunsetMins - currentMins
                        countdownTitleValue = "⚠️ নিষিদ্ধ সূর্যাস্ত শেষ হতে:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                    currentMins in sunsetMins until ishaMins -> {
                        activeWaqt = "মাগরিব"
                        val diff = ishaMins - currentMins
                        countdownTitleValue = "⏳ মাগরিব ওয়াক্তের বাকি সময়:"
                        countdownDetailsValue = formatRemainingText(diff)
                    }
                }

                // Reset original bg to all items
                views.setInt(R.id.detailed_tahajjud, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_sahari, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_fajr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_sunrise, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_ishraq, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_chasht, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_dhuhr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_asr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_sunset, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_maghrib, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                views.setInt(R.id.detailed_isha, "setBackgroundResource", R.drawable.widget_sub_card_bg)

                // Highlight correct item
                when (activeWaqt) {
                    "তাহাজ্জুদ" -> views.setInt(R.id.detailed_tahajjud, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "সাহরি" -> views.setInt(R.id.detailed_sahari, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "ফজর" -> views.setInt(R.id.detailed_fajr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "সূর্যোদয়" -> views.setInt(R.id.detailed_sunrise, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "ইশরাক" -> views.setInt(R.id.detailed_ishraq, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "চাশত" -> views.setInt(R.id.detailed_chasht, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "জোহর" -> views.setInt(R.id.detailed_dhuhr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "আসর" -> views.setInt(R.id.detailed_asr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "সূর্যাস্ত" -> views.setInt(R.id.detailed_sunset, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "মাগরিব" -> views.setInt(R.id.detailed_maghrib, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    "ইশা" -> views.setInt(R.id.detailed_isha, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                }

                // Apply text outputs
                views.setTextViewText(R.id.detailed_widget_countdown_title, countdownTitleValue)
                views.setTextViewText(R.id.detailed_widget_countdown_value, toBengaliDigits(countdownDetailsValue))

                // Calculate and apply upcoming/current forbidden details
                val (upcomingLabel, upcomingRange) = getUpcomingForbiddenText(
                    currentMins = currentMins,
                    fajrParts = fajrParts,
                    dhuhrMins = dhuhrMins,
                    sunsetMins = sunsetMins,
                    use24h = use24HourFormat
                )
                views.setTextViewText(R.id.detailed_widget_upcoming_label, upcomingLabel)
                views.setTextViewText(R.id.detailed_widget_upcoming_forbidden, toBengaliDigits(upcomingRange))

                if (isForbiddenActive) {
                    views.setTextViewText(R.id.detailed_widget_status_sub, "⚠️ নামাজ আদায় নিষিদ্ধ!")
                    views.setTextColor(R.id.detailed_widget_status_sub, 0xFFFF5252.toInt())
                    views.setInt(R.id.detailed_widget_forbidden_banner_box, "setBackgroundResource", R.drawable.widget_forbidden_bg)
                    views.setTextColor(R.id.detailed_widget_countdown_title, 0xFFFF8A80.toInt())
                } else {
                    views.setTextViewText(R.id.detailed_widget_status_sub, "পবিত্র $activeWaqt ওয়াক্ত চলমান")
                    views.setTextColor(R.id.detailed_widget_status_sub, 0xFF81C784.toInt())
                    views.setInt(R.id.detailed_widget_forbidden_banner_box, "setBackgroundResource", R.drawable.widget_sub_card_bg)
                    views.setTextColor(R.id.detailed_widget_countdown_title, 0xFFD4AF37.toInt())
                }

                // Click intent -> launches main app
                val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                if (launchIntent != null) {
                    val pendingIntent = android.app.PendingIntent.getActivity(
                        context, 0, launchIntent, 
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.detailed_widget_root, pendingIntent)
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
