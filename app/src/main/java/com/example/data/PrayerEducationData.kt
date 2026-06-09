package com.example.data

data class PrayerStep(
    val id: Int,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val arabicText: String = "",
    val transliterationBn: String = "",
    val transliterationEn: String = "",
    val translationBn: String = "",
    val translationEn: String = "",
    val postureTipBn: String = "",
    val postureTipEn: String = ""
)

data class WuduStep(
    val id: Int,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val illustrationLabel: String
)

data class PostureComparison(
    val id: Int,
    val postureNameBn: String,
    val postureNameEn: String,
    val correctTitleBn: String,
    val correctTitleEn: String,
    val correctDetailsBn: List<String>,
    val correctDetailsEn: List<String>,
    val wrongTitleBn: String,
    val wrongTitleEn: String,
    val wrongDetailsBn: List<String>,
    val wrongDetailsEn: List<String>,
    val diagramType: String
)

data class PrayerSura(
    val id: Int,
    val nameBn: String,
    val nameEn: String,
    val sNameArabic: String,
    val classificationBn: String,
    val classificationEn: String,
    val totalVerses: Int,
    val arabicText: String,
    val transliterationBn: String,
    val transliterationEn: String,
    val translationBn: String,
    val translationEn: String
)

data class RakatStructure(
    val timeBn: String,
    val timeEn: String,
    val sunnahBefore: Int,
    val fard: Int,
    val sunnahAfter: Int,
    val nafl: Int,
    val witr: Int,
    val totalRakat: Int
)

object PrayerEducationData {

