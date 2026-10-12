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
import com.example.data.AchievementData
import com.example.data.CurriculumData
import com.example.model.*
import com.example.ui.theme.AppTheme
import com.example.ui.theme.AppThemeMode

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    currentLanguage: AppLanguage,
    currentThemeMode: AppThemeMode,
    onSelectThemeMode: (AppThemeMode) -> Unit,
    onOpenLanguageDialog: () -> Unit,
    onNavigateToLesson: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val bookmarkedLessons = CurriculumData.allLessons.filter {
        userProfile.bookmarkedLessonIds.contains(it.id)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(tokens.background)
            .testTag("profile_screen_lazy_column"),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Profile Header: Minimalist and refined
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, tokens.border, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(tokens.surfaceSecondary)
                            .border(1.dp, tokens.border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (tokens.isDark) tokens.sagePrimary else tokens.textPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = userProfile.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (tokens.isDark) tokens.surfaceSecondary else tokens.surfaceSecondary
                    ) {
                        Text(
                            text = userProfile.skillLevel.uppercase(),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = tokens.textSecondary,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // XP Progress bar
                    val nextLevelTarget = 1000
                    val progressFraction = (userProfile.xp.toFloat() / nextLevelTarget).coerceIn(0f, 1f)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${userProfile.xp} XP",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Text(
                                text = "Next Rank: $nextLevelTarget XP",
                                style = MaterialTheme.typography.labelSmall,
                                color = tokens.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary,
                            trackColor = tokens.surfaceSecondary
                        )
                    }
                }
            }
        }

        // Stats Summary: Creamy white surfaces
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${userProfile.completedLessonIds.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = tokens.textPrimary)
                        Text(text = "Lessons", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${userProfile.completedProjectIds.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = tokens.textPrimary)
                        Text(text = "Projects", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = tokens.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${userProfile.streakDays}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = tokens.textPrimary)
                        Text(text = "Streak", style = MaterialTheme.typography.labelSmall, color = tokens.textSecondary)
                    }
                }
            }
        }

        // Appearance Mode Switcher (Section 3: Light, Dark, System default)
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
                        text = "Appearance Theme",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Sage Green + Warm Neutral palette across all screens.",
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.textSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // 3-Option Segmented Control: Dark, Light, System
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(tokens.surfaceSecondary, RoundedCornerShape(12.dp))
                            .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            val isSelected = currentThemeMode == mode
                            val bg = if (isSelected) {
                                if (tokens.isDark) tokens.sagePrimary else tokens.buttonPrimary
                            } else tokens.surfaceSecondary

                            val textColor = if (isSelected) {
                                if (tokens.isDark) tokens.surface else tokens.buttonPrimaryText
                            } else tokens.textSecondary

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bg)
                                    .clickable { onSelectThemeMode(mode) }
                                    .padding(vertical = 9.dp)
                                    .testTag("theme_mode_${mode.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }
        }

        // Language & Localization Settings
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
                        text = "Language & Region",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Curriculum available in English, Uzbek, and Russian.",
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.textSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLanguageDialog() },
                        shape = RoundedCornerShape(10.dp),
                        color = tokens.surfaceSecondary
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = currentLanguage.flag, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = currentLanguage.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = tokens.textPrimary
                                )
                            }
                            Text(
                                text = "Change",
                                style = MaterialTheme.typography.labelMedium,
                                color = tokens.textSecondary
                            )
                        }
                    }
                }
            }
        }

        // Achievements Showcase
        item {
            Column {
                Text(
                    text = "Achievement Badges",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(AchievementData.badges) { badge ->
                        val isUnlocked = badge.isUnlocked || userProfile.earnedBadgeIds.contains(badge.id) || userProfile.xp >= badge.xpReward
                        Card(
                            modifier = Modifier
                                .width(150.dp)
                                .border(1.dp, tokens.border, RoundedCornerShape(14.dp)),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUnlocked) tokens.surface else tokens.surfaceSecondary
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = badge.iconEmoji, fontSize = 26.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = badge.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUnlocked) tokens.textPrimary else tokens.textMuted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = badge.description,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = tokens.textMuted,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bookmarked Lessons
        if (bookmarkedLessons.isNotEmpty()) {
            item {
                Column {
                    Text(
                        text = "Saved Bookmarks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = tokens.textPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        bookmarkedLessons.forEach { lesson ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                                    .clickable { onNavigateToLesson(lesson.id) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = lesson.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.textPrimary
                                        )
                                        Text(
                                            text = lesson.moduleTitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = tokens.textSecondary
                                        )
                                    }
                                    Icon(
                                        Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = if (tokens.isDark) tokens.sagePrimary else tokens.textPrimary,
                                        modifier = Modifier.size(18.dp)
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
