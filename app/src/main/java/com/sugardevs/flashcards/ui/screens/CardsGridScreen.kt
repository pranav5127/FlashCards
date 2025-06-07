package com.sugardevs.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sugardevs.flashcards.ui.components.FlashCardTopic
import com.sugardevs.flashcards.ui.viewModels.CardsGridViewModel

@Composable
fun CardsGridScreen(
    modifier: Modifier = Modifier,
    viewModel: CardsGridViewModel = hiltViewModel(),
    onCardsClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val gradientPairs = listOf(
        0xFF64B5F6 to 0xFF2196F3,
        0xFF3DDC84 to 0xFF2B9D5C,
        0xFF42A5F5 to 0xFF00447F,
        0xFF7E57C2 to 0xFF5E35B1,
        0xFF740F90 to 0xFFB425B2
    ).map { Color(it.first) to Color(it.second) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(uiState.subjects) { subject ->
            val index = uiState.subjects.indexOf(subject)
            val (startColor, endColor) = gradientPairs[index % gradientPairs.size]

            FlashCardTopic(
                subject = subject,
                gradientStartColor = startColor,
                gradientEndColor = endColor,
                onCardClick = { onCardsClick(subject.topicId) }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CardsGridScreenPreview() {
    CardsGridScreen( onCardsClick = {})
}

