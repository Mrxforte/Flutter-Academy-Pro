package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProjectData
import com.example.model.*
import com.example.ui.components.CodeBlockView
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.AppTheme

@Composable
fun ProjectsScreen(
    currentLanguage: AppLanguage,
    completedProjectIds: Set<String>,
    onToggleProjectComplete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    var expandedProjectId by remember { mutableStateOf<String?>("proj_01") }

    val projects = remember(selectedDifficulty) {
        if (selectedDifficulty == null) ProjectData.projects
        else ProjectData.projects.filter { it.level == selectedDifficulty }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .testTag("projects_screen_column")
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
            Text(
                text = "Guided Project Lab",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = tokens.textPrimary
            )
            Text(
                text = "Production Flutter architectures with stage-by-stage guidance and runnable code.",
                style = MaterialTheme.typography.bodySmall,
                color = tokens.textSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Difficulty Filters
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    val isAll = selectedDifficulty == null
                    FilterChip(
                        selected = isAll,
                        onClick = { selectedDifficulty = null },
                        label = { Text("All Projects", style = MaterialTheme.typography.labelSmall) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                            selectedLabelColor = if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText,
                            containerColor = tokens.surfaceSecondary,
                            labelColor = tokens.textSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isAll,
                            borderColor = if (isAll) tokens.primary else tokens.border
                        )
                    )
                }
                items(listOf(Difficulty.BEGINNER, Difficulty.INTERMEDIATE, Difficulty.ADVANCED)) { diff ->
                    val isSelected = selectedDifficulty == diff
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedDifficulty = if (isSelected) null else diff },
                        label = { Text(diff.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelSmall) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                            selectedLabelColor = if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText,
                            containerColor = tokens.surfaceSecondary,
                            labelColor = tokens.textSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) tokens.primary else tokens.border
                        )
                    )
                }
            }
        }

        // Projects List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            items(projects) { project ->
                val isDone = completedProjectIds.contains(project.id)
                val isExpanded = expandedProjectId == project.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        // Project Card Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedProjectId = if (isExpanded) null else project.id
                                }
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DifficultyBadge(difficulty = project.level)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${project.durationMinutes} min",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tokens.textMuted
                                    )
                                    if (isDone) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Done",
                                            tint = tokens.success,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = project.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = project.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = tokens.textSecondary
                                )
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle project",
                                tint = tokens.textMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Expanded Content
                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.surfaceSecondary)
                                    .padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Features Checklist
                                Column {
                                    Text(
                                        text = "Features & Capabilities",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    project.features.forEach { feature ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, tint = tokens.success, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = feature, style = MaterialTheme.typography.bodySmall, color = tokens.textSecondary)
                                        }
                                    }
                                }

                                // Architecture Explanation
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Architecture: ${project.architectureType}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.primary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = project.architectureExplanation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary
                                        )
                                    }
                                }

                                // Folder Structure
                                Column {
                                    Text(
                                        text = "Project Tree Structure",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = tokens.textPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(tokens.codeBackground, RoundedCornerShape(10.dp))
                                            .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = project.folderTree,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp,
                                            color = tokens.codeText
                                        )
                                    }
                                }

                                // Steps & Code Walkthrough
                                project.steps.forEach { step ->
                                    Column {
                                        Text(
                                            text = "Step ${step.stepNumber}: ${step.title}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.textPrimary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = step.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        CodeBlockView(
                                            code = step.codeSnippet,
                                            title = step.fileName
                                        )
                                    }
                                }

                                // Common Bugs
                                if (project.commonBugs.isNotEmpty()) {
                                    Column {
                                        Text(
                                            text = "Watch Out (Common Mistakes)",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.warning
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        project.commonBugs.forEach { bug ->
                                            Text(
                                                text = "• $bug",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = tokens.textSecondary,
                                                modifier = Modifier.padding(vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                // Mark Complete Action Button: Black in light mode, Sage in dark mode
                                Button(
                                    onClick = { onToggleProjectComplete(project.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isDone) tokens.success else tokens.buttonPrimary,
                                        contentColor = if (isDone) Color.White else tokens.buttonPrimaryText
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("toggle_project_complete_${project.id}")
                                ) {
                                    Icon(
                                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (isDone) Color.White else tokens.buttonPrimaryText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isDone) "Project Completed (+150 XP)" else "Mark Project Finished (+150 XP)",
                                        color = if (isDone) Color.White else tokens.buttonPrimaryText,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold
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
