package com.sidebyside.translator.model

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val speaker: Speaker,
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val style: TranslationStyle,
    val timestamp: Long = System.currentTimeMillis(),
    val isSpokenInput: Boolean = false,
    val audioPlayed: Boolean = false
)