    val steps = listOf(
        PrayerStep(
            id = 1,
            nameBn = "১. নিয়ত ও তাকবিরে তাহরিমা (Intention & Opening)",
            nameEn = "1. Intention & Takbeer-e-Tahreema",
            descriptionBn = "ক্বিবলামুখী হয়ে সোজা হয়ে দাঁড়ান। নিয়ত মনে মনে স্থির করুন। পুরুষগণ দুই হাত কান পর্যন্ত এবং মহিলারা কাঁধ পর্যন্ত উঠিয়ে বলবেন: 'আল্লাহু আকবার'।",
            descriptionEn = "Stand straight facing the Ka'bah (Qibla). Focus your heart and mind on the prayer you are about to perform. Raise both hands to ear lobes (men) or chest/shoulders (women) and say 'Allahu Akbar'.",
            arabicText = "اللَّهُ أَكْبَرُ",
            transliterationBn = "আল্লাহু আকবার",
            transliterationEn = "Allahu Akbar",
            translationBn = "আল্লাহ সবচেয়ে মহান ও বড়।",
            translationEn = "Allah is the Greatest.",
            postureTipBn = "হাত বাঁধার পূর্বে আপনার দৃষ্টি সিজদার জায়গায় রাখুন।",
            postureTipEn = "Keep your eyes fixed on the spot where your head will touch the ground during prostration (Sajdah)."
        ),
        PrayerStep(
            id = 2,
            nameBn = "২. ছানা পাঠ (Recitation of Sana)",
            nameEn = "2. Recitation of Sanaa",
            descriptionBn = "হাত বাঁধার পর প্রথম রাকাতে আল্লাহ তাআলার প্রশংসা স্বরূপ ছানা বা সানা পাঠ করতে হয়। পুরুষরা নাভির নিচে হাত বাঁধবেন (ডান হাত বাম হাতের ওপর), মহিলারা বুকের ওপর হাত বাঁধবেন।",
            descriptionEn = "Fold your hands on your abdomen/below the navel (men) or over the chest (women) with your right hand over your left. Then, recite the opening supplication (Sanaa) silently.",
            arabicText = "سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ وَتَبَارَكَ اسْمُكَ وَتَعَالَى جَدُّكَ وَلَا إِلَهَ غَيْرُكَ",
            transliterationBn = "সুবহানাকা আল্লাহুম্মা ওয়া বিহামদিকা, ওয়া তাবারাকাসমুকা ওয়া তা'আলা জাদ্দুকা, ওয়া লা ইলাহা গাইরুকা।",
            transliterationEn = "Subhanaka Allahumma wa bihamdika, wa tabarakasmuka wa ta'ala jadduka, wa la ilaha ghayruk.",
            translationBn = "হে আল্লাহ! আমি তোমার সপ্রশংস পবিত্রতা ঘোষণা করছি। তোমার নাম কল্যাণময় এবং তোমার মর্যাদা অতি উচ্চে এবং তুমি ছাড়া অন্য কোনো উপাস্য নেই।",
            translationEn = "Glory be to You, O Allah, and all praise. Blessed is Your name, and exalted is Your majesty. There is no deity worthy of worship besides You.",
            postureTipBn = "ছানা শুধুমাত্র প্রথম রাকাতে তাকবিরে তাহরিমার পর পড়তে হয়।",
            postureTipEn = "Recitation of Sana is only done in the very first unit (Rak'ah) of prayer, right after the opening Takbeer."
        ),
        PrayerStep(
            id = 3,
            nameBn = "৩. আউযুবিল্লাহ, বিসমিল্লাহ ও সূরা ফাতিহা (Al-Fatihah)",
            nameEn = "3. Seek Refuge & Recite Surah Al-Fatihah",
            descriptionBn = "সানা পাঠ করার পর আউযুবিল্লাহ ও বিসমিল্লাহ পড়ে সূরা আল-ফাতিহা তিলাওয়াত করুন এবং শেষে মনে মনে বলুন 'আমীন'।",
            descriptionEn = "Seek refuge in Allah by reciting Ta'awwudh & Basmalah silently, then recite Surah Al-Fatihah. Say 'Ameen' softly at the end.",
            arabicText = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ . بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ . الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ . الرَّحْمَنِ الرَّحِيمِ . مَالِكِ يَوْمِ الدِّينِ . إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ . اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ . صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            transliterationBn = "আউযুবিল্লাহি মিনাশ শাইত্বনির রাজীম। বিসমিল্লাহির রহমানির রহীম। আলহামদুলিল্লাহি রাব্বিল আলামীন। আর-রাহমানির রাহীম। মালিকি ইয়াওমিদ্দীন। ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাস্তায়ীন। ইহদিনাস সিরাতাল মুস্তাকীম। সিরাতাল্লাযীনা আনআমতা আলাইহিম গাইরিল মাগদুবী আলাইহিম ওয়ালাদ্দাল্লীন।",
            transliterationEn = "A'udhu billahi minash-shaitanir-rajeem. Bismillaahir-Rahmaanir-Raheem. Alhamdu lillaahi Rabbil-'aalameen. Ar-Rahmaanir-Raheem. Maaliki Yawmid-Deen. Iyyaaka na'budu wa iyyaaka nasta'een. Ihdinas-Siraatal-Mustaqeem. Siraatal-ladheena an'amta 'alayhim ghayril-maghdoobi 'alayhim wa lad-daalleen.",
            translationBn = "আমি বিতাড়িত শয়তান থেকে আল্লাহর আশ্রয় প্রার্থনা করছি। পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি। সকল প্রশংসা জগতের প্রতিপালক আল্লাহর। তিনি পরম দয়াময়, এটি দয়ালু। তিনি শেষ বিচার দিনের মালিক। আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য চাই। আমাদের সরল সঠিক পথ প্রদর্শন করুন। তাদের পথ, যাদের আপনি অনুগ্রহ করেছেন; তাদের পথ নয় যারা ক্রুদ্ধ হয়েছে এবং যারা পথভ্রষ্ট হয়েছে।",
            translationEn = "I seek refuge in Allah from Satan, the rejected. In the name of Allah, the Most Gracious, the Most Merciful. All praise is due to Allah, Lord of the worlds - The Entirely Merciful, the Especially Merciful, Sovereign of the Day of Recompense. It is You we worship and You we ask for help. Guide us to the straight path - The path of those upon whom You have bestowed favor, not of those who have earned [Your] anger or of those who are astray.",
            postureTipBn = "সূরা ফাতিহা পড়া প্রত্যেক রাকাতে নামাজের জন্য ফরজ বা আবশ্যকীয় রুকন।",
            postureTipEn = "Recitation of Surah Al-Fatihah is an obligatory (Fard) component of every unit of prayer."
        ),
        PrayerStep(
            id = 4,
            nameBn = "৪. অন্য সূরা বা অংশ তিলাওয়াত (Surah Recitation)",
            nameEn = "4. Recitation of Another Surah",
            descriptionBn = "সূরা ফাতিহার পর অন্য একটি সম্পূর্ণ ছোট সূরা অথবা পবিত্র কুরআনের যেকোনো অংশ তিলাওয়াত করুন (প্রথম ২ রাকাতে)।",
            descriptionEn = "Recite any other short Surah or a passage of at least three verses from the Holy Quran (applicable in the first two Rak'ahs of obligatory prayers).",
            arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ. فَصَلِّ لِرَبِّكَ وَانْحَرْ. إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
            transliterationBn = "ইন্না আ'তাইনা কাল কাওসার। ফাসাল্লি লিরাব্বিকা ওয়ানহার। ইন্না শানিআকা হুওয়াল আবতার।",
            transliterationEn = "Inna a'tainakal-kauthar. Fa-salli li-rabbika wan-har. Inna shani'aka huwal-abtar.",
            translationBn = "নিশ্চয়ই আমি আপনাকে কাওসার দান করেছি। অতএব আপনার প্রতিপালকের উদ্দেশ্যে নামায পড়ুন এবং কোরবানী করুন। নিশ্চয়ই আপনার শত্রুই লেজকাটা, নির্বংশ।",
            translationEn = "Indeed, We have granted you, [O Muhammad], Al-Kawthar. So pray to your Lord and sacrifice [to Him alone]. Indeed, your enemy is the one cut off.",
            postureTipBn = "পছন্দসই যেকোনো সূরা পড়া যাবে; নিচে নামাজের জন্য উপযুক্ত দশটি ছোট সূরা ও বাংলা উচ্চারণ দেওয়া হয়েছে।",
            postureTipEn = "You can recite any Surah you have memorized. Refer to the 'Essential Surahs' section below."
        ),
        PrayerStep(
            id = 5,
            nameBn = "৫. রুকু ও তাসবীহ (Bowing / Ruku)",
            nameEn = "5. Bowing (Ruku)",
            descriptionBn = "তাকবির (আল্লাহু আকবার) বলতে বলতে রুকুতে যান। মাথা ও পিঠ সোজা সমান তলে রাখুন এবং দুই হাত প্রসারিত করে দুই হাঁটু শক্তভাবে আঁকড়ে ধরুন। রুকু অবস্থায় অত্যন্ত শান্তভাবে কমপক্ষে ৩ বার তাসবীহ পাঠ করুন।",
            descriptionEn = "Saying 'Allahu Akbar', bow down. Keep your back straight, head level with your back, and grip your knees firmly with your fingers spread. Recite the Ruku remembrance at least 3 times.",
            arabicText = "سُبْحَانَ رَبِّيَ الْعَظِيمِ",
            transliterationBn = "সুবহানা রাব্বিয়াল আযীম",
            transliterationEn = "Subhana Rabbiyal 'Azeem",
            translationBn = "আমার মহান প্রতিপালকের পবিত্রতা ও মহিমা ঘোষণা করছি।",
            translationEn = "Glory be to my Lord, the Almighty.",
            postureTipBn = "রুকুতে পিঠ সোজা রাখতে হবে যেন পিঠের উপর পানির পাত্র রাখলে তা সোজা থাকে।",
            postureTipEn = "In Ruku, your back should be so flat and level that if a cup of water was put on it, it would remain balanced."
        ),
        PrayerStep(
            id = 6,
            nameBn = "৬. ক্বাওমাহ বা রুকু থেকে সোজা দাঁড়ানো (Standing From Ruku)",
            nameEn = "6. Standing From Ruku (Qaumah)",
            descriptionBn = "রুকু থেকে ওঠার সময় বলুন 'সামিয়াল্লাহু লিমান হামিদাহ'। সোজা খাড়া হয়ে দাঁড়িয়ে বলুন 'রাব্বানা লাকাল হামদ'।",
            descriptionEn = "Rise from bowing while reciting the Sami'allahu remembrance, then stand completely upright and recite the praise of Lord.",
            arabicText = "سَمِعَ اللَّهُ لِمَنْ حَمِدَهُ. رَبَّنَا وَلَكَ الْحَمْدُ",
            transliterationBn = "সামি আল্লাহু লিমান হামিদাহ। রাব্বানা ওয়া লাকাল হামদ।",
            transliterationEn = "Sami'allahu liman hamidah. Rabbana wa lakal hamd.",
            translationBn = "আল্লাহ তাআলা সেই লোকের প্রশংসা শোনেন যে তাঁর প্রশংসা করে। হে আমাদের পালনকর্তা! সমস্ত প্রশংসা কেবল আপনারই জন্য।",
            translationEn = "Allah hears the one who praises Him. Our Lord, all praise is due to You.",
            postureTipBn = "রুকু থেকে পুরোপুরি সোজা হয়ে দাঁড়ানো ওয়াজিব। সোজা হয়ে ক্ষনিক স্থির দাঁড়ান।",
            postureTipEn = "Standing completely upright and pausing for a moment before proceeding to Sajdah is mandatory (Wajib)."
        ),
        PrayerStep(
            id = 7,
            nameBn = "৭. সিজদাহ ও তাসবীহ (Prostration / Sajdah)",
            nameEn = "7. Prostration (Sajdah)",
            descriptionBn = "তাকবির (আল্লাহু আকবার) বলতে বলতে হাঁটু মাটিতে রেখে সিজদাহে যান। কপাল, নাক, দুই হাতের তালু, দুই হাঁটু ও উভয় পায়ের আঙুলসমূহ মাটিতে স্পর্শ করান। সিজদায় কমপক্ষে ৩ বার এই পবিত্র তাসবীহ পাঠ করুন।",
            descriptionEn = "Saying 'Allahu Akbar', prostrate on the ground. Your forehead, nose, both palms, both knees, and vertical toes must touch the floor. Recite the Sajdah remembrance at least 3 times.",
            arabicText = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
            transliterationBn = "সুবহানা রাব্বিয়াল আ'লা",
            transliterationEn = "Subhana Rabbiyal A'la",
            translationBn = "আমার পরম উচ্চ প্রতিপালকের পবিত্রতা ঘোষণা করছি।",
            translationEn = "Glory be to my Lord, the Most High.",
            postureTipBn = "সিজদাহকালে কনুই মাটি থেকে উপরে রাখুন এবং দুই পা খাড়া রেখে আঙুল ক্বিবলামুখী করার চেষ্টা করুন।",
            postureTipEn = "Do not lay your forearms flat on the ground. Keep elbows raised, feet upright, and toes pointing towards Qibla."
        ),
        PrayerStep(
            id = 8,
            nameBn = "৮. জলসাহ বা দুই সিজদাহর মাঝে বসা (Sitting Between Sajdahs)",
            nameEn = "8. Sitting Between Sajdahs (Jalsah)",
            descriptionBn = "তাকবির বলে সিজদাহ থেকে উঠে বসুন। বাম পা বিছিয়ে তার ওপর বসুন এবং ডান পা খাড়া রাখুন। হাত দুটো উরুর ওপর রাখুন এবং ক্ষমা প্রার্থনা করে এই চমৎকার দোয়াটি পড়ুন।",
            descriptionEn = "Say 'Allahu Akbar' and rise to sit upright. Lay your left foot flat to sit on it and keep your right foot vertical with toes pointing towards Qibla. Place hands on your thighs and recite this beautiful supplication:",
            arabicText = "اللَّهُمَّ اغْفِرْ لِي وَارْحَمْنِي وَاهْدِنِي وَعَافِنِي وَارْزُقْنِي",
            transliterationBn = "আল্লাহুম্মাগফিরলী, ওয়ারহামনী, ওয়াহদিনী, ওয়া 'আফিনী, ওয়ারযুকনী।",
            transliterationEn = "Allahum-maghfir lee, war-hamnee, wah-dinee, wa 'aafinee, war-zuqnee.",
            translationBn = "হে আল্লাহ! আমাকে ক্ষমা করুন, আমার ওপর দয়া বর্ষণ করুন, আমাকে সঠিক সরল পথ দেখান, আমাকে শারীরিক সুস্থতা দান করুন এবং আমার রিজিক বৃদ্ধি করে দিন।",
            translationEn = "O Allah, forgive me, have mercy on me, guide me, grant me health/well-being, and provide sustenance for me.",
            postureTipBn = "দুই সিজদার মাঝখানে সোজা হয়ে স্থির হয়ে বসা অত্যন্ত গুরুত্বপূর্ণ ওয়াজিব আমল।",
            postureTipEn = "Sitting completely upright and pausing mindfully between the two prostrations is key."
        ),
        PrayerStep(
            id = 9,
            nameBn = "৯. দ্বিতীয় সাজদাহ (The Second Prostration)",
            nameEn = "9. The Second Prostration",
            descriptionBn = "আবার তাকবির বলতে বলতে দ্বিতীয় সিজদাহে যান এবং প্রথম সিজদার মতোই কপাল-নাক মাটিতে ঠেকিয়ে শান্তভাবে কমপক্ষে তিনবার 'সুবহানা রাব্বিয়াল আ'লা' বলুন।",
            descriptionEn = "Saying 'Allahu Akbar' again, perform the second Sajdah in exactly the same manner as the first. Recite 'Subhana Rabbiyal A'la' at least 3 times.",
            arabicText = "سُبْحَانَ رَبِّيَ الْأَعْلَى",
            transliterationBn = "সুবহানা রাব্বিয়াল আ'লা",
            transliterationEn = "Subhana Rabbiyal A'la",
            translationBn = "আমার পরম উচ্চ প্রতিপালকের পবিত্রতা ঘোষণা করছি।",
            translationEn = "Glory be to my Lord, the Most High.",
            postureTipBn = "সিজদাহ থেকে উঠে দ্বিতীয় রাকাতের জন্য দাঁড়ালে হাত না উঠিয়ে সোজা তাকবিরে উঠে দাঁড়ান ও হাত বাঁধবেন।",
            postureTipEn = "After rising from the second Sajdah of the first Rak'ah, say 'Allahu Akbar' and return straight to the standing position (Qiyam) for the second Rak'ah."
        ),
        PrayerStep(
            id = 10,
            nameBn = "১০. তাশাহহুদ ও দরূদ পাঠ (Tashahhud & Salawat)",
            nameEn = "10. Tashahhud & Durood (Sitting of Prayer)",
            descriptionBn = "প্রতি ২ রাকাত শেষে অথবা শেষ রাকাতে উঠে বসার পর তাশাহহুদ (আত্তাহিয়্যাতু) পড়তে হবে। এরপর দরূদ শরীফ (ইব্রাহীম) তিলাওয়াত করতে হবে।",
            descriptionEn = "Upon sitting in the final parts of the prayer, recite At-Tahiyyat (Tashahhud). When reciting 'Ash-hadu alla...', raise your right index finger briefly indicating the unity of Allah. Then, recite Durood-e-Ibrahim:",
            arabicText = "التَّحِيَّاتُ لِلَّهِ وَالصَّلَوَاتُ وَالطَّيِّبَاتُ، السَّلَامُ عَلَيْكَ أَيُّهَا النَّبِيُّ وَرَحْمَةُ اللَّهِ وَبَرَكَاتُهُ، السَّلَامُ عَلَيْنَا وَعَلَى عِبَادِ اللَّهِ الصَّالِحِينَ، أَشْهَدُ أَنْ لَا إِلَهَ إِلَّا اللَّهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ . اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ . اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
            transliterationBn = "আত্তাহিয়্যাতু লিল্লাহি ওয়াস সালাওয়াতু ওয়াত তাইয়্যিবাতু, আসসালামু আলাইকা আইয়্যুহান নাবীয়্যু ওয়ারাহমাতুল্লাহি ওয়াবারাকাতুহু, আসসালামু আলাইনা ওয়া আলা ইবাদিল্লাহিস সালিহীন, আশহাদু আল্লা ইলাহা ইল্লাল্লাহু ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু। অতঃপর দরূদ: আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিওঁ ওয়া আলা আলি মুহাম্মাদ, কামা সাল্লাইتا আলা ইব্রাহীমা ওয়া আলা আলি ইব্রাহীমা ইন্নাকা হামীদুম মজীদ। আল্লাহুম্মা বারিক আলা মুহাম্মাদিওঁ ওয়া আলা আলি মুহাম্মাদ, কামা বারাকতা আলা ইব্রাহীমা ওয়া আলা আলি ইব্রাহীমা ইন্নাকা হামীদুম মজীদ।",
            transliterationEn = "At-tahiyyatu lillahi was-salawatu wat-tayyibatu, as-salamu 'alayka ayyuhan-nabiyyu warahmatullaahi wabarakaatuhu, as-salamu 'alaynaa wa 'alaa 'ibaadillaahis-saaliheen. Ash-hadu alla ilaha illallahu wa ash-hadu anna Muhammadan 'abduhu wa rasooluhu. Then Durood: Allahumma salli 'ala Muhammadin wa 'ala aali Muhammadin kama sallayta 'ala Ibraheema wa 'ala aali Ibraheema innaka Hameedum Majeed. Allahumma baarik 'ala Muhammadin wa 'ala aali Muhammadin kama baarakta 'ala Ibraheema wa 'ala aali Ibraheema innaka Hameedum Majeed.",
            translationBn = "সমস্ত মৌখিক, শারীরিক ও আর্থিক ইবাদত একমাত্র আল্লাহর জন্য। হে নবী! আপনার ওপর শান্তি, আল্লাহর রহমত ও বরকত বর্ষিত হোক। আমাদের ওপর এবং আল্লাহর সকল সৎ বান্দাদের ওপর শান্তি বর্ষিত হোক। আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া কোনো উপাস্য নেই এবং আমি আরও সাক্ষ্য দিচ্ছি যে, মুহাম্মদ (সা.) আল্লাহর বান্দা ও রাসুল। অতঃপর দরূদ: হে আল্লাহ! আপনি মুহাম্মদ (সা.) এবং তাঁর বংশধরগণের ওপর রহমত বর্ষণ করুন, যেমন আপনি ইব্রাহিম (আ.) এবং তাঁর বংশধরগণের ওপর রহমত বর্ষণ করেছিলেন। নিশ্চয়ই আপনি প্রশংসিত ও মহিমান্বিত। হে আল্লাহ! আপনি মুহাম্মদ (সা.) এবং তাঁর বংশধরগণের ওপর বরকত নাজিল করুন, যেমন আপনি ইব্রাহিম (আ.) এবং তাঁর বংশধরগণের ওপর বরকত নাজিল করেছিলেন। নিশ্চয়ই আপনি প্রশংসিত ও মহিমান্বিত।",
            translationEn = "All compliments, prayers, and pure deeds are for Allah. Peace, mercy, and blessings of Allah be upon you, O Prophet, and His mercy and blessings. Peace be upon us and upon the righteous servants of Allah. I bear witness that there is no god but Allah, and I bear witness that Muhammad is His servant and His Messenger. Then Durood: O Allah, send Your mercy on Muhammad and on the family of Muhammad, as You sent mercy on Abraham and on the family of Abraham. Indeed, You are Praiseworthy, Full of Glory. O Allah, send Your blessings on Muhammad and on the family of Muhammad, as You sent blessings on Abraham and on the family of Abraham. Indeed, You are Praiseworthy, Full of Glory.",
            postureTipBn = "তাকওয়াপূর্ণ মনে স্থির হয়ে বসুন। বাম পা বিছিয়ে বসাই সুন্নত।",
            postureTipEn = "Keep calm, focusing on the beautiful dialogue of Tashahhud."
        ),
        PrayerStep(
            id = 11,
            nameBn = "১১. দোয়া মাসূরা পাঠ (Dua-e-Masoorah)",
            nameEn = "11. Supplication of Prophet (Dua-e-Masoorah)",
            descriptionBn = "শেষ রাকাতে তাশাহহুদ ও দরূদ পড়ার পর সালাম ফেরানোর পূর্ব মুহূর্তে গুনাহ মাফ ও হেদায়েতের জন্য দোয়া মাসূরা পাঠ করুন।",
            descriptionEn = "Before finishing the prayer with Salam, recite any standard Quranic or Sunnah supplication. The most common is Dua Masoorah:",
            arabicText = "اللَّهُمَّ إِنِّي ظَلَمْتُ نَفْسِي ظُلْمًا كَثِيرًا وَلَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ فَاغْفِرْ لِي مَغْفِرَةً مِنْ عِنْدِكَ وَارْحَمْنِي إِنَّكَ أَنْتَ الْغَفُورُ الرَّحِيمُ",
            transliterationBn = "আল্লাহুম্মা ইন্নী জলামতু নাফসী জুলমান কাসীরাঁ, ওয়ালা ইয়াগফিরুজ জুনূবা ইল্লা আনতা, ফাগফির লী মাগফিরাতাম মিন ইনদিকা ওয়ারহামনী ইন্নাকা আনতাল গাফুরুর রাহীম।",
            transliterationEn = "Allahumma inni zalamtu nafsi zulman katheeran wa la yaghfirudh-dhunuba illa Anta, faghfir lee maghfiratan min 'indika war-hamnee, innaka Antal-Ghafoorur-Raheem.",
            translationBn = "হে আল্লাহ! নিশ্চয়ই আমি আমার নিজের ওপর অনেক বড় অত্যাচার করেছি। আপনি ছাড়া পাপ মার্জনা করার ক্ষমতা কারও নেই। অতএব, আপনার অসীম দয়ায় আমাকে ক্ষমা করুন এবং আমার ওপর অনুগ্রহ বর্ষণ করুন। নিশ্চয়ই আপনি পরম ক্ষমাশীল ও অসীম দয়ালু।",
            translationEn = "O Allah, indeed I have wronged myself greatly, and none can forgive sins except You. So grant me forgiveness from You and have mercy on me. Indeed, You are the Forgiving, the Merciful.",
            postureTipBn = "দোয়া মাসূরা ছাড়াও কুরআনিক দোয়া যেমন 'রাব্বানা আতিনা ফিদ্দুনিয়া...' পাঠ করা যায়।",
            postureTipEn = "You can also recite other beautiful Quranic prayers such as 'Rabbana Atina fid-dunya...'."
        ),
        PrayerStep(
            id = 12,
            nameBn = "১২. সালাম বা নামাজ সম্পন্ন করা (The Salam / Tasleem)",
            nameEn = "12. Closing the Prayer (Salam)",
            descriptionBn = "ডান দিকে ঘাড় ঘুরিয়ে বলুন 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ'। এরপর বাম দিকে ঘাড় ঘুরিয়ে একইভাবে বলুন 'আসসালামু আলাইকুম ওয়া রাহমাতুল্লাহ'। এর মাধ্যমে নামাজ সফল ভাবে শেষ হবে।",
            descriptionEn = "Turn your face to the right, looking over your right shoulder, and say 'As-salamu 'alaykum wa rahmatullah'. Then turn your face to the left and repeat the same to finalize your worship.",
            arabicText = "السَّلَامُ عَلَيْكُمْ وَرَحْمَةُ اللَّهِ",
            transliterationBn = "আস-সালামু আলাইকুম ওয়া রাহমাতুল্লাহ",
            transliterationEn = "As-salamu 'alaykum wa rahmatullah",
            translationBn = "আপনাদের ওপর সর্বশক্তিমান আল্লাহর মহৎ শান্তি ও অসীম রহমত বর্ষিত হোক।",
            translationEn = "Peace and mercy of Allah be upon you.",
            postureTipBn = "সালাম ফেরানোর সময় কাঁধের পেছনের দিকে দৃষ্টি রাখুন। নামাজ শেষ করে হাত তুলে মোনাজাত ও জিকিরে নিমগ্ন হন।",
            postureTipEn = "Look over your shoulders during Salam. Spend moments in Tasbih, Astaghfirullah, and Supplication (Dua) of daily remembrance."
        )
    )

