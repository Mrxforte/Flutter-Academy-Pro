package com.example.model

data class Flashcard(
    val id: String,
    val category: String,
    val term: String,
    val definition: String,
    val codeSnippet: String? = null,
    val bestPracticeTip: String = "",
    val masteryLevel: Int = 0 // 0 = unlearned, 1 = hard, 2 = good, 3 = mastered
)
