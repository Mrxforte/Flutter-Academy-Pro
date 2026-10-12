package com.example.data

import com.example.model.AchievementBadge

object AchievementData {

    val badges: List<AchievementBadge> = listOf(
        AchievementBadge(
            id = "ach_first_lesson",
            title = "First Code Line",
            description = "Completed your first Dart or Flutter lesson in the academy",
            iconEmoji = "🚀",
            xpReward = 50,
            isUnlocked = true
        ),
        AchievementBadge(
            id = "ach_streak_3",
            title = "Consistent Learner",
            description = "Maintained a 3-day continuous learning streak",
            iconEmoji = "🔥",
            xpReward = 100,
            isUnlocked = true
        ),
        AchievementBadge(
            id = "ach_quiz_master",
            title = "Knowledge Champion",
            description = "Passed 5 curriculum quizzes with score above 80%",
            iconEmoji = "🏆",
            xpReward = 150,
            isUnlocked = true
        ),
        AchievementBadge(
            id = "ach_playground_runner",
            title = "Sandbox Wizard",
            description = "Executed and experimented with 10 code playground examples",
            iconEmoji = "⚡",
            xpReward = 100,
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_clean_architect",
            title = "Clean Architect",
            description = "Finished the Clean Architecture module and E-Commerce project",
            iconEmoji = "🏛️",
            xpReward = 300,
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_flutter_pro",
            title = "Certified Flutter Pro",
            description = "Passed the comprehensive certification mock exam",
            iconEmoji = "🎓",
            xpReward = 500,
            isUnlocked = false
        )
    )
}
