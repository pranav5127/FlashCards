package com.sugardevs.flashcards.ui.model

data class AnswerResult(
    val question: String,
    val selectedAnswer: String?,
    val correctAnswer: String,
    val isCorrect: Boolean
)