package com.sugardevs.flashcards.data.network.repository

import com.sugardevs.flashcards.data.network.api.ApiService
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import retrofit2.Response
import javax.inject.Inject

class CardsRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchCards(request: CardsRequest): Response<CardsResponse> {
       return apiService.getCards(request)
    }
}