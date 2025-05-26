package com.sugardevs.flashcards.data.model

data class CardsUiState(
    val isLoading: Boolean = true,
    val cards: List<String> = emptyList(),
    val currentCardIndex: Int = 0,
    val error: String? = null
)
