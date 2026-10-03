package com.sidebyside.translator.engine

import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationResult
import com.sidebyside.translator.model.TranslationStyle

/**
 * Pluggable translation interface.
 * While V1 focuses exclusively on Persian ↔ English, this abstraction ensures
 * additional language pairs can be added in the future without rewriting the core application.
 */
interface ITranslationEngine {
    val supportedPairs: List<Pair<Language, Language>>

    fun canHandle(from: Language, to: Language): Boolean {
        return supportedPairs.any { it.first == from && it.second == to }
    }

    suspend fun translate(
        text: String,
        from: Language,
        to: Language,
        style: TranslationStyle = TranslationStyle.NATURAL
    ): TranslationResult
}
