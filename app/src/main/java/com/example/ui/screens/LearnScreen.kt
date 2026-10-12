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
import androidx.compose.ui.draw.clip
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
                .padding(horizontal = 22.dp, vertical = 14.dp)
                .background(tokens.surfaceSecondary, RoundedCornerShape(12.dp))
                .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            val isDart = selectedTrack == CourseTrack.DART
            val dartBg = if (isDart) {
                if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary
            } else tokens.surfaceSecondary

            val dartTextColor = if (isDart) {
                if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText
            } else tokens.textSecondary

            val flutterBg = if (!isDart) {
                if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary
            } else tokens.surfaceSecondary

            val flutterTextColor = if (!isDart) {
                if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText
            } else tokens.textSecondary

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(dartBg)
                    .clickable {
                        selectedTrack = CourseTrack.DART
                        expandedModuleId = "dart_mod_1"
                    }
                    .padding(vertical = 9.dp)
                    .testTag("tab_dart"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dart Mastery",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isDart) FontWeight.Bold else FontWeight.Normal,
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
                    .padding(vertical = 9.dp)
                    .testTag("tab_flutter"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Flutter Professional",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (!isDart) FontWeight.Bold else FontWeight.Normal,
                    color = flutterTextColor
                )
            }
        }

        // Search Bar & Filter Chips
        Column(modifier = Modifier.padding(horizontal = 22.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("curriculum_search_input"),
                placeholder = {
                    Text(
                        text = "Search lessons, widgets, syntax...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = tokens.textMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = tokens.textMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = tokens.textMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = tokens.primary,
                    unfocusedBorderColor = tokens.border,
                    focusedContainerColor = tokens.surface,
                    unfocusedContainerColor = tokens.surface
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Difficulty Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    val isAll = selectedDifficulty == null
                    FilterChip(
                        selected = isAll,
                        onClick = { selectedDifficulty = null },
                        label = { Text("All", style = MaterialTheme.typography.labelSmall) },
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
                items(Difficulty.entries.toTypedArray()) { diff ->
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

        Spacer(modifier = Modifier.height(14.dp))

        // Modules & Lessons List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            items(modules) { module ->
                val filteredLessons = module.lessons.filter { lesson ->
                    val matchesDiff = selectedDifficulty == null || lesson.difficulty == selectedDifficulty
                    val matchesQuery = searchQuery.isEmpty() ||
                        lesson.title.contains(searchQuery, ignoreCase = true) ||
                        lesson.description.contains(searchQuery, ignoreCase = true)
                    matchesDiff && matchesQuery
                }

                if (searchQuery.isNotEmpty() && filteredLessons.isEmpty()) {
                    return@items
                }

                val completedCount = module.lessons.count { completedLessonIds.contains(it.id) }
                val isExpanded = expandedModuleId == module.id || searchQuery.isNotEmpty()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
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
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = module.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = tokens.textPrimary
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = module.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = tokens.textSecondary,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    LinearProgressIndicator(
                                        progress = {
                                            if (module.lessons.isNotEmpty()) completedCount.toFloat() / module.lessons.size else 0f
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                                        trackColor = tokens.surfaceSecondary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "$completedCount/${module.lessons.size} done",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tokens.textSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = tokens.textSecondary
                            )
                        }

                        // Expanded Lessons list
                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(tokens.surfaceSecondary)
                            ) {
                                HorizontalDivider(color = tokens.border)

                                filteredLessons.forEach { lesson ->
                                    val isDone = completedLessonIds.contains(lesson.id)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onNavigateToLesson(lesson.id) }
                                            .padding(horizontal = 18.dp, vertical = 12.dp)
                                            .testTag("lesson_item_${lesson.id}"),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = if (isDone) "Done" else "Pending",
                                                tint = if (isDone) tokens.success else tokens.border,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = lesson.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Medium,
                                                    color = tokens.textPrimary
                                                )
                                                Text(
                                                    text = "${lesson.durationMinutes} min read",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = tokens.textMuted
                                                )
                                            }
                                        }

                                        DifficultyBadge(difficulty = lesson.difficulty)
                                    }
                                    HorizontalDivider(
                                        color = tokens.border.copy(alpha = 0.5f),
                                        modifier = Modifier.padding(horizontal = 18.dp)
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
