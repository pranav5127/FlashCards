package com.sugardevs.flashcards.ui.model

data class HomeUiState(
    val userName: String = "User",
    val subjects:Topic = Topic()
)

data class Topic(
    val subject1: String = "Android",
    val subject2: String = "Databases",
    val subject3: String = "Computer Networks",
    val subject4: String = "Data Structures",
)
