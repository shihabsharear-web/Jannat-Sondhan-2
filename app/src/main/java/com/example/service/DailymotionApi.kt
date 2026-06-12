package com.example.service

import android.content.Context
import android.util.Log
import com.example.data.DailymotionVideo
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class DailymotionRawVideo(
    @Json(name = "id") val id: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "thumbnail_360_url") val thumbnail360Url: String? = null,
    @Json(name = "owner.screenname") val channelName: String? = null,
    @Json(name = "duration") val duration: Int? = null
)

data class DailymotionRawResponse(
    @Json(name = "list") val list: List<DailymotionRawVideo>? = null,
    @Json(name = "has_more") val hasMore: Boolean? = null
)

interface DailymotionApiService {
    @GET("videos")
    suspend fun searchVideos(
        @Query("search") search: String,
        @Query("fields") fields: String = "id,title,thumbnail_360_url,owner.screenname,duration",
        @Query("limit") limit: Int = 20,
        @Query("page") page: Int = 1
    ): DailymotionRawResponse

    @GET("video/{id}/related")
    suspend fun getRelatedVideos(
        @Path("id") videoId: String,
        @Query("fields") fields: String = "id,title,thumbnail_360_url,owner.screenname,duration",
        @Query("limit") limit: Int = 10
    ): DailymotionRawResponse
}

object DailymotionApiClient {
    private const val TAG = "DailymotionApiClient"
    private const val BASE_URL = "https://api.dailymotion.com/"
    private const val CACHE_PREFS_NAME = "ibadah_dailymotion_cache"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private const val API_KEY = "6f339b7f5f0df9e3e36e"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // To support multiple possible custom keys, pass in all formats
            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("access_token", API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .header("Authorization", "Bearer $API_KEY")
                .header("x-api-key", API_KEY)
                .header("api-key", API_KEY)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    private val service: DailymotionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DailymotionApiService::class.java)
    }

    private fun formatDuration(seconds: Int?): String {
        if (seconds == null || seconds <= 0) return "00:00"
        val m = seconds / 60
        val s = seconds % 60
        val h = m / 60
        val remM = m % 60
        return if (h > 0) {
            String.format("%d:%02d:%02d", h, remM, s)
        } else {
            String.format("%02d:%02d", m, s)
        }
    }

    suspend fun searchVideos(context: Context, query: String, category: String = "all", isIslamicFilter: Boolean = false): List<DailymotionVideo> {
        val finalQuery = buildFinalQuery(query, category, isIslamicFilter)
        
        // Try to get from cache first
        val cached = getFromCache(context, finalQuery)
        if (cached != null) {
            Log.d(TAG, "Search results returned from cache for: $finalQuery")
            return cached
        }

        return try {
            val response = service.searchVideos(finalQuery)
            val converted = response.list?.mapNotNull { raw ->
                val id = raw.id ?: return@mapNotNull null
                val title = raw.title ?: "No Title"
                DailymotionVideo(
                    id = id,
                    title = title,
                    channelName = raw.channelName ?: "Dailymotion Channel",
                    thumbnail = raw.thumbnail360Url ?: "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?auto=format&fit=crop&q=80&w=400",
                    durationString = formatDuration(raw.duration),
                    durationSeconds = raw.duration ?: 0
                )
            } ?: emptyList()
            
            if (converted.isNotEmpty()) {
                saveToCache(context, finalQuery, converted)
            }
            converted
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching from Dailymotion api", e)
            emptyList()
        }
    }

    suspend fun getRelatedVideos(videoId: String): List<DailymotionVideo> {
        return try {
            val response = service.getRelatedVideos(videoId)
            response.list?.mapNotNull { raw ->
                val id = raw.id ?: return@mapNotNull null
                DailymotionVideo(
                    id = id,
                    title = raw.title ?: "Untitled",
                    channelName = raw.channelName ?: "Dailymotion Channel",
                    thumbnail = raw.thumbnail360Url ?: "https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?auto=format&fit=crop&q=80&w=400",
                    durationString = formatDuration(raw.duration),
                    durationSeconds = raw.duration ?: 0
                )
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting related Dailymotion videos", e)
            emptyList()
        }
    }

    private fun buildFinalQuery(query: String, category: String, isIslamicFilter: Boolean): String {
        val parts = mutableListOf<String>()
        if (query.isNotBlank()) {
            parts.add(query)
        }
        
        if (isIslamicFilter) {
            when (category) {
                "waz" -> {
                    parts.add("islamic waz bangla")
                }
                "quran" -> {
                    parts.add("quran recitation soothing")
                }
                "nasheed" -> {
                    parts.add("beautiful nasheed gojal")
                }
                "dua" -> {
                    parts.add("duas azkar prayer")
                }
                else -> {
                    parts.add("islamic")
                }
            }
        } else {
            when (category) {
                "waz" -> parts.add("waz bn")
                "quran" -> parts.add("recitation surah")
                "nasheed" -> parts.add("nasheed gojal")
                "dua" -> parts.add("dua islamic")
            }
        }
        
        return parts.distinct().joinToString(" ").trim()
    }

    private fun saveToCache(context: Context, query: String, videos: List<DailymotionVideo>) {
        try {
            val prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE)
            val listType = Types.newParameterizedType(List::class.java, DailymotionVideo::class.java)
            val adapter = moshi.adapter<List<DailymotionVideo>>(listType)
            val json = adapter.toJson(videos)
            prefs.edit().putString(query.trim().lowercase(), json).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error caching results", e)
        }
    }

    private fun getFromCache(context: Context, query: String): List<DailymotionVideo>? {
        return try {
            val prefs = context.getSharedPreferences(CACHE_PREFS_NAME, Context.MODE_PRIVATE)
            val json = prefs.getString(query.trim().lowercase(), null)
            if (!json.isNullOrEmpty()) {
                val listType = Types.newParameterizedType(List::class.java, DailymotionVideo::class.java)
                val adapter = moshi.adapter<List<DailymotionVideo>>(listType)
                adapter.fromJson(json)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
