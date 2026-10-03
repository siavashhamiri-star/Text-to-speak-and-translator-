package com.sidebyside.translator.engine

import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationResult
import com.sidebyside.translator.model.TranslationStyle

/**
 * Composite engine that manages the Offline-First architecture.
 * Prioritizes offline local engine, with optional online AI enhancements when enabled.
 */
class CompositeTranslationEngine(
    private val offlineEngine: OfflineTranslationEngine = OfflineTranslationEngine(),
    private val onlineEngine: OnlineGeminiTranslationEngine = OnlineGeminiTranslationEngine(),
    var isOnlineAiEnabled: Boolean = false
) : ITranslationEngine {

    override val supportedPairs: List<Pair<Language, Language>>
        get() = offlineEngine.supportedPairs

    override suspend fun translate(
        text: String,
        from: Language,
        to: Language,
        style: TranslationStyle
    ): TranslationResult {
        if (text.isBlank()) {
            return offlineEngine.translate(text, from, to, style)
        }

        if (isOnlineAiEnabled) {
            try {
                return onlineEngine.translate(text, from, to, style)
            } catch (e: Exception) {
                // Graceful fallback to offline engine
                android.util.Log.w("CompositeTranslation", "Online translation failed, falling back to offline: ${e.message}")
            }
        }

        return offlineEngine.translate(text, from, to, style)
    }
}
