package com.sugardevs.flashcards.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
    questionText: String = "What is the capital of France?",
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onSubmitClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Exam",
                onBackClick = { /*TODO*/ },
                onProfileClick = { /*TODO*/ }
            )
        },
        bottomBar = {
            BottomBar()
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question Counter
            Text(
                text = "Question $questionNumber of $totalQuestions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            // Question Title
            Text(
                text = "Question $questionNumber",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Question Text
            Text(
                text = questionText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )

            // Options
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

            // Icon Buttons Row
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
        ExamQuestions()
    }
}

@Preview(showBackground = true)
@Composable
fun ExamQuestionPreviewDark() {
    FlashCardsTheme(darkTheme = true) {
        ExamQuestions()
    }
}
