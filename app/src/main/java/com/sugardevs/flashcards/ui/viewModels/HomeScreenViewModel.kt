package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.TopicRepository
import com.sugardevs.flashcards.ui.model.HomeUiState
import com.sugardevs.flashcards.ui.model.Subject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val topicRepository: TopicRepository,

) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadInitialHomeScreenData()
    }

    private fun loadInitialHomeScreenData() {
        loadTopics()
        loadRecentCards()
    }

    private fun loadTopics() {
        viewModelScope.launch {
            _homeUiState.update { it.copy(isLoadingTopics = true, errorMessage = null) }
            try {
                topicRepository.getAllCardsNewestFirstFlow().collect { topicsList ->
                    _homeUiState.update { currentState ->
                        currentState.copy(
                            subjects = topicsList.map { topic ->
                                Subject(
                                    topicId = topic.id,
                                    name = topic.name
                                )
                            },
                            isLoadingTopics = false
                        )
                    }
                }
            } catch (e: Exception) {
                _homeUiState.update { currentState ->
                    currentState.copy(
                        isLoadingTopics = false,
                        errorMessage = "Failed to load topics: ${e.message}"
                    )
                }
            }
        }
    }

    private fun loadRecentCards() {
        viewModelScope.launch {
            topicRepository.getAllCardsNewestFirstFlow().collect { recentCardsList ->
                _homeUiState.update { currentState ->
                    currentState.copy(
                        recentCards = recentCardsList.take(4),
                        isLoadingCards = false
                    )
                }
            }
        }
    }

}
