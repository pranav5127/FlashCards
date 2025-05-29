package com.sugardevs.flashcards.data.network.api

import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("topic")
    suspend fun getCards(
        @Body request: CardsRequest
    ): CardsResponse
}