package com.sidebyside.translator.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.Speaker
import com.sidebyside.translator.model.TranslationStyle
import com.sidebyside.translator.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LiveVoiceScreen(
    translationEngine: CompositeTranslationEngine,
    speechManager: SpeechManager,
    ttsManager: TtsManager
) {
    val coroutineScope = rememberCoroutineScope()

    var activeSpeaker by remember { mutableStateOf(Speaker.PERSON_A) }
    var isLiveSessionActive by remember { mutableStateOf(false) }
    var interimTranscript by remember { mutableStateOf("") }
    var finalizedInput by remember { mutableStateOf("") }
    var translatedResult by remember { mutableStateOf("") }
    var audioLevel by remember { mutableFloatStateOf(0f) }
    var statusText by remember { mutableStateOf("Tap to start continuous voice translation") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isLiveSessionActive) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    fun startLiveListening() {
        val currentLang = activeSpeaker.defaultLanguage
        val targetLang = currentLang.opposite

        speechManager.onRmsChanged = { level ->
            audioLevel = level
        }

        speechManager.onPartialResult = { partial ->
            interimTranscript = partial
        }

        speechManager.onFinalResult = { text ->
            finalizedInput = text
            interimTranscript = ""
            statusText = "Translating speech..."
            coroutineScope.launch {
                val res = translationEngine.translate(
                    text = text,
                    from = currentLang,
                    to = targetLang,
                    style = TranslationStyle.NATURAL
                )
                translatedResult = res.translatedText
                statusText = "Playing audio translation..."
                ttsManager.speak(res.translatedText, targetLang)
            }
        }

        speechManager.onError = { error ->
            statusText = error
            isLiveSessionActive = false
        }

        speechManager.startListening(currentLang)
        isLiveSessionActive = true
        statusText = "Listening for ${currentLang.displayName} speech..."
    }

    fun stopLiveListening() {
        speechManager.stopListening()
        ttsManager.stop()
        isLiveSessionActive = false
        statusText = "Voice session paused"
    }

    DisposableEffect(Unit) {
        onDispose {
            speechManager.stopListening()
            ttsManager.stop()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Speaker Selector & Status
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .background(DarkSurface, RoundedCornerShape(24.dp))
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (activeSpeaker != Speaker.PERSON_A) {
                            activeSpeaker = Speaker.PERSON_A
                            if (isLiveSessionActive) {
                                stopLiveListening()
                                startLiveListening()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("🇮🇷 فارسی → انگلیسی", fontSize = 13.sp)
                }

                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (activeSpeaker != Speaker.PERSON_B) {
                            activeSpeaker = Speaker.PERSON_B
                            if (isLiveSessionActive) {
                                stopLiveListening()
                                startLiveListening()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeSpeaker == Speaker.PERSON_B) EnglishPrimary else Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("🇬🇧 English → Persian", fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = statusText,
                color = if (isLiveSessionActive) AccentAmber else TextSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Live Audio Visualizer / Pulse Circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(if (isLiveSessionActive) pulseScale else 1f),
            contentAlignment = Alignment.Center
        ) {
            // Background wave ring
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = if (activeSpeaker == Speaker.PERSON_A) {
                    PersianPrimary.copy(alpha = 0.15f + audioLevel * 0.35f)
                } else {
                    EnglishPrimary.copy(alpha = 0.15f + audioLevel * 0.35f)
                }
            ) {}

            // Center Action Button
            FilledIconButton(
                onClick = {
                    if (isLiveSessionActive) {
                        stopLiveListening()
                    } else {
                        startLiveListening()
                    }
                },
                modifier = Modifier.size(96.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isLiveSessionActive) AccentRed else {
                        if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else EnglishPrimary
                    }
                )
            ) {
                Icon(
                    imageVector = if (isLiveSessionActive) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Toggle live continuous translation",
                    modifier = Modifier.size(44.dp),
                    tint = Color.White
                )
            }
        }

        // Transcription & Output Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Original / Interim
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text(
                        text = "Original (${activeSpeaker.defaultLanguage.displayName}):",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = when {
                            interimTranscript.isNotBlank() -> interimTranscript
                            finalizedInput.isNotBlank() -> finalizedInput
                            else -> "—"
                        },
                        fontSize = 16.sp,
                        color = if (interimTranscript.isNotBlank()) AccentAmber else TextPrimary,
                        textAlign = if (activeSpeaker.defaultLanguage.isRtl) TextAlign.Right else TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Translated Result
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (activeSpeaker == Speaker.PERSON_A) EnglishSurface else PersianSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Translated (${activeSpeaker.defaultLanguage.opposite.displayName}):",
                            fontSize = 12.sp,
                            color = if (activeSpeaker == Speaker.PERSON_A) EnglishPrimary else PersianPrimary
                        )

                        if (translatedResult.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    ttsManager.speak(translatedResult, activeSpeaker.defaultLanguage.opposite)
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Replay",
                                    tint = if (activeSpeaker == Speaker.PERSON_A) EnglishPrimary else PersianPrimary
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (translatedResult.isNotBlank()) translatedResult else "—",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = if (activeSpeaker.defaultLanguage.opposite.isRtl) TextAlign.Right else TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
