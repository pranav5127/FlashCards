package com.sugardevs.flashcards.data.network.model

import java.util.UUID

data class CardsRequest(
    val id: String = UUID.randomUUID().toString(),
    val topic: String,
)
