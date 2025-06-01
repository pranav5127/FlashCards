package com.sugardevs.flashcards.ui.model

data class HomeUiState(
    val userName: String = "User",
    val subjects:List<String> = emptyList(),
    val recentCards: List<String> = emptyList()

)