    val suras = listOf(
        PrayerSura(
            id = 1,
            nameBn = "সূরা আল-ফাতিহা (সবচেয়ে গুরুত্বপূর্ণ সুরূ)",
            nameEn = "Surah Al-Fatihah (The Opening)",
            sNameArabic = "سُورَةُ الْفَاتِحَةِ",
            classificationBn = "মাক্কী",
            classificationEn = "Meccan",
            totalVerses = 7,
            arabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ (১) الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ (২) الرَّحْمَٰنِ الرَّحِيمِ (৩) مَالِكِ يَوْمِ الدِّينِ (৪) إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ (৫) اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ (৬) صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ (৭)",
            transliterationBn = "১. বিসমিল্লাহির রাহমানির রাহীম। ২. আলহামদুলিল্লাহি রাব্বিল আলামীন। ৩. আর-রাহমানির রাহীম। ৪. মালিকি ইয়াওমিদ্দীন। ৫. ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাস্তায়ীন। ৬. ইহদিনাস সিরাতাল মুস্তাকীম। ৭. সিরাতাল্লাযীনা আনআমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ্দাল্লীন।",
            transliterationEn = "1. Bismillaahir-Rahmaanir-Raheem. 2. Alhamdu lillaahi Rabbil-'aalameen. 3. Ar-Rahmaanir-Raheem. 4. Maaliki Yawmid-Deen. 5. Iyyaaka na'budu wa iyyaaka nasta'een. 6. Ihdinas-Siraatal-Mustaqeem. 7. Siraatal-ladheena an'amta 'alayhim ghayril-maghdoobi 'alayhim wa lad-daalleen.",
            translationBn = "১. অনন্ত দয়ালু পরম করুণাময় আল্লাহর নামে। ২. সমস্ত প্রশংসা বিশ্বজগতের মালিক আল্লাহর। ৩. পরম দয়ালু অত্যন্ত মেহেরবান। ৪. কর্মফল বা বিচার দিবসের একমাত্র অধিপতি। ৫. আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই সাহায্য প্রার্থনা করি। ৬. আমাদের সরল সঠিক সৎ পথ প্রদর্শন করুন। ৭. তাদের পথ যাদের আপনি পুরস্কৃত করেছেন, তাদের পথ নয় যারা আপনার ক্রোধে পতিত বা পথভ্রষ্ট হয়েছে।",
            translationEn = "1. In the name of Allah, the Entirely Merciful, the Especially Merciful. 2. [All] praise is [due] to Allah, Lord of the worlds - 3. The Entirely Merciful, the Especially Merciful, 4. Sovereign of the Day of Recompense. 5. It is You we worship and You we ask for help. 6. Guide us to the straight path - 7. The path of those upon whom You have bestowed favor, not of those who have earned [Your] anger or of those who are astray."
        ),
        PrayerSura(
            id = 2,
            nameBn = "সূরা আল-ফীল (হাতি বাহিনী)",
            nameEn = "Surah Al-Fil (The Elephant)",
            sNameArabic = "سُورَةُ الْفِيلِ",
            classificationBn = "মাক্কী",
            classificationEn = "Meccan",
            totalVerses = 5,
            arabicText = "أَلَمْ تَرَ كَيْفَ فَعَلَ رَبُّكَ بِأَصْحَابِ الْفِيلِ (১) أَلَمْ يَجْعَلْ كَيْدَهُمْ فِي تَضْلِيلٍ (২) وَأَرْسَلَ عَلَيْهِمْ طَيْرًا أَبَابِيلَ (৩) تَرْمِيهِمْ بِحِجَارَةٍ مِنْ سِجِّيلٍ (৪) فَجَعَلَهُمْ كَعَصْفٍ مَأْكُولٍ (৫)",
            transliterationBn = "১. আলাম তারা কাইফা ফা'আলা রাব্বুকা বি আসহাবিল ফীল। ২. আলাম ইয়াজ'আল কাইদাহুম ফী তাদলীলে। ৩. ওয়া আরসালা আলাইহিম তাইরান আবাবীল। ৪. তারমীহিম বিহিজারাত মিন সিজ্জীল। ৫. ফাজ্বা'আলাহুম কা'আছফিম মাকূল।",
            transliterationEn = "1. Alam tara kayfa fa'ala rabbuka bi-as-haabil-feele. 2. Alam yaj'al kaydahum fee tadleele. 3. Wa arsala 'alayhim tayran abaabeela. 4. Tarmeehim bi-hijaaratim min sijjeele. 5. Fa-ja'alahum ka'asfim ma'kool.",
            translationBn = "১. আপনি কি লক্ষ্য করেননি আপনার প্রতিপালক হস্তীবাহিনীর সাথে কীরূপ আচরণ করেছিলেন? ২. তিনি কি তাদের চক্রান্ত ব্যর্থ করে দেননি? ৩. এবং তিনি তাদের বিরুদ্ধে ঝাঁকে ঝাঁকে পাখি (আবাবীলে) প্রেরণ করেছিলেন। ৪. যারা তাদের ওপর পাথর নিক্ষেপ করেছিল। ৫. অতঃপর তিনি তাদের চর্বিত ঘাসের মত বা ধ্বংস স্তূপে পরিণত করেন।",
            translationEn = "1. Have you not considered, [O Muhammad], how your Lord dealt with the companions of the elephant? 2. Did He not make their plan into misguidance? 3. And He sent against them birds in flocks, 4. Striking them with stones of hard clay, 5. And He made them like eaten straw."
        ),
        PrayerSura(
            id = 3,
            nameBn = "সূরা আল-কাওসার (প্রাচুর্য)",
            nameEn = "Surah Al-Kawthar (The Abundance)",
            sNameArabic = "سُورَةُ الْكَوْثَرِ",
            classificationBn = "মাক্কী",
            classificationEn = "Meccan",
            totalVerses = 3,
            arabicText = "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ (১) فَصَلِّ لِرَبِّكَ وَانْحَرْ (২) إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ (৩)",
            transliterationBn = "১. ইন্না আ'তাইনা কাল কাওসার। ২. ফাসাল্লি লিরাব্বিকা ওয়ানহার। ৩. ইন্না শানিআকা হুওয়াল আবতার।",
            transliterationEn = "1. Innaa a'taynaakal-Kawthar. 2. Fa-salli li-Rabbika wanhar. 3. Inna shaani'aka huwal-abtar.",
            translationBn = "১. নিশ্চয়ই আমি আপনাকে কাওসার (প্রাচুর্য ও জান্নাতের প্রস্রবণ) দান করেছি। ২. অতএব আপনার প্রতিপালকের সন্তুষ্টির উদ্দেশ্যে সালাত আদায় করুন ও কোরবানি করুন। ৩. নিশ্চয়ই আপনার শত্রুই নির্বংশ, তার চিহ্ন মুছে যাবে।",
            translationEn = "1. Indeed, We have granted you, [O Muhammad], Al-Kawthar. 2. So pray to your Lord and sacrifice [to Him alone]. 3. Indeed, your enemy is the one cut off."
        ),
        PrayerSura(
            id = 4,
            nameBn = "সূরা আল-ইখলাস (খাঁটি বিশ্বাস/তাওহীদ)",
            nameEn = "Surah Al-Ikhlas (The Sincerity)",
            sNameArabic = "سُورَةُ الْإِخْلَاصِ",
            classificationBn = "মাক্কী",
            classificationEn = "Meccan",
            totalVerses = 4,
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ (১) اللَّهُ الصَّمَدُ (২) لَمْ يَلِدْ وَلَمْ يُولَدْ (৩) وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ (৪)",
            transliterationBn = "১. কুল হুওয়াল্লাহু আহাদ। ২. আল্লাহুচ্ছামাদ। ৩. লাম ইয়ালিদ ওয়া লাম ইউলাদ। ৪. ওয়া লাম ইয়াকুল্লাহু কুফুওয়ান আহাদ।",
            transliterationEn = "1. Qul Huwal-laahu Ahad. 2. Allaahus-Samad. 3. Lam yalid wa lam yoolad. 4. Wa lam yakul-lahoo kufuwan ahad.",
            translationBn = "১. বলুন, তিনিই আল্লাহ, একক ও অদ্বিতীয়। ২. আল্লাহ কারোর প্রতি মুখাপেক্ষী নন, সকলে তাঁর মুখাপেক্ষী। ৩. তিনি জনক নন এবং জন্ম নেননি। ৪. এবং তাঁর সমকক্ষ কেউই নেই।",
            translationEn = "1. Say, 'He is Allah, [who is] One, 2. Allah, the Eternal Refuge. 3. He neither begets nor is born, 4. And there is none co-equal or comparable to Him.'"
        ),
        PrayerSura(
            id = 5,
            nameBn = "সূরা আল-ফালাক (ঊষাকাল)",
            nameEn = "Surah Al-Falaq (The Daybreak)",
            sNameArabic = "سُورَةُ الْفَلَقِ",
            classificationBn = "মাদানী",
            classificationEn = "Medinan",
            totalVerses = 5,
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ (১) مِنْ شَرِّ مَا خَلَقَ (২) مِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَ (৩) وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ (৪) وَمِنْ شَرِّ حَاسِدٍ إِذَا حَسَدَ (৫)",
            transliterationBn = "১. কুল আউযু বিরাব্বিল ফালাক। ২. মিন শাররি মা খালাক। ৩. ওয়া মিন শাররি গাসিকিন ইযা ওয়াকাব। ৪. ওয়া মিন শাররিন নাফ্ফাসাতি ফিল উকাদ। ৫. ওয়া মিন শাররি হাসিদিন ইযা হাসাদ।",
            transliterationEn = "1. Qul a'oodhu bi-Rabbil-falaq. 2. Min sharri maa khalaq. 3. Wa min sharri ghaasiqin idhaa waqab. 4. Wa min sharrin-naffaathaati fil-'uqad. 5. Wa min sharri haasidin idhaa hasad.",
            translationBn = "১. বলুন, আমি আশ্রয় প্রার্থনা করছি উষাকালের প্রতিপালকের। ২. তিনি যা সৃষ্টি করেছেন তার অনিষ্ট হতে। ৩. এবং অন্ধকার রাত্রির অনিষ্ট হতে যখন তা আচ্ছন্ন করে। ৪. এবং গ্রন্থিতে ফুঁৎকার দানকারী নারী বা জাদুকরদের অনিষ্ট হতে। ৫. এবং হিংসুকের অনিষ্ট হতে যখন সে হিংসা করে।",
            translationEn = "1. Say, 'I seek refuge in the Lord of the daybreak 2. From the evil of that which He created 3. And from the evil of darkness when it settles 4. And from the evil of the blowers in knots 5. And from the evil of an envier when he envies.'"
        ),
        PrayerSura(
            id = 6,
            nameBn = "সূরা আন-নাস (মানবজাতি)",
            nameEn = "Surah An-Nas (Mankind)",
            sNameArabic = "سُورَةُ النَّاسِ",
            classificationBn = "মাদানী",
            classificationEn = "Medinan",
            totalVerses = 6,
            arabicText = "قُل * أَعُوذُ بِرَبِّ النَّاسِ (১) مَلِكِ النَّاسِ (২) إِلَٰهِ النَّاسِ (৩) مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ (৪) الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ (৫) مِنَ الْجِنَّةِ وَالنَّاسِ (৬)",
            transliterationBn = "১. কুল আউযু বিরাব্বিন নাস। ২. মালিকিন নাস। ৩. ইলাহিন নাস। ৪. মিন শাররিল ওয়াসওয়াসিল খান্নাস। ৫. আল্লাযী ইউওয়াসউইসু ফী ছুদূরিন নাস। ৬. মিনাল জিন্নাতি ওয়ান নাস।",
            transliterationEn = "1. Qul a'oodhu bi-Rabbin-naas. 2. Malikin-naas. 3. Ilaahin-naas. 4. Min sharril-waswaasil-khannaas. 5. Alladhee yuwaswisu fee sudoorin-naas. 6. Minal-jinnati wan-naas.",
            translationBn = "১. বলুন, আমি আশ্রয় প্রার্থনা করছি মানুষের প্রতিপালকের। ২. মানুষের একমাত্র অধিপতির। ৩. মানুষের একমাত্র উপাস্যের। ৪. আত্মগোপনকারী কুমন্ত্রণাদাতার অনিষ্ট হতে। ৫. যে মানুষের অন্তরে কুমন্ত্রণা ও বিভ্রান্তিবোধ সৃষ্টি করে। ৬. জীন এবং মানুষ উভয়ের মধ্য থেকে।",
            translationEn = "1. Say, 'I seek refuge in the Lord of mankind, 2. The Sovereign of mankind, 3. The God of mankind, 4. From the evil of the retreating whisperer - 5. Who whispers [evil] into the breasts of mankind - 6. From among the jinn and mankind.'"
        )
    )

