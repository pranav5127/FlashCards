package com.sugardevs.flashcards.utils

import com.sugardevs.flashcards.data.network.model.CardsResponse


sealed class UploadUiState {
    object Idle : UploadUiState()
    object Loading : UploadUiState()
    data class Success(val response: CardsResponse) : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}