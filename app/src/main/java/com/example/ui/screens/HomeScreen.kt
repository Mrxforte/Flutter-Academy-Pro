package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun HomeScreen(
    userProfile: UserProfile,
    currentLanguage: AppLanguage,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToPlayground: (String?) -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToProfile: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens

    val continueLesson = CurriculumData.allLessons.firstOrNull {
        !userProfile.completedLessonIds.contains(it.id)
    } ?: CurriculumData.allLessons.first()

    val totalLessons = CurriculumData.allLessons.size
    val completedCount = userProfile.completedLessonIds.size
    val overallProgressPercent = if (totalLessons > 0) ((completedCount.toFloat() / totalLessons) * 100).toInt() else 0

    // Curated list of required core tracks
    val coreTracks = listOf(
        Triple("Dart Fundamentals", "dart_mod_1", "Core syntax, variables & null safety"),
        Triple("Dart OOP", "dart_mod_2", "Classes, mixins & inheritance"),
        Triple("Flutter Widgets", "fl_mod_1", "Stateless, Stateful & BuildContext"),
        Triple("Layouts and Constraints", "fl_mod_2", "Row, Column, Stack & Flex"),
        Triple("Navigation", "fl_mod_4", "GoRouter, Navigator 2.0 & routes"),
        Triple("State Management", "fl_mod_5", "BLoC, Riverpod, Provider"),
        Triple("API Integration", "fl_mod_7", "HTTP, JSON serialization & caching"),
        Triple("Firebase", "fl_mod_9", "Auth, Firestore & Cloud Storage"),
        Triple("Clean Architecture", "fl_mod_6", "Domain, Data & Presentation layers"),
        Triple("Testing and Deployment", "fl_mod_12", "Unit, Widget tests & CI/CD")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. HEADER: Small greeting, Strong heading, Profile Avatar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good day, ${userProfile.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.textSecondary,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Learn Flutter & Dart",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(tokens.surface)
                        .border(1.dp, tokens.border, CircleShape)
                        .clickable { onNavigateToProfile?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.primary
                    )
                }
            }
        }

        // 2. FEATURED LEARNING CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, tokens.border, RoundedCornerShape(18.dp))
                    .clickable { onNavigateToLesson(continueLesson.id) }
                    .testTag("continue_learning_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = tokens.subtleAccentBg
                        ) {
                            Text(
                                text = "CURRENT PATH: ${continueLesson.courseTrack.name}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = tokens.primary,
                                letterSpacing = 0.5.sp
                            )
                        }

                        DifficultyBadge(difficulty = continueLesson.difficulty)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = continueLesson.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = continueLesson.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = tokens.textSecondary,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress Overview inside Featured Card
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Overall Course Progress",
                                style = MaterialTheme.typography.labelSmall,
                                color = tokens.textSecondary
                            )
                            Text(
                                text = "$overallProgressPercent% • $completedCount/$totalLessons completed",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (completedCount.toFloat() / totalLessons.coerceAtLeast(1)).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = tokens.primary,
                            trackColor = tokens.surfaceSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${continueLesson.durationMinutes} min read",
                            style = MaterialTheme.typography.labelSmall,
                            color = tokens.textSecondary
                        )

                        Button(
                            onClick = { onNavigateToLesson(continueLesson.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tokens.buttonPrimary,
                                contentColor = tokens.buttonPrimaryText
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 11.dp),
                            modifier = Modifier.testTag("resume_lesson_button")
                        ) {
                            Text(
                                text = "Continue Learning",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = tokens.buttonPrimaryText
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = tokens.buttonPrimaryText
                            )
                        }
                    }
                }
            }
        }

        // 3. PROGRESS OVERVIEW: Lessons Completed, Streak, Quiz Scores, Learning Hours
        item {
            Column {
                Text(
                    text = "Progress Overview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = tokens.textPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProgressStatBox(
                        title = "Lessons Done",
                        value = "$completedCount / $totalLessons",
                        badge = "✓",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressStatBox(
                        title = "Learning Streak",
                        value = "${userProfile.streakDays} Days",
                        badge = "🔥",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProgressStatBox(
                        title = "Quiz Score",
                        value = "${userProfile.xp} XP",
                        badge = "⚡",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressStatBox(
                        title = "Learning Hours",
                        value = "${(userProfile.todayMinutesSpent / 60.0 + 3.5).let { "%.1f".format(it) }}h",
                        badge = "⏱",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 4. DAILY CODING CHALLENGE
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
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
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = tokens.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Daily Challenge",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = tokens.surfaceSecondary
                            ) {
                                Text(
                                    text = "5 MIN • INTERMEDIATE",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tokens.textSecondary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Filter collections with .where() & format records",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { onNavigateToPlayground(null) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tokens.buttonPrimary,
                            contentColor = tokens.buttonPrimaryText
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("solve_challenge_button")
                    ) {
                        Text("Solve", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // 5. LIVE FLUTTER WIDGET PLAYGROUND BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, tokens.border, RoundedCornerShape(16.dp))
                    .clickable { onNavigateToPlayground(null) }
                    .testTag("home_playground_banner"),
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
                        Icon(
                            imageVector = Icons.Default.Widgets,
                            contentDescription = null,
                            tint = tokens.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Flutter Widget Playground",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary
                        )
                        Text(
                            text = "Real dynamically rendered Flutter widget tree",
                            style = MaterialTheme.typography.bodySmall,
                            color = tokens.textSecondary
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open Playground",
                        tint = tokens.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 6. COURSE LIST (All 10 required courses)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Curriculum Courses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = tokens.textPrimary
                    )
                    Text(
                        text = "${coreTracks.size} Core Tracks",
                        style = MaterialTheme.typography.labelSmall,
                        color = tokens.textMuted
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    coreTracks.forEach { (title, moduleId, subtitle) ->
                        val matchingModule = CurriculumData.allModules.firstOrNull { it.id == moduleId }
                        val moduleLessons = matchingModule?.lessons ?: emptyList()
                        val completedInModule = moduleLessons.count { userProfile.completedLessonIds.contains(it.id) }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(14.dp))
                                .clickable {
                                    val firstLesson = moduleLessons.firstOrNull() ?: continueLesson
                                    onNavigateToLesson(firstLesson.id)
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(tokens.subtleAccentBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (moduleId.startsWith("dart")) Icons.Default.Terminal else Icons.Default.Layers,
                                        contentDescription = null,
                                        tint = tokens.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

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
                                        color = tokens.textSecondary,
                                        maxLines = 1
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = tokens.surfaceSecondary
                                ) {
                                    Text(
                                        text = "$completedInModule/${moduleLessons.size.coerceAtLeast(3)}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
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

@Composable
private fun ProgressStatBox(
    title: String,
    value: String,
    badge: String,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    Card(
        modifier = modifier.border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = tokens.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(tokens.surfaceSecondary),
                contentAlignment = Alignment.Center
            ) {
                Text(badge, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = tokens.textSecondary,
                    maxLines = 1
                )
            }
        }
    }
}