    val rakatList = listOf(
        RakatStructure("ফজর (Fajr)", "Fajr", 2, 2, 0, 0, 0, 4),
        RakatStructure("যোহর (Zuhr)", "Zuhr", 4, 4, 2, 2, 0, 12),
        RakatStructure("আসর (Asr)", "Asr", 4, 4, 0, 0, 0, 8),
        RakatStructure("মাগরিব (Maghrib)", "Maghrib", 0, 3, 2, 2, 0, 7),
        RakatStructure("এশা (Isha)", "Isha", 4, 4, 2, 2, 3, 15)
    )

    val rulesBn = listOf(
        "নামাজের ফরজ ১৪ টি - এগুলো বাদ পড়লে নামাজ বাতিল হয়ে যাবে (৭ টি বাইরে এবং ৭ টি ভেতরে):",
        "নামাজের বাইরের ফরজ (আহকাম): ১. শরীর পবিত্র হওয়া, ২. কাপড় পবিত্র হওয়া, ৩. নামাজের জায়গা পবিত্র হওয়া, ৪. সতর ঢাকা (নাভি থেকে হাঁটু পর্যন্ত আবৃত রাখা), ৫. ক্বিবলামুখী হওয়া, ৬. ওয়াক্ত চেনা বা নামাজের সময় হওয়া, ৭. নামাজের মনে মনে নিয়ত করা।",
        "নামাজের ভেতরের ফরজ (আরকান): ১. তাকবীরে তাহরিমা বা 'আল্লাহু আকবার' বলে নামাজ শুরু করা, ২. দাঁড়িয়ে নামাজ পড়া (অক্ষম হলে বসে), ৩. কিরাত পড়া বা সূরা ফাতিহা তিলাওয়াত করা, ৪. রুকু করা, ৫. সেজদাহ করা, ৬. শেষ জলসাতে তাশাহহুদ পরিমাণ বসা, ৭. সালাম ফিরিয়ে নামাজ শেষ করা।"
    )

