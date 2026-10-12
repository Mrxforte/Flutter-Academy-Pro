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

    // Default to Widget Playground (0) if initialPlaygroundCode is provided or as first tab
    var selectedSection by remember { mutableIntStateOf(0) } // 0: Widget Playground, 1: Dart Sandbox, 2: Flashcards, 3: Certification

    // Dart Sandbox state
    val defaultCode = """
void main() {
  print("Welcome to Flutter Academy! 🌿");
  var features = ["Sage UI", "Live Widgets", "Dart 3", "Impeller"];
  for (var f in features) {
    print("Mastering: ${'$'}f");
  }
}
    """.trimIndent()

    var editableCode by remember {
        mutableStateOf(defaultCode)
    }
    var executionResult by remember { mutableStateOf<ExecutionResult?>(null) }

    // Flashcard state
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
                val tabBg = if (isSelected) {
                    if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary
                } else tokens.surfaceSecondary

                val tabTextColor = if (isSelected) {
                    if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText
                } else tokens.textSecondary

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(tabBg)
                        .clickable { selectedSection = index }
                        .padding(vertical = 8.dp)
                        .testTag("tab_practice_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = tabTextColor,
                        maxLines = 1
                    )
                }
            }
        }

        when (selectedSection) {
            0 -> {
                // TAB 0: Real Interactive Flutter Widget Playground
                FlutterPlaygroundView(
                    initialCode = initialPlaygroundCode,
                    modifier = Modifier.fillMaxSize()
                )
            }

            1 -> {
                // TAB 1: Dart Console Sandbox
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    item {
                        Text(
                            text = "Dart In-Memory Sandbox",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = tokens.textPrimary
                        )
                        Text(
                            text = "Execute and inspect pure Dart algorithms, collections, and classes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }

                    // Template Chips
                    item {
                        val templates = listOf(
                            "Dart Basics" to """
void main() {
  var name = "Flutter Dev";
  print("Hello, ${'$'}name!");
}
                            """.trimIndent(),
                            "Collection-If" to """
void main() {
  bool isPro = true;
  var features = [
    "Free Lessons",
    if (isPro) "Certificate Exam",
  ];
  print(features);
}
                            """.trimIndent(),
                            "Async-Await" to """
Future<String> fetchUserData() async {
  return "User: Azamat (Sage Pro)";
}

void main() async {
  print("Loading...");
  var user = await fetchUserData();
  print(user);
}
                            """.trimIndent()
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(templates) { (title, code) ->
                                val isChosen = editableCode == code
                                FilterChip(
                                    selected = isChosen,
                                    onClick = {
                                        editableCode = code
                                        executionResult = null
                                    },
                                    label = { Text(title, style = MaterialTheme.typography.labelSmall) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                                        selectedLabelColor = if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText,
                                        containerColor = tokens.surfaceSecondary,
                                        labelColor = tokens.textSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isChosen,
                                        borderColor = if (isChosen) tokens.primary else tokens.border
                                    )
                                )
                            }
                        }
                    }

                    // Editable Code Area
                    item {
                        OutlinedTextField(
                            value = editableCode,
                            onValueChange = { editableCode = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .testTag("playground_code_input"),
                            textStyle = LocalTextStyle.current.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = tokens.codeText
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = tokens.codeBackground,
                                unfocusedContainerColor = tokens.codeBackground,
                                focusedBorderColor = tokens.primary,
                                unfocusedBorderColor = tokens.border
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Action buttons: Black CTA in light mode, Sage in dark mode
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    executionResult = DartSimulatorEngine.execute(editableCode)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("playground_run_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = tokens.buttonPrimary,
                                    contentColor = tokens.buttonPrimaryText
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = tokens.buttonPrimaryText, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Run Code", color = tokens.buttonPrimaryText, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            }

                            OutlinedButton(
                                onClick = {
                                    editableCode = defaultCode
                                    executionResult = null
                                },
                                shape = RoundedCornerShape(12.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(tokens.border)
                                )
                            ) {
                                Text("Reset", style = MaterialTheme.typography.labelMedium, color = tokens.textSecondary)
                            }
                        }
                    }

                    // Console Output
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.codeBackground),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = ">_ Console Output",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.secondary
                                    )
                                    if (executionResult != null) {
                                        Text(
                                            text = "${executionResult?.executionTimeMs}ms",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = tokens.textMuted
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = executionResult?.output ?: "Press 'Run Code' to execute Dart code.",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = if (executionResult?.isSuccess == true) tokens.codeText else tokens.textMuted
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // TAB 2: Spaced Repetition Flashcards
                val card = flashcards[currentCardIndex]
                val rotation by animateFloatAsState(
                    targetValue = if (isCardFlipped) 180f else 0f,
                    animationSpec = tween(300),
                    label = "card_flip"
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Card ${currentCardIndex + 1} of ${flashcards.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = tokens.textMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .border(1.dp, tokens.border, RoundedCornerShape(18.dp))
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .clickable { isCardFlipped = !isCardFlipped }
                            .testTag("flashcard_item"),
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
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (tokens.isDark) tokens.surfaceSecondary else tokens.sageSoft
                                    ) {
                                        Text(
                                            text = card.category.uppercase(),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (tokens.isDark) tokens.sagePrimary else Color(0xFF000000),
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Text(
                                        text = card.term,
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text = "Tap card to reveal explanation",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tokens.textMuted
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = card.definition,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = tokens.textPrimary
                                    )
                                    if (card.codeSnippet != null) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(tokens.codeBackground, RoundedCornerShape(8.dp))
                                                .border(1.dp, tokens.border, RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = card.codeSnippet,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 11.sp,
                                                color = tokens.secondary
                                            )
                                        }
                                    }
                                    if (card.bestPracticeTip.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "💡 Tip: ${card.bestPracticeTip}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = tokens.warning
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Rating actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isCardFlipped = false
                                currentCardIndex = (currentCardIndex + 1) % flashcards.size
                                Toast.makeText(context, "Scheduled in 1 day", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = androidx.compose.ui.graphics.SolidColor(tokens.border)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Hard (+1d)", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                        }

                        Button(
                            onClick = {
                                isCardFlipped = false
                                currentCardIndex = (currentCardIndex + 1) % flashcards.size
                                Toast.makeText(context, "Scheduled in 3 days", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonSecondary,
                                contentColor = tokens.buttonSecondaryText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Good (+3d)", style = MaterialTheme.typography.labelSmall, color = tokens.buttonSecondaryText)
                        }

                        Button(
                            onClick = {
                                isCardFlipped = false
                                currentCardIndex = (currentCardIndex + 1) % flashcards.size
                                Toast.makeText(context, "Mastered (+7d)", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Easy (+7d)", style = MaterialTheme.typography.labelSmall, color = tokens.buttonPrimaryText)
                        }
                    }
                }
            }

            3 -> {
                // TAB 3: Certification Exams
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    item {
                        Text(
                            text = "Certification & Technical Mock Exams",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = tokens.textPrimary
                        )
                        Text(
                            text = "Timed assessments aligned with technical Flutter developer interviews.",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }

                    val exams = listOf(
                        Triple("Comprehensive Flutter Mock Exam", "10 questions • 15 minutes • Passing: 80%", "all"),
                        Triple("Dart Language & Null Safety Test", "8 questions • 10 minutes • Passing: 75%", "dart"),
                        Triple("Flutter UI, State & Architecture", "8 questions • 10 minutes • Passing: 75%", "flutter")
                    )

                    items(exams) { (title, subtitle, trackKey) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (tokens.isDark) tokens.surfaceSecondary else tokens.sageSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Assignment,
                                        contentDescription = null,
                                        tint = if (tokens.isDark) tokens.sagePrimary else tokens.textPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tokens.textSecondary
                                    )
                                }
                                Button(
                                    onClick = { onStartQuiz(trackKey) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = tokens.buttonPrimary,
                                        contentColor = tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                    modifier = Modifier.testTag("start_exam_${trackKey}")
                                ) {
                                    Text("Start", color = tokens.buttonPrimaryText, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
