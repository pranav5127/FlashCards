package com.sugardevs.flashcards.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CardsResponse(
    val topic: String,
    val points: List<String>,
)
