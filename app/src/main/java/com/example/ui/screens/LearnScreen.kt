package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurriculumData
import com.example.model.*
import com.example.ui.components.DifficultyBadge
import com.example.ui.theme.AppTheme

@Composable
fun LearnScreen(
    currentLanguage: AppLanguage,
    completedLessonIds: Set<String>,
    onNavigateToLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    var selectedTrack by remember { mutableStateOf(CourseTrack.DART) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    var expandedModuleId by remember { mutableStateOf<String?>("dart_mod_1") }

    val modules = remember(selectedTrack) {
        CurriculumData.getModulesForTrack(selectedTrack)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .testTag("learn_screen_column")
    ) {
        // Segmented Track Selector (Dart vs Flutter)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .background(tokens.surfaceSecondary, RoundedCornerShape(12.dp))
                .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            val isDart = selectedTrack == CourseTrack.DART
            val dartBg = if (isDart) tokens.buttonPrimary else tokens.surfaceSecondary
            val dartTextColor = if (isDart) tokens.buttonPrimaryText else tokens.textSecondary

            val flutterBg = if (!isDart) tokens.buttonPrimary else tokens.surfaceSecondary
            val flutterTextColor = if (!isDart) tokens.buttonPrimaryText else tokens.textSecondary

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(dartBg)
                    .clickable {
                        selectedTrack = CourseTrack.DART
                        expandedModuleId = "dart_mod_1"
                    }
                    .padding(vertical = 10.dp)
                    .testTag("tab_dart"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dart Mastery",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isDart) FontWeight.Bold else FontWeight.Medium,
                    color = dartTextColor
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(flutterBg)
                    .clickable {
                        selectedTrack = CourseTrack.FLUTTER
                        expandedModuleId = "fl_mod_1"
                    }
                    .padding(vertical = 10.dp)
                    .testTag("tab_flutter"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Flutter Framework",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (!isDart) FontWeight.Bold else FontWeight.Medium,
                    color = flutterTextColor
                )
            }
        }

        // Search & Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search lessons, topics...", style = MaterialTheme.typography.bodySmall, color = tokens.textMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = tokens.textSecondary, modifier = Modifier.size(18.dp)) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("learn_search_field"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = tokens.primary,
                    unfocusedBorderColor = tokens.border,
                    focusedTextColor = tokens.textPrimary,
                    unfocusedTextColor = tokens.textPrimary,
                    focusedContainerColor = tokens.surface,
                    unfocusedContainerColor = tokens.surface
                ),
                singleLine = true
            )
        }

        // Difficulty Chips Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                val isAll = selectedDifficulty == null
                FilterChip(
                    selected = isAll,
                    onClick = { selectedDifficulty = null },
                    label = { Text("All Levels", style = MaterialTheme.typography.labelSmall) },
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
            items(Difficulty.entries.toTypedArray()) { diff ->
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

        // Modules & Lessons List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(modules) { module ->
                val isExpanded = expandedModuleId == module.id
                val lessons = remember(module, searchQuery, selectedDifficulty) {
                    module.lessons.filter { lesson ->
                        val matchesQuery = searchQuery.isBlank() ||
                                lesson.title.contains(searchQuery, ignoreCase = true) ||
                                lesson.description.contains(searchQuery, ignoreCase = true) ||
                                lesson.detailedContent.contains(searchQuery, ignoreCase = true)
                        val matchesDiff = selectedDifficulty == null || lesson.difficulty == selectedDifficulty
                        matchesQuery && matchesDiff
                    }
                }

                if (lessons.isNotEmpty() || searchQuery.isBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = tokens.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column {
                            // Module Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedModuleId = if (isExpanded) null else module.id
                                    }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(tokens.subtleAccentBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (selectedTrack == CourseTrack.DART) Icons.Default.Terminal else Icons.Default.Layers,
                                            contentDescription = null,
                                            tint = tokens.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = module.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.textPrimary
                                        )
                                        Text(
                                            text = "${lessons.size} lessons • ${module.description}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Toggle Module",
                                    tint = tokens.textSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Expanded Lessons List
                            AnimatedVisibility(visible = isExpanded) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .padding(bottom = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    HorizontalDivider(color = tokens.border, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(4.dp))

                                    lessons.forEach { lesson ->
                                        val isCompleted = completedLessonIds.contains(lesson.id)

                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(1.dp, tokens.border, RoundedCornerShape(10.dp))
                                                .clickable { onNavigateToLesson(lesson.id) }
                                                .testTag("lesson_card_${lesson.id}"),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (isCompleted) tokens.surfaceSecondary else tokens.surface
                                            ),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(26.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            if (isCompleted) tokens.success.copy(alpha = 0.15f)
                                                            else tokens.surfaceSecondary
                                                        ),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.PlayArrow,
                                                        contentDescription = null,
                                                        tint = if (isCompleted) tokens.success else tokens.textSecondary,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(10.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = lesson.title,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = tokens.textPrimary
                                                    )
                                                    Text(
                                                        text = "${lesson.durationMinutes} min • ${lesson.description}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = tokens.textSecondary,
                                                        maxLines = 1
                                                    )
                                                }

                                                Spacer(modifier = Modifier.width(8.dp))
                                                DifficultyBadge(difficulty = lesson.difficulty)
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