    val rulesEn = listOf(
        "There are 14 Obligatory acts (Arkan and Shurut) of prayer:",
        "Conditions outside Prayer (Shurut): 1. Cleanliness of body, 2. Cleanliness of clothing, 3. Cleanliness of place, 4. Covering of private parts (Satr), 5. Facing Qibla, 6. Correct timing of prayer, 7. Heartfelt/Mental intention.",
        "Pillars inside Prayer (Arkan): 1. Opening Takbeer, 2. Standing upright (Qiyam), 3. Recitation of Quran, 4. Bowing (Ruku), 5. Prostration (Sajdah), 6. Final sitting with Tashahhud, 7. Saying Salam to close prayer."
    )

    val wuduSteps = listOf(
        WuduStep(
            id = 1,
            nameBn = "১. নিয়ত ও বিসমিল্লাহ (Niyyah & Basmalah)",
            nameEn = "1. Niyyah & Saying Bismillah",
            descriptionBn = "মনে মনে ওযুর নিয়ত স্থির করুন। এরপর বলুন 'বিসমিল্লাহির রহমানির রাহীম' (আল্লাহর নামে শুরু করছি)।",
            descriptionEn = "Begin with a sincere intention in your heart to perform Wudu. Say 'Bismillahir Rahmanir Raheem' (In the name of Allah, the Most Gracious, the Most Merciful).",
            illustrationLabel = "Niyyah"
        ),
        WuduStep(
            id = 2,
            nameBn = "২. দুই হাত ধোয়া (Washing Hands)",
            nameEn = "2. Washing Hands Up to the Wrists",
            descriptionBn = "প্রথমে ডান হাত কবজি পর্যন্ত ভালো করে ৩ বার ধুয়ে নিন। এরপর বাম হাতও কবজি পর্যন্ত ৩ বার ধুয়ে নিন। আঙুলের ফাঁকে ফাঁকে খিলাল করুন।",
            descriptionEn = "Wash your right hand up to the wrist three times, ensuring water reaches between the fingers. Then, wash your left hand three times in the same manner.",
            illustrationLabel = "Hands"
        ),
        WuduStep(
            id = 3,
            nameBn = "৩. কুলি করা (Rinsing the Mouth)",
            nameEn = "3. Rinsing the Mouth",
            descriptionBn = "ডান হাত দিয়ে মুখে পানি নিয়ে ৩ বার ভালো করে কুলি করুন। রোজা না থাকলে মুখের ভেতরের শেষ অংশ পর্যন্ত পানি পৌঁছানোর চেষ্টা করুন।",
            descriptionEn = "Take water with your right hand into your mouth, rinse thoroughly, and spit it out. Repeat this three times.",
            illustrationLabel = "Mouth"
        ),
        WuduStep(
            id = 4,
            nameBn = "৪. নাকে পানি দেওয়া ও পরিষ্কার করা (Inhaling Water)",
            nameEn = "4. Sniffing and Blowing Water from the Nose",
            descriptionBn = "ডান হাত দিয়ে নাকে পানি টেনে নিন এবং বাম হাতের কনিষ্ঠ ও বুড়ো আঙুল দিয়ে আলতো করে নাক পরিষ্কার করুন ৩ বার।",
            descriptionEn = "Sniff water up into your nose with your right hand, then blow it out gently using your left hand. Repeat this three times.",
            illustrationLabel = "Nose"
        ),
        WuduStep(
            id = 5,
            nameBn = "৫. সমস্ত মুখমণ্ডল ধোয়া (Washing the Face)",
            nameEn = "5. Washing the Whole Face",
            descriptionBn = "কপালে চুল গজানোর স্বাভাবিক স্থান থেকে থুতনির নিচ পর্যন্ত এবং এক কানের লতি থেকে অন্য কানের লতি পর্যন্ত সমস্ত মুখমণ্ডল ৩ বার ভালো করে ধুয়ে নিন। পুরুষের ঘন দাড়ি থাকলে দাড়ি খিলাল করুন।",
            descriptionEn = "Wash your entire face three times, from the hairline at the top of the forehead to the bottom of the chin, and from earlobe to earlobe.",
            illustrationLabel = "Face"
        ),
        WuduStep(
            id = 6,
            nameBn = "৬. দুই কনুই পর্যন্ত হাত ধোয়া (Washing Arms)",
            nameEn = "6. Washing Arms up to the Elbows",
            descriptionBn = "ডান হাত কনিষ্ঠা আঙুলের ডগা থেকে কনুই ওপর পর্যন্ত ৩ বার ধুয়ে নিন। এরপর বাম হাতও কনুইয়ের ওপর পর্যন্ত ৩ বার একইভাবে ধুয়ে নিন। প্রথমে ডান হাত ধোয়া আবশ্যক।",
            descriptionEn = "Wash your right arm from the fingertips to just above the elbow three times. Then, wash your left arm three times in the same way.",
            illustrationLabel = "Arms"
        ),
        WuduStep(
            id = 7,
            nameBn = "৭. মাথা ও কান মাসেহ করা (Wiping Head & Ears)",
            nameEn = "7. Wiping the Head & Ears",
            descriptionBn = "দুই হাত পানি দিয়ে ভিজিয়ে নিন। দুহাতের চার আঙুল মাথার চুলে কপালে ঠেকিয়ে পেছনের দিকে ঘাড় পর্যন্ত টেনে নিয়ে যান, পরক্ষণে আবার পেছনে থেকে সামনে টেনে আনুন (১ বার)। শাহাদাত আঙুল দিয়ে কানের ভেতরের অংশ এবং বৃদ্ধাঙ্গুল দিয়ে কানের বাইরের অংশ মাসেহ করুন।",
            descriptionEn = "Wet both hands. Run your wet fingers from the front hair line to the back of your neck and return to the front (once). Use index fingers to wipe inside ears and thumbs for the back of ears.",
            illustrationLabel = "Head"
        ),
        WuduStep(
            id = 8,
            nameBn = "৮. দুই পা টাখনুসহ ধোয়া (Washing Feet)",
            nameEn = "8. Washing Feet including the Ankles",
            descriptionBn = "প্রথমে ডান পা পায়ের পাতা ও গিরা বা টাখনুসহ ৩ বার ধুয়ে নিন। এরপর বাম পা-ও টাখনুসহ ৩ বার ধুয়ে নিন। বাম হাতের কনিষ্ঠা আঙুল দিয়ে পায়ের আঙুলগুলো নিচ থেকে ওপরে খিলাল করুন।",
            descriptionEn = "Wash your right foot from the toes to above the ankle three times, ensuring water reaches between all toes. Then wash your left foot three times in the same way.",
            illustrationLabel = "Feet"
        )
    )

