package com.example

import com.example.data.CurriculumData
import com.example.data.QuizBank
import com.example.engine.DartSimulatorEngine
import com.example.model.AppLanguage
import com.example.model.CourseTrack
import com.example.model.Strings
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testDartSimulatorPrints() {
        val code = """
            void main() {
              var name = "Flutter";
              print("Hello " + name);
            }
        """.trimIndent()
        val result = DartSimulatorEngine.execute(code)
        assertTrue(result.isSuccess)
        assertTrue(result.output.contains("Hello Flutter"))
    }

    @Test
    fun testDartSimulatorArithmetic() {
        val code = "print(10 + 20);"
        val result = DartSimulatorEngine.execute(code)
        assertTrue(result.isSuccess)
        assertEquals("30", result.output.trim())
    }

    @Test
    fun testCurriculumIntegrity() {
        assertFalse(CurriculumData.allLessons.isEmpty())
        assertFalse(CurriculumData.allModules.isEmpty())

        val dartLessons = CurriculumData.getLessonsForTrack(CourseTrack.DART)
        val flutterLessons = CurriculumData.getLessonsForTrack(CourseTrack.FLUTTER)
        assertTrue(dartLessons.isNotEmpty())
        assertTrue(flutterLessons.isNotEmpty())

        CurriculumData.allLessons.forEach { lesson ->
            assertNotNull(lesson.id)
            assertNotNull(lesson.title)
            assertTrue(lesson.objectives.isNotEmpty())
            assertTrue(lesson.codeExample.isNotEmpty())
        }
    }

    @Test
    fun testQuizBankValidation() {
        val questions = QuizBank.questions
        assertTrue(questions.size >= 8)
        questions.forEach { q ->
            assertTrue(q.correctIndex in 0 until q.options.size)
            assertTrue(q.explanation.isNotEmpty())
        }
    }

    @Test
    fun testLocalizationStrings() {
        val homeEn = Strings.get("home", AppLanguage.EN)
        val homeUz = Strings.get("home", AppLanguage.UZ)
        val homeRu = Strings.get("home", AppLanguage.RU)

        assertEquals("Home", homeEn)
        assertEquals("Asosiy", homeUz)
        assertEquals("Главная", homeRu)
    }
}
