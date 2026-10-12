package com.example.storage

import android.content.Context
import com.example.model.AppLanguage
import com.example.model.UserProfile
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LearningRepository private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("flutter_academy_prefs", Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(
        AppLanguage.fromCode(prefs.getString("pref_language", "en") ?: "en")
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _themeMode = MutableStateFlow(
        AppThemeMode.fromString(prefs.getString("pref_theme_mode", "DARK") ?: "DARK")
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _isDarkMode = MutableStateFlow(
        prefs.getBoolean("pref_dark_mode", true)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = prefs.getString("user_name", "Flutter Developer") ?: "Flutter Developer",
            xp = prefs.getInt("user_xp", 450),
            streakDays = prefs.getInt("user_streak", 5),
            dailyGoalMinutes = prefs.getInt("user_goal", 20),
            todayMinutesSpent = prefs.getInt("user_today_spent", 15),
            skillLevel = prefs.getString("user_level", "Junior Flutter Dev") ?: "Junior Flutter Dev",
            completedLessonIds = prefs.getStringSet("completed_lessons", setOf("dart_1_1", "fl_1_1"))?.toSet() ?: setOf("dart_1_1", "fl_1_1"),
            bookmarkedLessonIds = prefs.getStringSet("bookmarked_lessons", setOf("fl_2_1", "dart_6_1"))?.toSet() ?: setOf("fl_2_1", "dart_6_1"),
            completedProjectIds = prefs.getStringSet("completed_projects", emptySet())?.toSet() ?: emptySet()
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
        prefs.edit().putString("pref_language", lang.code).apply()
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        _isDarkMode.value = (mode == AppThemeMode.DARK)
        prefs.edit()
            .putString("pref_theme_mode", mode.name)
            .putBoolean("pref_dark_mode", mode == AppThemeMode.DARK)
            .apply()
    }

    fun setDarkMode(dark: Boolean) {
        val mode = if (dark) AppThemeMode.DARK else AppThemeMode.LIGHT
        setThemeMode(mode)
    }

    fun toggleLessonCompleted(lessonId: String) {
        _userProfile.update { current ->
            val set = current.completedLessonIds.toMutableSet()
            val wasCompleted = set.contains(lessonId)
            val xpDiff = if (wasCompleted) -50 else 50
            if (wasCompleted) set.remove(lessonId) else set.add(lessonId)

            val updatedXp = (current.xp + xpDiff).coerceAtLeast(0)
            prefs.edit()
                .putStringSet("completed_lessons", set)
                .putInt("user_xp", updatedXp)
                .apply()

            current.copy(completedLessonIds = set, xp = updatedXp)
        }
    }

    fun toggleLessonBookmarked(lessonId: String) {
        _userProfile.update { current ->
            val set = current.bookmarkedLessonIds.toMutableSet()
            if (set.contains(lessonId)) set.remove(lessonId) else set.add(lessonId)

            prefs.edit().putStringSet("bookmarked_lessons", set).apply()
            current.copy(bookmarkedLessonIds = set)
        }
    }

    fun toggleProjectCompleted(projectId: String) {
        _userProfile.update { current ->
            val set = current.completedProjectIds.toMutableSet()
            val wasDone = set.contains(projectId)
            val xpDiff = if (wasDone) -150 else 150
            if (wasDone) set.remove(projectId) else set.add(projectId)

            val updatedXp = (current.xp + xpDiff).coerceAtLeast(0)
            prefs.edit()
                .putStringSet("completed_projects", set)
                .putInt("user_xp", updatedXp)
                .apply()

            current.copy(completedProjectIds = set, xp = updatedXp)
        }
    }

    fun addXp(amount: Int) {
        _userProfile.update { current ->
            val updatedXp = current.xp + amount
            val newLevel = when {
                updatedXp > 2000 -> "Principal Architect"
                updatedXp > 1200 -> "Senior Flutter Dev"
                updatedXp > 600 -> "Mid Flutter Dev"
                else -> "Junior Flutter Dev"
            }
            prefs.edit()
                .putInt("user_xp", updatedXp)
                .putString("user_level", newLevel)
                .apply()

            current.copy(xp = updatedXp, skillLevel = newLevel)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LearningRepository? = null

        fun getInstance(context: Context): LearningRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LearningRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