    val postureComparisons = listOf(
        PostureComparison(
            id = 1,
            postureNameBn = "১. দাঁড়িয়ে থাকা (কিয়াম)",
            postureNameEn = "1. Standing (Qiyam)",
            correctTitleBn = "সঠিক কিয়াম পদ্ধতি ✔",
            correctTitleEn = "Correct Standing Posture ✔",
            correctDetailsBn = listOf(
                "দৃষ্টি সিজদার স্থানে স্থির রাখুন। চোখ বন্ধ করবেন না।",
                "পিঠ সোজা ও শান্ত অবস্থায় রাখুন। নড়াচড়া করবেন না।",
                "বাম হাতের উপর ডান হাত নাভির নিচে বা বুকের ওপর বাঁধুন।"
            ),
            correctDetailsEn = listOf(
                "Keep your eyes focused downward on the spot of prostration.",
                "Keep your spine naturally erect and in a state of calm. Do not tilt.",
                "Place right hand over left hand below navel or on the chest."
            ),
            wrongTitleBn = "ভুল কিয়াম পদ্ধতি ❌",
            wrongTitleEn = "Common Standing Mistakes ❌",
            wrongDetailsBn = listOf(
                "চোখ এদিক-ওদিক ঘোরানো বা আকাশের দিকে তাকানো।",
                "সোজা না দাঁড়িয়ে বাঁকা হয়ে এক পায়ের ওপর ভর দিয়ে দাঁড়ানো।",
                "অযথাই কাপড় নিয়ে নাড়াচাড়া বা অলসভাবে দাঁড়ানো।"
            ),
            wrongDetailsEn = listOf(
                "Looking around at people or staring upward.",
                "Leaning heavily on one leg or swaying side to side.",
                "Fidgeting, adjusting clothes repeatedly, or lazy posture."
            ),
            diagramType = "qiyam"
        ),
        PostureComparison(
            id = 2,
            postureNameBn = "২. রুকু বা অবনত হওয়া",
            postureNameEn = "2. Bowing (Ruku)",
            correctTitleBn = "সঠিক রুকু পদ্ধতি ✔",
            correctTitleEn = "Correct Bowing Posture ✔",
            correctDetailsBn = listOf(
                "পিঠ সোজা ও সুষম সমান্তরাল রাখতে হবে যেন পানির কাপ রাখলেও সোজা থাকে।",
                "দুই হাত প্রসারিত করে দুই হাঁটু শক্তভাবে ধারণ করতে হবে।",
                "মাথা পিঠের লাইনে সমান রাখুন - খুব ঝুলে থাকবে না বা উঁচু থাকবে না।"
            ),
            correctDetailsEn = listOf(
                "Keep your back completely flat and horizontal to the floor.",
                "Grasp your knees firmly with your fingers spread.",
                "Keep your head level with your spine—neither raised nor hanging."
            ),
            wrongTitleBn = "ভুল রুকু পদ্ধতি ❌",
            wrongTitleEn = "Common Bowing Mistakes ❌",
            wrongDetailsBn = listOf(
                "পিঠ ধনুকের মতো বাঁকা বা কুঁজো করে রাখা।",
                "কনুই ও পিঠ শিথিল রাখা, হাঁটু ঠিকমতো জড়িয়ে না ধরা।",
                "মাথা নিচের দিকে ঝুলিয়ে রাখা অথবা কপাল সামনের দিকে তুলে রাখা।"
            ),
            wrongDetailsEn = listOf(
                "Curving or hunching your back like a loop.",
                "Keeping elbows bent inward or ignoring proper grip.",
                "Hanging the head vertically or stretching it upward."
            ),
            diagramType = "ruku"
        ),
        PostureComparison(
            id = 3,
            postureNameBn = "৩. সিজদাহ বা সমর্পণ",
            postureNameEn = "3. Prostration (Sajdah)",
            correctTitleBn = "সঠিক সিজদাহ পদ্ধতি ✔",
            correctTitleEn = "Correct Prostration ✔",
            correctDetailsBn = listOf(
                "কপাল ও নাক উভয়ই মাটিতে শক্তভাবে স্পর্শ করান।",
                "দুই কনুই মাটি থেকে ওপরে ও পাঁজর থেকে বাইরে ফাঁকা রাখুন।",
                "দুই পা একসাথে খাড়া রেখে পায়ের আঙুল ক্বিবলামুখী রাখুন।"
            ),
            correctDetailsEn = listOf(
                "Ensure both your forehead and nose make firm contact with the floor.",
                "Keep your elbows raised off the ground and away from ribs.",
                "Keep your feet upright with your toes pointing towards the Qibla."
            ),
            wrongTitleBn = "ভুল সিজদাহ পদ্ধতি ❌",
            wrongTitleEn = "Common Sajdah Mistakes ❌",
            wrongDetailsBn = listOf(
                "শুধু কপাল ছোঁয়ানো, নাক মাটি থেকে উঁচিয়ে রাখা।",
                "দুই কনুই ও বাহু কুকুরের মতো মাটিতে বিছিয়ে দেওয়া বা মেঝেতে লাগিয়ে রাখা।",
                "পা মাটি থেকে তুলে রাখা বা আঙুল না বাঁকিয়ে শুইয়ে রাখা।"
            ),
            wrongDetailsEn = listOf(
                "Touching forehead while leaving the nose hovering.",
                "Laying forearms/elbows completely flat on the ground.",
                "Lifting feet off the ground or neglecting bent toes."
            ),
            diagramType = "sajdah"
        ),
        PostureComparison(
            id = 4,
            postureNameBn = "৪. বৈঠকে বসা",
            postureNameEn = "4. Sitting (Jalsah)",
            correctTitleBn = "সঠিক বৈঠক পদ্ধতি ✔",
            correctTitleEn = "Correct Sitting Position ✔",
            correctDetailsBn = listOf(
                "বাম পা মাটিতে বিছিয়ে তার ওপরে শান্তভাবে বসুন।",
                "ডান পা খাড়া রাখুন যার আঙুল ক্বিবলার দিকে বাঁকানো থাকবে।",
                "হাত দুটি দুই উরুর ওপরে পাতা অবস্থায় সোজা সোজা রাখুন।"
            ),
            correctDetailsEn = listOf(
                "Sit flat on your left foot laid horizontally on the carpet.",
                "Keep your right foot vertical with the toes folded forward pointing to Qibla.",
                "Place both hands flat on your thighs nicely."
            ),
            wrongTitleBn = "ভুল বৈঠক পদ্ধতি ❌",
            wrongTitleEn = "Common Sitting Mistakes ❌",
            wrongDetailsBn = listOf(
                "দুই পা খাড়া রেখে দুই গোড়ালির ওপরে বসা (ইক'আ)।",
                "বাম পা খাড়া রাখা বা পা অগোছালোভাবে ভাঁজ করে বসা।",
                "তাড়াহুড়ো করে সোজা হয়ে বসার আগেই দ্বিতীয় সিজদায় চলে যাওয়া।"
            ),
            wrongDetailsEn = listOf(
                "Squatting or sitting on both heels with upright feet.",
                "Keeping the left foot vertical or twisted carelessly.",
                "Going straight to next sajdah without fully pausing in an upright seat."
            ),
            diagramType = "jalsah"
        )
    )

