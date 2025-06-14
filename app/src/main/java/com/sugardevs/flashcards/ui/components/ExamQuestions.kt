package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.viewModels.ExamScreenViewModel

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
                text = "Question ${examEntity.questionId} of ${questions.size} ",
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
                            isSelected = selectedAnswers[examEntity.id] == option,
                            onOptionSelected = {
                                selectedAnswers = selectedAnswers + (examEntity.id to option)
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
                        painter = painterResource(R.drawable.outline_arrow_circle_left_24),
                        contentDescription = "Previous",
                        modifier = Modifier.size(64.dp)
                    )
                }

                if (currentQuestionIndex < questions.size - 1) {
                    IconButton(onClick = {
                        currentQuestionIndex++
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_arrow_circle_right_24),
                            contentDescription = "Next",
                            modifier = Modifier.size(64.dp)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        println("Exam Submitted. Answers: $selectedAnswers")
                        viewModel.showScore(selectedAnswers, examEntity.topicId)
                        onSubmitExam()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_check_circle_outline_24),
                            contentDescription = "Submit",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OptionItem(
    optionText: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOptionSelected)
    ) {
        Text(
            text = optionText,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}