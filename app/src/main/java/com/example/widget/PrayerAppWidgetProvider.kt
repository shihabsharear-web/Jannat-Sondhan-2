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
import java.text.SimpleDateFormat

class PrayerAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // If we receive a custom update action or standard check
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE || 
            intent.action == "com.example.action.PRAYER_WIDGET_REFRESH") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, PrayerAppWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            onUpdate(context, appWidgetManager, appWidgetIds)
        }
    }

    companion object {
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

        private fun formatTo12Hour(time24: String): String {
            return try {
                val parts = time24.split(":")
                val h = parts[0].toInt()
                val m = parts[1].toInt()
                val ampm = if (h >= 12) "PM" else "AM"
                val h12 = if (h % 12 == 0) 12 else h % 12
                String.format("%02d:%02d %s", h12, m, ampm)
            } catch (e: Exception) {
                time24
            }
        }

        // Static fallback cities matching the app Exactly
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
                // Clean the suffix " , বাংলাদেশ" if any to keep widget titles compact
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
            val city = getSelectedCity(context)
            val views = RemoteViews(context.packageName, R.layout.ibadah_prayer_widget)

            // 1. Set City Name In Left Corner
            val displayLabel = "জান্নাতর সন্ধান • " + city.nameBn
            views.setTextViewText(R.id.widget_title, displayLabel)

            // 2. Calculations for Sunrise / Sunset
            val calNow = Calendar.getInstance()
            val currentMins = calNow.get(Calendar.HOUR_OF_DAY) * 60 + calNow.get(Calendar.MINUTE)

            // Setup Sunrise
            val fajrParts = city.fajrTime.split(":")
            val sunriseHour = (fajrParts[0].toInt() + 1)
            val sunriseMin = (fajrParts[1].toInt() + 20) % 60
            val sunriseStr = String.format("%02d:%02d", sunriseHour, sunriseMin)
            val sunriseMins = sunriseHour * 60 + sunriseMin

            // Setup Sunset
            val sunsetStr = city.maghribTime
            val sunsetParts = sunsetStr.split(":")
            val sunsetMins = if (sunsetParts.size == 2) sunsetParts[0].toInt() * 60 + sunsetParts[1].toInt() else 1110

            // sequential cycle "একটা শেষ হলে আরেকটা এখানে থাকবে"
            val isPastSunset = currentMins >= sunsetMins
            val isBeforeSunrise = currentMins < sunriseMins
            val showSunriseCorner = isBeforeSunrise || isPastSunset

            if (showSunriseCorner) {
                views.setTextViewText(R.id.widget_cycle_label, "সূর্যোদয়")
                views.setTextViewText(R.id.widget_cycle_time, toBengaliDigits(formatTo12Hour(sunriseStr)))
            } else {
                views.setTextViewText(R.id.widget_cycle_label, "সূর্যাস্ত")
                views.setTextViewText(R.id.widget_cycle_time, toBengaliDigits(formatTo12Hour(sunsetStr)))
            }

            // 3. Set standard five waqt timings
            views.setTextViewText(R.id.fajr_time, toBengaliDigits(city.fajrTime))
            views.setTextViewText(R.id.dhuhr_time, toBengaliDigits(city.dhuhrTime))
            views.setTextViewText(R.id.asr_time, toBengaliDigits(city.asrTime))
            views.setTextViewText(R.id.maghrib_time, toBengaliDigits(city.maghribTime))
            views.setTextViewText(R.id.isha_time, toBengaliDigits(city.ishaTime))

            // Highlight current active Waqt
            // Parse all timings to compute current active block
            val dhuhrParts = city.dhuhrTime.split(":")
            val dhuhrMins = if (dhuhrParts.size == 2) dhuhrParts[0].toInt() * 60 + dhuhrParts[1].toInt() else 720

            val asrParts = city.asrTime.split(":")
            val asrMins = if (asrParts.size == 2) asrParts[0].toInt() * 60 + asrParts[1].toInt() else 945

            val ishaParts = city.ishaTime.split(":")
            val ishaMins = if (ishaParts.size == 2) ishaParts[0].toInt() * 60 + ishaParts[1].toInt() else 1200

            val fajrMins = if (fajrParts.size == 2) fajrParts[0].toInt() * 60 + fajrParts[1].toInt() else 260

            var currentActiveWaqt = "ফজর"
            val isFajr = currentMins in fajrMins until dhuhrMins
            val isDhuhr = currentMins in dhuhrMins until asrMins
            val isAsr = currentMins in asrMins until sunsetMins
            val isMaghrib = currentMins in sunsetMins until ishaMins
            val isIsha = currentMins >= ishaMins || currentMins < fajrMins

            // Reset backgrounds and highlighting
            views.setInt(R.id.widget_waqt_fajr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
            views.setInt(R.id.widget_waqt_dhuhr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
            views.setInt(R.id.widget_waqt_asr, "setBackgroundResource", R.drawable.widget_sub_card_bg)
            views.setInt(R.id.widget_waqt_maghrib, "setBackgroundResource", R.drawable.widget_sub_card_bg)
            views.setInt(R.id.widget_waqt_isha, "setBackgroundResource", R.drawable.widget_sub_card_bg)

            when {
                isFajr -> {
                    views.setInt(R.id.widget_waqt_fajr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    currentActiveWaqt = "ফজর"
                }
                isDhuhr -> {
                    views.setInt(R.id.widget_waqt_dhuhr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    currentActiveWaqt = "যোহর"
                }
                isAsr -> {
                    views.setInt(R.id.widget_waqt_asr, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    currentActiveWaqt = "আসর"
                }
                isMaghrib -> {
                    views.setInt(R.id.widget_waqt_maghrib, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    currentActiveWaqt = "মাগরিব"
                }
                isIsha -> {
                    views.setInt(R.id.widget_waqt_isha, "setBackgroundResource", R.drawable.widget_sub_card_bg_active)
                    currentActiveWaqt = "এশা"
                }
            }

            // 4. Forbidden Times calculation
            // Sunrise window: sunrise - (sunrise + 15 mins)
            val isSunriseForbidden = currentMins >= sunriseMins && currentMins < (sunriseMins + 15)
            // Zawal window: (dhuhr - 10) - dhuhr
            val isZawalForbidden = currentMins >= (dhuhrMins - 10) && currentMins < dhuhrMins
            // Sunset window: (sunset - 15) - sunset
            val isSunsetForbidden = currentMins >= (sunsetMins - 15) && currentMins < sunsetMins

            val isAnyForbiddenActive = isSunriseForbidden || isZawalForbidden || isSunsetForbidden

            if (isAnyForbiddenActive) {
                views.setTextViewText(R.id.widget_status_sub, "⚠️ নামাজ আদায় নিষিদ্ধ!")
                views.setTextColor(R.id.widget_status_sub, 0xFFFF5252.toInt())
            } else {
                views.setTextViewText(R.id.widget_status_sub, "পবিত্র $currentActiveWaqt ওয়াক্ত চলমান")
                views.setTextColor(R.id.widget_status_sub, 0xFF81C784.toInt())
            }

            // Set Forbidden Details content
            val fSunStart = sunriseStr
            val fSunEnd = String.format("%02d:%02d", sunriseHour, (sunriseMin + 15) % 60)
            val fZawStart = String.format("%02d:%02d", if (dhuhrParts[1].toInt() >= 10) dhuhrParts[0].toInt() else (dhuhrParts[0].toInt() - 1 + 24) % 24, (dhuhrParts[1].toInt() - 10 + 60) % 60)
            val fZawEnd = city.dhuhrTime
            val fSetStart = String.format("%02d:%02d", if (sunsetParts[1].toInt() >= 15) sunsetParts[0].toInt() else (sunsetParts[0].toInt() - 1 + 24) % 24, (sunsetParts[1].toInt() - 15 + 60) % 60)
            val fSetEnd = city.maghribTime

            val dispFForbidden = String.format(
                "সূর্যোদয়: %s-%s  |  দ্বিপ্রহর: %s-%s  |  সূর্যাস্ত: %s-%s",
                fSunStart, fSunEnd, fZawStart, fZawEnd, fSetStart, fSetEnd
            )
            views.setTextViewText(R.id.widget_forbidden_details, toBengaliDigits(dispFForbidden))

            // Single click intent: launches the main app activity
            val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            if (launchIntent != null) {
                val pendingIntent = android.app.PendingIntent.getActivity(
                    context, 0, launchIntent, 
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
