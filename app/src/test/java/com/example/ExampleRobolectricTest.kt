package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.service.DailymotionApiClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("জান্নাত সন্ধান", appName)
  }

  @Test
  fun testDailymotionApiSearch() = runTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    println("--- START DAILYMOTION API TEST ---")
    try {
        val results = DailymotionApiClient.searchVideos(context, "waz", "waz", false)
        println("RESULTS SIZE: ${results.size}")
        results.forEach {
            println("VIDEO: id=${it.id}, title=${it.title}, dur=${it.durationString}, thumbnail=${it.thumbnail}")
        }
        assertNotNull(results)
    } catch (e: Exception) {
        println("ERROR CALLING API:")
        e.printStackTrace()
        throw e
    }
  }
}
