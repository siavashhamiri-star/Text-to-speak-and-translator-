package com.sidebyside.translator

import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.OfflineTranslationEngine
import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationStyle
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TranslationEngineTest {

    private lateinit var offlineEngine: OfflineTranslationEngine
    private lateinit var compositeEngine: CompositeTranslationEngine

    @Before
    fun setup() {
        offlineEngine = OfflineTranslationEngine()
        compositeEngine = CompositeTranslationEngine(offlineEngine = offlineEngine, isOnlineAiEnabled = false)
    }

    @Test
    fun testPersianToEnglish_NaturalStyle() = runBlocking {
        val result = offlineEngine.translate(
            text = "سلام چطوری",
            from = Language.PERSIAN,
            to = Language.ENGLISH,
            style = TranslationStyle.NATURAL
        )

        assertNotNull(result)
        assertEquals(Language.PERSIAN, result.sourceLanguage)
        assertEquals(Language.ENGLISH, result.targetLanguage)
        assertTrue("Expected natural greeting translation", result.translatedText.contains("how are you doing", ignoreCase = true))
        assertTrue(result.isOffline)
    }

    @Test
    fun testPersianToEnglish_DifferentStyles() = runBlocking {
        val styles = listOf(
            TranslationStyle.NATURAL,
            TranslationStyle.FRIENDLY,
            TranslationStyle.STANDARD,
            TranslationStyle.VERY_CASUAL,
            TranslationStyle.CASUAL_SLANG
        )

        for (style in styles) {
            val result = offlineEngine.translate(
                text = "سلام چطوری",
                from = Language.PERSIAN,
                to = Language.ENGLISH,
                style = style
            )
            assertTrue("Translation should not be empty for style ${style.name}", result.translatedText.isNotBlank())
            assertEquals(style, result.style)
        }
    }

    @Test
    fun testEnglishToPersian_NaturalStyle() = runBlocking {
        val result = offlineEngine.translate(
            text = "How are you",
            from = Language.ENGLISH,
            to = Language.PERSIAN,
            style = TranslationStyle.NATURAL
        )

        assertNotNull(result)
        assertEquals(Language.ENGLISH, result.sourceLanguage)
        assertEquals(Language.PERSIAN, result.targetLanguage)
        assertTrue("Expected Persian translation containing چطوره or حال", result.translatedText.contains("چطور") || result.translatedText.contains("حال"))
    }

    @Test
    fun testEnglishToPersian_SlangStyle() = runBlocking {
        val result = offlineEngine.translate(
            text = "How are you",
            from = Language.ENGLISH,
            to = Language.PERSIAN,
            style = TranslationStyle.CASUAL_SLANG
        )

        assertNotNull(result)
        assertTrue("Slang output should be colloquial", result.translatedText.contains("ردیفی") || result.translatedText.contains("خبر"))
    }

    @Test
    fun testCompositeFallback_WhenOnlineFailsOrDisabled() = runBlocking {
        val result = compositeEngine.translate(
            text = "ممنون",
            from = Language.PERSIAN,
            to = Language.ENGLISH,
            style = TranslationStyle.NATURAL
        )

        assertTrue(result.isOffline)
        assertTrue(result.translatedText.contains("Thank you", ignoreCase = true))
    }

    @Test
    fun testEmptyInputHandling() = runBlocking {
        val result = offlineEngine.translate(
            text = "   ",
            from = Language.PERSIAN,
            to = Language.ENGLISH,
            style = TranslationStyle.NATURAL
        )

        assertEquals("", result.translatedText)
    }

    @Test
    fun testPunctuationAndNormalization() = runBlocking {
        val result1 = offlineEngine.translate("سلام!", Language.PERSIAN, Language.ENGLISH, TranslationStyle.NATURAL)
        val result2 = offlineEngine.translate("سلام", Language.PERSIAN, Language.ENGLISH, TranslationStyle.NATURAL)
        assertEquals(result1.translatedText, result2.translatedText)
    }
}
