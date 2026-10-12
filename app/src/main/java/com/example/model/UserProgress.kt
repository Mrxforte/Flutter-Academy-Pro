package com.example.model

data class UserProfile(
    val name: String = "Flutter Developer",
    val xp: Int = 450,
    val streakDays: Int = 5,
    val dailyGoalMinutes: Int = 20,
    val todayMinutesSpent: Int = 15,
    val skillLevel: String = "Junior Flutter Dev",
    val completedLessonIds: Set<String> = emptySet(),
    val bookmarkedLessonIds: Set<String> = emptySet(),
    val earnedBadgeIds: Set<String> = emptySet(),
    val completedProjectIds: Set<String> = emptySet(),
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false
)
