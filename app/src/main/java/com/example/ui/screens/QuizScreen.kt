package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuizBank
import com.example.model.*
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    quizType: String,
    currentLanguage: AppLanguage,
    onFinish: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens

    val questions = remember(quizType) {
        when (quizType) {
            "dart" -> QuizBank.getQuestionsForTrack(CourseTrack.DART)
            "flutter" -> QuizBank.getQuestionsForTrack(CourseTrack.FLUTTER)
            else -> QuizBank.getRandomMockExam(10)
        }
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(tokens.background), contentAlignment = Alignment.Center) {
            Text("No questions available", color = tokens.textPrimary)
        }
        return
    }

    val currentQuestion = questions.getOrNull(currentIndex) ?: questions.last()

    Scaffold(
        containerColor = tokens.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (!isQuizFinished) "Question ${currentIndex + 1} of ${questions.size}" else "Assessment Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("quiz_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = tokens.textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = tokens.surface)
            )
        }
    ) { innerPadding ->
        if (isQuizFinished) {
            // Final Summary Screen: Minimal, warm & elegant
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val scorePercent = ((correctCount.toFloat() / questions.size) * 100).toInt()
                val isPassed = scorePercent >= 70

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(
                            if (isPassed) tokens.success.copy(alpha = 0.15f) else tokens.warning.copy(alpha = 0.15f),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPassed) "✓" else "!",
                        style = MaterialTheme.typography.headlineLarge,
                        color = if (isPassed) tokens.success else tokens.warning,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = if (isPassed) "Assessment Passed" else "Review Needed",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "You scored $correctCount of ${questions.size} correctly ($scorePercent%)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = tokens.textSecondary
                )
                Spacer(modifier = Modifier.height(18.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (tokens.isDark) tokens.surfaceSecondary else tokens.sageSoft,
                    modifier = Modifier.border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "+${correctCount * 25} XP Earned",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
                        color = if (tokens.isDark) tokens.sagePrimary else Color(0xFF000000),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onFinish(correctCount * 25) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("finish_quiz_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tokens.buttonPrimary,
                        contentColor = tokens.buttonPrimaryText
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Return to Practice", color = tokens.buttonPrimaryText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                }
            }
        } else {
            // Active Question
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Thin progress indicator
                item {
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / questions.size },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                        trackColor = if (tokens.isDark) tokens.surfaceSecondary else tokens.warmBeige.copy(alpha = 0.5f)
                    )
                }

                // Question Header & Difficulty
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (tokens.isDark) tokens.surfaceSecondary else tokens.sageSoft
                        ) {
                            Text(
                                text = currentQuestion.topic.uppercase(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (tokens.isDark) tokens.sagePrimary else Color(0xFF000000),
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        DifficultyBadge(difficulty = currentQuestion.difficulty)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                }

                // Code snippet (if any)
                if (currentQuestion.codeSnippet != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(tokens.codeBackground, RoundedCornerShape(12.dp))
                                .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = currentQuestion.codeSnippet,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = tokens.codeText
                            )
                        }
                    }
                }

                // Answer Options (Clean white or elevated dark surfaces)
                items(currentQuestion.options.size) { optIndex ->
                    val optionText = currentQuestion.options[optIndex]
                    val isSelected = selectedOptionIndex == optIndex
                    val isCorrectOption = optIndex == currentQuestion.correctIndex

                    val borderColor = when {
                        !isSubmitted && isSelected -> if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary
                        isSubmitted && isCorrectOption -> tokens.success
                        isSubmitted && isSelected && !isCorrectOption -> tokens.error
                        else -> tokens.border
                    }

                    val bgColor = when {
                        !isSubmitted && isSelected -> if (tokens.isDark) tokens.surfaceElevated else tokens.sageSoft.copy(alpha = 0.35f)
                        isSubmitted && isCorrectOption -> tokens.success.copy(alpha = 0.12f)
                        isSubmitted && isSelected && !isCorrectOption -> tokens.error.copy(alpha = 0.12f)
                        else -> tokens.surface
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                            .clickable(enabled = !isSubmitted) {
                                selectedOptionIndex = optIndex
                            }
                            .testTag("quiz_option_$optIndex"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSubmitted) selectedOptionIndex = optIndex },
                                enabled = !isSubmitted,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                                    unselectedColor = tokens.textMuted
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textPrimary,
                                modifier = Modifier.weight(1f)
                            )

                            if (isSubmitted) {
                                if (isCorrectOption) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = tokens.success, modifier = Modifier.size(20.dp))
                                } else if (isSelected) {
                                    Icon(Icons.Default.Close, contentDescription = "Wrong", tint = tokens.error, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                // Explanation Box after submission
                if (isSubmitted) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Explanation",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (tokens.isDark) tokens.sagePrimary else tokens.textPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = tokens.textSecondary
                                )
                            }
                        }
                    }
                }

                // Action Button: Submit or Next (Black in light, Sage in dark)
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    if (!isSubmitted) {
                        Button(
                            onClick = {
                                if (selectedOptionIndex != null) {
                                    isSubmitted = true
                                    if (selectedOptionIndex == currentQuestion.correctIndex) {
                                        correctCount++
                                    }
                                }
                            },
                            enabled = selectedOptionIndex != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("quiz_submit_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText,
                                disabledContainerColor = tokens.surfaceSecondary,
                                disabledContentColor = tokens.textMuted
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Submit Answer", color = if (selectedOptionIndex != null) tokens.buttonPrimaryText else tokens.textMuted, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentIndex + 1 < questions.size) {
                                    currentIndex++
                                    selectedOptionIndex = null
                                    isSubmitted = false
                                } else {
                                    isQuizFinished = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("quiz_next_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                if (currentIndex + 1 < questions.size) "Next Question" else "See Results",
                                color = tokens.buttonPrimaryText,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
