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
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Profile Header
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
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(tokens.subtleAccentBg)
                            .border(1.dp, tokens.border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = tokens.primary
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
                        color = tokens.surfaceSecondary
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
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = tokens.primary,
                            trackColor = tokens.surfaceSecondary
                        )
                    }
                }
            }
        }

        // Stats Summary Cards
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

        // Appearance Mode Switcher
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
                        text = "Clean light theme with #FFFFFF cards and crisp typography.",
                        style = MaterialTheme.typography.bodySmall,
                        color = tokens.textSecondary
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(tokens.surfaceSecondary, RoundedCornerShape(12.dp))
                            .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
                            .padding(4.dp)
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            val isSelected = currentThemeMode == mode
                            val bg = if (isSelected) tokens.buttonPrimary else tokens.surfaceSecondary
                            val textColor = if (isSelected) tokens.buttonPrimaryText else tokens.textSecondary

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
                    .border(1.dp, tokens.border, RoundedCornerShape(16.dp))
                    .clickable { onOpenLanguageDialog() }
                    .testTag("profile_language_selector"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = tokens.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(tokens.subtleAccentBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = currentLanguage.flag, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Curriculum Language",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                            Text(
                                text = currentLanguage.displayName,
                                style = MaterialTheme.typography.bodySmall,
                                color = tokens.textSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Select language",
                        tint = tokens.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Bookmarks Section
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
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = tokens.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = lesson.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = tokens.textPrimary
                                        )
                                        Text(
                                            text = lesson.courseTrack.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = tokens.textSecondary
                                        )
                                    }
                                    Icon(
                                        Icons.Default.ChevronRight,
                                        contentDescription = null,
                                        tint = tokens.textSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Badges & Achievements Section
        item {
            Column {
                Text(
                    text = "Achievements & Badges",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = tokens.textPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AchievementData.badges.forEach { badge ->
                        val isUnlocked = userProfile.earnedBadgeIds.contains(badge.id) || badge.isUnlocked
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, tokens.border, RoundedCornerShape(12.dp)),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = tokens.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isUnlocked) tokens.warning.copy(alpha = 0.12f)
                                            else tokens.surfaceSecondary
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = badge.iconEmoji,
                                        fontSize = 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = badge.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isUnlocked) tokens.textPrimary else tokens.textSecondary
                                    )
                                    Text(
                                        text = badge.description,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = tokens.textSecondary
                                    )
                                }
                                if (isUnlocked) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tokens.success.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = "+${badge.xpReward} XP",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tokens.success
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
