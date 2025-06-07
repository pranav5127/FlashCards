package com.sugardevs.flashcards.ui.screens
import androidx.compose.runtime.Composable

@Composable
fun ExamGridScreen(
    onExamClick: (String) -> Unit
) {
    CardsGridScreen(onCardsClick = onExamClick )
}

