package com.sidebyside.translator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.TranslationStyle
import com.sidebyside.translator.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun SideBySideScreen(
    translationEngine: CompositeTranslationEngine,
    speechManager: SpeechManager,
    ttsManager: TtsManager,
    onNavigateToChat: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedStyle by remember { mutableStateOf(TranslationStyle.NATURAL) }
    var flipTopPanel by remember { mutableStateOf(false) }

    // Person A (Persian) State
    var persianInput by remember { mutableStateOf("") }
    var persianTranslatedOutput by remember { mutableStateOf("") }
    var isPersianListening by remember { mutableStateOf(false) }

    // Person B (English) State
    var englishInput by remember { mutableStateOf("") }
    var englishTranslatedOutput by remember { mutableStateOf("") }
    var isEnglishListening by remember { mutableStateOf(false) }

    var audioLevel by remember { mutableFloatStateOf(0f) }
    var statusNotice by remember { mutableStateOf("Ready to translate") }

    DisposableEffect(Unit) {
        speechManager.onRmsChanged = { level ->
            audioLevel = level
        }
        speechManager.onError = { error ->
            statusNotice = error
            isPersianListening = false
            isEnglishListening = false
        }
        onDispose {
            speechManager.stopListening()
            ttsManager.stop()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Toolbar: Style & Table-Flip Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Side by Side",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Style: ${selectedStyle.titleEn} (${selectedStyle.titleFa})",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Style Cycle Button
                IconButton(
                    onClick = {
                        val nextOrdinal = (selectedStyle.ordinal + 1) % TranslationStyle.entries.size
                        selectedStyle = TranslationStyle.entries[nextOrdinal]
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Switch Style",
                        tint = AccentAmber
                    )
                }

                // Table Flip View Button (Inverts top panel for face-to-face seating)
                IconButton(
                    onClick = { flipTopPanel = !flipTopPanel }
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Flip Top Screen for Across-Table Partner",
                        tint = if (flipTopPanel) PersianPrimary else TextSecondary
                    )
                }
            }
        }

        // Two Panels: Top (English / Partner) and Bottom (Persian / Local)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // TOP PANEL: English (Person B)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .rotate(if (flipTopPanel) 180f else 0f)
                    .background(EnglishSurface)
                    .border(1.dp, BorderColor)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🇬🇧", fontSize = 24.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "English Speaker",
                                color = EnglishPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        if (persianTranslatedOutput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    ttsManager.speak(persianTranslatedOutput, Language.ENGLISH)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak English translation",
                                    tint = EnglishPrimary
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Received translation from Persian (displayed clearly in English)
                    if (persianTranslatedOutput.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = "Translation from Persian:",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = persianTranslatedOutput,
                                    fontSize = 18.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Waiting for Persian speaker to talk or type...",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    // English Speaker Input Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = englishInput,
                            onValueChange = { englishInput = it },
                            placeholder = { Text("Type English...", color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = EnglishPrimary,
                                unfocusedBorderColor = BorderColor
                            ),
                            trailingIcon = {
                                if (englishInput.isNotBlank()) {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                val res = translationEngine.translate(
                                                    text = englishInput,
                                                    from = Language.ENGLISH,
                                                    to = Language.PERSIAN,
                                                    style = selectedStyle
                                                )
                                                englishTranslatedOutput = res.translatedText
                                                ttsManager.speak(res.translatedText, Language.PERSIAN)
                                                englishInput = ""
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", tint = EnglishPrimary)
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.width(8.dp))

                        // Mic Button
                        FilledIconButton(
                            onClick = {
                                if (isEnglishListening) {
                                    speechManager.stopListening()
                                    isEnglishListening = false
                                } else {
                                    isPersianListening = false
                                    isEnglishListening = true
                                    speechManager.onFinalResult = { text ->
                                        englishInput = text
                                        isEnglishListening = false
                                        coroutineScope.launch {
                                            val res = translationEngine.translate(
                                                text = text,
                                                from = Language.ENGLISH,
                                                to = Language.PERSIAN,
                                                style = selectedStyle
                                            )
                                            englishTranslatedOutput = res.translatedText
                                            ttsManager.speak(res.translatedText, Language.PERSIAN)
                                        }
                                    }
                                    speechManager.startListening(Language.ENGLISH)
                                }
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isEnglishListening) AccentRed else EnglishPrimary
                            ),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Icon(
                                imageVector = if (isEnglishListening) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "English Mic",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Divider with Swap / Mode info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(DarkSurface),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SyncAlt,
                        contentDescription = "Bidirectional Persian-English",
                        tint = PersianPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Persian (فارسی) ↔ English",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // BOTTOM PANEL: Persian (Person A)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(PersianSurface)
                    .border(1.dp, BorderColor)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🇮🇷", fontSize = 24.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "فارسی‌زبان (Person A)",
                                color = PersianPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }

                        if (englishTranslatedOutput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    ttsManager.speak(englishTranslatedOutput, Language.PERSIAN)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak Persian translation",
                                    tint = PersianPrimary
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Received translation from English (displayed clearly in Persian RTL)
                    if (englishTranslatedOutput.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DarkSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    text = "ترجمه از انگلیسی:",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = englishTranslatedOutput,
                                    fontSize = 18.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "منتظر صحبت یا تایپ به زبان فارسی...",
                            color = TextMuted,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    // Persian Speaker Input Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = persianInput,
                            onValueChange = { persianInput = it },
                            placeholder = { Text("متن فارسی را بنویسید...", color = TextMuted) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = PersianPrimary,
                                unfocusedBorderColor = BorderColor
                            ),
                            trailingIcon = {
                                if (persianInput.isNotBlank()) {
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                val res = translationEngine.translate(
                                                    text = persianInput,
                                                    from = Language.PERSIAN,
                                                    to = Language.ENGLISH,
                                                    style = selectedStyle
                                                )
                                                persianTranslatedOutput = res.translatedText
                                                ttsManager.speak(res.translatedText, Language.ENGLISH)
                                                persianInput = ""
                                            }
                                        }
                                    ) {
                                        Icon(Icons.Default.Send, contentDescription = "Send", tint = PersianPrimary)
                                    }
                                }
                            }
                        )

                        Spacer(Modifier.width(8.dp))

                        // Mic Button
                        FilledIconButton(
                            onClick = {
                                if (isPersianListening) {
                                    speechManager.stopListening()
                                    isPersianListening = false
                                } else {
                                    isEnglishListening = false
                                    isPersianListening = true
                                    speechManager.onFinalResult = { text ->
                                        persianInput = text
                                        isPersianListening = false
                                        coroutineScope.launch {
                                            val res = translationEngine.translate(
                                                text = text,
                                                from = Language.PERSIAN,
                                                to = Language.ENGLISH,
                                                style = selectedStyle
                                            )
                                            persianTranslatedOutput = res.translatedText
                                            ttsManager.speak(res.translatedText, Language.ENGLISH)
                                        }
                                    }
                                    speechManager.startListening(Language.PERSIAN)
                                }
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isPersianListening) AccentRed else PersianPrimary
                            ),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Icon(
                                imageVector = if (isPersianListening) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = "Persian Mic",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
