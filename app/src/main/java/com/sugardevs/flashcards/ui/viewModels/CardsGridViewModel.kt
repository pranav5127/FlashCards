package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.TopicRepository
import com.sugardevs.flashcards.ui.model.GridUiState
import com.sugardevs.flashcards.ui.model.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsGridViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GridUiState())
    val uiState: StateFlow<GridUiState> = _uiState.asStateFlow()

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            try {
                topicRepository.getAllCardsNewestFirstFlow().collect { topicsList ->
                    _uiState.value = _uiState.value.copy(
                        subjects = topicsList.map { topic ->
                            Subject(
                                topicId = topic.id,
                                name = topic.name
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
