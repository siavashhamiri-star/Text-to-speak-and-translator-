package com.sidebyside.translator.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.sidebyside.translator.engine.CompositeTranslationEngine
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.model.ConversationMode
import com.sidebyside.translator.ui.screens.*
import com.sidebyside.translator.ui.theme.DarkBackground
import com.sidebyside.translator.ui.theme.DarkSurface
import com.sidebyside.translator.ui.theme.PersianPrimary
import com.sidebyside.translator.ui.theme.SideBySideTheme

class MainActivity : ComponentActivity() {

    private lateinit var translationEngine: CompositeTranslationEngine
    private lateinit var speechManager: SpeechManager
    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        translationEngine = CompositeTranslationEngine()
        speechManager = SpeechManager(this)
        ttsManager = TtsManager(this)

        setContent {
            SideBySideTheme {
                MainAppContainer(
                    translationEngine = translationEngine,
                    speechManager = speechManager,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechManager.stopListening()
        ttsManager.shutdown()
    }
}

@Composable
fun MainAppContainer(
    translationEngine: CompositeTranslationEngine,
    speechManager: SpeechManager,
    ttsManager: TtsManager
) {
    var selectedMode by remember { mutableStateOf(ConversationMode.SIDE_BY_SIDE) }
    var hasMicPermission by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    val micPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = selectedMode == ConversationMode.SIDE_BY_SIDE,
                    onClick = { selectedMode = ConversationMode.SIDE_BY_SIDE },
                    icon = { Icon(Icons.Default.CompareArrows, contentDescription = "Side by Side") },
                    label = { Text("Side by Side", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PersianPrimary,
                        selectedTextColor = PersianPrimary,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = selectedMode == ConversationMode.CHAT,
                    onClick = { selectedMode = ConversationMode.CHAT },
                    icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                    label = { Text("Chat", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PersianPrimary,
                        selectedTextColor = PersianPrimary,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = selectedMode == ConversationMode.LIVE_VOICE,
                    onClick = { selectedMode = ConversationMode.LIVE_VOICE },
                    icon = { Icon(Icons.Default.Mic, contentDescription = "Live Voice") },
                    label = { Text("Live Voice", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PersianPrimary,
                        selectedTextColor = PersianPrimary,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = selectedMode == ConversationMode.PRACTICE,
                    onClick = { selectedMode = ConversationMode.PRACTICE },
                    icon = { Icon(Icons.Default.School, contentDescription = "Practice") },
                    label = { Text("Practice", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PersianPrimary,
                        selectedTextColor = PersianPrimary,
                        indicatorColor = DarkSurface
                    )
                )

                NavigationBarItem(
                    selected = selectedMode == ConversationMode.VIDEO,
                    onClick = { selectedMode = ConversationMode.VIDEO },
                    icon = { Icon(Icons.Default.Videocam, contentDescription = "Video") },
                    label = { Text("Video", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PersianPrimary,
                        selectedTextColor = PersianPrimary,
                        indicatorColor = DarkSurface
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            when (selectedMode) {
                ConversationMode.SIDE_BY_SIDE -> {
                    SideBySideScreen(
                        translationEngine = translationEngine,
                        speechManager = speechManager,
                        ttsManager = ttsManager,
                        onNavigateToChat = { selectedMode = ConversationMode.CHAT }
                    )
                }
                ConversationMode.CHAT -> {
                    ChatScreen(
                        translationEngine = translationEngine,
                        speechManager = speechManager,
                        ttsManager = ttsManager
                    )
                }
                ConversationMode.LIVE_VOICE -> {
                    LiveVoiceScreen(
                        translationEngine = translationEngine,
                        speechManager = speechManager,
                        ttsManager = ttsManager
                    )
                }
                ConversationMode.PRACTICE -> {
                    PracticeScreen(
                        speechManager = speechManager,
                        ttsManager = ttsManager
                    )
                }
                ConversationMode.VIDEO -> {
                    VideoScreen(
                        translationEngine = translationEngine,
                        speechManager = speechManager,
                        ttsManager = ttsManager,
                        hasCameraPermission = hasCameraPermission,
                        onRequestCameraPermission = {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }
            }
        }
    }
}
