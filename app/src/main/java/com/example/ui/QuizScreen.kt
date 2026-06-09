package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizData
import com.example.data.QuizQuestion
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

// Styling Theme Colors - Aligned with the application colors
private val EmeraldDeep = Color(0xFF0F5A47)
private val EmeraldLight = Color(0xFFE8F5E9)
private val IslamicGold = Color(0xFFFFD700)
private val SoftGoldBorder = Color(0xFFCFB53B)
private val SoftCharcoal = Color(0xFF2C3E50)

// Helper function to deserialize JSON from Gemini response safely
fun parseQuizQuestionsFromJson(jsonStr: String): List<QuizQuestion>? {
    try {
        val clean = jsonStr.trim().removeSurrounding("```json", "```").trim()
        val startIndex = clean.indexOf('[')
        val endIndex = clean.lastIndexOf(']')
        if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) return null
        val cleanedJson = clean.substring(startIndex, endIndex + 1)

        val array = org.json.JSONArray(cleanedJson)
        val list = mutableListOf<QuizQuestion>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            
            val optionsBnArr = obj.getJSONArray("optionsBn")
            val optionsBnList = List(optionsBnArr.length()) { optionsBnArr.getString(it) }
            
            val optionsEnArr = obj.getJSONArray("optionsEn")
            val optionsEnList = List(optionsEnArr.length()) { optionsEnArr.getString(it) }
            
            list.add(
                QuizQuestion(
                    id = "ai_${System.currentTimeMillis()}_$i",
                    questionBn = obj.getString("questionBn"),
                    questionEn = obj.getString("questionEn"),
                    optionsBn = optionsBnList,
                    optionsEn = optionsEnList,
                    correctIdx = obj.getInt("correctIdx"),
                    explanationBn = obj.optString("explanationBn", ""),
                    explanationEn = obj.optString("explanationEn", ""),
                    points = 10
                )
            )
        }
        if (list.isNotEmpty()) {
            return list
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

// Helper to display numbers in Bengali
fun String.toBanglaNumber(): String {
    val englishDigits = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val banglaDigits = listOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    var result = ""
    for (char in this) {
        val idx = englishDigits.indexOf(char)
        if (idx != -1) {
            result += banglaDigits[idx]
        } else {
            result += char
        }
    }
    return result
}

// Data class for Level
data class QuizLevel(
    val id: Int,
    val nameBn: String,
    val nameEn: String,
    val difficultyBn: String,
    val difficultyEn: String,
    val starsCount: Int
)

val quizLevels = listOf(
    QuizLevel(1, "লেভেল ১: ঈমানের খুঁটিনাটি", "Level 1: Pillars of Faith", "খুব সহজ", "Very Easy", 1),
    QuizLevel(2, "লেভেল ২: পবিত্র কুরআন ও সূরা", "Level 2: Quran & Suras", "সহজ", "Easy", 1),
    QuizLevel(3, "লেভেল ৩: সালাত ও পবিত্রতা", "Level 3: Prayers & Wudu", "সহজ", "Easy", 2),
    QuizLevel(4, "লেভেল ৪: নবী-রাসূলদের জীবনী", "Level 4: Lives of the Prophets", "সাধারণ", "Standard", 2),
    QuizLevel(5, "লেভেল ৫: রাসুলুল্লাহ (সা.)-এর মক্কী জীবন", "Level 5: Prophet's (PBUH) Makkah Life", "মাঝারি", "Medium", 3),
    QuizLevel(6, "লেভেল ৬: রাসুলুল্লাহ (সা.)-এর মাদানী জীবন", "Level 6: Prophet's (PBUH) Madinah Life", "মাঝারি", "Medium", 3),
    QuizLevel(7, "লেভেল ৭: গৌরবময় খিলাফত ও সাহাবীগণ", "Level 7: Caliphs & Noble Companions", "উন্নত", "Advanced", 4),
    QuizLevel(8, "লেভেল ৮: ইসলামী ইতিহাস ও জিহাদ", "Level 8: Islamic History & Battle Fields", "কঠিন", "Challenging", 4),
    QuizLevel(9, "লেভেল ৯: শরীয়াহ ও মাসয়ালা-মাসায়েল", "Level 9: Shariah & Fiqh Jurisprudence", "দারুণ কঠিন", "Deep", 5),
    QuizLevel(10, "লেভেল ১০: কিতাবুল্লাহ ও হাদিস সংকলন", "Level 10: Compilation of Hadiths", "বিশেষজ্ঞ", "Grand Master", 5)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicQuizScreen(
    viewModel: IbadahViewModel,
    isBn: Boolean,
    onBack: () -> Unit
) {
    val userPoints by viewModel.userPoints.collectAsState()
    val completedDates by viewModel.completedQuizDates.collectAsState()
    val userName by viewModel.userName.collectAsState()

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val isDailyDone = completedDates.contains(todayStr)

    // Active Quiz Play State
    var activeQuizType by remember { mutableStateOf<String?>(null) } // "daily" or null
    var activeQuizLevel by remember { mutableStateOf(1) }
    var activeQuestions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var currentIdx by remember { mutableStateOf(0) }
    var selectedOptionIdx by remember { mutableStateOf<Int?>(null) }
    var pointsEarnedThisSession by remember { mutableStateOf(0) }
    var correctAnswersCount by remember { mutableStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    // Loading & Prompt States
    var isLoadingQuestions by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var tempNameInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isBn) "ইসলামিক কুইজ" else "Islamic Quiz",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (activeQuizType != null && !quizCompleted) {
                            activeQuizType = null
                        } else {
                            onBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldDeep
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDFDF5)) // Aligned perfectly with Prayer Education Screen
                .padding(innerPadding)
        ) {
            if (activeQuizType == null) {
                // Quiz Home
                QuizDashboard(
                    isBn = isBn,
                    userPoints = userPoints,
                    userName = userName,
                    isDailyDone = isDailyDone,
                    onStartQuiz = { level ->
                        activeQuizType = "daily"
                        activeQuizLevel = level
                        currentIdx = 0
                        selectedOptionIdx = null
                        pointsEarnedThisSession = 0
                        correctAnswersCount = 0
                        quizCompleted = false
                        
                        if (userName.isNotEmpty()) {
                            isLoadingQuestions = true
                            coroutineScope.launch {
                                val difficultyDesc = when(level) {
                                    1 -> "Level 1: Basic pillars of Faith, extremely easy, for beginners"
                                    2 -> "Level 2: Quran & Suras basic, very easy"
                                    3 -> "Level 3: Prayers & Wudu, easy"
                                    4 -> "Level 4: Lives of the Prophets, easy"
                                    5 -> "Level 5: Prophet's (PBUH) Makkah life, standard"
                                    6 -> "Level 6: Prophet's (PBUH) Madinah life, standard"
                                    7 -> "Level 7: Caliphs & companions, advanced"
                                    8 -> "Level 8: Islamic History & battlefields, difficult"
                                    9 -> "Level 9: Shariah & Jurisprudence, hard"
                                    10 -> "Level 10: Compilation of Hadiths, very challenging expert"
                                    else -> "Graduated difficulty"
                                }
                                val prompt = """
                                    Generate exactly 5 random, unique and interesting Islamic multiple-choice quiz questions for level $level.
                                    Difficulty level: $difficultyDesc
                                    You MUST output the response in a structured JSON format.
                                    Each question in the JSON array must strictly have these fields:
                                    "questionBn": a string of the question in Bengali,
                                    "questionEn": a string of the question in English,
                                    "optionsBn": an array of exactly 4 strings for options in Bengali,
                                    "optionsEn": an array of exactly 4 strings for options in English,
                                    "correctIdx": an integer from 0 to 3 representing the correct option index,
                                    "explanationBn": a string explaining the answer in Bengali,
                                    "explanationEn": a string explaining the answer in English.
                                    
                                    Make sure the questions are authentic and informative. Do not wrap in markdown tags - output raw JSON text ONLY.
                                """.trimIndent()

                                try {
                                    val result = com.example.service.GeminiApiClient.askGemini(prompt)
                                    val parsing = parseQuizQuestionsFromJson(result)
                                    if (parsing != null && parsing.size == 5) {
                                        activeQuestions = parsing
                                    } else {
                                        activeQuestions = QuizData.getLevelQuestions(level)
                                    }
                                } catch (e: Exception) {
                                    activeQuestions = QuizData.getLevelQuestions(level)
                                } finally {
                                    isLoadingQuestions = false
                                }
                            }
                        }
                    },
                    onResetName = {
                        viewModel.setUserName("")
                    }
                )
            } else {
                // Determine whether user name should be prompted first
                if (userName.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(EmeraldLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = EmeraldDeep,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = if (isBn) "কুইজে আপনার নাম লিখুন" else "Enter your Name",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = SoftCharcoal
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = if (isBn) "কুইজ শুরু করার আগে অনুগ্রহ করে আপনার নামটি লিখুন" else "Please enter your name before initiating the quiz session",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                OutlinedTextField(
                                    value = tempNameInput,
                                    onValueChange = { tempNameInput = it },
                                    label = { Text(if (isBn) "আপনার নাম" else "Your Name") },
                                    placeholder = { Text(if (isBn) "সাজিদ মাহমুদ" else "e.g. Sajid Mahmud") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EmeraldDeep,
                                        focusedLabelColor = EmeraldDeep
                                    )
                                )
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                AnimatedVisibility(
                                    visible = tempNameInput.isNotBlank(),
                                    enter = fadeIn() + expandVertically()
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.setUserName(tempNameInput.trim())
                                            isLoadingQuestions = true
                                            coroutineScope.launch {
                                                val difficultyDesc = when(activeQuizLevel) {
                                                    1 -> "Level 1: Basic pillars of Faith, extremely easy, for beginners"
                                                    2 -> "Level 2: Quran & Suras basic, very easy"
                                                    3 -> "Level 3: Prayers & Wudu, easy"
                                                    4 -> "Level 4: Lives of the Prophets, easy"
                                                    5 -> "Level 5: Prophet's (PBUH) Makkah life, standard"
                                                    6 -> "Level 6: Prophet's (PBUH) Madinah life, standard"
                                                    7 -> "Level 7: Caliphs & companions, advanced"
                                                    8 -> "Level 8: Islamic History & battlefields, difficult"
                                                    9 -> "Level 9: Shariah & Jurisprudence, hard"
                                                    10 -> "Level 10: Compilation of Hadiths, very challenging expert"
                                                    else -> "Graduated difficulty"
                                                }
                                                val prompt = """
                                                    Generate exactly 5 random, unique and interesting Islamic multiple-choice quiz questions for level $activeQuizLevel.
                                                    Difficulty level: $difficultyDesc
                                                    You MUST output the response in a structured JSON format.
                                                    Each question in the JSON array must strictly have these fields:
                                                    "questionBn": a string of the question in Bengali,
                                                    "questionEn": a string of the question in English,
                                                    "optionsBn": an array of exactly 4 strings for options in Bengali,
                                                    "optionsEn": an array of exactly 4 strings for options in English,
                                                    "correctIdx": an integer from 0 to 3 representing the correct option index,
                                                    "explanationBn": a string explaining the answer in Bengali,
                                                    "explanationEn": a string explaining the answer in English.
                                                    
                                                    Make sure the questions are authentic and informative. Do not wrap in markdown tags - output raw JSON text ONLY.
                                                """.trimIndent()

                                                try {
                                                    val result = com.example.service.GeminiApiClient.askGemini(prompt)
                                                    val parsing = parseQuizQuestionsFromJson(result)
                                                    if (parsing != null && parsing.size == 5) {
                                                        activeQuestions = parsing
                                                    } else {
                                                        activeQuestions = QuizData.getLevelQuestions(activeQuizLevel)
                                                    }
                                                } catch (e: Exception) {
                                                    activeQuestions = QuizData.getLevelQuestions(activeQuizLevel)
                                                } finally {
                                                    isLoadingQuestions = false
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(24.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep)
                                    ) {
                                        Text(
                                            text = if (isBn) "স্টার্ট কুইজ" else "Start Quiz",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (isLoadingQuestions) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = EmeraldDeep)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (isBn) "কুইজের প্রশ্ন জেনারেট হচ্ছে..." else "Generating quiz questions...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftCharcoal
                            )
                        }
                    }
                } else {
                    if (!quizCompleted) {
                        val currentQuestion = activeQuestions.getOrNull(currentIdx)
                        if (currentQuestion != null) {
                            QuizActivePlay(
                                isBn = isBn,
                                quizType = activeQuizType ?: "daily",
                                question = currentQuestion,
                                currentIndex = currentIdx,
                                totalQuestions = activeQuestions.size,
                                selectedIdx = selectedOptionIdx,
                                onOptionSelected = { idx ->
                                    if (selectedOptionIdx == null) {
                                        selectedOptionIdx = idx
                                        if (idx == currentQuestion.correctIdx) {
                                            correctAnswersCount++
                                            if (!isDailyDone) {
                                                pointsEarnedThisSession += currentQuestion.points
                                            }
                                        }
                                    }
                                },
                                onNext = {
                                    if (currentIdx < activeQuestions.size - 1) {
                                        currentIdx++
                                        selectedOptionIdx = null
                                    } else {
                                        quizCompleted = true
                                        if (!isDailyDone) {
                                            viewModel.addPoints(pointsEarnedThisSession)
                                            viewModel.markQuizDateCompleted(todayStr)
                                        }
                                    }
                                }
                            )
                        } else {
                            activeQuizType = null
                        }
                    } else {
                        QuizResult(
                            isBn = isBn,
                            quizType = activeQuizType ?: "daily",
                            correctCount = correctAnswersCount,
                            totalCount = activeQuestions.size,
                            pointsEarned = pointsEarnedThisSession,
                            alreadyRewarded = isDailyDone,
                            onClose = {
                                activeQuizType = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuizDashboard(
    isBn: Boolean,
    userPoints: Int,
    userName: String,
    isDailyDone: Boolean,
    onStartQuiz: (Int) -> Unit,
    onResetName: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header with Pencil Edit
        if (userName.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldLight.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, EmeraldDeep.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = EmeraldDeep,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) "স্বাগতম, $userName!" else "Welcome, $userName!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = EmeraldDeep
                            )
                            Text(
                                text = if (isBn) "আজকের কুইজে অংশগ্রহণ করতে নিচে লেভেল নির্বাচন করুন" else "Select a difficulty level below to play",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        IconButton(onClick = onResetName) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Name",
                                tint = EmeraldDeep,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Cumulative point crown
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = EmeraldDeep
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Points Medal",
                            tint = IslamicGold,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBn) "আপনার সর্বমোট সংগ্রহ" else "Your Cumulative Points",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$userPoints ${if (isBn) "পয়েন্ট" else "Points"}",
                        color = IslamicGold,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // Categories Header
        item {
            Text(
                text = if (isBn) "কুইজের ১০টি চমৎকার লেভেল বেছে নিন" else "Select from 10 Progressive Levels",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SoftCharcoal,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        // Render the 10 levels dynamically
        items(quizLevels.size) { index ->
            val level = quizLevels[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartQuiz(level.id) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // level badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(EmeraldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isBn) level.id.toString().toBanglaNumber() else level.id.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = EmeraldDeep
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) level.nameBn else level.nameEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SoftCharcoal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        when (level.starsCount) {
                                            1 -> Color(0xFFE8F5E9)
                                            2 -> Color(0xFFFFF3E0)
                                            3 -> Color(0xFFFFF8E1)
                                            4 -> Color(0xFFFBE9E7)
                                            else -> Color(0xFFEFEBE9)
                                        },
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isBn) level.difficultyBn else level.difficultyEn,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (level.starsCount) {
                                        1 -> Color(0xFF2E7D32)
                                        2 -> Color(0xFFEF6C00)
                                        3 -> Color(0xFFF57F17)
                                        4 -> Color(0xFFD84315)
                                        else -> Color(0xFF4E342E)
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            repeat(level.starsCount) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = IslamicGold,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Level",
                        tint = EmeraldDeep
                    )
                }
            }
        }

        // Bottom advice card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = EmeraldLight.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = SoftGoldBorder,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBn) {
                            "রাসূলুল্লাহ (সা.) বলেছেন: 'জ্ঞান অনুসন্ধান করা প্রত্যেক মুসলিমের উপর ফরয।' (ইবনে মাজা)"
                        } else {
                            "The Prophet (PBUH) said: 'Seeking knowledge is obligatory upon every Muslim.' (Ibn Majah)"
                        },
                        fontSize = 11.sp,
                        color = EmeraldDeep,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuizActivePlay(
    isBn: Boolean,
    quizType: String,
    question: QuizQuestion,
    currentIndex: Int,
    totalQuestions: Int,
    selectedIdx: Int?,
    onOptionSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tracker Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "আজকের কুইজ" else "Today's Quiz",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = EmeraldDeep
                )
                Text(
                    text = if (isBn) "প্রশ্ন ${currentIndex + 1}/$totalQuestions" else "Question ${currentIndex + 1} of $totalQuestions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = SoftCharcoal
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = (currentIndex + 1).toFloat() / totalQuestions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = EmeraldDeep,
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Question card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(
                        text = if (isBn) question.questionBn else question.questionEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = SoftCharcoal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Options list
            val options = if (isBn) question.optionsBn else question.optionsEn
            options.forEachIndexed { idx, option ->
                val isSelected = selectedIdx == idx
                val isCorrect = question.correctIdx == idx
                val isRevealed = selectedIdx != null

                // Save critical vertical height on small displays: when revealed, hide incorrect, unselected options.
                val skipRendering = isRevealed && !isSelected && !isCorrect
                if (!skipRendering) {
                    val containerColor = when {
                        isSelected && isCorrect -> Color(0xFFD1FAE5) // Green
                        isSelected && !isCorrect -> Color(0xFFFEE2E2) // Red
                        isRevealed && isCorrect -> Color(0xFFD1FAE5) // Reveal correct on error
                        else -> Color.White
                    }

                    val bdrColor = when {
                        isSelected && isCorrect -> Color(0xFF10B981)
                        isSelected && !isCorrect -> Color(0xFFEF4444)
                        isRevealed && isCorrect -> Color(0xFF10B981)
                        else -> Color(0xFFE2E8F0)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable(enabled = !isRevealed) { onOptionSelected(idx) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        border = BorderStroke(1.dp, bdrColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        when {
                                            isRevealed && isCorrect -> Color(0xFF10B981)
                                            isSelected && !isCorrect -> Color(0xFFEF4444)
                                            else -> Color(0xFFF1F5F9)
                                        },
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isRevealed && isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                } else if (isSelected && !isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                } else {
                                    Text(
                                        text = ('A' + idx).toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = option,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = SoftCharcoal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Explanation (visible when selectedIdx is not null)
            AnimatedVisibility(
                visible = selectedIdx != null,
                enter = fadeIn() + expandVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldLight.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = EmeraldDeep,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBn) "ব্যাখ্যা ও তথ্য উৎস" else "Explanation & Reference",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = EmeraldDeep
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (isBn) question.explanationBn else question.explanationEn,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }

        // Action Button layout
        Button(
            onClick = onNext,
            enabled = selectedIdx != null,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(42.dp)
                .padding(bottom = 2.dp),
            shape = RoundedCornerShape(21.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep)
        ) {
            Text(
                text = if (currentIndex < totalQuestions - 1) {
                    if (isBn) "পরবর্তী প্রশ্ন" else "Next Question"
                } else {
                    if (isBn) "ফলাফল দেখুন" else "Finish & View Score"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun QuizResult(
    isBn: Boolean,
    quizType: String,
    correctCount: Int,
    totalCount: Int,
    pointsEarned: Int,
    alreadyRewarded: Boolean,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Celebration Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(EmeraldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trophy",
                        tint = IslamicGold,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBn) "মাশাআল্লাহ! কুইজ সম্পন্ন" else "MashaAllah! Quiz Completed",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = EmeraldDeep
                )

                Spacer(modifier = Modifier.height(12.dp))

                Divider(color = Color(0xFFF1F5F9))

                Spacer(modifier = Modifier.height(12.dp))

                // Score metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isBn) "সটীক উত্তর" else "Accuracy",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$correctCount / $totalCount",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SoftCharcoal
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isBn) "অর্জিত পয়েন্ট" else "Points Claimed",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "+$pointsEarned",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pointsEarned > 0) Color(0xFF10B981) else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Summary Notice message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFFFBEB), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (alreadyRewarded) {
                            if (isBn) {
                                "আপনি আজকে ইতিমধ্যে একবার এই কুইজ সম্পূর্ণ করেছেন। পরবর্তীতে দৈনিক কুইজটি কালকে আবার খেলতে পারবেন ও পয়েন্ট অর্জন করতে পারবেন।"
                            } else {
                                "You already successfully completed this quiz today. Points can only be earned on the very first completion each day."
                            }
                        } else {
                            if (isBn) {
                                "সফলতার সাথে কুইজ সম্পন্ন হয়েছে! আপনার অর্জিত পয়েন্ট সর্বমোট অ্যাকাউন্টে যোগ করা হয়েছে।"
                            } else {
                                "Successfully completed! Your earned points has been synced to your cumulative totals."
                            }
                        },
                        fontSize = 10.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF92400E)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep)
                ) {
                    Text(
                        text = if (isBn) "হোম স্ক্রিনে ফিরে যান" else "Return to Home",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
