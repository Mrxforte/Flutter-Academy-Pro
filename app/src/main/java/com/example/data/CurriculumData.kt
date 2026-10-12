package com.example.data

import com.example.model.*

object CurriculumData {

    val allModules: List<CourseModule> by lazy {
        CurriculumDartData.modules + CurriculumFlutterData.modules
    }

    val allLessons: List<Lesson> by lazy {
        allModules.flatMap { it.lessons }
    }

    fun getLessonById(id: String): Lesson? {
        return allLessons.firstOrNull { it.id == id }
    }

    fun getModulesForTrack(track: CourseTrack): List<CourseModule> {
        return allModules.filter { it.track == track }
    }

    fun getLessonsForTrack(track: CourseTrack): List<Lesson> {
        return allLessons.filter { it.courseTrack == track }
    }

    fun searchLessons(
        query: String,
        trackFilter: CourseTrack? = null,
        difficultyFilter: Difficulty? = null
    ): List<Lesson> {
        val q = query.trim().lowercase()
        return allLessons.filter { lesson ->
            val matchesTrack = trackFilter == null || lesson.courseTrack == trackFilter
            val matchesDifficulty = difficultyFilter == null || lesson.difficulty == difficultyFilter
            val matchesQuery = q.isEmpty() ||
                lesson.title.lowercase().contains(q) ||
                lesson.description.lowercase().contains(q) ||
                lesson.detailedContent.lowercase().contains(q) ||
                lesson.objectives.any { it.lowercase().contains(q) }

            matchesTrack && matchesDifficulty && matchesQuery
        }
    }
}
