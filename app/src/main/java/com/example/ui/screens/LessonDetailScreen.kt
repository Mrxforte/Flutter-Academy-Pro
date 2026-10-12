package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurriculumData
import com.example.engine.DartSimulatorEngine
import com.example.engine.ExecutionResult
import com.example.model.*
import com.example.ui.components.ArchitectureDiagramView
import com.example.ui.components.CodeBlockView
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lessonId: String,
    currentLanguage: AppLanguage,
    isCompleted: Boolean,
    isBookmarked: Boolean,
    onBack: () -> Unit,
    onToggleComplete: () -> Unit,
    onToggleBookmark: () -> Unit,
    onNavigateToPlaygroundWithCode: (String) -> Unit,
    onNavigateToNextLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val lesson = CurriculumData.getLessonById(lessonId) ?: run {
        Box(modifier = Modifier.fillMaxSize().background(tokens.background), contentAlignment = Alignment.Center) {
            Text("Lesson not found", color = tokens.textPrimary)
        }
        return
    }

    var executionResult by remember(lessonId) { mutableStateOf<ExecutionResult?>(null) }
    var showChallengeSolution by remember(lessonId) { mutableStateOf(false) }

    Scaffold(
        containerColor = tokens.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = lesson.title,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("lesson_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = tokens.textPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onToggleBookmark, modifier = Modifier.testTag("lesson_bookmark_button")) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) tokens.primary else tokens.textSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = tokens.surface)
            )
        },
        bottomBar = {
            Surface(
                color = tokens.surface,
                modifier = Modifier.border(width = 1.dp, color = tokens.border, shape = RoundedCornerShape(0.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onToggleComplete,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isCompleted) tokens.success else tokens.textSecondary
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(
                                if (isCompleted) tokens.success.copy(alpha = 0.5f) else tokens.border
                            )
                        ),
                        modifier = Modifier.testTag("mark_complete_button")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = null,
                            tint = if (isCompleted) tokens.success else tokens.textSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isCompleted) "Completed" else "Mark Complete",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    val nextLesson = CurriculumData.allLessons.getOrNull(
                        CurriculumData.allLessons.indexOfFirst { it.id == lesson.id } + 1
                    )

                    if (nextLesson != null) {
                        Button(
                            onClick = { onNavigateToNextLesson(nextLesson.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("next_lesson_button")
                        ) {
                            Text("Next Lesson", color = tokens.buttonPrimaryText, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = tokens.buttonPrimaryText, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("lesson_detail_lazy_column"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Module + Difficulty + Duration
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lesson.moduleTitle.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = tokens.primary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DifficultyBadge(difficulty = lesson.difficulty)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${lesson.durationMinutes} min",
                            style = MaterialTheme.typography.labelSmall,
                            color = tokens.textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
            }

            // Learning Objectives
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = tokens.secondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Learning Objectives",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        lesson.objectives.forEach { obj ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = tokens.secondary, fontWeight = FontWeight.Bold)
                                Text(
                                    text = obj,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = tokens.textSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Analogy Box
            if (lesson.analogy.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.warning.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                            Text(text = "💡", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Conceptual Analogy",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tokens.warning
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.analogy,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = tokens.textSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Deep Explanation Content
            item {
                Text(
                    text = lesson.detailedContent,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp,
                    color = tokens.textPrimary
                )
            }

            // Architecture Diagram
            if (lesson.diagramDescription.isNotEmpty()) {
                item {
                    ArchitectureDiagramView(
                        title = lesson.diagramTitle.ifEmpty { "Architecture Schema" },
                        asciiDiagram = lesson.diagramDescription
                    )
                }
            }

            // Code Example with Interactive Runner
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Interactive Code Example",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = tokens.textPrimary
                        )
                        TextButton(
                            onClick = { onNavigateToPlaygroundWithCode(lesson.codeExample) }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp), tint = tokens.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Open in Sandbox", style = MaterialTheme.typography.labelSmall, color = tokens.primary)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    CodeBlockView(
                        code = lesson.codeExample,
                        title = "Dart & Flutter Example",
                        onRunCode = {
                            executionResult = DartSimulatorEngine.execute(lesson.codeExample)
                        }
                    )

                    // Execution Console Output Panel
                    if (executionResult != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(10.dp)),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.codeBackground),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = ">_ Console Output (${executionResult?.executionTimeMs}ms)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.secondary
                                    )
                                    Text(
                                        text = "Dart VM Active",
                                        fontSize = 10.sp,
                                        color = tokens.textMuted
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = executionResult?.output ?: "",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = tokens.textPrimary
                                )

                                if (executionResult?.widgetTreePreview != null) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = executionResult?.widgetTreePreview ?: "",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = tokens.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step-by-Step Code Walkthrough
            if (lesson.walkthroughSteps.isNotEmpty()) {
                item {
                    Text(
                        text = "Step-by-Step Walkthrough",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                }
                items(lesson.walkthroughSteps) { step ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(tokens.primary.copy(alpha = 0.15f), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${step.stepNumber}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = tokens.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = step.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tokens.textPrimary
                                )
                                if (step.codeLine.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = step.codeLine,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = tokens.secondary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = step.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = tokens.textSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Common Mistakes & Fixes
            if (lesson.mistakes.isNotEmpty()) {
                item {
                    Text(
                        text = "Common Mistakes & Fixes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                }
                items(lesson.mistakes) { mistake ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = mistake.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            // Wrong
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.error.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .border(1.dp, tokens.error.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "✕ Avoid",
                                    color = tokens.error,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mistake.wrongCode,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = tokens.error
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            // Right
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.success.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .border(1.dp, tokens.success.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "✓ Best Practice",
                                    color = tokens.success,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = mistake.rightCode,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = tokens.success
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mistake.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = tokens.textSecondary
                            )
                        }
                    }
                }
            }

            // Practical Challenge Box
            if (lesson.challengePrompt.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.primary.copy(alpha = 0.3f), RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎯", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Practice Challenge",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tokens.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = lesson.challengePrompt,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row {
                                OutlinedButton(
                                    onClick = { showChallengeSolution = !showChallengeSolution },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (showChallengeSolution) "Hide Solution" else "Show Solution",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onNavigateToPlaygroundWithCode(lesson.challengeSolution) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = tokens.buttonPrimary,
                                        contentColor = tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Open in Sandbox", color = tokens.buttonPrimaryText, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            if (showChallengeSolution) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(tokens.codeBackground, RoundedCornerShape(8.dp))
                                        .border(1.dp, tokens.border, RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = lesson.challengeSolution,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        color = tokens.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