    val wuduFarzBn = listOf(
        "ওযুর ফরজ মূলত ৪ টি - এগুলো বাদ পড়লে ওযু বাতিল হয়ে যাবে। ফরজসমূহ নিম্নরূপ:",
        "১. সমস্ত মুখমন্ডল কপাল থেকে থুতনি এবং এক কানের লতি থেকে অন্য কান পর্যন্ত ধৌত করা।",
        "২. দুই হাতের কনুইসহ ভালো করে ধৌত করা।",
        "৩. মাথার চার ভাগের এক ভাগ মাসেহ করা।",
        "৪. দুই পায়ের টাখনুসহ ভালো করে ধৌত করা।"
    )

    val wuduFarzEn = listOf(
        "There are 4 obligatory acts (Fard) of Wudu, without which Wudu is not valid:",
        "1. Washing the entire face from hair-line to chin, earlobe to earlobe.",
        "2. Washing both arms up to and including the elbows.",
        "3. Wiping (Masah) at least a quarter of the head with wet hands.",
        "4. Washing both feet up to and including the ankles."
    )

    val wuduInvalidatorsBn = listOf(
        "ওযু প্রধানত ৭ টি কারণে ভেঙে যায় বা বাতিল হয়ে যায়:",
        "১. মূত্র বা পায়খানার রাস্তা দিয়ে কোনো কিছু বের হলে।",
        "২. মুখ ভরে বমি হলে।",
        "৩. শরীরের ক্ষতস্থান থেকে রক্ত, পুঁজ বা পানি বের হয়ে গড়িয়ে পড়লে।",
        "৪. কাত হয়ে, চিত হয়ে বা কোনো কিছুর সাথে হেলান দিয়ে চোখ মুদে গভীর ঘুমিয়ে পড়লে।",
        "৫. পাগল, বেহুঁশ বা অচেতন হয়ে পড়লে।",
        "৬. নামাজে কোনো বালেগ বা প্রাপ্তবয়স্ক ব্যক্তি উচ্চস্বরে খিলখিল করে হাসলে।",
        "৭. অতিরিক্ত নেশাগ্রস্ত হওয়া বা কোনো কঠিন রোগে অজ্ঞান হয়ে পড়লে।"
    )

