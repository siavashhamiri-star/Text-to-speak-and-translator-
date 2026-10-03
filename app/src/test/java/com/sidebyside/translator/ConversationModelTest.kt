package com.sidebyside.translator

import com.sidebyside.translator.model.*
import org.junit.Assert.*
import org.junit.Test

class ConversationModelTest {

    @Test
    fun testLanguageProperties() {
        assertEquals("fa", Language.PERSIAN.code)
        assertEquals("en", Language.ENGLISH.code)
        assertTrue(Language.PERSIAN.isRtl)
        assertFalse(Language.ENGLISH.isRtl)
        assertEquals(Language.ENGLISH, Language.PERSIAN.opposite)
        assertEquals(Language.PERSIAN, Language.ENGLISH.opposite)
    }

    @Test
    fun testSpeakerSwitching() {
        val speakerA = Speaker.PERSON_A
        val speakerB = speakerA.other
        assertEquals(Speaker.PERSON_B, speakerB)
        assertEquals(Speaker.PERSON_A, speakerB.other)
        assertEquals(Language.PERSIAN, speakerA.defaultLanguage)
        assertEquals(Language.ENGLISH, speakerB.defaultLanguage)
    }

    @Test
    fun testChatMessageCreation() {
        val message = ChatMessage(
            speaker = Speaker.PERSON_A,
            originalText = "سلام",
            translatedText = "Hello",
            sourceLanguage = Language.PERSIAN,
            targetLanguage = Language.ENGLISH,
            style = TranslationStyle.NATURAL,
            isSpokenInput = true
        )

        assertEquals("سلام", message.originalText)
        assertEquals("Hello", message.translatedText)
        assertTrue(message.isSpokenInput)
        assertFalse(message.audioPlayed)
    }

    @Test
    fun testTranslationStylesCount() {
        assertEquals(5, TranslationStyle.entries.size)
        assertNotNull(TranslationStyle.fromId("natural"))
        assertNotNull(TranslationStyle.fromId("friendly"))
        assertNotNull(TranslationStyle.fromId("standard"))
        assertNotNull(TranslationStyle.fromId("very_casual"))
        assertNotNull(TranslationStyle.fromId("casual_slang"))
    }
}
