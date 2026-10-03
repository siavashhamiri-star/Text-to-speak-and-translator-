package com.sidebyside.translator.ui.screens

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.engine.VideoCommunicationFoundation
import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.Speaker
import com.sidebyside.translator.model.TranslationStyle
import com.sidebyside.translator.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun VideoScreen(
    translationEngine: CompositeTranslationEngine,
    speechManager: SpeechManager,
    ttsManager: TtsManager,
    hasCameraPermission: Boolean,
    onRequestCameraPermission: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var isCameraActive by remember { mutableStateOf(hasCameraPermission) }
    var activeSpeaker by remember { mutableStateOf(Speaker.PERSON_A) }
    var liveOriginalSubtitle by remember { mutableStateOf("چطور می‌توانم به ایستگاه قطار بروم؟") }
    var liveTranslatedSubtitle by remember { mutableStateOf("How can I get to the train station?") }
    var isVoiceTranslating by remember { mutableStateOf(false) }

    val videoFoundation = remember {
        VideoCommunicationFoundation(context, lifecycleOwner)
    }

    DisposableEffect(Unit) {
        onDispose {
            videoFoundation.release()
            speechManager.stopListening()
            ttsManager.stop()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Video Calling Viewport
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            // Main Stage: Remote Peer Presentation Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Remote caller",
                        modifier = Modifier.size(96.dp),
                        tint = EnglishPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Remote Partner (English)",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Signaling channel ready for WebRTC remote stream",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Local Front-Camera Preview (PIP Window)
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(width = 110.dp, height = 150.dp)
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurface)
                    .border(2.dp, PersianPrimary, RoundedCornerShape(12.dp))
            ) {
                if (hasCameraPermission && isCameraActive) {
                    AndroidView(
                        factory = { ctx ->
                            PreviewView(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                videoFoundation.startFrontCamera(this) { _ -> }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = onRequestCameraPermission) {
                            Icon(
                                imageVector = Icons.Default.VideocamOff,
                                contentDescription = "Grant Camera Permission",
                                tint = AccentAmber
                            )
                        }
                    }
                }
            }

            // Real-Time Subtitles Overlay (Persian & English)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = DarkSurface.copy(alpha = 0.92f)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🇮🇷 فارسی (Original):",
                            fontSize = 11.sp,
                            color = PersianPrimary
                        )
                        Text(
                            text = "Live Translation Overlay",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Text(
                        text = liveOriginalSubtitle,
                        fontSize = 15.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(6.dp))
                    Divider(color = BorderColor)
                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "🇬🇧 English (Translated):",
                        fontSize = 11.sp,
                        color = EnglishPrimary
                    )
                    Text(
                        text = liveTranslatedSubtitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Call Control Bar
        Surface(
            color = DarkSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic toggle
                FilledIconButton(
                    onClick = {
                        isVoiceTranslating = !isVoiceTranslating
                        if (isVoiceTranslating) {
                            speechManager.onFinalResult = { text ->
                                liveOriginalSubtitle = text
                                coroutineScope.launch {
                                    val res = translationEngine.translate(
                                        text = text,
                                        from = Language.PERSIAN,
                                        to = Language.ENGLISH,
                                        style = TranslationStyle.NATURAL
                                    )
                                    liveTranslatedSubtitle = res.translatedText
                                    ttsManager.speak(res.translatedText, Language.ENGLISH)
                                }
                            }
                            speechManager.startListening(Language.PERSIAN)
                        } else {
                            speechManager.stopListening()
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isVoiceTranslating) PersianPrimary else DarkSurfaceVariant
                    ),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = if (isVoiceTranslating) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Toggle Translation Mic",
                        tint = Color.White
                    )
                }

                // Camera toggle
                FilledIconButton(
                    onClick = {
                        if (!hasCameraPermission) {
                            onRequestCameraPermission()
                        } else {
                            isCameraActive = !isCameraActive
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isCameraActive) DarkSurfaceVariant else AccentAmber
                    ),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = if (isCameraActive) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Toggle Local Camera",
                        tint = Color.White
                    )
                }

                // Speaker Switcher
                FilledIconButton(
                    onClick = {
                        activeSpeaker = activeSpeaker.other
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = DarkSurfaceVariant
                    ),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch active speaker",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
