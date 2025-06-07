package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sugardevs.flashcards.ui.components.ExamCard
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExamScreen(
    subject: String,
    questionCount: Int
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ExamCard(subject, questionCount)
    }
}


@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    FlashCardsTheme {
        ExamScreen(
            subject = "1",
            questionCount = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreviewDark() {
    FlashCardsTheme(
        darkTheme = true
    ) {
        ExamScreen(
            subject = "2",
            questionCount = 2
        )
    }
}
