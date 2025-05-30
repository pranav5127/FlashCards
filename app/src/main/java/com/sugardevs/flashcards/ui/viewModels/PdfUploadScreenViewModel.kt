package com.sugardevs.flashcards.ui.viewModels

import android.os.Message
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sugardevs.flashcards.data.model.CardsUiState
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.model.CardsResponse
import com.sugardevs.flashcards.data.network.repository.CardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

sealed class UploadUiState {
    object Idle : UploadUiState()
    object Loading : UploadUiState()
    data class Success(val response: CardsResponse) : UploadUiState()
    data class Error(val message: String) : UploadUiState()
}

@HiltViewModel
class PdfUploadScreenViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    var uiState by mutableStateOf<UploadUiState>(UploadUiState.Idle)

    var text by mutableStateOf("")
        private set

    fun onTextChange(newText: String) {
        text = newText
    }


    fun uploadTopic(topic: String) {
        viewModelScope.launch {
            uiState = UploadUiState.Loading

            uiState = try {
                val result = repository.fetchCards(CardsRequest(topic = topic))
                if (result.isSuccessful && result.body() != null) {
                    UploadUiState.Success(result.body()!!)
                } else {
                    UploadUiState.Error("Error: ${result.errorBody()?.string() ?: "Unknown error"}")
                }
            } catch (e: Exception) {
                UploadUiState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun onPdfUploadButtonPress(pdfFile: File) {
        /*TODO*/
    }

}