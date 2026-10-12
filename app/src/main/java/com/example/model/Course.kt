package com.example.model

enum class CourseTrack {
    DART,
    FLUTTER
}

enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

data class LessonStep(
    val stepNumber: Int,
    val title: String,
    val codeLine: String,
    val explanation: String
)

data class CommonMistake(
    val title: String,
    val wrongCode: String,
    val rightCode: String,
    val explanation: String
)

data class Lesson(
    val id: String,
    val courseTrack: CourseTrack,
    val moduleId: String,
    val moduleTitle: String,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val durationMinutes: Int,
    val objectives: List<String>,
    val analogy: String,
    val detailedContent: String,
    val codeExample: String,
    val expectedOutput: String,
    val walkthroughSteps: List<LessonStep>,
    val mistakes: List<CommonMistake>,
    val diagramType: String = "",
    val diagramTitle: String = "",
    val diagramDescription: String = "",
    val reviewQuestions: List<QuizQuestion> = emptyList(),
    val challengePrompt: String = "",
    val challengeSolution: String = "",
    val isCompleted: Boolean = false,
    val isBookmarked: Boolean = false
)

data class CourseModule(
    val id: String,
    val track: CourseTrack,
    val order: Int,
    val title: String,
    val description: String,
    val iconName: String,
    val lessons: List<Lesson>
)
