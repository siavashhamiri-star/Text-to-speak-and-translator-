package com.sidebyside.translator

import com.sidebyside.translator.model.ScenarioCategory
import com.sidebyside.translator.practice.PracticeEngine
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PracticeEngineTest {

    private lateinit var practiceEngine: PracticeEngine

    @Before
    fun setup() {
        practiceEngine = PracticeEngine()
    }

    @Test
    fun testAllNineCategoriesPresent() {
        val categoriesInScenarios = practiceEngine.scenarios.map { it.category }.toSet()
        for (category in ScenarioCategory.entries) {
            assertTrue("Scenario category ${category.name} must exist in offline practice", categoriesInScenarios.contains(category))
        }
    }

    @Test
    fun testAccurateAnswerScoring() {
        val everydayScenario = practiceEngine.scenarios.first { it.category == ScenarioCategory.EVERYDAY }
        val turn = everydayScenario.turns.first()

        val result = practiceEngine.evaluateAnswer("Good morning! I'm doing great, thank you.", turn)
        assertTrue("Accurate answer should score high", result.scorePercent >= 80)
        assertTrue(result.isAccurate)
        assertTrue(result.feedbackFa.isNotBlank())
        assertTrue(result.feedbackEn.isNotBlank())
    }

    @Test
    fun testEmptyInputScoring() {
        val everydayScenario = practiceEngine.scenarios.first { it.category == ScenarioCategory.EVERYDAY }
        val turn = everydayScenario.turns.first()

        val result = practiceEngine.evaluateAnswer("   ", turn)
        assertEquals(0, result.scorePercent)
        assertFalse(result.isAccurate)
    }

    @Test
    fun testPartialAnswerScoring() {
        val everydayScenario = practiceEngine.scenarios.first { it.category == ScenarioCategory.EVERYDAY }
        val turn = everydayScenario.turns.first()

        val result = practiceEngine.evaluateAnswer("Morning! I am doing good.", turn)
        assertTrue(result.scorePercent > 50)
    }
}
