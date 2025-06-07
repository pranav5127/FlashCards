package com.sugardevs.flashcards.data.network.api

import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import okhttp3.MultipartBody
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("topic")
    suspend fun getCards(
        @Body request: CardsRequest
    ): Response<CardsResponse>

    @Multipart
    @POST("upload")
    suspend fun uploadFile(
        @Part topicFile: MultipartBody.Part
    ): Response<CardsResponse>

}