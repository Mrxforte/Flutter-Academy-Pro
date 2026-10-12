package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FlashcardData
import com.example.engine.DartSimulatorEngine
import com.example.engine.ExecutionResult
import com.example.model.AppLanguage
import com.example.playground.ui.FlutterPlaygroundView
import com.example.ui.theme.AppTheme

@Composable
fun PracticeScreen(
    currentLanguage: AppLanguage,
    initialPlaygroundCode: String?,
    onStartQuiz: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val context = LocalContext.current

    var selectedSection by remember { mutableIntStateOf(0) } // 0: Widget Playground, 1: Dart Sandbox, 2: Flashcards, 3: Certification

    val defaultCode = """
void main() {
  print("Welcome to Flutter Academy! 🚀");
  var features = ["Clean Architecture", "Live Widgets", "Dart 3", "Impeller"];
  for (var f in features) {
    print("Mastering: ${'$'}f");
  }
}
    """.trimIndent()

    var editableCode by remember {
        mutableStateOf(defaultCode)
    }
    var executionResult by remember { mutableStateOf<ExecutionResult?>(null) }

    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isCardFlipped by remember { mutableStateOf(false) }
    val flashcards = FlashcardData.flashcards

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .testTag("practice_screen_column")
    ) {
        // Section Segmented Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .background(tokens.surfaceSecondary, RoundedCornerShape(12.dp))
                .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            val sections = listOf("Widget Canvas", "Dart Sandbox", "Flashcards", "Certification")
            sections.forEachIndexed { index, title ->
                val isSelected = selectedSection == index
                val tabBg = if (isSelected) tokens.buttonPrimary else tokens.surfaceSecondary
                val tabTextColor = if (isSelected) tokens.buttonPrimaryText else tokens.textSecondary

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(tabBg)
                        .clickable { selectedSection = index }
                        .padding(vertical = 9.dp)
                        .testTag("practice_tab_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = tabTextColor,
                        maxLines = 1
                    )
                }
            }
        }

        // Section Content
        when (selectedSection) {
            0 -> {
                FlutterPlaygroundView(
                    initialCode = initialPlaygroundCode,
                    modifier = Modifier.fillMaxSize()
                )
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Dart Execution Sandbox",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.textPrimary
                                        )
                                        Text(
                                            text = "Live AST code interpreter for loops, collections & null safety",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            val result = DartSimulatorEngine.execute(editableCode)
                                            executionResult = result
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = tokens.buttonPrimary,
                                            contentColor = tokens.buttonPrimaryText
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                        modifier = Modifier.testTag("run_dart_sandbox_button")
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Run", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = editableCode,
                                    onValueChange = { editableCode = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(230.dp)
                                        .testTag("dart_sandbox_editor"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = tokens.primary,
                                        unfocusedBorderColor = tokens.border,
                                        focusedTextColor = tokens.textPrimary,
                                        unfocusedTextColor = tokens.textPrimary,
                                        focusedContainerColor = tokens.codeBackground,
                                        unfocusedContainerColor = tokens.codeBackground
                                    ),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }

                    // Console Output
                    if (executionResult != null) {
                        item {
                            val res = executionResult!!
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "Console Output",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.textPrimary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = if (res.isSuccess) tokens.success.copy(alpha = 0.12f) else tokens.error.copy(alpha = 0.12f)
                                        ) {
                                            Text(
                                                text = if (res.isSuccess) "EXIT 0" else "ERROR",
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (res.isSuccess) tokens.success else tokens.error
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(tokens.codeBackground, RoundedCornerShape(10.dp))
                                            .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                                            .padding(14.dp)
                                    ) {
                                        Text(
                                            text = if (res.output.isEmpty()) "(No standard output produced)" else res.output,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp,
                                            color = if (res.isSuccess) tokens.textPrimary else tokens.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Flashcards Carousel
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val currentCard = flashcards[currentCardIndex]

                    val rotation by animateFloatAsState(
                        targetValue = if (isCardFlipped) 180f else 0f,
                        animationSpec = tween(durationMillis = 400),
                        label = "card_flip"
                    )

                    Text(
                        text = "Card ${currentCardIndex + 1} of ${flashcards.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = tokens.textSecondary
                    )

                    // 3D Flip Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .border(1.dp, tokens.border, RoundedCornerShape(18.dp))
                            .clickable { isCardFlipped = !isCardFlipped }
                            .testTag("flashcard_flip_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation <= 90f) {
                                // Front: Question
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = tokens.subtleAccentBg
                                    ) {
                                        Text(
                                            text = currentCard.category.uppercase(),
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Text(
                                        text = currentCard.term,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = tokens.textPrimary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Tap to reveal answer ↺",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tokens.textMuted
                                    )
                                }
                            } else {
                                // Back: Answer
                                Column(
                                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = tokens.success.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "DEFINITION",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.success
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text(
                                        text = currentCard.definition,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = tokens.textPrimary,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    // Navigation Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (currentCardIndex > 0) {
                                    isCardFlipped = false
                                    currentCardIndex--
                                }
                            },
                            enabled = currentCardIndex > 0,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.border),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = tokens.textPrimary)
                        ) {
                            Text("Previous")
                        }

                        Button(
                            onClick = {
                                if (currentCardIndex < flashcards.size - 1) {
                                    isCardFlipped = false
                                    currentCardIndex++
                                }
                            },
                            enabled = currentCardIndex < flashcards.size - 1,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            )
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
            3 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Text(
                            text = "Interactive Certification Exams",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary
                        )
                        Text(
                            text = "Test your skills against scenario-based questions with instant grading.",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }

                    val quizzes = listOf(
                        Triple("Comprehensive Flutter & Dart Mock Exam", "all", "10 randomized questions across all tracks"),
                        Triple("Dart Specialist Assessment", "dart", "Variables, records, patterns, streams & OOP"),
                        Triple("Flutter Senior Architect Quiz", "flutter", "State management, RenderObjects, animations & performance")
                    )

                    items(quizzes) { (title, key, desc) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(16.dp))
                                .clickable { onStartQuiz(key) }
                                .testTag("quiz_launch_$key"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(tokens.subtleAccentBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = tokens.primary, modifier = Modifier.size(22.dp))
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tokens.textSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { onStartQuiz(key) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = tokens.buttonPrimary,
                                        contentColor = tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text("Start", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
