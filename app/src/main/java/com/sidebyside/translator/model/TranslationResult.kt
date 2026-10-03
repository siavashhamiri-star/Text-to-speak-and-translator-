package com.sidebyside.translator.model

data class TranslationResult(
    val originalText: String,
    val translatedText: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val style: TranslationStyle,
    val isOffline: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val alternatives: List<String> = emptyList(),
    val confidence: Float = 1.0f
)
