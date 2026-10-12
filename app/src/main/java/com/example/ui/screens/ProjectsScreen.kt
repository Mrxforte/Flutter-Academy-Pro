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
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
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
                            selectedContainerColor = tokens.primary,
                            selectedLabelColor = Color.White,
                            containerColor = tokens.surface,
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
                            selectedContainerColor = tokens.primary,
                            selectedLabelColor = Color.White,
                            containerColor = tokens.surface,
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

        // Project Cards List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(projects) { project ->
                val isExpanded = expandedProjectId == project.id
                val isCompleted = completedProjectIds.contains(project.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, tokens.border, RoundedCornerShape(16.dp))
                        .testTag("project_card_${project.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column {
                        // Card Header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedProjectId = if (isExpanded) null else project.id
                                }
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    DifficultyBadge(difficulty = project.level)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tokens.surfaceSecondary
                                    ) {
                                        Text(
                                            text = "${project.durationMinutes}m build",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.textSecondary
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onToggleProjectComplete(project.id) },
                                        modifier = Modifier.size(32.dp).testTag("toggle_complete_${project.id}")
                                    ) {
                                        Icon(
                                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = "Mark Complete",
                                            tint = if (isCompleted) tokens.success else tokens.textMuted,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Toggle Details",
                                        tint = tokens.textSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = project.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = project.summary,
                                style = MaterialTheme.typography.bodyMedium,
                                color = tokens.textSecondary
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Architecture badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = tokens.subtleAccentBg
                                ) {
                                    Text(
                                        text = project.architectureType,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = tokens.primary
                                    )
                                }
                            }
                        }

                        // Expanded Stage-by-Stage Content
                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp)
                                    .padding(bottom = 18.dp)
                            ) {
                                HorizontalDivider(color = tokens.border, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Architectural Stages (${project.steps.size} Steps)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    project.steps.forEachIndexed { sIdx, step ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = tokens.surfaceSecondary),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .background(tokens.primary, RoundedCornerShape(6.dp)),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = "${step.stepNumber}",
                                                            color = Color.White,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(10.dp))
                                                    Text(
                                                        text = step.title,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = tokens.textPrimary
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = step.description,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = tokens.textSecondary
                                                )

                                                if (step.codeSnippet.isNotBlank()) {
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                    CodeBlockView(
                                                        code = step.codeSnippet,
                                                        title = step.fileName
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
            }
        }
    }
}
