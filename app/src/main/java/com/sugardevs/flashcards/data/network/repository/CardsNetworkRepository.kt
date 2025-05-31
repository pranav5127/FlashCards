package com.sugardevs.flashcards.data.network.repository

import com.sugardevs.flashcards.data.network.api.ApiService
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class CardsNetworkRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchCards(request: CardsRequest): Response<CardsResponse> {
       return apiService.getCards(request)
    }

    suspend fun uploadPdfFile(file: File): Response<CardsResponse> {
       val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
       val multipart = MultipartBody.Part.createFormData("file", file.name, requestFile)
       return apiService.uploadFile(multipart)
    }
}