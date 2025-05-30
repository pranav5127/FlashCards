package com.sugardevs.flashcards.data.network.model

import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class CardsRequest(
    val id: String = UUID.randomUUID().toString(),
    val topic: String,
)
