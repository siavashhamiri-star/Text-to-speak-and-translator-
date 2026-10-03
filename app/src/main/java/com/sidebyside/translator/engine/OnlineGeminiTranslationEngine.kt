package com.sidebyside.translator.engine

import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationResult
import com.sidebyside.translator.model.TranslationStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Optional online translation engine using Gemini API.
 * Designed according to security guidelines: NEVER hardcodes API keys.
 * Reads API key dynamically from environment or local config, or reaches proxy endpoint.
 */
class OnlineGeminiTranslationEngine(
    private val apiKeyProvider: () -> String? = { System.getenv("GEMINI_API_KEY") },
    private val proxyEndpoint: String? = null
) : ITranslationEngine {

    override val supportedPairs: List<Pair<Language, Language>> = listOf(
        Pair(Language.PERSIAN, Language.ENGLISH),
        Pair(Language.ENGLISH, Language.PERSIAN)
    )

    override suspend fun translate(
        text: String,
        from: Language,
        to: Language,
        style: TranslationStyle
    ): TranslationResult = withContext(Dispatchers.IO) {
        val apiKey = apiKeyProvider()
        if (apiKey.isNullOrBlank() && proxyEndpoint.isNullOrBlank()) {
            throw IllegalStateException("Online AI is not configured. Missing API key or proxy.")
        }

        val prompt = buildPrompt(text, from, to, style)
        val url = if (!proxyEndpoint.isNullOrBlank()) {
            URL(proxyEndpoint)
        } else {
            URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
        }

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.doOutput = true

        val requestBody = JSONObject().apply {
            val contentsArray = org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            }
            put("contents", contentsArray)
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(requestBody.toString())
            writer.flush()
        }

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw IllegalStateException("Gemini API call failed with response code $responseCode")
        }

        val responseStr = BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
            reader.readText()
        }

        val jsonResponse = JSONObject(responseStr)
        val candidates = jsonResponse.getJSONArray("candidates")
        val content = candidates.getJSONObject(0).getJSONObject("content")
        val parts = content.getJSONArray("parts")
        val translatedText = parts.getJSONObject(0).getString("text").trim()

        TranslationResult(
            originalText = text,
            translatedText = translatedText,
            sourceLanguage = from,
            targetLanguage = to,
            style = style,
            isOffline = false,
            confidence = 0.98f
        )
    }

    private fun buildPrompt(text: String, from: Language, to: Language, style: TranslationStyle): String {
        val styleInstruction = when (style) {
            TranslationStyle.NATURAL -> "Translate into natural, conversational, everyday speech."
            TranslationStyle.FRIENDLY -> "Translate with a warm, polite, and approachable friendly tone."
            TranslationStyle.STANDARD -> "Translate with clear, grammatical, and standard sentence structure."
            TranslationStyle.VERY_CASUAL -> "Translate into informal colloquial speech as used between close friends."
            TranslationStyle.CASUAL_SLANG -> "Translate using modern conversational idioms, slang, and colloquial expressions without offensive language."
        }

        return "You are an expert bilingual Persian ↔ English translator for a real-time conversation between two people.\n" +
                "Source Language: ${from.displayName} (${from.nativeName})\n" +
                "Target Language: ${to.displayName} (${to.nativeName})\n" +
                "Tone/Style Requirement: $styleInstruction\n" +
                "Original Text: \"$text\"\n" +
                "Output ONLY the translated text without commentary, quotes, or metadata."
    }
}
