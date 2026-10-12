package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CurriculumData
import com.example.model.*
import com.example.storage.LearningRepository
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.screens.*
import com.example.ui.theme.AppTheme
import com.example.ui.theme.AppThemeMode

sealed class AppDestination {
    data object Home : AppDestination()
    data object Learn : AppDestination()
    data object Practice : AppDestination()
    data object Projects : AppDestination()
    data object Profile : AppDestination()
    data class LessonDetail(val lessonId: String) : AppDestination()
    data class Quiz(val quizType: String) : AppDestination()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    repository: LearningRepository,
    modifier: Modifier = Modifier
) {
    val tokens = AppTheme.tokens
    val currentLanguage by repository.currentLanguage.collectAsState()
    val themeMode by repository.themeMode.collectAsState()
    val userProfile by repository.userProfile.collectAsState()

    var currentDestination by remember { mutableStateOf<AppDestination>(AppDestination.Home) }
    var playgroundInitialCode by remember { mutableStateOf<String?>(null) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showSearchDialog by remember { mutableStateOf(false) }

    // Intercept hardware back button when in sub-screens
    BackHandler(enabled = currentDestination !is AppDestination.Home) {
        when (currentDestination) {
            is AppDestination.LessonDetail -> currentDestination = AppDestination.Learn
            is AppDestination.Quiz -> currentDestination = AppDestination.Practice
            else -> currentDestination = AppDestination.Home
        }
    }

    Scaffold(
        containerColor = tokens.background,
        topBar = {
            if (currentDestination !is AppDestination.LessonDetail && currentDestination !is AppDestination.Quiz) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(tokens.subtleAccentBg, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "⚡", fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Flutter Academy",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = tokens.textPrimary
                            )
                        }
                    },
                    actions = {
                        // Compact Streak Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = tokens.warning.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, tokens.warning.copy(alpha = 0.25f)),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔥 ${userProfile.streakDays}d",
                                    fontSize = 11.sp,
                                    color = tokens.warning,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Search Action
                        IconButton(
                            onClick = { showSearchDialog = true },
                            modifier = Modifier.testTag("app_search_button")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = tokens.textSecondary, modifier = Modifier.size(20.dp))
                        }

                        // Language Toggle
                        IconButton(
                            onClick = { showLanguageDialog = true },
                            modifier = Modifier.testTag("app_language_button")
                        ) {
                            Text(text = currentLanguage.flag, fontSize = 18.sp)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = tokens.surface,
                        titleContentColor = tokens.textPrimary,
                        actionIconContentColor = tokens.textSecondary
                    )
                )
            }
        },
        bottomBar = {
            if (currentDestination !is AppDestination.LessonDetail && currentDestination !is AppDestination.Quiz) {
                val navColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tokens.primary,
                    selectedTextColor = tokens.primary,
                    unselectedIconColor = tokens.textSecondary,
                    unselectedTextColor = tokens.textSecondary,
                    indicatorColor = tokens.subtleAccentBg
                )

                Surface(
                    color = tokens.surface,
                    modifier = Modifier.border(width = 1.dp, color = tokens.border, shape = RoundedCornerShape(0.dp))
                ) {
                    NavigationBar(
                        containerColor = tokens.surface,
                        tonalElevation = 0.dp,
                        windowInsets = NavigationBarDefaults.windowInsets
                    ) {
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Home,
                            onClick = { currentDestination = AppDestination.Home },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
                            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
                            colors = navColors,
                            modifier = Modifier.testTag("nav_home")
                        )
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Learn,
                            onClick = { currentDestination = AppDestination.Learn },
                            icon = { Icon(Icons.Default.School, contentDescription = "Learn", modifier = Modifier.size(22.dp)) },
                            label = { Text("Learn", style = MaterialTheme.typography.labelSmall) },
                            colors = navColors,
                            modifier = Modifier.testTag("nav_learn")
                        )
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Practice,
                            onClick = { currentDestination = AppDestination.Practice },
                            icon = { Icon(Icons.Default.Terminal, contentDescription = "Practice", modifier = Modifier.size(22.dp)) },
                            label = { Text("Practice", style = MaterialTheme.typography.labelSmall) },
                            colors = navColors,
                            modifier = Modifier.testTag("nav_practice")
                        )
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Projects,
                            onClick = { currentDestination = AppDestination.Projects },
                            icon = { Icon(Icons.Default.Build, contentDescription = "Projects", modifier = Modifier.size(22.dp)) },
                            label = { Text("Projects", style = MaterialTheme.typography.labelSmall) },
                            colors = navColors,
                            modifier = Modifier.testTag("nav_projects")
                        )
                        NavigationBarItem(
                            selected = currentDestination is AppDestination.Profile,
                            onClick = { currentDestination = AppDestination.Profile },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(22.dp)) },
                            label = { Text("Profile", style = MaterialTheme.typography.labelSmall) },
                            colors = navColors,
                            modifier = Modifier.testTag("nav_profile")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(tokens.background)
        ) {
            when (val dest = currentDestination) {
                is AppDestination.Home -> {
                    HomeScreen(
                        userProfile = userProfile,
                        currentLanguage = currentLanguage,
                        onNavigateToLesson = { currentDestination = AppDestination.LessonDetail(it) },
                        onNavigateToPlayground = {
                            playgroundInitialCode = it
                            currentDestination = AppDestination.Practice
                        },
                        onNavigateToQuiz = { currentDestination = AppDestination.Quiz("all") },
                        onNavigateToProjects = { currentDestination = AppDestination.Projects },
                        onNavigateToProfile = { currentDestination = AppDestination.Profile }
                    )
                }
                is AppDestination.Learn -> {
                    LearnScreen(
                        currentLanguage = currentLanguage,
                        completedLessonIds = userProfile.completedLessonIds,
                        onNavigateToLesson = { currentDestination = AppDestination.LessonDetail(it) }
                    )
                }
                is AppDestination.Practice -> {
                    PracticeScreen(
                        currentLanguage = currentLanguage,
                        initialPlaygroundCode = playgroundInitialCode,
                        onStartQuiz = { currentDestination = AppDestination.Quiz(it) }
                    )
                }
                is AppDestination.Projects -> {
                    ProjectsScreen(
                        currentLanguage = currentLanguage,
                        completedProjectIds = userProfile.completedProjectIds,
                        onToggleProjectComplete = { repository.toggleProjectCompleted(it) }
                    )
                }
                is AppDestination.Profile -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        currentLanguage = currentLanguage,
                        currentThemeMode = themeMode,
                        onSelectThemeMode = { repository.setThemeMode(it) },
                        onOpenLanguageDialog = { showLanguageDialog = true },
                        onNavigateToLesson = { currentDestination = AppDestination.LessonDetail(it) }
                    )
                }
                is AppDestination.LessonDetail -> {
                    LessonDetailScreen(
                        lessonId = dest.lessonId,
                        currentLanguage = currentLanguage,
                        isCompleted = userProfile.completedLessonIds.contains(dest.lessonId),
                        isBookmarked = userProfile.bookmarkedLessonIds.contains(dest.lessonId),
                        onBack = { currentDestination = AppDestination.Learn },
                        onToggleComplete = { repository.toggleLessonCompleted(dest.lessonId) },
                        onToggleBookmark = { repository.toggleLessonBookmarked(dest.lessonId) },
                        onNavigateToPlaygroundWithCode = { code ->
                            playgroundInitialCode = code
                            currentDestination = AppDestination.Practice
                        },
                        onNavigateToNextLesson = { nextId ->
                            currentDestination = AppDestination.LessonDetail(nextId)
                        }
                    )
                }
                is AppDestination.Quiz -> {
                    QuizScreen(
                        quizType = dest.quizType,
                        currentLanguage = currentLanguage,
                        onFinish = { xpGained ->
                            repository.addXp(xpGained)
                            currentDestination = AppDestination.Practice
                        },
                        onBack = { currentDestination = AppDestination.Practice }
                    )
                }
            }

            // Global Language Selector Dialog
            if (showLanguageDialog) {
                LanguageSelectorDialog(
                    currentLanguage = currentLanguage,
                    onLanguageSelected = { repository.setLanguage(it) },
                    onDismissRequest = { showLanguageDialog = false }
                )
            }

            // Quick Search Dialog
            if (showSearchDialog) {
                var query by remember { mutableStateOf("") }
                val searchResults = remember(query) {
                    if (query.isBlank()) emptyList()
                    else CurriculumData.allLessons.filter {
                        it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.detailedContent.contains(query, ignoreCase = true)
                    }.take(6)
                }

                AlertDialog(
                    onDismissRequest = { showSearchDialog = false },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = tokens.surface,
                    modifier = Modifier.border(1.dp, tokens.border, RoundedCornerShape(16.dp)),
                    title = {
                        Text(
                            text = "Search Curriculum",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = tokens.textPrimary
                        )
                    },
                    text = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = query,
                                onValueChange = { query = it },
                                placeholder = { Text("e.g. Riverpod, Widgets, Async...", color = tokens.textMuted) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_dialog_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = tokens.primary,
                                    unfocusedBorderColor = tokens.border,
                                    focusedTextColor = tokens.textPrimary,
                                    unfocusedTextColor = tokens.textPrimary,
                                    focusedContainerColor = tokens.surfaceSecondary,
                                    unfocusedContainerColor = tokens.surfaceSecondary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            if (searchResults.isEmpty() && query.isNotBlank()) {
                                Text(
                                    text = "No matching lessons found",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = tokens.textMuted,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                            searchResults.forEach { lesson ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showSearchDialog = false
                                            currentDestination = AppDestination.LessonDetail(lesson.id)
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = tokens.primary, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = lesson.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = tokens.textPrimary
                                        )
                                        Text(
                                            text = lesson.courseTrack.name,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = tokens.textSecondary
                                        )
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showSearchDialog = false }) {
                            Text("Done", color = tokens.primary)
                        }
                    }
                )
            }
        }
    }
}
