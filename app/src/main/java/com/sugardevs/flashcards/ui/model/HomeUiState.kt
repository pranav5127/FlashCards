package com.sugardevs.flashcards.ui.model

import com.sugardevs.flashcards.data.local.model.TopicEntity

data class Subject(
    val topicId: String,
    val name: String
)

data class HomeUiState(
    val userName: String = "User",
    val subjects: List<Subject> = emptyList(),
    val recentCards: List<TopicEntity> = emptyList(),
    val isLoadingTopics: Boolean = false,
    val isLoadingCards: Boolean = false,
    val errorMessage: String? = null
)
