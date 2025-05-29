package com.sugardevs.flashcards.data.network.repository

import com.sugardevs.flashcards.data.network.api.ApiService
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchCards(request: CardsRequest): CardsResponse {
       return apiService.getCards(request)
    }
}