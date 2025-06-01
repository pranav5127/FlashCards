package com.sugardevs.flashcards.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.CardsDbRepository
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.repository.CardsNetworkRepository
import com.sugardevs.flashcards.utils.UploadUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PdfUploadScreenViewModel @Inject constructor(
    private val repository: CardsNetworkRepository,
    private val dbRepository: CardsDbRepository
) : ViewModel() {

    var uiState by mutableStateOf<UploadUiState>(UploadUiState.Idle)

    var text by mutableStateOf("")
        private set

    var navigateToTopic by mutableStateOf<String?>(null)
        private set

    fun onTextChange(newText: String) {
        text = newText
    }


    fun uploadTopic(topic: String) {
        if (topic.isBlank()) {
            uiState = UploadUiState.Error("Topic cannot be empty.")
            return
        }

        viewModelScope.launch {
            uiState = UploadUiState.Loading
            try {
                Log.d("PdfUploadViewModel", "Uploading topic: $topic")
                val result = repository.fetchCards(CardsRequest(topic = topic))
                if (result.isSuccessful && result.body() != null) {
                    val response = result.body()!!

                    dbRepository.saveCards(response.topic, response.points)
                    uiState = UploadUiState.Success(response)
                    navigateToTopic = response.topic
                    text = "" // Clear input after success
                } else {
                    uiState = UploadUiState.Error(
                        "Error: ${
                            result.errorBody()?.string() ?: "Unknown error"
                        }"
                    )
                }
            } catch (e: Exception) {
                uiState = UploadUiState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun onPdfUploadButtonPress(pdfFile: File) {
        viewModelScope.launch {
            uiState = UploadUiState.Loading
            try {
                Log.d("PdfUploadViewModel", "Uploading PDF: ${pdfFile.name}")
                val result = repository.uploadPdfFile(pdfFile)
                if (result.isSuccessful && result.body() != null) {
                    val response = result.body()!!

                    dbRepository.saveCards(response.topic, response.points)
                    uiState = UploadUiState.Success(response)
                    navigateToTopic = response.topic
                } else {
                    uiState = UploadUiState.Error(
                        "Error: ${
                            result.errorBody()?.string() ?: "Unknown error"
                        }"
                    )
                }
            } catch (e: Exception) {
                uiState = UploadUiState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun onNavigated() {
        navigateToTopic = null
        uiState = UploadUiState.Idle
    }
}
