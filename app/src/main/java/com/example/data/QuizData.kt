package com.example.data

object QuizData {
    val questions = listOf(
        QuizQuestion(
            id = "q1",
            questionBn = "পবিত্র কুরআনে মোট কতটি সূরা আছে?",
            questionEn = "How many Surahs are there in the Holy Quran?",
            optionsBn = listOf("১১০টি", "১১৪টি", "১২৪টি", "৮৬টি"),
            optionsEn = listOf("110", "114", "124", "86"),
            correctIdx = 1,
            explanationBn = "পবিত্র কুরআনে মোট ১১৪টি সূরা রয়েছে। এর মধ্যে মাক্কী ৮৬টি এবং মাদানী ২৮টি সূরা।",
            explanationEn = "There are 114 Surahs in total in the Holy Quran. 86 are Meccan and 28 are Medinan."
        ),
        QuizQuestion(
            id = "q2",
            questionBn = "ইসলামের প্রথম যুদ্ধ কোনটি?",
            questionEn = "What was the first battle in Islam?",
            optionsBn = listOf("উহুদের যুদ্ধ", "খন্দকের যুদ্ধ", "বদরের যুদ্ধ", "খায়বারের যুদ্ধ"),
            optionsEn = listOf("Battle of Uhud", "Battle of Khandaq", "Battle of Badr", "Battle of Khyber"),
            correctIdx = 2,
            explanationBn = "২য় হিজরীর ১৭ই রমজান ঐতিহাসিক বদরের যুদ্ধ সংঘটিত হয়। এটি ইসলামের প্রথম স্বশস্ত্র যুদ্ধ।",
            explanationEn = "The Battle of Badr took place on the 17th of Ramadan, 2 AH. It was the first armed conflict in Islam."
        ),
        QuizQuestion(
            id = "q3",
            questionBn = "মহিলাদের মধ্যে কে প্রথম ইসলাম গ্রহণ করেন?",
            questionEn = "Who was the first woman to embrace Islam?",
            optionsBn = listOf("হযরত আয়েশা (রা.)", "হযরত খাদিজা (রা.)", "হযরত ফাতেমা (রা.)", "হযরত সুমাইয়া (রা.)"),
            optionsEn = listOf("Aisha (R.A.)", "Khadija (R.A.)", "Fatimah (R.A.)", "Sumayyah (R.A.)"),
            correctIdx = 1,
            explanationBn = "রাসূলুল্লাহ (সা.)-এর প্রথম স্ত্রী হযরত খাদিজাতুল কুবরা (রা.) সর্বপ্রথম ইসলাম গ্রহণ করেন।",
            explanationEn = "Prophet Muhammad's (PBUH) first wife, Khadija (R.A.), was the first person to accept Islam."
        ),
        QuizQuestion(
            id = "q4",
            questionBn = "পবিত্র কুরআনে রাসুলুল্লাহ (সা.)-এর নাম 'মুহাম্মদ' হিসেবে কতবার উল্লেখ করা হয়েছে?",
            questionEn = "How many times is the name 'Muhammad' mentioned in the Quran?",
            optionsBn = listOf("৩ বার", "৪ বার", "৫ বার", "১০ বার"),
            optionsEn = listOf("3 times", "4 times", "5 times", "10 times"),
            correctIdx = 1,
            explanationBn = "পবিত্র কুরআনে সরাসরি 'মুহাম্মদ' নাম ৪ বার এবং 'আহমাদ' নাম ১ বার উল্লেখ করা হয়েছে।",
            explanationEn = "The name 'Muhammad' is mentioned directly 4 times in the Quran, and the name 'Ahmad' is mentioned once."
        ),
        QuizQuestion(
            id = "q5",
            questionBn = "কোন সাহাবীকে 'আস-সিদ্দিক' উপাধি দেওয়া হয়েছিল?",
            questionEn = "Which companion was given the title 'As-Siddiq' (The Truthful)?",
            optionsBn = listOf("হযরত ওমর (রা.)", "হযরত ওসমান (রা.)", "হযরত আবু বকর (রা.)", "হযরত আলী (রা.)"),
            optionsEn = listOf("Umar (R.A.)", "Uthman (R.A.)", "Abu Bakr (R.A.)", "Ali (R.A.)"),
            correctIdx = 2,
            explanationBn = "মেরাজের ঘটনাকে বিনা দ্বিধায় বিশ্বাস করার কারণে হযরত আবু বকর (রা.)-কে রাসূলুল্লাহ (সা.) 'আস-সিদ্দিক' বা মহাসত্যবাদী উপাধি দেন।",
            explanationEn = "Prophet Muhammad (PBUH) titled Abu Bakr (R.A.) 'As-Siddiq' due to his immediate and unwavering belief in the Mi'raj (ascension) event."
        ),
        QuizQuestion(
            id = "q6",
            questionBn = "মেরাজের রাতে প্রথমে কত ওয়াক্ত নামাজ ফরজ করা হয়েছিল?",
            questionEn = "Initially, how many daily prayers were prescribed during Mi'raj?",
            optionsBn = listOf("৫ ওয়াক্ত", "১০ ওয়াক্ত", "৫০ ওয়াক্ত", "২৫ ওয়াক্ত"),
            optionsEn = listOf("5 times", "10 times", "50 times", "25 times"),
            correctIdx = 2,
            explanationBn = "মেরাজের রাতে প্রথমে ৫০ ওয়াক্ত নামাজ ফরজ করা হয়েছিল। পরে হযরত মুসা (আ.)-এর পরামর্শে কয়েক দফায় তা কমিয়ে ৫ ওয়াক্ত করা হয়, তবে পুণ্যের দিক থেকে তা ৫০ ওয়াক্তেরই সমান।",
            explanationEn = "Initially, 50 daily prayers were made obligatory. Upon advice from Prophet Musa (A.S.), it was reduced to 5, though the reward remains equivalent to 50."
        ),
        QuizQuestion(
            id = "q7",
            questionBn = "কোন নবীকে জ্বলন্ত আগুনে নিক্ষেপ করা হয়েছিল কিন্তু আগুন তাকে স্পর্শ করেনি?",
            questionEn = "Which Prophet was thrown into the fire but was not harmed?",
            optionsBn = listOf("হযরত নূহ (আ.)", "হযরত ইব্রাহিম (আ.)", "হযরত মুসা (আ.)", "হযরত ঈসা (আ.)"),
            optionsEn = listOf("Nuh (A.S.)", "Ibrahim (A.S.)", "Musa (A.S.)", "Isa (A.S.)"),
            correctIdx = 1,
            explanationBn = "রাজা নমরূদ হযরত ইব্রাহিম (আ.)-কে আগুনে নিক্ষেপ করেছিল, কিন্তু আল্লাহর হুকুমে আগুন শান্তিদায়ক ও শীতল হয়ে গিয়েছিল।",
            explanationEn = "King Nimrod threw Prophet Ibrahim (A.S.) into a colossal fire, but by Allah's command, the fire became cool and safe for him."
        ),
        QuizQuestion(
            id = "q8",
            questionBn = "পবিত্র কুরআন কোন মাসে অবতীর্ণ হওয়া শুরু হয়?",
            questionEn = "In which month did the revelation of the Quran begin?",
            optionsBn = listOf("মহররম মাস", "জিলহজ মাস", "রমজান মাস", "রবিউল আউয়াল মাস"),
            optionsEn = listOf("Muharram", "Dhul-Hijjah", "Ramadan", "Rabi' al-awwal"),
            correctIdx = 2,
            explanationBn = "রমজান মাসের শবে কদরে (লাইলাতুল কদর) হেরা গুহায় প্রথম পবিত্র কুরআন নাযিল হওয়া শুরু হয়।",
            explanationEn = "The revelation of the Holy Quran first commenced in the Cave of Hira during Laylatul Qadr in the Holy month of Ramadan."
        ),
        QuizQuestion(
            id = "q9",
            questionBn = "পবিত্র কুরআনের কোন সূরাটি সবচেয়ে দীর্ঘ?",
            questionEn = "Which Surah is the longest in the Holy Quran?",
            optionsBn = listOf("সূরা আল-ইমরান", "সূরা আল-বাকারা", "সূরা আন-নিসা", "সূরা আল-মায়েদা"),
            optionsEn = listOf("Surah Al-Imran", "Surah Al-Baqarah", "Surah An-Nisa", "Surah Al-Ma'idah"),
            correctIdx = 1,
            explanationBn = "সূরা আল-বাকারা পবিত্র কুরআনের ২য় সূরা এবং এটিই সবচেয়ে দীর্ঘ। এর মোট আয়াত সংখ্যা ২৮৬টি।",
            explanationEn = "Surah Al-Baqarah is the 2nd Surah and the longest in the Quran, containing 286 verses."
        ),
        QuizQuestion(
            id = "q10",
            questionBn = "কোন সূরাকে কুরআনের 'মন বা হৃদয়' বলা হয়?",
            questionEn = "Which Surah is known as the 'Heart' of the Quran?",
            optionsBn = listOf("সূরা আর-রহমান", "সূরা আল-ফাতিহা", "সূরা ইয়াসিন", "সূরা আল-মুলক"),
            optionsEn = listOf("Surah Ar-Rahman", "Surah Al-Fatihah", "Surah Ya-Sin", "Surah Al-Mulk"),
            correctIdx = 2,
            explanationBn = "একটি হাদিসে এসেছে, রাসূলুল্লাহ (সা.) বলেছেন, প্রতিটি জিনিসের একটি হৃদয় থাকে এবং পবিত্র কুরআনের হৃদয় হলো সূরা ইয়াসিন।",
            explanationEn = "According to a well-known Hadith, everything has a heart, and the heart of the Holy Quran is Surah Ya-Sin."
        ),
        QuizQuestion(
            id = "q11",
            questionBn = "ইসলামের স্তম্ভ বা ভিত্তি কয়টি?",
            questionEn = "How many pillars of Islam are there?",
            optionsBn = listOf("৩টি", "৫টি", "৭টি", "১০টি"),
            optionsEn = listOf("3", "5", "7", "10"),
            correctIdx = 1,
            explanationBn = "ইসলামের মূল ভিত্তি বা স্তম্ভ ৫টি: ঈমান, সালাত, সাওম (রোজা), যাকাত ও হজ।",
            explanationEn = "Islam is built upon five pillars: Shahadah (Faith), Salah (Prayer), Sawm (Fasting), Zakat (Almsgiving), and Hajj (Pilgrimage)."
        ),
        QuizQuestion(
            id = "q12",
            questionBn = "আল্লাহর তরফ থেকে ওহী বা বাণী রাসুলদের কাছে নিয়ে আসতেন কোন ফেরেশতা?",
            questionEn = "Which Angel was responsible for bringing revelations to the Prophets?",
            optionsBn = listOf("হযরত জিবরাঈল (আ.)", "হযরত মিকাঈল (আ.)", "হযরত আজরাঈল (আ.)", "হযরত ইসরাফীল (আ.)"),
            optionsEn = listOf("Jibril (A.S.)", "Mikail (A.S.)", "Azrail (A.S.)", "Israfil (A.S.)"),
            correctIdx = 0,
            explanationBn = "হযরত জিবরাঈল (আ.) ওহী বহনের দায়িত্বে নিয়োজিত প্রধান ফেরেশতা ছিলেন।",
            explanationEn = "Angel Jibril (Gabriel) A.S. was the chief angel responsible for delivering Allah's revelations to the Prophets."
        ),
        QuizQuestion(
            id = "q13",
            questionBn = "রাসূলুল্লাহ (সা.) কোন শহরে জন্মগ্রহণ করেন?",
            questionEn = "In which city was Prophet Muhammad (PBUH) born?",
            optionsBn = listOf("মদীনা", "মক্কা", "তায়েফ", "জেরুজালেম"),
            optionsEn = listOf("Madinah", "Makkah", "Taif", "Jerusalem"),
            correctIdx = 1,
            explanationBn = "রাসূলুল্লাহ (সা.) ৫৭০ খ্রিষ্টাব্দে হিজাজের প্রধান নগরী পবিত্র মক্কা নগরীতে কুরাইশ বংশে জন্মগ্রহণ করেন।",
            explanationEn = "Prophet Muhammad (PBUH) was born in 570 CE in the prestigious city of Makkah, located in the Hijaz region."
        ),
        QuizQuestion(
            id = "q14",
            questionBn = "হিজরতের পর রাসূলুল্লাহ (সা.) মদীনায় কত বছর অবস্থান করেছিলেন?",
            questionEn = "How many years did the Prophet (PBUH) live in Madinah after Hijrah?",
            optionsBn = listOf("৫ বছর", "৮ বছর", "১০ বছর", "১৩ বছর"),
            optionsEn = listOf("5 years", "8 years", "10 years", "13 years"),
            correctIdx = 2,
            explanationBn = "হিজরতের পর তিনি মদীনাতুল মুনাওয়ারায় ১০ বছর অবস্থান করেন এবং ইসলামের প্রচার ও মদীনা রাষ্ট্র পরিচালনা করেন।",
            explanationEn = "Following the Hijrah (migration), Prophet Muhammad (PBUH) spent 10 years in Madinah establishing and spreading Islam."
        ),
        QuizQuestion(
            id = "q15",
            questionBn = "ইসলামের প্রথম মুয়াজ্জিন বা আযান দাতা কে?",
            questionEn = "Who was the first Muezzin (caller to prayer) in Islam?",
            optionsBn = listOf("হযরত আবু বকর (রা.)", "হযরত সালমান ফারসী (রা.)", "হযরত বিলাল ইবনে রাবাহ (রা.)", "হযরত আব্দুল্লাহ ইবনে মাসউদ (রা.)"),
            optionsEn = listOf("Abu Bakr (R.A.)", "Salman Al-Farsi (R.A.)", "Bilal ibn Rabah (R.A.)", "Abdullah ibn Masood (R.A.)"),
            correctIdx = 2,
            explanationBn = "হাবশী কৃতদাস থেকে মুক্ত হওয়া প্রিয় সাহাবী হযরত বিলাল ইবনে রাবাহ (রা.) ইসলামের প্রথম মুয়াজ্জিন ছিলেন।",
            explanationEn = "Bilal ibn Rabah (R.A.), a close African companion freed from slavery, was appointed as the very first Muezzin of Islam."
        ),
        QuizQuestion(
            id = "q16",
            questionBn = "পবিত্র কুরআনের কোন সূরাটি 'বিসমিল্লাহ' ছাড়া শুরু হয়েছে?",
            questionEn = "Which Surah in the Holy Quran does not begin with 'Bismillah'?",
            optionsBn = listOf("সূরা আত-তাওবাহ", "সূরা আন-নামল", "সূরা আল-কাফিরুন", "সূরা আল-ইখলাস"),
            optionsEn = listOf("Surah At-Tawbah", "Surah An-Naml", "Surah Al-Kafiroon", "Surah Al-Ikhlas"),
            correctIdx = 0,
            explanationBn = "সূরা আত-তাওবাহ এর শুরুতে বিসমিল্লাহ নেই। অপরদিকে সূরা আন-নামল-এ দুইবার বিসমিল্লাহ এসেছে।",
            explanationEn = "Surah At-Tawbah is the only Surah that does not start with Bismillah. Conversely, Surah An-Naml contains it twice."
        ),
        QuizQuestion(
            id = "q17",
            questionBn = "কোন খলিফাকে 'যুন-নুরাইন' (দুই জ্যোতির অধিকারী) বলা হয়?",
            questionEn = "Which Caliph was known as 'Dhun-Noorayn' (Possessor of Two Lights)?",
            optionsBn = listOf("হযরত আবু বকর (রা.)", "হযরত ওমর (রা.)", "হযরত ওসমান (রা.)", "হযরত আলী (রা.)"),
            optionsEn = listOf("Abu Bakr (R.A.)", "Umar (R.A.)", "Uthman (R.A.)", "Ali (R.A.)"),
            correctIdx = 2,
            explanationBn = "রাসূলুল্লাহ্ (সা.)-এর দুই কন্যা হযরত রুকাইয়া ও উম্মে কুলসুম-কে বিয়ে করার জন্য হযরত ওসমান (রা.)-কে যুন-নুরাইন বা দুই জ্যোতির অধিকারী বলা হয়।",
            explanationEn = "Uthman (R.A.) is referred to as 'Dhun-Noorayn' because he had the unique honor of marrying two daughters of the Prophet (PBUH) in succession."
        ),
        QuizQuestion(
            id = "q18",
            questionBn = "সপ্তাহের কোন দিনটিকে মুসলিমদের জন্য সাপ্তাহিক ঈদের দিন ও শ্রেষ্ঠ দিন গণ্য করা হয়?",
            questionEn = "Which day is regarded as the best weekly day for Muslims?",
            optionsBn = listOf("সোমবার", "বৃহস্পতিবার", "শুক্রবার (জুম্মা)", "রবিবার"),
            optionsEn = listOf("Monday", "Thursday", "Friday (Jummah)", "Sunday"),
            correctIdx = 2,
            explanationBn = "শুক্রবার বা জুম্মার দিনকে সপ্তাহের শ্রেষ্ঠ দিন এবং গরিবের হজের দিন গণ্য করা হয়।",
            explanationEn = "Friday (Jummah) is considered the master of all days of the week, highly sacred and blessed in Islam."
        ),
        QuizQuestion(
            id = "q19",
            questionBn = "যাকাত ফরয হওয়ার জন্য সম্পদের শতকরা হার কত?",
            questionEn = "What is the compulsory percentage rate of Zakat on accumulative wealth?",
            optionsBn = listOf("১.৫%", "২.৫%", "৫.০%", "১০%"),
            optionsEn = listOf("1.5%", "2.5%", "5.0%", "10%"),
            correctIdx = 1,
            explanationBn = "নেসাব পরিমাণ যাকাতযোগ্য সম্পদ এক বছর জমাকৃত থাকলে তার উপর বার্ষিক ২.৫% (শতকরা আড়াই টাকা) যাকাত প্রদান করা ফরজ।",
            explanationEn = "Muslims who meet the Nisab threshold must pay 2.5% of their qualifying annual savings as Zakat."
        ),
        QuizQuestion(
            id = "q20",
            questionBn = "ইসলামী শরীয়তে ক্বিবলা দিক পরিবর্তনের পূর্বে প্রথম ক্বিবলা কোনটি ছিল?",
            questionEn = "What was the first Qibla (direction of prayer) before Kaaba?",
            optionsBn = listOf("মসজিদুল হারাম", "মসজিদুল আকসা (বায়তুল মুকাদ্দাস)", "মসজিদে নববী", "মসজিদে কুবা"),
            optionsEn = listOf("Masjid al-Haram", "Masjid al-Aqsa (Jerusalem)", "Masjid an-Nabawi", "Masjid Quba"),
            correctIdx = 1,
            explanationBn = "হিজরতের প্রায় ১৭ মাস পর্যন্ত রাসূলুল্লাহ্ (সা.) ও সাহাবীগণ বায়তুল মুকাদ্দাস (মসজিদুল আকসা) এর দিকে ফিরে সালাত আদায় করতেন। এরপর কাবা ঘর ক্বিবলা হিসেবে নির্ধারিত হয়।",
            explanationEn = "Before changing the direction toward the Kaaba in Makkah, Muslims initially faced Masjid al-Aqsa in Jerusalem for their prayers."
        ),
        QuizQuestion(
            id = "q21",
            questionBn = "আল্লাহু তায়ালা হযরত মূসা (আ.)-এর উপর কোন আসমানী কিতাব অবতীর্ণ করেছিলেন?",
            questionEn = "Which divine scripture was revealed to Prophet Musa (A.S.)?",
            optionsBn = listOf("তাওরাত", "যবুর", "ইঞ্জিল", "কুরআন"),
            optionsEn = listOf("Tawrat (Torah)", "Zabur (Psalms)", "Injeel (Gospel)", "Quran"),
            correctIdx = 0,
            explanationBn = "আল্লাহ তাআলা প্রধান চার জন রাসূলের উপর চারটি প্রধান কিতাব অবতীর্ণ করেছেন। হযরত মূসা (আ.)-এর প্রতি তাওরাত নাযিল করা হয়েছিল।",
            explanationEn = "Allah revealed four major scriptural books. The Tawrat (Torah) was sent down to Prophet Musa (Leviticus/Moses)."
        ),
        QuizQuestion(
            id = "q22",
            questionBn = "আল্লাহ তায়ালা হযরত ঈসা (আ.)-এর উপর কোন আসমানী কিতাব অবতীর্ণ করেন?",
            questionEn = "Which divine scripture was revealed to Prophet Isa (A.S.)?",
            optionsBn = listOf("তাওরাত", "যবুর", "ইঞ্জিল", "কুরআন"),
            optionsEn = listOf("Tawrat", "Zabur", "Injeel (Gospel)", "Quran"),
            correctIdx = 2,
            explanationBn = "হযরত ঈসা (আ.)-এর উপর ইঞ্জিল কিতাব অবতীর্ণ হয়েছিল যা পরবর্তীতে বাইবেল নামে পরিচিত হয়।",
            explanationEn = "The Injeel (Gospel) was revealed to Prophet Isa (A.S.) by Allah as a guideway."
        ),
        QuizQuestion(
            id = "q23",
            questionBn = "কোন নবী পশু-পাখি ও বাতাসের ভাষা বুঝতে পারতেন এবং তাদের পরিচালনা করার ক্ষমতা রাখতেন?",
            questionEn = "Which Prophet could understand the language of animals and control the wind?",
            optionsBn = listOf("হযরত দাউদ (আ.)", "হযরত সুলাইমান (আ.)", "হযরত ইউসুফ (আ.)", "হযরত ইউনুস (আ.)"),
            optionsEn = listOf("Dawud (A.S.)", "Sulaiman (A.S.)", "Yusuf (A.S.)", "Yunus (A.S.)"),
            correctIdx = 1,
            explanationBn = "হযরত সুলাইমান (আ.) আল্লাহ প্রদত্ত মুজিযাবলে জিন জাতি, বাতাস এবং সমস্ত পশু-পাখির ভাষা বুঝতেন ও তাদের শাসন করতেন।",
            explanationEn = "Prophet Sulaiman (Solomon) A.S. was uniquely gifted with miracles to converse with animals, Jinns, and control the wind flow."
        ),
        QuizQuestion(
            id = "q24",
            questionBn = "ইসলামী হিজরী সনের প্রবর্তন করেন কোন খলিফা?",
            questionEn = "Which caliph introduced the Hijri Calendar?",
            optionsBn = listOf("হযরত আবু বকর (রা.)", "হযরত ওমর ইবনুল খাত্তাব (রা.)", "হযরত ওসমান (রা.)", "হযরত আলী (রা.)"),
            optionsEn = listOf("Abu Bakr (R.A.)", "Umar ibn al-Khattab (R.A.)", "Uthman (R.A.)", "Ali (R.A.)"),
            correctIdx = 1,
            explanationBn = "হযরত ওমর (রা.)-এর খেলাফতকালে সাহাবীদের পরামর্শক্রমে রাসূলুল্লাহ (সা.)-এর হিজরতের বছর থেকে হিজরী সনের গণনা প্রবর্তন করা হয়।",
            explanationEn = "The Hijri Islamic calendar was introduced by the second Caliph, Umar ibn al-Khattab (R.A.), referencing the Prophet's migration year."
        ),
        QuizQuestion(
            id = "q25",
            questionBn = "হজ ও ওমরার সময় কাবা শরীফ কতবার তাওয়াফ (প্রদক্ষিণ) করতে হয়?",
            questionEn = "How many times must Muslims circle the Kaaba during Tawaf?",
            optionsBn = listOf("৩ বার", "৫ বার", "৭ বার", "১০ বার"),
            optionsEn = listOf("3 times", "5 times", "7 times", "10 times"),
            correctIdx = 2,
            explanationBn = "হজ এবং ওমরার তাওয়াফে কাবা শরীফ হাজরে আসওয়াদ থেকে শুরু করে ঘড়ির কাটার বিপরীত দিকে ৭ বার চক্কর দেওয়া ওয়াজিব।",
            explanationEn = "During Tawaf (circumambulation) of Hajj or Umrah, Muslims are required to encircle the Holy Kaaba exactly 7 times."
        ),
        QuizQuestion(
            id = "q26",
            questionBn = "ইসলামিক সনের কোন মাসে পবিত্র ঈদুল ফিতর উদযাপিত হয়?",
            questionEn = "In which Islamic month is Eid-ul-Fitr celebrated?",
            optionsBn = listOf("রমজান মাস", "শাওয়াল মাস", "মহররম মাস", "জিলহজ মাস"),
            optionsEn = listOf("Ramadan", "Shawwal", "Muharram", "Dhul-Hijjah"),
            correctIdx = 1,
            explanationBn = "পবিত্র রমজান মাসের পূর্ণ সিয়াম সাধনার পর শাওয়াল মাসের ১ম তারিখ মুসলমানদের মহিমান্বিত আনন্দের দিন পবিত্র ঈদুল ফিতর উদযাপিত হয়।",
            explanationEn = "Eid-ul-Fitr is celebrated on the 1st of Shawwal, marking the end of the holy fasting month of Ramadan."
        ),
        QuizQuestion(
            id = "q27",
            questionBn = "পবিত্র কুরআনে মোট কতটি সেজদার আয়াত আছে?",
            questionEn = "How many Sajdahs of recitation (Sajdah-e-Tilawat) are there in the Quran?",
            optionsBn = listOf("১০টি", "১২টি", "১৪টি", "১৮টি"),
            optionsEn = listOf("10", "12", "14", "18"),
            correctIdx = 2,
            explanationBn = "পবিত্র কুরআনের বিভিন্ন আয়াতে মোট ১৪টি স্থানে তিলাওয়াতের সেজদা দিতে রবের সামনে অবনত হওয়ার নির্দেশ রয়েছে।",
            explanationEn = "There are 14 verses of Sajdah in the Quran where prostration is compulsory upon recitation or listening."
        ),
        QuizQuestion(
            id = "q28",
            questionBn = "কোন বীর সাহাবীকে রাসুলুল্লাহ (সা.) 'সাইফুল্লাহ' (আল্লাহর তরবারি) উপাধি দিয়েছিলেন?",
            questionEn = "Which brave companion was given the title 'Saifullah' (the Sword of Allah)?",
            optionsBn = listOf("হযরত আলী (রা.)", "হযরত খালিদ বিন ওয়ালিদ (রা.)", "হযরত হামযা (রা.)", "হযরত সা'দ ইবনে আবি ওয়াক্কাস (রা.)"),
            optionsEn = listOf("Ali (R.A.)", "Khalid bin Walid (R.A.)", "Hamzah (R.A.)", "Sa'd ibn Abi Waqqas (R.A.)"),
            correctIdx = 1,
            explanationBn = "ঐতিহাসিক মুতার যুদ্ধে অসাধারণ কৃতিত্ব ও বীরত্ব প্রদর্শনের কারণে হযরত খালিদ বিন ওয়ালিদ (রা.)-কে আল্লাহর রসূল (সা.) 'সাইফুল্লাহ' উপাধিতে ভূষিত করেন।",
            explanationEn = "Prophet Muhammad (PBUH) titled Khalid bin Walid (R.A.) 'Saifullah' due to his extraordinary military strategy and courage during the Battle of Mu'tah."
        ),
        QuizQuestion(
            id = "q29",
            questionBn = "কোন হিজরী মাসে পবিত্র হজ পালন করা হয়?",
            questionEn = "In which Islamic Hijri month is the Hajj pilgrimage performed?",
            optionsBn = listOf("রমজান মাস", "রজব মাস", "শাওয়াল মাস", "জিলহজ মাস"),
            optionsEn = listOf("Ramadan", "Rajab", "Shawwal", "Dhul-Hijjah"),
            correctIdx = 3,
            explanationBn = "ইসলামের অন্যতম প্রধান স্তম্ভ হজ প্রতি বছর জিলহজ মাসের ৮ তারিখ থেকে ১২ তারিখ পর্যন্ত পবিত্র মক্কায় পালন করা হয়।",
            explanationEn = "Hajj, the fifth pillar of Islam, is performed annually in the holy month of Dhul-Hijjah, from the 8th to the 12th of the month."
        ),
        QuizQuestion(
            id = "q30",
            questionBn = "কিয়ামতের দিনে হাশরের ময়দানে রাসুলুল্লাহ (সা.) উম্মতদের যে হাউজ থেকে পানি পান করাবেন তার নাম কী?",
            questionEn = "What is the name of the pool from which the Prophet will quench the thirst of believers on Judgement Day?",
            optionsBn = listOf("হাউজে কাউসার", "জমজম কূপ", "নাহরুত তাসনীম", "সালসাবিল"),
            optionsEn = listOf("Hawd al-Kauthar", "Zamzam Well", "Nahr-ut-Tasneem", "Salsabeel"),
            correctIdx = 0,
            explanationBn = "হাউজে কাউসার হলো জান্নাতের এমন একটি ঝরনা ও হাউজ যার পানি দুধের চেয়ে সাদা এবং মধুর চেয়ে মিষ্টি, যা থেকে উম্মতগণ তৃষ্ণা মেটাবেন।",
            explanationEn = "Hawd al-Kauthar is the special pool of abundance given to our Prophet Muhammad (PBUH) on the Day of Judgment."
        ),
        QuizQuestion(
            id = "q31",
            questionBn = "কোন নবীকে বিশাল এক মাছ গিলে ফেলেছিল এবং তিনি মাছের পেটে আল্লাহর ইবাদত করেছিলেন?",
            questionEn = "Which Prophet was swallowed by a massive fish/whale and prayed inside its belly?",
            optionsBn = listOf("হযরত নূহ (আ.)", "হযরত ইউনুস (আ.)", "হযরত আইয়ুব (আ.)", "হযরত ইউসুফ (আ.)"),
            optionsEn = listOf("Nuh (A.S.)", "Yunus (A.S.)", "Ayyub (A.S.)", "Yusuf (A.S.)"),
            correctIdx = 1,
            explanationBn = "আল্লাহ তাআলার হুকুমে হযরত ইউনুস (আ.)-কে মাছ গ্রাস করে নিয়েছিল। তিনি মাছের পেটে অত্যন্ত জনপ্রিয় তওবা বা 'লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনায যলিমীন' জিকির করে মুক্তি পান।",
            explanationEn = "Prophet Yunus (Jonah) A.S. was swallowed by a large fish and made the famous repentance prayer of 'La ilaha illa Anta...' to seek Allah's help."
        ),
        QuizQuestion(
            id = "q32",
            questionBn = "কোন বীর সাহাবীকে 'আসাদুল্লাহ' (আল্লাহর সিংহ) উপাধি দেওয়া হয়েছিল?",
            questionEn = "Which companion was given the title 'Asadullah' (the Lion of Allah)?",
            optionsBn = listOf("হযরত আলী (রা.)", "হযরত ওমর (রা.)", "হযরত হামযা ইবনে আব্দুল মুত্তালিব (রা.)", "হযরত খালিদ ইবনে ওয়ালিদ (রা.)"),
            optionsEn = listOf("Ali (R.A.)", "Umar (R.A.)", "Hamzah ibn Abdul-Muttalib (R.A.)", "Khalid ibn Walid (R.A.)"),
            correctIdx = 2,
            explanationBn = "রাসুলুল্লাহ (সা.)-এর চাচা হযরত হামযা (রা.) রাসুলের অত্যন্ত প্রিয় ছিলেন এবং বদর ও উহুদের যুদ্ধে অনন্য বীরত্বের জন্য তিনি 'আসাদুল্লাহ' বা আল্লাহর সিংহ হিসেবে স্মরণীয়।" +
                    "\n(নোট: পরবর্তীতে হযরত আলী (রা.)-কে 'আসাদুল্লাহিল গালিব' বলা হতো।)",
            explanationEn = "Prophet Muhammad's uncle Hamzah (R.A.) was titled 'Asadullah' due to his unmatched strength and valiant defense of Islam in early battles."
        ),
        QuizQuestion(
            id = "q33",
            questionBn = "ইসলামী বিশ্বাস অনুযায়ী জাহান্নাম বা দোজখের কতটি প্রধান দরজা বা গেট আছে?",
            questionEn = "According to Islamic belief, how many gates does Jahannam (Hell) have?",
            optionsBn = listOf("৩টি", "৫টি", "৭টি", "৮টি"),
            optionsEn = listOf("3", "5", "7", "8"),
            correctIdx = 2,
            explanationBn = "সূরা আল-হিজরে এসেছে যে, জাহান্নামের ৭টি প্রধান দরজা রয়েছে এবং প্রতিটি দরজার জন্য পাপীদের নির্দিষ্ট শ্রেণী নির্ধারিত থাকবে।",
            explanationEn = "The Quran mentions in Surah Al-Hijr that Jahannam (Hellfire) has exactly 7 gates, each allocated for a specific category of sinners."
        ),
        QuizQuestion(
            id = "q34",
            questionBn = "মহিমান্বিত জান্নাত বা বেহেশতের কতটি প্রধান ফটক ও স্তর রয়েছে?",
            questionEn = "How many main gates of Jannah (Paradise) are designated in Islam?",
            optionsBn = listOf("৫টি", "৭টি", "৮টি", "১০টি"),
            optionsEn = listOf("5", "7", "8", "10"),
            correctIdx = 2,
            explanationBn = "সহীহ বুখারী ও মুসলিমের হাদীস অনুযায়ী জান্নাতের ৮টি ফটক রয়েছে, যার একটি বাবের নাম 'আর-রাইয়ান' যা দিয়ে শুধুমাত্র রোজাদারগণ প্রবেশ করবেন।",
            explanationEn = "According to authentic Hadiths, Jannah (Paradise) has 8 entrance gates, with one unique gate named Ar-Rayyan dedicated for those who fast."
        ),
        QuizQuestion(
            id = "q35",
            questionBn = "আল্লাহ তাআলার নির্দেশে কোন নবী ও তাঁর পুত্র মিলে পবিত্র কাবা গৃহ পুনরায় নির্মাণ বা নির্মাণ করেছিলেন?",
            questionEn = "Which Prophet and his son constructed the Holy Kaaba in Makkah?",
            optionsBn = listOf("হযরত নূহ ও হযরত সাম (আ.)", "হযরত ইব্রাহিম ও হযরত ইসমাইল (আ.)", "হযরত দাউদ ও হযরত সুলাইমান (আ.)", "হযরত জাকারিয়া ও হযরত ইয়াহইয়া (আ.)"),
            optionsEn = listOf("Nuh & Sam (A.S.)", "Ibrahim & Ishmael (A.S.)", "Dawud & Sulaiman (A.S.)", "Zakariya & Yahya (A.S.)"),
            correctIdx = 1,
            explanationBn = "হযরত ইব্রাহিম (আ.) এবং তাঁর জ্যেষ্ঠ পুত্র হযরত ইসমাইল (আ.) মিলে আল্লাহর আদেশে মক্কার পবিত্র উপত্যকায় কাবা ঘর তৈরি বা সংস্কার সম্পন্ন করেন।",
            explanationEn = "Prophet Ibrahim (Abraham) A.S. and his son Prophet Ismail (Ishmael) A.S. raised the foundation pillars of the Holy Kaaba."
        ),
        QuizQuestion(
            id = "q36",
            questionBn = "জান্নাতের কোন বিশেষ দরজা দিয়ে শুধু রোযাদাররাই কিয়ামতের দিন জান্নাতে প্রবেশ করবেন?",
            questionEn = "Which gate of Heaven is reserved exclusively for those who fast?",
            optionsBn = listOf("বাবুস সালাত", "বাবুল জিহাদ", "বাবুল কাউসার", "আর্-রাইয়ান"),
            optionsEn = listOf("Bab-us-Salah", "Bab-ul-Jihad", "Bab-ul-Kauthar", "Ar-Rayyan"),
            correctIdx = 3,
            explanationBn = "আল্লাহ তাআলা কেবল সিয়াম পালনকারী রোজাদারদের সম্মানিত করতে জান্নাতের 'আর-রাইয়ান' নামক ফটকটি নির্দিষ্ট করেছেন। তারা প্রবেশ করার পর এটি বন্ধ করে দেওয়া হবে।",
            explanationEn = "Ar-Rayyan is the special gate in Paradise designated solely for fasting Muslims to enter through on Resurrection Day."
        ),
        QuizQuestion(
            id = "q37",
            questionBn = "পবিত্র কুরআন প্রায় দীর্ঘ কত বছর ধরে ধাপে ধাপে সফলভাবে অবতীর্ণ ও সম্পূর্ণ হয়েছিল?",
            questionEn = "Over approximately how many years was the Quran gradually revealed?",
            optionsBn = listOf("১০ বছর", "১৩ বছর", "২০ বছর", "২৩ বছর"),
            optionsEn = listOf("10 years", "13 years", "20 years", "23 years"),
            correctIdx = 3,
            explanationBn = "পবিত্র কুরআন মক্কায় ১৩ বছর ও মদীনায় ১০ বছর মিলিয়ে সর্বমোট ২৩ বছরে অল্প অল্প করে অবতীর্ণ সমাপ্ত হয়।",
            explanationEn = "The Quran was revealed slowly over a total period of approximately 23 years (13 years in Makkah and 10 in Madinah)."
        ),
        QuizQuestion(
            id = "q38",
            questionBn = "কোন ঐতিহাসিক যুদ্ধে মুসলিমদের চরম বিপত্তির সম্মুখীন হতে হয় এবং রসূলের দাঁত মোবারক শহীদ হয়েছিল?",
            questionEn = "In which historical battle did Muslims face hardship and the Prophet was wounded?",
            optionsBn = listOf("বদরের যুদ্ধ", "উহুদের যুদ্ধ", "খন্দকের যুদ্ধ", "হুনাইনের যুদ্ধ"),
            optionsEn = listOf("Battle of Badr", "Battle of Uhud", "Battle of Khandaq", "Battle of Hunayn"),
            correctIdx = 1,
            explanationBn = "৩য় হিজরীর উহুদের যুদ্ধে মুসলিমদের সাময়িক বিপত্তি ঘটে এবং ৭০ জন সাহাবী শাহাদাত বরণ করেন।",
            explanationEn = "The Battle of Uhud took place in 3 AH, during which several great companions, including Hamza (R.A.), lost their lives."
        ),
        QuizQuestion(
            id = "q39",
            questionBn = "কোন নবী অত্যন্ত কঠিন রোগে আক্রান্ত হয়ে বহু বছর প্রচণ্ড কষ্টের সাথে অনন্য চরম সবর ও ধৈর্য ধারণ করেছিলেন?",
            questionEn = "Which Prophet is renowned for his extraordinary patience (Sabr) during years of severe illness?",
            optionsBn = listOf("হযরত আইয়ুব (আ.)", "হযরত ইউসুফ (আ.)", "হযরত জাকারিয়া (আ.)", "হযরত ইউনূস (আ.)"),
            optionsEn = listOf("Ayyub (A.S.)", "Yusuf (A.S.)", "Zakariya (A.S.)", "Yunus (A.S.)"),
            correctIdx = 0,
            explanationBn = "হযরত আইয়ুব (আ.) কঠিন রোগে আক্রান্ত হওয়ার পর তাঁর শরীর, পরিবার ও সম্পদ হারানোর চরম সংকটে আল্লাহর ওপর অটল বিশ্বস্ত ধৈর্য দেখিয়েছিলেন।",
            explanationEn = "Prophet Ayyub (Job) A.S. is the ultimate Islamic icon of patience (Sabr) and fortitude during long years of testing illness."
        ),
        QuizQuestion(
            id = "q40",
            questionBn = "রাসূলুল্লাহ্ (সা.)-এর নিজস্ব কথা, কাজ ও মৌন সম্মতিকে ইসলামী পরিভাষায় কী বলা হয়?",
            questionEn = "What are the collected sayings, actions, and approvals of Prophet Muhammad called?",
            optionsBn = listOf("ফেকাহ", "তাফসীর", "হাদিস", "শরীয়াহ"),
            optionsEn = listOf("Fiqh", "Tafsir", "Hadith", "Shariah"),
            correctIdx = 2,
            explanationBn = "রাসূলুল্লাহ (সা.) এর সকল বাণী, আমল এবং সম্মতিকে ইসলামে 'হাদিস' বলা হয়, যা পবিত্র কুরআনের পর দ্বিতীয় উৎস।",
            explanationEn = "Hadith (meaning report or narrative) refers to the words, habits, and approvals of Prophet Muhammad (PBUH)."
        ),
        QuizQuestion(
            id = "q41",
            questionBn = "ঐতিহাসিক মক্কার মুসলমানগণ কোন খ্রিষ্টাব্দে প্রথম মদীনায় হিজরত করেছিলেন?",
            questionEn = "In which Gregorian year did the historic emigration (Hijrah) to Madinah occur?",
            optionsBn = listOf("৫৭০ খ্রিষ্টাব্দে", "৬১০ খ্রিষ্টাব্দে", "৬২২ খ্রিষ্টাব্দে", "৬৩২ খ্রিষ্টাব্দে"),
            optionsEn = listOf("570 CE", "610 CE", "622 CE", "632 CE"),
            correctIdx = 2,
            explanationBn = "৬২২ খ্রিষ্টাব্দে রাসূলুল্লাহ (সা.) ও তাঁর মহান সঙ্গী হযরত আবু বকর (রা.) মক্কা ত্যাগ করে মদীনায় ঐতিহাসিক যাত্রা তথা হিজরত সম্পন্ন করেন।",
            explanationEn = "In 622 CE, the Muslims migrated from persecution in Makkah to the city of Yathrib, which was later renamed Madinah."
        ),
        QuizQuestion(
            id = "q42",
            questionBn = "আমাদের প্রিয় নবীজী মুহাম্মদ (সা.)-এর শ্রদ্ধেয় আম্মাজানের নাম কী ছিল?",
            questionEn = "What was the name of the beloved Prophet's mother?",
            optionsBn = listOf("হযরত হালিমা", "হযরত আমিনা", "হযরত ফাতেমা", "হযরত আয়েশা"),
            optionsEn = listOf("Halimah", "Aminah", "Fatimah", "Aisha"),
            correctIdx = 1,
            explanationBn = "রাসূলুল্লাহ (সা.)-এর মাতার নাম হযরত আমিনা এবং বাবার নাম আব্দুল্লাহ ছিল। তিনি বাল্যকালেই তাঁর পিতামাতাকে হারান।",
            explanationEn = "Aminah bint Wahb was the honourable mother of Prophet Muhammad (PBUH), who sadly passed away when he was 6 years old."
        ),
        QuizQuestion(
            id = "q43",
            questionBn = "আল্লাহর কোন অলৌকিক নবী মায়ের কোলে ছোট্ট শিশু অবস্থাতেই মানুষের সামনে নিজের কথার ব্যাখ্যা দিয়েছিলেন?",
            questionEn = "Which miraculous Prophet spoke to defend his mother while she carried him in the cradle?",
            optionsBn = listOf("হযরত মুসা (আ.)", "হযরত ঈসা (আ.)", "হযরত ইউসুফ (আ.)", "হযরত ইব্রাহিম (আ.)"),
            optionsEn = listOf("Musa (A.S.)", "Isa (A.S.)", "Yusuf (A.S.)", "Ibrahim (A.S.)"),
            correctIdx = 1,
            explanationBn = "হযরত ঈসা (আ.) বা যিশু তাঁর পবিত্র কুমারী মাতা মরিয়মের সতীত্ব ঘোষণার জন্য দোলনায় থাকাাবস্থায় আল্লাহর ইচ্ছায় কথা বলে উঠেছিলেন।",
            explanationEn = "Prophet Isa (Jesus) A.S. was granted the miracle of speech as an infant in the cradle to defend his pure mother, Maryam."
        ),
        QuizQuestion(
            id = "q44",
            questionBn = "আল্লাহ তাআলার দুই মহান নবী হযরত মূসা (আ.) এবং হযরত হারুন (আ.)-এর মধ্যে রক্ত সম্পর্কের কী যোগসূত্র ছিল?",
            questionEn = "What was the kinship relation between Prophet Musa (A.S.) and Prophet Harun (A.S.)?",
            optionsBn = listOf("পিতা-পুত্র", "মামা-ভাগ্নে", "ভাই-ভাই", "চাচাতো ভাই"),
            optionsEn = listOf("Father & Son", "Uncle & Nephew", "Brothers", "Cousins"),
            correctIdx = 2,
            explanationBn = "হযরত হারুন (আ.) ছিলেন হযরত মূসা (আ.)-এর বড় ভাই। মূসা (আ.) আল্লাহর কাছে দোয়া করেছিলেন যেন দ্বীনের কাজে তাঁর ভাই হারুনকে অংশীদার বা সাহায্যকারী করা হয়।",
            explanationEn = "Prophet Harun (Aaron) A.S. was the elder brother of Prophet Musa (Moses) A.S. and served as his spokesperson and helper."
        ),
        QuizQuestion(
            id = "q45",
            questionBn = "পবিত্র কুরআনের কোন সূরায় লাইলাতুল কদরের মর্যাদা 'হাজার মাসের চেয়েও উত্তম' বলা হয়েছে?",
            questionEn = "In which Surah is Laylatul Qadr (the Night of Power) described as better than a thousand months?",
            optionsBn = listOf("সূরা আদ-দোখান", "সূরা আল-কাদর", "সূরা আল-মুয্যাম্মিল", "সূরা আল-ইখলাস"),
            optionsEn = listOf("Surah Ad-Dukhan", "Surah Al-Qadr", "Surah Al-Muzzammil", "Surah Al-Ikhlas"),
            correctIdx = 1,
            explanationBn = "সূরা আল-কাদরের ৩ নম্বর আয়াতে এসেছে, 'লাতিলাতুল কদর খাইরুম মিন আলফি শাহর' অর্থাৎ কদরের রাত হাজার মাস অপেক্ষা উত্তম ও পুণ্যময়।",
            explanationEn = "Surah Al-Qadr is dedicated to explaining the greatness of Laylatul Qadr, declaring it superior to 1,000 months of worship."
        ),
        QuizQuestion(
            id = "q46",
            questionBn = "হৃদয় থেকে বিশ্বাসের সাথে স্বীকারোক্তি দেওয়া ও মুখে বলা অর্থাৎ ইসলামের প্রথম ও মূল স্তম্ভ কোনটি?",
            questionEn = "What is the very first and fundamental pillar of Islam?",
            optionsBn = listOf("ঈমান বা কালিমাহ", "সালাত / নামাজ", "রোযা / সাওম", "যাকাত"),
            optionsEn = listOf("Shahadah (Faith)", "Salah (Prayer)", "Sawm (Fasting)", "Zakat"),
            correctIdx = 0,
            explanationBn = "ইসলামের ১ম খুটি হলো ঈমান তথা লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ এর সাক্ষ্য হৃদয়ে অটলভাবে ধরে রাখা।",
            explanationEn = "Shahadah (declaring faith that there is no God but Allah, and Muhammad is His messenger) is the first pillar of Islam."
        ),
        QuizQuestion(
            id = "q47",
            questionBn = "ঐতিহাসিক হুদাইবিয়ার সন্ধি চুক্তিনামা নিজ হাতে কে লিখেছিলেন?",
            questionEn = "Who wrote down the final charter text of the Treaty of Hudaibiyah?",
            optionsBn = listOf("হযরত আবু বকর (রা.)", "হযরত ওমর (রা.)", "হযরত আলী ইবনে আবি তালিব (রা.)", "হযরত ওসমান (রা.)"),
            optionsEn = listOf("Abu Bakr (R.A.)", "Umar (R.A.)", "Ali ibn Abi Talib (R.A.)", "Uthman (R.A.)"),
            correctIdx = 2,
            explanationBn = "৬ষ্ঠ হিজরীর ঐতিহাসিক হুদায়বিয়ার সন্ধিনামা রাসূলুল্লাহ্ (সা.)-এর শ্রুতলিপি বা আদেশে লেখক খতিব হিসেবে হযরত আলী (রা.) স্বহস্তে লিপিবদ্ধ করেছিলেন।",
            explanationEn = "Hazrat Ali ibn Abi Talib (R.A.) drafted the actual physical document of the Treaty of Hudaibiyah."
        ),
        QuizQuestion(
            id = "q48",
            questionBn = "প্রিয় নবী মুহাম্মদ (সা.)-এর মোট কতজন পবিত্র কন্যাসন্তান বা দুহিতা ছিলেন?",
            questionEn = "How many daughters did Prophet Muhammad (PBUH) have?",
            optionsBn = listOf("২ জন", "৩ জন", "৪ জন", "৫ জন"),
            optionsEn = listOf("2", "3", "4", "5"),
            correctIdx = 2,
            explanationBn = "রাসূলুল্লাহ্ (সা.)-এর ৪ জন কন্যা ছিলেন: হযরত যয়নব, হযরত রুকাইয়া, হযরত উম্মে কুলসুম এবং আল্লাহর সিংহের অর্ধাঙ্গিনী উম্মত জননী হযরত ফাতিমা (রা.)।",
            explanationEn = "The Prophet (PBUH) had four daughters: Ruqayyah, Zainab, Umm Kulthum, and Fatimah (R.A.)."
        ),
        QuizQuestion(
            id = "q49",
            questionBn = "আরবি 'ইসলাম' শব্দের আভিধানিক অর্থ কী?",
            questionEn = "What is the literal linguistic meaning of the Arabic word 'Islam'?",
            optionsBn = listOf("যুদ্ধ করা", "আত্মসমর্পণ ও শান্তি", "আল্লাহর ঘর", "ইবাদতখানা"),
            optionsEn = listOf("To fight", "Submission and Peace", "House of Allah", "Place of worship"),
            correctIdx = 1,
            explanationBn = "ইসলাম শব্দের মূল ধাতু 'সলম', যার অর্থ শান্তি এবং আল্লাহর আইনের প্রতি অনুগত হয়ে নিজেকে পুরোপুরি আত্মসমর্পণ করা।",
            explanationEn = "The word Islam is derived from the root 'salama', which literally means peace and absolute submission."
        ),
        QuizQuestion(
            id = "q50",
            questionBn = "বদরের যুদ্ধে মক্কার পৌত্তলিক বা কুরাইশদের সৈন্যদের সর্বাধিনায়ক ও সেনাপতি কে ছিল?",
            questionEn = "Who was the chief commander of the pagan Meccan army during the Battle of Badr?",
            optionsBn = listOf("আবু লাহাব", "আবু জহল", "আবু সুফিয়ান", "উতবাহ ইবনে রাবিআহ"),
            optionsEn = listOf("Abu Lahab", "Abu Jahl", "Abu Sufyan", "Utbah ibn Rabi'ah"),
            correctIdx = 1,
            explanationBn = "বদরের প্রথম সমরাঙ্গনে পৌত্তলিক বাহিনীর প্রধান নেতৃত্ব ও হিংস্র আক্রোশের নায়ক ছিল আবু জহল, যে যুদ্ধক্ষেত্রে মুসলিমদের দ্বারা নিহত হয়েছিল।",
            explanationEn = "Abu Jahl (Amr ibn Hisham) was the chief leader of the Meccan polytheist army and was slain in the Battle of Badr."
        )
    )

    fun getDailyQuestions(): List<QuizQuestion> {
        val calendar = java.util.Calendar.getInstance()
        val dayOfYear = calendar.get(java.util.Calendar.DAY_OF_YEAR)
        val size = 5
        val list = mutableListOf<QuizQuestion>()
        for (i in 0 until size) {
            val index = (dayOfYear + i * 3) % questions.size
            val question = questions[index]
            if (!list.any { it.id == question.id }) {
                list.add(question)
            }
        }
        // Fallback for safety
        if (list.size < size) {
            questions.forEach {
                if (list.size < size && !list.any { q -> q.id == it.id }) {
                    list.add(it)
                }
            }
        }
        return list
    }

    fun getLevelQuestions(level: Int): List<QuizQuestion> {
        val size = 5
        val startIndex = ((level - 1) * 5) % questions.size
        val list = mutableListOf<QuizQuestion>()
        for (i in 0 until size) {
            val index = (startIndex + i) % questions.size
            list.add(questions[index])
        }
        return list
    }
}
