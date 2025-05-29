package com.sugardevs.flashcards.data.model

import java.util.Collections

data class CardsUiState(
    val id: String = "",
    val isLoading: Boolean = true,
    val cards: List<String> = Collections.emptyList(),
    val currentCardIndex: Int = 0,
    val error: String? = null
)