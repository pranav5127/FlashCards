package com.sugardevs.flashcards.ui.model

import com.sugardevs.flashcards.data.local.model.TopicEntity


data class HomeUiState(
    val userName: String = "User",
    val subjects: List<TopicEntity> = emptyList(), // Holds TopicEntity objects
    val recentCards: List<String> = emptyList(),   // Holds card content strings
    val isLoadingTopics: Boolean = true,
    val isLoadingCards: Boolean = true,
    val errorMessage: String? = null
)

