package com.example.model

data class ProjectStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val codeSnippet: String,
    val fileName: String
)

data class PracticalProject(
    val id: String,
    val title: String,
    val level: Difficulty,
    val category: String,
    val summary: String,
    val durationMinutes: Int,
    val features: List<String>,
    val folderTree: String,
    val pubspecYaml: String,
    val architectureType: String,
    val architectureExplanation: String,
    val steps: List<ProjectStep>,
    val commonBugs: List<String>,
    val extensionChallenges: List<String>,
    val isCompleted: Boolean = false
)
