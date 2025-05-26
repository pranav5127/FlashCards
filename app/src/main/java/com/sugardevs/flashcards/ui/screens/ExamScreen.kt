package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sugardevs.flashcards.R
import com.sugardevs.flashcards.ui.components.BottomBar
import com.sugardevs.flashcards.ui.components.ExamCard
import com.sugardevs.flashcards.ui.components.TopBar
import com.sugardevs.flashcards.ui.theme.FlashCardsTheme

@Composable
fun ExamScreen(subject: String = "Data Structures", questionCount: Int = 10) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.exam_screen),
                onBackClick = { /*TODO*/ },
                onProfileClick = { /*TODO*/ }
            )
        },
        bottomBar = {
            BottomBar()
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                ExamCard(subject, questionCount)
            }
        }
    )
}


@Preview
@Composable
fun ExamScreenPreview() {
   ExamScreen()
}
@Preview
@Composable
fun ExamScreenPreviewDark() {
    FlashCardsTheme(
        darkTheme = true
    ) {
        ExamScreen()
    }
}