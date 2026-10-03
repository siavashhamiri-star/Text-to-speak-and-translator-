package com.sidebyside.translator.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.sidebyside.translator.model.Language
import java.util.Locale
import java.util.UUID

/**
 * Native Android TextToSpeech manager for bilingual Persian and English speech synthesis.
 * Handles missing voice data gracefully and provides utterance completion callbacks.
 */
class TtsManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    var isInitialized: Boolean = false
        private set

    var onPlaybackStarted: (() -> Unit)? = null
    var onPlaybackFinished: (() -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    onPlaybackStarted?.invoke()
                }

                override fun onDone(utteranceId: String?) {
                    onPlaybackFinished?.invoke()
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    onPlaybackFinished?.invoke()
                    onError?.invoke("Playback encountered an error.")
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    onPlaybackFinished?.invoke()
                    onError?.invoke("TTS playback error code: $errorCode")
                }
            })
        } else {
            isInitialized = false
            onError?.invoke("TextToSpeech initialization failed on this device.")
        }
    }

    fun speak(text: String, language: Language, speechRate: Float = 0.95f, pitch: Float = 1.0f) {
        if (!isInitialized || tts == null) {
            onError?.invoke("Speech synthesis engine is not ready yet.")
            return
        }

        if (text.isBlank()) return

        val targetLocale = if (language == Language.PERSIAN) Locale("fa", "IR") else Locale.US
        val langResult = tts?.setLanguage(targetLocale)

        if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
            if (language == Language.PERSIAN) {
                onError?.invoke("Persian voice is not installed in your device's Speech Services. Please install Persian TTS in Android Settings.")
            } else {
                onError?.invoke("English speech synthesis is not supported on this device.")
            }
            return
        }

        tts?.setSpeechRate(speechRate)
        tts?.setPitch(pitch)

        val utteranceId = UUID.randomUUID().toString()
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
