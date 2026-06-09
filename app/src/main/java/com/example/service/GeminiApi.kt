package com.example.service

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.BuildConfig

// --- Moshi Data Classes for Gemini API ---

data class Content(
    @Json(name = "parts") val parts: List<Part>
)

data class InlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

data class Part(
    @Json(name = "text") val text: String?,
    @Json(name = "inlineData") val inlineData: InlineData? = null
)

data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>?
)

data class Candidate(
    @Json(name = "content") val content: Content?
)

// --- Retrofit API Service ---

interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    /**
     * Sends the prompt to the Gemini 3.5-flash model, fallback models if needed.
     * Extracts and returns the plain response text.
     */
    suspend fun askGemini(prompt: String, systemInstructionMessage: String = ""): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If API key is empty or placeholder, do a high quality simulation/local generation to ensure the UX is seamless.
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured. Falling back to local smart assistant handler.")
            return@withContext getLocalSmartReply(prompt)
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = if (systemInstructionMessage.isNotEmpty()) {
                Content(parts = listOf(Part(text = systemInstructionMessage)))
            } else {
                null
            }
        )

        val modelsToTry = listOf("gemini-3.5-flash", "gemini-2.5-flash")
        var lastException: Exception? = null

        for (model in modelsToTry) {
            try {
                Log.d(TAG, "Attempting text generation with model: $model")
                val response = service.generateContent(model, apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrEmpty()) {
                    return@withContext responseText
                }
            } catch (e: Exception) {
                Log.e(TAG, "Model $model failed: ${e.message}")
                lastException = e
            }
        }

        Log.e(TAG, "All generative models failed calling Gemini API.", lastException)
        return@withContext getLocalSmartReply(prompt, isError = true, originalError = lastException?.localizedMessage)
    }

    /**
     * Generates extremely detailed and helpful responses locally when offline,
     * when the key hasn't been set by the user, or when an error occurs.
     */
    private fun getLocalSmartReply(prompt: String, isError: Boolean = false, originalError: String? = null): String {
        val query = prompt.lowercase()
        val defaultPre = if (isError) {
            "[স্থানীয় অফলাইন মোড সক্রিয় - কানেকশনে ত্রুটি: $originalError]\n\n"
        } else {
            "[ডেমো মোড: এপিআই কী সেট নেই। সঠিক উত্তরের জন্য এআই স্টুডিওর সেক্রিটস প্যানেলে GEMINI_API_KEY যুক্ত করুন।]\n\n"
        }

        return when {
            query.contains("নামাজ") || query.contains("নামায") || query.contains("সালাত") || query.contains("prayer") -> {
                defaultPre + """
                    সালাত বা নামাজ ইসলামের দ্বিতীয় সবচেয়ে গুরুত্বপূর্ণ স্তম্ভ। কুরআন মাজিদে মহান আল্লাহ ইরশাদ করেছেন:
                    "নিশ্চয়ই সালাত বা নামাজ মুমিনদের ওপর নির্দিষ্ট সময়ে ফরয করা হয়েছে।" (সূরা আন-নিসা: ১০৩)
                    
                    নামাজের উপকারিতা স্বয়ংসম্পূর্ণ:
                    ১. এটি আত্মিক প্রশান্তি বৃদ্ধি করে এবং অশ্লীল ও মন্দ কাজ থেকে বিরত রাখে।
                    ২. আল্লাহর সাথে সরাসরি সম্পর্ক স্থাপন করায় ও মানুষের গুণাবলি সুন্দর করে।
                    ৩. নামাজের মাধ্যমে দৈনিক শৃঙ্খলা বজায় থাকে ও পাপ মোচন হয়।
                    
                    নিয়মিত নামাজ আদায় করুন এবং আপনার ইবাদতের সময় সূচী অ্যাপের হোম স্ক্রিনে অনুসরণ করুন।
                """.trimIndent()
            }
            query.contains("কোরআন") || query.contains("কুরআন") || query.contains("quran") -> {
                defaultPre + """
                    পবিত্র কুরআন মানবজাতির হেদায়েতের উদ্দেশ্যে নাযিলকৃত মহান আল্লাহর চূড়ান্ত বাণী। আল্লাহ তায়ালা বলেন:
                    "এটি সেই কিতাব, যাতে কোনো সন্দেহ নেই; এটি মুত্তাকীদের জন্য পথপ্রদর্শক।" (সূরা আল-বাকারাহ: ২)
                    
                    কুরআন পঠন ও অনুশীলনের নিয়মাবলী:
                    ১. কুরআন তিলাওয়াতের আগে ওযূ করা মুস্তাহাব এবং মনোযোগ সহকারে শোনা ওয়াজিব।
                    ২. তিলাওয়াতের পাশাপাশি অর্থ ও তাফসীর পড়া আবশ্যক। এতে আল্লাহ কি বলেছেন তা অন্তরে উপলব্ধি করা যায়।
                    ৩. প্রতিদিন অন্তত একটি আয়াত হলেও নিয়মিত তিলাওয়াতের অভ্যাস গড়ে তুলুন।
                    
                    আমাদের 'কুরআন' ফিচারে আপনি তাফসীর ও অনুবাদ লাভ করতে পারেন।
                """.trimIndent()
            }
            query.contains("রমজান") || query.contains("রোজা") || query.contains("fasting") || query.contains("ramadan") || query.contains("সিয়াম") -> {
                defaultPre + """
                    রমজান মাস মহান আল্লাহর অত্যন্ত বরকতময় মাস, যাতে কুরআন নাযিল হয়েছে। কুরআনে বর্ণিত আছে:
                    "হে মুমিনগণ! তোমাদের ওপর সিয়াম (রোজা) ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের ওপর; যেন তোমরা তাকওয়া অবলম্বন করতে পারো।" (সূরা আল-বাকারাহ: ১৮৩)
                    
                    রোজার তাৎপর্য:
                    - তাকওয়া (আল্লাহভীতি) অর্জন করাই সিয়ামের মূল লক্ষ্য।
                    - সংযত জীবন যাপন ও দরিদ্রদের ক্ষুধার যন্ত্রণা অনুভব শেখা।
                    - আত্মশুদ্ধি এবং আল্লাহর দরবারে ক্ষমা পাওয়ার অপূর্ব সুযোগ।
                    
                    আপনার রমজানের সেহরি-ইফতারের সময়সূচী ও আমল ট্র্যাকিং করুন আমাদের রমজান বিভাগ থেকে।
                """.trimIndent()
            }
            query.contains("যাকাত") || query.contains("zakat") || query.contains("যাকাত") -> {
                defaultPre + """
                    যাকাত ইসলামের অন্যতম একটি ফরজ রুকন। যাকাত মানে হচ্ছে বৃদ্ধি পাওয়া ও পবিত্র হওয়া।
                    "তাদের সম্পদ থেকে সদকা গ্রহণ করুন, যার মাধ্যমে আপনি তাদেরকে পবিত্র ও পরিশোধিত করবেন।" (সূরা আত-তাওবাহ: ১০৩)
                    
                    যাকাত প্রদানের নিয়ম:
                    ১. কোন মুসলিমের মালিকানায় সাড়ে সাত তোলা স্বর্ণ (৮৭.৪৫ গ্রাম) বা সাড়ে বায়ান্ন তোলা রূপা (৬১২.১৫ গ্রাম) অথবা তার সমমূল্যের নগদ অর্থ/ব্যবসার সম্পদ ১ বছর থাকলে যাকাত ফরজ হয়।
                    ২. সাড়ে বায়ান্ন তোলা রূপার মূল্যকে নিসাব ধরে যাকাত গণনা করা উত্তম ও দরিদ্রবান্ধব।
                    ৩. যাকাত হিসাবের হার হচ্ছে মোট অতিরিক্ত সম্পদের ২.৫% বা ১/৪০ অংশ।
                    
                    আপনার হিসাব সহজে করতে আমাদের অ্যাপের 'যাকাত ক্যালকুলেটর' ব্যবহার করতে পারেন।
                """.trimIndent()
            }
            query.contains("দোয়া") || query.contains("দুয়া") || query.contains("dua") -> {
                defaultPre + """
                    দোয়া মুমিনের সবচেয়ে বড় অস্ত্র ও অন্যতম ইবাদত। আল্লাহর রাসূল (সা.) বলেছেন, "দোয়া-ই হলো ইবাদাত।"
                    আল্লাহ বলেন, "তোমরা আমাকে ডাকো, আমি তোমাদের ডাকে সাড়া দেবো।" (সূরা গাফির: ৬০)
                    
                    দোয়া কবুলের কিছু প্রধান শর্ত ও সময়:
                    - হারাম উপার্জন ও খাদ্য বর্জন করা।
                    - সেজদার সময়, শেষ শেষ রাতে এবং আজান ও ইকামতের মধ্যবর্তী সময়ে দোয়া করা।
                    - আল্লাহর প্রশংসা ও দরূদ পাঠের মাধ্যমে দোয়া শুরু করা।
                    
                    আমাদের 'দুয়া ও জিকির' বিভাগে সকাল-সন্ধ্যা ও হিজবুল মুসলিমের দৈনন্দিন দোয়া রয়েছে।
                """.trimIndent()
            }
            else -> {
                defaultPre + """
                    আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ! 
                    আমি জান্নাত সন্ধান এআই সহকারী (Jannat Sondhan AI Assistant)। আপনার যেকোনো ইসলামিক প্রশ্ন যেমন নামাজ, রোজা, যাকাত, দোয়া, বা কুরআন বিষয়ক জিজ্ঞাসা এখানে করতে পারেন।
                    
                    আপনি যদি বাংলায় বা ইংরেজিতে প্রশ্ন লেখেন, আমি তার সঠিক কুরআন এবং হাদিস ভিত্তিক সমাধান প্রদান করব।
                    
                    💡 পরামর্শ: এআই স্টুডিওর ডানদিকের সেক্রিটস প্যানেলে 'GEMINI_API_KEY' যুক্ত করলে আমি সরাসরি ইন্টারনেটের মাধ্যমে রিয়েল-টাইমে জেনারেটিভ এআই ব্যবহার করে উত্তর প্রদান করব!
                """.trimIndent()
            }
        }
    }

    /**
     * Translates Arabic text in a captured image into a target language.
     */
    suspend fun translateArabicImage(base64Image: String, targetLanguage: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key is not configured for image translation. Falling back to mock handler.")
            return@withContext getMockImageTranslation(targetLanguage)
        }

        val prompt = """
            Analyze the provided image. It contains Arabic text (could be from the Quran, a book, sign, calligraphy, or handwriting).
            1. Transcribe the Arabic text exactly as written.
            2. Translate this Arabic text into the target language: $targetLanguage.
            3. Provide a brief tafsir/explanation or context of what this text/verse means.
            
            Please layout the response beautifully in clean Markdown with sections:
            ### 📜 Transcription (আরবি পাঠ্য):
            [Arabic text]
            
            ### 📝 Translation (অনুবাদ):
            [Translated text in $targetLanguage]
            
            ### 💡 Explanation / Context (তাফসীর ও ব্যাখ্যা):
            [Context or verse details in $targetLanguage]
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64Image), text = null)
                    )
                )
            ),
            systemInstruction = Content(parts = listOf(Part(text = "You are a professional Arabic translation engine and Islamic scholar.")))
        )

        val modelsToTry = listOf("gemini-3.5-flash", "gemini-2.5-flash")
        var lastException: Exception? = null

        for (model in modelsToTry) {
            try {
                Log.d(TAG, "Attempting image translation with model: $model")
                val response = service.generateContent(model, apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!responseText.isNullOrEmpty()) {
                    return@withContext responseText
                }
            } catch (e: Exception) {
                Log.e(TAG, "Model $model translation failed: ${e.message}")
                lastException = e
            }
        }

        Log.e(TAG, "All translation models failed calling Gemini API.", lastException)
        return@withContext getMockImageTranslation(targetLanguage, isError = true, errorMsg = lastException?.localizedMessage)
    }

    private fun getMockImageTranslation(targetLanguage: String, isError: Boolean = false, errorMsg: String? = null): String {
        val prefix = if (isError) {
            "[স্থানীয় অনুবাদ মোড - কানেকশনে ত্রুটি: $errorMsg]\n\n"
        } else {
            "[ডেমো অনুবাদ মোড: এপিআই কী সেট নেই। সঠিক অনুবাদের জন্য এআই স্টুডিওর সেক্রিটস প্যানেলে GEMINI_API_KEY যুক্ত করুন।]\n\n"
        }
        
        val isBn = targetLanguage.lowercase() == "bengali" || targetLanguage.lowercase() == "bangla"
        
        return if (isBn) {
            prefix + """
                ### 📜 Transcription (আরবি পাঠ্য):
                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝١ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝٢
                
                ### 📝 Translation (অনুবাদ):
                ১. পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি।
                ২. সমস্ত প্রশংসা সৃষ্টির পালনকর্তা আল্লাহর জন্য।
                
                ### 💡 Explanation / Context (তাফসীর ও ব্যাখ্যা):
                এটি অত্যন্ত পবিত্র সূরা আল-ফাতিহা-এর প্রথম দুটি আয়াত। এই সূরাটি কুরআনের শুরু এবং নামাজের প্রতি রাকাতে এটি আবৃত্তি করা বাধ্যতামূলক। এখানে আল্লাহ তায়ালা নিজের প্রশংসা ও দয়াশীলতার বহিঃপ্রকাশ করেছেন।
            """.trimIndent()
        } else {
            prefix + """
                ### 📜 Transcription (Arabic Text):
                بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ۝١ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝٢
                
                ### 📝 Translation (Translation):
                1. In the name of Allah, the Entirely Merciful, the Especially Merciful.
                2. [All] praise is [due] to Allah, Lord of the worlds.
                
                ### 💡 Explanation / Context:
                This is Surah Al-Fatihah, verses 1 and 2. It is the opening chapter of the Holy Quran, highlighting Allah's Mercy, Compassion, and Sovereignty over the universe.
            """.trimIndent()
        }
    }
}
