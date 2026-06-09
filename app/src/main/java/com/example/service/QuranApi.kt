package com.example.service

import com.example.data.Verse
import com.example.data.Surah
import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// API models
data class QuranResponse(
    @Json(name = "code") val code: Int,
    @Json(name = "status") val status: String,
    @Json(name = "data") val data: List<SurahEdition>?
)

data class SurahEdition(
    @Json(name = "number") val number: Int,
    @Json(name = "name") val name: String,
    @Json(name = "englishName") val englishName: String,
    @Json(name = "ayahs") val ayahs: List<Ayah>?
)

data class Ayah(
    @Json(name = "numberInSurah") val numberInSurah: Int,
    @Json(name = "text") val text: String
)

interface QuranApiService {
    @GET("v1/surah/{number}/editions/quran-uthmani,bn.bengali,en.sahih")
    suspend fun getFullSurah(
        @Path("number") surahNumber: Int
    ): QuranResponse
}

object QuranApiClient {
    private const val TAG = "QuranApiClient"
    private const val BASE_URL = "https://api.alquran.cloud/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val service: QuranApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(QuranApiService::class.java)
    }

    suspend fun fetchFullSurah(surahNumber: Int): List<Verse>? = withContext(Dispatchers.IO) {
        try {
            val response = service.getFullSurah(surahNumber)
            if (response.code == 200 && response.data != null && response.data.size >= 3) {
                val arabicSurah = response.data[0]
                val bengaliSurah = response.data[1]
                val englishSurah = response.data[2]

                val arabicAyahs = arabicSurah.ayahs ?: return@withContext null
                val bengaliAyahs = bengaliSurah.ayahs ?: return@withContext null
                val englishAyahs = englishSurah.ayahs ?: return@withContext null

                val minSize = minOf(arabicAyahs.size, bengaliAyahs.size, englishAyahs.size)
                val fullList = mutableListOf<Verse>()

                for (i in 0 until minSize) {
                    val ar = arabicAyahs[i]
                    val bn = bengaliAyahs[i]
                    val en = englishAyahs[i]

                    // Remove standard Bismillah prefix from non-first verses/Fatihah
                    var cleanArText = ar.text
                    if (surahNumber != 1 && ar.numberInSurah == 1 && cleanArText.startsWith("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")) {
                        cleanArText = cleanArText.substring("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ".length).trim()
                        if (cleanArText.isEmpty()) {
                            // If it only contained Bismillah and nothing else, keep it
                            cleanArText = ar.text
                        }
                    }

                    fullList.add(
                        Verse(
                            number = ar.numberInSurah,
                            textArabic = cleanArText,
                            textBengali = bn.text,
                            textEnglish = en.text,
                            tafsirBengali = "মহান আল্লাহর বাণী। আয়াতটির মাধ্যমে হিদায়াত ও রহমত লাভ সম্ভব।"
                        )
                    )
                }
                return@withContext fullList
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to download full surah: ${e.message}", e)
        }
        return@withContext null
    }
}
