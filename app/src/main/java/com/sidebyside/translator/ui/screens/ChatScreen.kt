package com.sidebyside.translator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.model.*
import com.sidebyside.translator.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    translationEngine: CompositeTranslationEngine,
    speechManager: SpeechManager,
    ttsManager: TtsManager
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var activeSpeaker by remember { mutableStateOf(Speaker.PERSON_A) }
    var selectedStyle by remember { mutableStateOf(TranslationStyle.NATURAL) }
    var inputText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                speaker = Speaker.PERSON_A,
                originalText = "سلام! روزتون بخیر، چطور می‌تونم کمکتون کنم؟",
                translatedText = "Hello! Good day, how can I help you?",
                sourceLanguage = Language.PERSIAN,
                targetLanguage = Language.ENGLISH,
                style = TranslationStyle.NATURAL
            ),
            ChatMessage(
                speaker = Speaker.PERSON_B,
                originalText = "Hi there! I'm looking for the fastest way to get downtown.",
                translatedText = "سلام! من دنبال سریع‌ترین راه برای رسیدن به مرکز شهر هستم.",
                sourceLanguage = Language.ENGLISH,
                targetLanguage = Language.PERSIAN,
                style = TranslationStyle.NATURAL
            )
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            speechManager.stopListening()
            ttsManager.stop()
        }
    }

    fun sendMessage(text: String, isSpoken: Boolean = false) {
        if (text.isBlank()) return
        val currentSpeaker = activeSpeaker
        val sourceLang = currentSpeaker.defaultLanguage
        val targetLang = sourceLang.opposite

        coroutineScope.launch {
            val result = translationEngine.translate(
                text = text,
                from = sourceLang,
                to = targetLang,
                style = selectedStyle
            )

            val newMsg = ChatMessage(
                speaker = currentSpeaker,
                originalText = text,
                translatedText = result.translatedText,
                sourceLanguage = sourceLang,
                targetLanguage = targetLang,
                style = selectedStyle,
                isSpokenInput = isSpoken
            )

            messages.add(newMsg)
            inputText = ""
            listState.animateScrollToItem(messages.size - 1)

            // Auto-speak translated voice
            ttsManager.speak(result.translatedText, targetLang)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Chat Header with Speaker Switcher & Style Selector
        Surface(
            color = DarkSurface,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speaker Switcher Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(DarkSurfaceVariant)
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Speaker A (Persian)
                    Surface(
                        color = if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else Color.Transparent,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🇮🇷", fontSize = 16.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "فارسی",
                                color = if (activeSpeaker == Speaker.PERSON_A) Color.White else TextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Swap Button
                    IconButton(
                        onClick = { activeSpeaker = activeSpeaker.other },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch active speaker",
                            tint = TextSecondary
                        )
                    }

                    // Speaker B (English)
                    Surface(
                        color = if (activeSpeaker == Speaker.PERSON_B) EnglishPrimary else Color.Transparent,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🇬🇧", fontSize = 16.sp)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "English",
                                color = if (activeSpeaker == Speaker.PERSON_B) Color.White else TextSecondary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Style Badge
                FilterChip(
                    selected = true,
                    onClick = {
                        val nextOrdinal = (selectedStyle.ordinal + 1) % TranslationStyle.entries.size
                        selectedStyle = TranslationStyle.entries[nextOrdinal]
                    },
                    label = { Text(selectedStyle.titleEn, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DarkSurfaceVariant,
                        selectedLabelColor = AccentAmber
                    )
                )
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isPersonA = msg.speaker == Speaker.PERSON_A
                val bubbleColor = if (isPersonA) PersianSurface else EnglishSurface
                val accentColor = if (isPersonA) PersianPrimary else EnglishPrimary

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isPersonA) Arrangement.Start else Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = bubbleColor,
                        shadowElevation = 2.dp,
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Header: Speaker badge & Replay button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(msg.speaker.badgeSymbol, fontSize = 16.sp)
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = if (isPersonA) msg.speaker.labelFa else msg.speaker.labelEn,
                                        color = accentColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        ttsManager.speak(msg.translatedText, msg.targetLanguage)
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Replay translation audio",
                                        tint = accentColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            // Original text
                            Text(
                                text = msg.originalText,
                                color = TextPrimary,
                                fontSize = 15.sp,
                                textAlign = if (msg.sourceLanguage.isRtl) TextAlign.Right else TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Divider(
                                color = BorderColor,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Translated text
                            Text(
                                text = msg.translatedText,
                                color = accentColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = if (msg.targetLanguage.isRtl) TextAlign.Right else TextAlign.Left,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        // Input Area
        Surface(
            color = DarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speech Input Button
                FilledIconButton(
                    onClick = {
                        if (isListening) {
                            speechManager.stopListening()
                            isListening = false
                        } else {
                            isListening = true
                            speechManager.onFinalResult = { recognized ->
                                isListening = false
                                sendMessage(recognized, isSpoken = true)
                            }
                            speechManager.startListening(activeSpeaker.defaultLanguage)
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isListening) AccentRed else if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else EnglishPrimary
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Voice input",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Text Input Field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (activeSpeaker == Speaker.PERSON_A) "پیام خود را به فارسی بنویسید..." else "Type message in English...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else EnglishPrimary,
                        unfocusedBorderColor = BorderColor
                    )
                )

                Spacer(Modifier.width(8.dp))

                // Send Button
                IconButton(
                    onClick = {
                        sendMessage(inputText, isSpoken = false)
                    },
                    enabled = inputText.isNotBlank(),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send text message",
                        tint = if (inputText.isNotBlank()) {
                            if (activeSpeaker == Speaker.PERSON_A) PersianPrimary else EnglishPrimary
                        } else TextMuted
                    )
                }
            }
        }
    }
}
