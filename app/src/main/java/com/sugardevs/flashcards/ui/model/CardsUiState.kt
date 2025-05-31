package com.sugardevs.flashcards.ui.model

data class CardsUiState(
    val isLoading: Boolean = true,
    val cards: List<String> = emptyList(),
    val currentCardIndex: Int = 0,
    val error: String? = null
)
