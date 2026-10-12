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
                        fontWeight = FontWeight.Bold,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tokens.surface,
                    titleContentColor = tokens.textPrimary,
                    navigationIconContentColor = tokens.textPrimary,
                    actionIconContentColor = tokens.textSecondary
                )
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
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCompleted) tokens.success else tokens.border
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isCompleted) tokens.success.copy(alpha = 0.08f) else tokens.surface,
                            contentColor = if (isCompleted) tokens.success else tokens.textPrimary
                        ),
                        modifier = Modifier.testTag("toggle_lesson_completed_button")
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isCompleted) tokens.success else tokens.textSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isCompleted) "Completed" else "Mark Complete", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    // Next Lesson Button
                    val allLessons = CurriculumData.allLessons
                    val currentIndex = allLessons.indexOfFirst { it.id == lessonId }
                    val nextLesson = if (currentIndex != -1 && currentIndex < allLessons.size - 1) allLessons[currentIndex + 1] else null

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
                            Text("Next Lesson", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            // Header Info Card
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = tokens.subtleAccentBg
                            ) {
                                Text(
                                    text = lesson.courseTrack.name,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.primary
                                )
                            }
                            DifficultyBadge(difficulty = lesson.difficulty)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = tokens.textSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = tokens.textMuted, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${lesson.durationMinutes} min read", style = MaterialTheme.typography.labelSmall, color = tokens.textMuted)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = tokens.warning, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "+50 XP reward", style = MaterialTheme.typography.labelSmall, color = tokens.warning)
                        }
                    }
                }
            }

            // Real-world Analogy Card
            if (lesson.analogy.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "💡", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Mental Model & Analogy",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = lesson.analogy,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textSecondary,
                                lineHeight = 21.sp
                            )
                        }
                    }
                }
            }

            // Diagram (if present)
            if (lesson.diagramTitle.isNotBlank()) {
                item {
                    ArchitectureDiagramView(
                        title = lesson.diagramTitle,
                        asciiDiagram = lesson.diagramDescription
                    )
                }
            }

            // Detailed Content & Code Example
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
                        Text(
                            text = "Concept Deep Dive",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = lesson.detailedContent,
                            style = MaterialTheme.typography.bodyMedium,
                            color = tokens.textPrimary,
                            lineHeight = 22.sp
                        )

                        if (lesson.codeExample.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            CodeBlockView(
                                code = lesson.codeExample,
                                title = "Code Example",
                                onRunCode = {
                                    val result = DartSimulatorEngine.execute(lesson.codeExample)
                                    executionResult = result
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { onNavigateToPlaygroundWithCode(lesson.codeExample) },
                                colors = ButtonDefaults.textButtonColors(contentColor = tokens.primary)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Open in Live Playground Canvas", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Walkthrough Steps
            if (lesson.walkthroughSteps.isNotEmpty()) {
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
                            Text(
                                text = "Step-by-Step Breakdown",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                lesson.walkthroughSteps.forEach { step ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, tokens.border, RoundedCornerShape(10.dp)),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .background(tokens.primary, RoundedCornerShape(6.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "${step.stepNumber}",
                                                        color = Color.White,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = step.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = tokens.textPrimary
                                                )
                                            }
                                            if (step.codeLine.isNotBlank()) {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = step.codeLine,
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 11.sp,
                                                    color = tokens.primary
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
                    }
                }
            }

            // Common Mistakes
            if (lesson.mistakes.isNotEmpty()) {
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
                            Text(
                                text = "Common Pitfalls & Fixes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                lesson.mistakes.forEach { mistake ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .border(1.dp, tokens.border, RoundedCornerShape(10.dp)),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = mistake.title,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = tokens.textPrimary
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Row(verticalAlignment = Alignment.Top) {
                                                Text("❌", fontSize = 12.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = mistake.wrongCode,
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 11.sp,
                                                    color = tokens.error
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Row(verticalAlignment = Alignment.Top) {
                                                Text("✅", fontSize = 12.sp)
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = mistake.rightCode,
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 11.sp,
                                                    color = tokens.success
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = mistake.explanation,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = tokens.textSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Live Simulator Output Banner (if triggered)
            if (executionResult != null) {
                item {
                    val res = executionResult!!
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
                                Text(
                                    text = "Execution Output",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                                IconButton(onClick = { executionResult = null }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Close, contentDescription = "Close", tint = tokens.textSecondary, modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.codeBackground, RoundedCornerShape(10.dp))
                                    .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = if (res.output.isEmpty()) "(Execution completed with no printed output)" else res.output,
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

            // Hands-on Challenge
            if (lesson.challengePrompt.isNotBlank()) {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(tokens.warning.copy(alpha = 0.12f), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = tokens.warning, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Hands-on Challenge",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = lesson.challengePrompt,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textPrimary
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { showChallengeSolution = !showChallengeSolution }
                                ) {
                                    Text(if (showChallengeSolution) "Hide Solution" else "Reveal Solution", color = tokens.primary)
                                }

                                Button(
                                    onClick = { onNavigateToPlaygroundWithCode(lesson.challengeSolution) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = tokens.buttonPrimary,
                                        contentColor = tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Try in Canvas", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (showChallengeSolution && lesson.challengeSolution.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                CodeBlockView(
                                    code = lesson.challengeSolution,
                                    title = "Challenge Solution"
                                )
                            }
                        }
                    }
                }
            }

            // Learning Objectives Card
            if (lesson.objectives.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Learning Objectives",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            lesson.objectives.forEach { obj ->
                                Row(
                                    modifier = Modifier.padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("✓", color = tokens.success, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = obj,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = tokens.textSecondary
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
