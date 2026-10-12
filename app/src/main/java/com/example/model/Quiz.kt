package com.example.model

enum class QuestionType {
    SINGLE_CHOICE,
    PREDICT_OUTPUT,
    FIND_BUG,
    CODE_COMPLETION,
    ARCHITECTURE
}

data class QuizQuestion(
    val id: String,
    val topic: String,
    val track: CourseTrack,
    val difficulty: Difficulty,
    val type: QuestionType,
    val question: String,
    val codeSnippet: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class QuizSessionState(
    val title: String,
    val questions: List<QuizQuestion>,
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val correctAnswersCount: Int = 0,
    val isFinished: Boolean = false,
    val wrongQuestions: List<QuizQuestion> = emptyList(),
    val timeRemainingSeconds: Int = 300 // for timed mock exams
)
