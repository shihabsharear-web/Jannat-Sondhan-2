package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.ComponentName
import android.widget.RemoteViews
import com.example.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class IbadahDashboardWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        
        if (action == "com.example.action.TOGGLE_DEED") {
            val deedId = intent.getStringExtra("deed_id")
            if (deedId != null) {
                try {
                    val prefs = context.getSharedPreferences("jannat_deeds_prefs", Context.MODE_PRIVATE)
                    val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                    
                    val currentSet = (prefs.getStringSet("deeds_$dateStr", emptySet()) ?: emptySet()).toMutableSet()
                    
                    if (currentSet.contains(deedId)) {
                        currentSet.remove(deedId)
                    } else {
                        currentSet.add(deedId)
                    }
                    
                    // Save the updated deeds back
                    prefs.edit().putStringSet("deeds_$dateStr", currentSet).apply()
                    
                    // Refresh all widgets immediately
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val thisWidget = ComponentName(context, IbadahDashboardWidgetProvider::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                    onUpdate(context, appWidgetManager, appWidgetIds)
                    
                    // Also trigger broadcast to let any active app viewmodels or other components know
                    val refreshIntent = Intent("com.example.action.IBADAH_DEEDS_DATA_CHANGED")
                    context.sendBroadcast(refreshIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE || 
                   action == "com.example.action.IBADAH_DASHBOARD_WIDGET_REFRESH") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, IbadahDashboardWidgetProvider::class.java)
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
            return result
        }

        private fun getBengaliDateLabel(): String {
            try {
                val cal = Calendar.getInstance()
                val day = cal.get(Calendar.DAY_OF_MONTH)
                val year = cal.get(Calendar.YEAR)
                
                val monthsBn = listOf(
                    "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
                    "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
                )
                val monthStrBn = monthsBn[cal.get(Calendar.MONTH)]
                
                return "${toBengaliDigits(day.toString())} $monthStrBn, ${toBengaliDigits(year.toString())}"
            } catch (e: Exception) {
                return "১১ জুন, ২০২৬"
            }
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            try {
                val views = RemoteViews(context.packageName, R.layout.ibadah_dashboard_widget)
                
                // 1. Set Date label
                views.setTextViewText(R.id.widget_date_text, getBengaliDateLabel())
                
                // 2. Fetch current completions for today
                val prefs = context.getSharedPreferences("jannat_deeds_prefs", Context.MODE_PRIVATE)
                val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val completedSet = prefs.getStringSet("deeds_$dateStr", emptySet()) ?: emptySet()
                
                // Count how many of d1 to d8 are completed
                val deedsTracked = listOf("d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8")
                var completedCount = 0
                for (deedId in deedsTracked) {
                    if (completedSet.contains(deedId)) {
                        completedCount++
                    }
                }
                
                // Calculate percentage
                val progressPercent = (completedCount * 100) / deedsTracked.size
                
                // 3. Apply progress configurations
                views.setProgressBar(R.id.widget_progress_bar, deedsTracked.size, completedCount, false)
                
                val progressText = "${toBengaliDigits(completedCount.toString())}/${toBengaliDigits(deedsTracked.size.toString())} সম্পন্ন (${toBengaliDigits(progressPercent.toString())}%)"
                views.setTextViewText(R.id.widget_progress_text, progressText)
                
                // 4. Update the individual rows in layout
                val rowIds = listOf(
                    R.id.deed_row_1, R.id.deed_row_2, R.id.deed_row_3, R.id.deed_row_4,
                    R.id.deed_row_5, R.id.deed_row_6, R.id.deed_row_7, R.id.deed_row_8
                )
                val checkIds = listOf(
                    R.id.deed_check_1, R.id.deed_check_2, R.id.deed_check_3, R.id.deed_check_4,
                    R.id.deed_check_5, R.id.deed_check_6, R.id.deed_check_7, R.id.deed_check_8
                )
                
                for (i in deedsTracked.indices) {
                    val deedId = deedsTracked[i]
                    val isChecked = completedSet.contains(deedId)
                    
                    // Toggle check indicator symbol
                    val checkSymbol = if (isChecked) "✅" else "⚪"
                    views.setTextViewText(checkIds[i], checkSymbol)
                    
                    // Highlight the row visually on checked
                    val bgRes = if (isChecked) R.drawable.widget_sub_card_bg_active else R.drawable.widget_sub_card_bg
                    views.setInt(rowIds[i], "setBackgroundResource", bgRes)
                    
                    // Setup broadcast PendingIntent for click toggle
                    val clickIntent = Intent(context, IbadahDashboardWidgetProvider::class.java).apply {
                        action = "com.example.action.TOGGLE_DEED"
                        putExtra("deed_id", deedId)
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        2001 + i,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(rowIds[i], pendingIntent)
                }
                
                // 5. App Click Intent to Launch Core App
                val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                if (launchIntent != null) {
                    val corePendingIntent = PendingIntent.getActivity(
                        context,
                        2000,
                        launchIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.dashboard_widget_root, corePendingIntent)
                }
                
                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
