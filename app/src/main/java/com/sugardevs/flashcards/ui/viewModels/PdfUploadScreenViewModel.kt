package com.sugardevs.flashcards.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.model.ExamEntity
import com.sugardevs.flashcards.data.local.repository.CardsRepository
import com.sugardevs.flashcards.data.local.repository.ExamRepository
import com.sugardevs.flashcards.data.local.repository.TopicRepository
import com.sugardevs.flashcards.data.network.model.CardsRequest
import com.sugardevs.flashcards.data.network.repository.CardsNetworkRepository
import com.sugardevs.flashcards.utils.UploadUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PdfUploadScreenViewModel @Inject constructor(
    private val networkRepository: CardsNetworkRepository,
    private val cardsRepository: CardsRepository,
    private val topicRepository: TopicRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    var uiState by mutableStateOf<UploadUiState>(UploadUiState.Idle)

    var text by mutableStateOf("")
        private set

    var navigateToTopicId by mutableStateOf<String?>(null)
        private set

    fun onTextChange(newText: String) {
        text = newText
    }

    fun uploadTopic(topicNameFromInput: String) {
        val trimmedTopicName = topicNameFromInput.trim()
        if (trimmedTopicName.isBlank()) {
            uiState = UploadUiState.Error("Topic name cannot be empty.")
            return
        }

        viewModelScope.launch {
            uiState = UploadUiState.Loading
            try {
                Log.d("PdfUploadViewModel", "Fetching cards for topic: $trimmedTopicName")
                val result = networkRepository.fetchCards(CardsRequest(topic = trimmedTopicName))

                if (result.isSuccessful && result.body() != null) {
                    val response = result.body()!!
                    val topicIdAndNameFromServer = response.topic.trim()

                    if (topicIdAndNameFromServer.isBlank()) {
                        Log.e("PdfUploadViewModel", "Received blank topic name/ID from server for input: $trimmedTopicName")
                        uiState = UploadUiState.Error("Received empty topic from server.")
                        return@launch
                    }

                    topicRepository.ensureTopicExists(topicIdAndNameFromServer, topicIdAndNameFromServer)

                    Log.d("PdfUploadViewModel", "Saving cards for topic ID (from server): '$topicIdAndNameFromServer'")
                    cardsRepository.saveCards(topicIdAndNameFromServer, response.points)
                    examRepository.insertQuestionsFromDto(topicIdAndNameFromServer, response.questions)
                    Log.d("PdfUploadViewModel", "Saved ${response.points.size} cards for topic: $topicIdAndNameFromServer")

                    uiState = UploadUiState.Success(response)
                    navigateToTopicId = topicIdAndNameFromServer
                    text = ""
                } else {
                    if (result.code() == 400) {
                        // Handle Bad Request (400) error
                        Log.e("PdfUploadViewModel", "Bad Request (400) for topic '$trimmedTopicName'")
                        val errorBody = result.errorBody()?.string() ?: "Bad Request - The server could not process your request."
                        uiState = UploadUiState.Error("Error: $errorBody")
                    } else {
                        val errorBody = result.errorBody()?.string() ?: "Unknown error"
                        Log.e("PdfUploadViewModel", "Error fetching cards for topic '$trimmedTopicName': ${result.code()} - $errorBody")
                        uiState = UploadUiState.Error("Error: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("PdfUploadViewModel", "Exception in uploadTopic for '$trimmedTopicName'", e)
                uiState = UploadUiState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun onPdfUploadButtonPress(pdfFile: File) {
        viewModelScope.launch {
            uiState = UploadUiState.Loading
            try {
                Log.d("PdfUploadViewModel", "Uploading PDF: ${pdfFile.name}")
                val result = networkRepository.uploadPdfFile(pdfFile)

                if (result.isSuccessful && result.body() != null) {
                    val response = result.body()!!
                    val topicIdAndNameFromServer = response.topic.trim()

                    if (topicIdAndNameFromServer.isBlank()) {
                        Log.e("PdfUploadViewModel", "Received blank topic name/ID from PDF response for file: ${pdfFile.name}")
                        uiState = UploadUiState.Error("Received empty topic from PDF processing.")
                        return@launch
                    }

                    topicRepository.ensureTopicExists(topicIdAndNameFromServer, topicIdAndNameFromServer)

                    Log.d("PdfUploadViewModel", "Saving cards from PDF for topic ID (from server): '$topicIdAndNameFromServer'")
                    cardsRepository.saveCards(topicIdAndNameFromServer, response.points)
                    examRepository.insertQuestionsFromDto(topicIdAndNameFromServer, response.questions)
                    Log.d("PdfUploadViewModel", "Saved ${response.points.size} cards for topic: $topicIdAndNameFromServer from PDF")

                    uiState = UploadUiState.Success(response)
                    navigateToTopicId = topicIdAndNameFromServer
                } else {
                    if (result.code() == 400) {
                        // Handle Bad Request (400) error
                        Log.e("PdfUploadViewModel", "Bad Request (400) when uploading PDF '${pdfFile.name}'")
                        val errorBody = result.errorBody()?.string() ?: "Bad Request - The server could not process the PDF upload."
                        uiState = UploadUiState.Error("Error: $errorBody")
                    } else {
                        val errorBody = result.errorBody()?.string() ?: "Unknown error"
                        Log.e("PdfUploadViewModel", "Error uploading PDF '${pdfFile.name}': ${result.code()} - $errorBody")
                        uiState = UploadUiState.Error("Error: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("PdfUploadViewModel", "Exception in onPdfUploadButtonPress for '${pdfFile.name}'", e)

                uiState = UploadUiState.Error("Exception: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }


    fun onNavigated() {
        navigateToTopicId = null
        uiState = UploadUiState.Idle
    }
}
