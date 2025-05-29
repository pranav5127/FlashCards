package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sugardevs.flashcards.ui.components.ExamCard
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExamScreen(
    subject: String = "Data Structures",
    questionCount: Int = 10
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ExamCard(subject, questionCount)
    }
}


@Preview(showBackground = true)
@Composable
fun ExamScreenPreview() {
    FlashCardsTheme {
        ExamScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ExamScreenPreviewDark() {
    FlashCardsTheme(
        darkTheme = true
    ) {
        ExamScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Exam Screen With Scaffold Preview")
@Composable
fun ExamScreenWithScaffoldPreview() {
    FlashCardsTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Exam Screen Preview") }) },
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                ExamScreen()
            }
        }
    }
}