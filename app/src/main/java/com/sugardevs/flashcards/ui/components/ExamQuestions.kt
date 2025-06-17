package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.ui.viewModels.ExamScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamQuestions(
    modifier: Modifier = Modifier,
    topicId: String,
    viewModel: ExamScreenViewModel = hiltViewModel(),
    onSubmitExam: () -> Unit = {}
) {
    val questions by viewModel.questionsByTopic.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val showDialog = viewModel.showDialog
    val finalScoreText = viewModel.finalScoreText

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mapOf<Int, String>()) }

    LaunchedEffect(topicId) {
        viewModel.loadQuestionsByTopicId(topicId)
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (errorMessage != null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $errorMessage", color = Color.Red)
        }
        return
    }

    if (questions.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available for this topic.")
        }
        return
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    currentQuestion?.let { examEntity ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Topic: $topicId",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = examEntity.question,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    examEntity.options.forEach { option ->
                        OptionItem(
                            optionText = option,
                            isSelected = selectedAnswers[examEntity.questionId] == option,
                            onOptionSelected = {
                                selectedAnswers = selectedAnswers + (examEntity.questionId to option)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                        }
                    },
                    enabled = currentQuestionIndex > 0
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowCircleLeft,
                        contentDescription = "Previous",
                        modifier = Modifier.size(64.dp)
                    )
                }

                if (currentQuestionIndex < questions.size - 1) {
                    IconButton(onClick = {
                        currentQuestionIndex++
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowCircleRight,
                            contentDescription = "Next",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        viewModel.submitExam(selectedAnswers, examEntity.topicId)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircleOutline,
                            contentDescription = "Submit",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        ShowDialog(
            onConfirmation = {
                viewModel.dismissDialog()
                onSubmitExam()
            },
            dialogText = finalScoreText
        )
    }
}

@Composable
fun OptionItem(
    optionText: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOptionSelected() }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = optionText,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ShowDialog(
    onConfirmation: () -> Unit,
    dialogText: String
) {
    AlertDialog(
        onDismissRequest = { onConfirmation() },
        title = { Text(text = "Exam Score") },
        text = { Text(text = dialogText) },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text("OK")
            }
        }
    )
}
