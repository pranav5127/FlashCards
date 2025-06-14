package com.sugardevs.flashcards.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CardsResponse(
    val topic: String,
    val points: List<String>,
    val questions: List<ExamQuestionDto>,
)

@Serializable
data class ExamQuestionDto(
    val questionId: Int,
    val question: String,
    val options: List<String>,
    val answer: String
)