    val wuduInvalidatorsEn = listOf(
        "Wudu is invalidated or broken by the following 7 major reasons:",
        "1. Excretion of anything from the private or excretory organs (urine, stool, gas, etc.).",
        "2. Vomiting in large mouthful amounts.",
        "3. Flowing of blood, pus, or fluid from any wound/body part.",
        "4. Deep sleep while lying down, reclining, or leaning on support.",
        "5. Fainting, losing consciousness, or sanity.",
        "6. Laughing out loud during prayer (applicable to mature individuals).",
        "7. Extreme intoxication or deep stupor."
    )

    val prayerInvalidatorsBn = listOf(
        "নামাজ প্রধানত নিচের আমল ও कारण সমূহের দ্বারা বাতিল বা নষ্ট হয়ে যায়:",
        "১. নামাজের মধ্যে ইচ্ছা বা অনিচ্ছাকৃত কোনো কথা বা বাক্য মুখে উচ্চারণ করলে।",
        "২. নামাজের ভেতর কারো সালামের উত্তর দিলে কিংবা কাউকে নতুন করে সালাম দিলে।",
        "৩. দুনিয়াবী কোনো সংবাদের প্রত্যুত্তর বা কোনো হাঁচির জবাবে উচ্চস্বরে কিছু বলা।",
        "৪. দুনিয়াবী কষ্ট বা বেদনায় উফ-আহ শব্দ করা কিংবা শব্দ করে ক্রন্দন করা (আল্লাহর ভয়ে ক্রন্দন করা প্রশংসনীয়)।",
        "৫. বিনা ওজরে বা অহেতুক গলা ঝাড়া বা ইচ্ছা করে কাশি দিলে।",
        "৬. আমলে কাসির করা (এমন অহেতুক কাজ করা যা বাইরে থেকে দেখলে মনে হয় নামাজ পড়ছেন না)।",
        "৭. নামাজের কিরাতে এমন মারাত্মক তাসহিহ বা ভুল করা যার দ্বারা কুরআনের অর্থ পুরোপুরি বিকৃত হয়ে যায়।",
        "৮. নামাজের ভেতরে কোনো কিছু চিবানো, পান করা বা কোনো খাবার মুখে পোরা বা খাওয়া।",
        "৯. কিবলা থেকে বুক বা সিনা ৩৬০ ডিগ্রি বা আংশিকভাবেও ঘুরিয়ে ফেলা।",
        "১০. নামাজের কোনো প্রধান ফরজ অথবা রুকন (যেমনঃ রুকু বা সেজদাহ) অবহেলা করে ত্যাগ করা।",
        "১১. নামাজের ভেতর ওযু ভেঙে গেলে বা অপবিত্রতা স্পর্শ করলে।"
    )

    val prayerInvalidatorsEn = listOf(
        "Prayer is invalidated or broken by the following key factors:",
        "1. Uttering any spoken words or speech during prayer, intentionally or otherwise.",
        "2. Initiating a Salam or greeting, or replying to one during prayer.",
        "3. Verbally responding to worldly news or answering someone's sneeze.",
        "4. Sighing, whining, or weeping aloud due to worldly distress or physical pain (weeping out of fear of Allah is permitted).",
        "5. Coughing or clearing the throat deliberately without a genuine, physical reason.",
        "6. Amal-e-Kaseer: Engaged in continuous, excessive movements outside Salat actions.",
        "7. Making major, grotesque errors in Arabic recitation that change the core meanings of Quranic verses.",
        "8. Eating, drinking, or placing any edible item inside the mouth during prayer.",
        "9. Turning the chest away from the Qibla direction.",
        "10. Omitting any mandatory pillar (Fard/Arkan) of the prayer, such as ruku or sajdah.",
        "11. Breaking Wudu or having any major impurity touch the body during Salat."
    )

    val prayerInnerAmalsBn = listOf(
        "নামাজের ভেতর ও বাহিরে পালনযোগ্য গুরুত্বপূর্ণ আমল, সুন্নত ও আদবসমূহ:",
        "১. তাকবীরে তাহরিমার সময় পুরুষদের দুই হাত কান পর্যন্ত এবং নারীদের কাঁধ পর্যন্ত উঠানো অত্যন্ত গুরুত্বপূর্ণ সুন্নত আমল।",
        "২. বাম হাতের পাতার ওপর ডান হাতের পাতা রাখা এবং পুরুষদের ক্ষেত্রে নাভির নিচে হাত বাঁধা ও নারীদের ক্ষেত্রে বুকের ওপর হাত বাঁধা বা নিয়ত করা।",
        "৩. কিয়াম শুরুর শুরুতে ছানা (সুবহানাকাল্লাহুম্মা...) দিয়ে নামায শুরু করা।",
        "৪. সূরা ফাতিহা শুরুর পূর্বে মনে মনে 'আউযুবিল্লাহ' ও 'বিসমিল্লাহ' পড়া এবং সূরা ফাতিহার শেষে মনে মনে বা উচ্চস্বরে 'আমীন' বলা।",
        "৫. সিজদা থেকে উঠার সময় বা যাওয়ার সময় এবং রুকুতে যাওয়ার পূর্বে তাকবীর 'আল্লাহু আকবার' বলা।",
        "৬. রুকুতে গিয়ে কমপক্ষে তিনবার তাসবীহ (সুবহানা রাব্বিয়াল আজীম) পড়া।",
        "৭. রুকু থেকে সোজা হয়ে দাঁড়ানোর সময় 'সামিআল্লাহু লিমান হামিদাহ' বলা এবং দাঁড়িয়ে 'রাব্বানা লাকাল হামদ' আবৃত্তি করা।",
        "৮. সেজদাতে গিয়ে কমপক্ষে তিনবার তাসবীহ (সুবহানা রাব্বিয়াল আ'লা) স্পষ্ট স্বরে পাঠ করা।",
        "৯. দুই সেজদার মধ্যবর্তী ক্ষণস্থায়ী বৈঠকে বসে ক্ষমা ও সুস্থতার দোয়া বা তাসবীহ পড়া।",
        "১০. শেষ বৈঠকে তাশাহহুদ পাঠের পর রাসূল (সা.)-এর প্রতি দরূদে ইব্রাহীম এবং হাদীসে বর্ণিত দোয়া মাসূরা পড়ে সালাম ফেরানো।"
    )

    val prayerInnerAmalsEn = listOf(
        "Important sunnah actions, etiquettes, and inner deeds within the prayers:",
        "1. Raising hands up to the earlobes (men) or to the shoulders (women) when uttering the opening Takbeer.",
        "2. Placing the right palm over the left hand clasping it below the navel (men) or resting comfortably on the chest (women).",
        "3. Starting with Sana (Subhanak-Allahumma...) as the initial opening invocation of Salat.",
        "4. Saying 'A'udhu billahi' and 'Bismillah' silently, and saying 'Ameen' after finishing Surah Al-Fatihah.",
        "5. Pronouncing Takbeer ('Allahu Akbar') while transitioning between Standing, Bowing, and Prostrations.",
        "6. Uttering 'Subhana Rabbiyal Azeem' at least three times clearly while in Bowing (Ruku) position.",
        "7. Saying 'Sami'Allahu liman hamidah' when rising from ruku, and 'Rabbana lakal hamd' when standing upright.",
        "8. Uttering 'Subhana Rabbiyal A'la' at least three times clearly while in Prostration (Sajdah) position.",
        "9. Sitting upright momentarily between both sajdahs and supplicating for forgiveness (Rabbi ghfir li).",
        "10. Reciting Durood-e-Ibrahim on Prophet Muhammad (PBUH) and any recommended Dua-e-Masoora during the final Tashahhud sitting."
    )
}
