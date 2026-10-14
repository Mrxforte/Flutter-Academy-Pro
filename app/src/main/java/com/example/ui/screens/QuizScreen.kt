package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
            "dart" -> QuizBank.questions.filter { it.track == CourseTrack.DART }
            "flutter" -> QuizBank.questions.filter { it.track == CourseTrack.FLUTTER }
            else -> QuizBank.questions.shuffled().take(10)
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
                        fontWeight = FontWeight.Bold,
                        color = tokens.appBarContentColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("quiz_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = tokens.appBarContentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tokens.appBarBackground,
                    titleContentColor = tokens.appBarContentColor,
                    navigationIconContentColor = tokens.appBarContentColor
                )
            )
        }
    ) { innerPadding ->
        if (isQuizFinished) {
            // Final Summary Screen
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
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = if (isPassed) "🏆" else "📚", fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = if (isPassed) "Exam Completed!" else "Keep Practicing!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "You answered $correctCount of ${questions.size} questions correctly ($scorePercent%).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = tokens.textSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Breakdown Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "$correctCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = tokens.success)
                            Text(text = "Correct", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(tokens.border))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${questions.size - correctCount}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = tokens.error)
                            Text(text = "Incorrect", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                        }
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(tokens.border))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "+${correctCount * 15}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = tokens.primary)
                            Text(text = "XP Earned", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = { onFinish(correctCount * 15) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_finish_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tokens.buttonPrimary,
                        contentColor = tokens.buttonPrimaryText
                    )
                ) {
                    Text("Collect Rewards & Finish", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // Active Question Screen
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Question Progress
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Track: ${currentQuestion.track.name}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tokens.textSecondary
                                )
                                Text(
                                    text = "${currentIndex + 1} / ${questions.size}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { ((currentIndex + 1).toFloat() / questions.size) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = tokens.primary,
                                trackColor = tokens.surfaceSecondary
                            )
                        }
                    }

                    // Question Card
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                DifficultyBadge(difficulty = currentQuestion.difficulty)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = currentQuestion.question,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary,
                                    lineHeight = 24.sp
                                )

                                val snippet = currentQuestion.codeSnippet
                                if (!snippet.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(tokens.codeBackground, RoundedCornerShape(10.dp))
                                            .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = snippet,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp,
                                            color = tokens.textPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Options List
                    itemsIndexed(currentQuestion.options) { oIndex, optionText ->
                        val isSelected = selectedOptionIndex == oIndex
                        val isCorrect = oIndex == currentQuestion.correctIndex

                        val borderCol = when {
                            !isSubmitted && isSelected -> tokens.primary
                            isSubmitted && isCorrect -> tokens.success
                            isSubmitted && isSelected && !isCorrect -> tokens.error
                            else -> tokens.border
                        }

                        val containerCol = when {
                            !isSubmitted && isSelected -> tokens.subtleAccentBg
                            isSubmitted && isCorrect -> tokens.success.copy(alpha = 0.1f)
                            isSubmitted && isSelected && !isCorrect -> tokens.error.copy(alpha = 0.1f)
                            else -> tokens.surface
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, borderCol, RoundedCornerShape(12.dp))
                                .clickable(enabled = !isSubmitted) {
                                    selectedOptionIndex = oIndex
                                }
                                .testTag("quiz_option_$oIndex"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = containerCol),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (isSelected || (isSubmitted && isCorrect)) borderCol else tokens.surfaceSecondary
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ('A'.code + oIndex).toChar().toString(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected || (isSubmitted && isCorrect)) Color.White else tokens.textSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = tokens.textPrimary,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isSubmitted) {
                                    if (isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = tokens.success, modifier = Modifier.size(18.dp))
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Close, contentDescription = "Incorrect", tint = tokens.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Box (after submission)
                    if (isSubmitted && currentQuestion.explanation.isNotBlank()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = tokens.surfaceSecondary,
                                border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "Explanation",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tokens.textSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Bottom Action Button
                Surface(
                    color = tokens.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
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
                                .height(46.dp)
                                .testTag("quiz_submit_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            )
                        ) {
                            Text("Submit Answer", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentIndex < questions.size - 1) {
                                    currentIndex++
                                    selectedOptionIndex = null
                                    isSubmitted = false
                                } else {
                                    isQuizFinished = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("quiz_continue_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            )
                        ) {
                            Text(
                                text = if (currentIndex < questions.size - 1) "Next Question →" else "View Assessment Results",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
