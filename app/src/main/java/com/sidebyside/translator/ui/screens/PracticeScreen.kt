package com.sidebyside.translator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.sidebyside.translator.engine.SpeechManager
import com.sidebyside.translator.engine.TtsManager
import com.sidebyside.translator.model.Language
import com.sidebyside.translator.model.PracticeScenario
import com.sidebyside.translator.model.ScenarioCategory
import com.sidebyside.translator.practice.EvaluationResult
import com.sidebyside.translator.practice.PracticeEngine
import com.sidebyside.translator.ui.theme.*

@Composable
fun PracticeScreen(
    speechManager: SpeechManager,
    ttsManager: TtsManager
) {
    val practiceEngine = remember { PracticeEngine() }
    var selectedCategory by remember { mutableStateOf(ScenarioCategory.EVERYDAY) }
    var selectedScenario by remember {
        mutableStateOf(practiceEngine.scenarios.first { it.category == ScenarioCategory.EVERYDAY })
    }
    var currentTurnIndex by remember { mutableIntStateOf(0) }
    var userSpeechInput by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    var evaluationResult by remember { mutableStateOf<EvaluationResult?>(null) }

    val currentTurn = selectedScenario.turns.getOrNull(currentTurnIndex) ?: selectedScenario.turns.first()

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
            .padding(12.dp)
    ) {
        // Offline Mode Badge & Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "AI / Offline Language Practice",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "تقویت مکالمه انگلیسی به زبان فارسی (۱۰۰٪ آفلاین)",
                    color = PersianPrimary,
                    fontSize = 12.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PersianBackground
            ) {
                Text(
                    text = "Offline Ready",
                    color = PersianPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Categories Scroll Bar
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(ScenarioCategory.entries) { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedCategory = cat
                        val firstMatch = practiceEngine.scenarios.find { it.category == cat }
                        if (firstMatch != null) {
                            selectedScenario = firstMatch
                            currentTurnIndex = 0
                            userSpeechInput = ""
                            evaluationResult = null
                        }
                    },
                    label = {
                        Text("${cat.iconSymbol} ${cat.titleEn}")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PersianPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Active Scenario Card
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedScenario.titleEn,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            IconButton(
                                onClick = {
                                    ttsManager.speak(currentTurn.promptEn, Language.ENGLISH)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Listen to native prompt",
                                    tint = EnglishPrimary
                                )
                            }
                        }

                        Text(
                            text = selectedScenario.titleFa,
                            fontSize = 13.sp,
                            color = PersianPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))
                        Divider(color = BorderColor)
                        Spacer(Modifier.height(10.dp))

                        Text(
                            text = "Conversation Prompt:",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "\"${currentTurn.promptEn}\"",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EnglishPrimary
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = currentTurn.promptFa,
                            fontSize = 14.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Expected Native Responses & Persian Explanations
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            text = "💡 راهنما و پاسخ‌های پیشنهادی بومی:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentAmber,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = currentTurn.explanationFa,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(10.dp))
                        currentTurn.expectedAnswersEn.forEach { expected ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        userSpeechInput = expected
                                        evaluationResult = practiceEngine.evaluateAnswer(expected, currentTurn)
                                    }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ArrowRight, contentDescription = null, tint = PersianPrimary)
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = expected,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Evaluation Card (if tested)
            if (evaluationResult != null) {
                item {
                    val eval = evaluationResult!!
                    val scoreColor = if (eval.scorePercent >= 80) PersianPrimary else if (eval.scorePercent >= 60) AccentAmber else AccentRed

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DarkSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Practice Score: ${eval.scorePercent}%",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = scoreColor
                                )
                                Text(
                                    text = if (eval.isAccurate) "✅ عالی" else "⚡ تلاش مجدد",
                                    fontSize = 14.sp,
                                    color = scoreColor
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = eval.feedbackEn, fontSize = 14.sp, color = TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = eval.feedbackFa,
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Vocabulary Notes
            if (currentTurn.vocabularyNotes.isNotEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DarkSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                text = "📚 واژگان کلیدی این سناریو:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PersianPrimary,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(Modifier.height(8.dp))
                            currentTurn.vocabularyNotes.forEach { vocab ->
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(vocab.termEn, fontWeight = FontWeight.Bold, color = EnglishPrimary)
                                        Text(vocab.meaningFa, color = TextPrimary)
                                    }
                                    Text(
                                        text = "${vocab.pronunciationEn} • Example: \"${vocab.exampleEn}\"",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Input & Test Bar
        Surface(
            color = DarkSurface,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speech input button
                FilledIconButton(
                    onClick = {
                        if (isListening) {
                            speechManager.stopListening()
                            isListening = false
                        } else {
                            isListening = true
                            speechManager.onFinalResult = { recognized ->
                                isListening = false
                                userSpeechInput = recognized
                                evaluationResult = practiceEngine.evaluateAnswer(recognized, currentTurn)
                            }
                            speechManager.startListening(Language.ENGLISH)
                        }
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (isListening) AccentRed else EnglishPrimary
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Speak in English",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = userSpeechInput,
                    onValueChange = { userSpeechInput = it },
                    placeholder = { Text("Speak or type English answer...", fontSize = 13.sp, color = TextMuted) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = EnglishPrimary,
                        unfocusedBorderColor = BorderColor
                    )
                )

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        evaluationResult = practiceEngine.evaluateAnswer(userSpeechInput, currentTurn)
                    },
                    enabled = userSpeechInput.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Evaluate",
                        tint = if (userSpeechInput.isNotBlank()) PersianPrimary else TextMuted
                    )
                }
            }
        }
    }
}
