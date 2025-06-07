package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExamQuestions(
    modifier: Modifier = Modifier,
    questionNumber: Int = 1,
    totalQuestions: Int = 10,
    subject: String,
    questionText: String = "What is the capital of France?",
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Question $questionNumber of $totalQuestions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = subject,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = questionText,
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
                OptionItem("Paris")
                OptionItem("London")
                OptionItem("Berlin")
                OptionItem("Madrid")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onPreviousClick,
                enabled = questionNumber > 1
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_circle_left_24),
                    contentDescription = "Previous",
                    modifier = Modifier.size(64.dp)
                )
            }

            if (questionNumber < totalQuestions) {
                IconButton(onClick = onNextClick) {
                    Icon(
                        painter = painterResource(R.drawable.outline_arrow_circle_right_24),
                        contentDescription = "Next",
                        modifier = Modifier.size(64.dp)
                    )
                }
            } else {
                IconButton(onClick = onSubmitClick) {
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

@Composable
fun OptionItem(optionText: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = optionText,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExamQuestionPreviewLight() {
    FlashCardsTheme(darkTheme = false) {
        ExamQuestions(subject = "")
    }
}

@Preview(showBackground = true)
@Composable
fun ExamQuestionPreviewDark() {
    FlashCardsTheme(darkTheme = true) {
        ExamQuestions(subject = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Exam Questions With Scaffold Preview")
@Composable
fun ExamQuestionsWithScaffoldPreview() {
    FlashCardsTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Exam Questions Preview") }) },
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                ExamQuestions(subject = "")
            }
        }
    }
}