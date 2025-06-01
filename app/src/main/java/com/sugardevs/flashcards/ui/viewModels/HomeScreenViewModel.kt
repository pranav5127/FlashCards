package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.CardsDbRepository
import com.sugardevs.flashcards.data.local.repository.TopicRepository
import com.sugardevs.flashcards.ui.model.HomeUiState
import com.sugardevs.flashcards.utils.SortMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val topicRepository: TopicRepository,
    private val cardsDbRepository: CardsDbRepository
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
            // Update loading state
            _homeUiState.update { it.copy(isLoadingTopics = true, errorMessage = null) }
            try {

                topicRepository.getAllTopics().collectLatest { topicsList ->
                    _homeUiState.update { currentState ->
                        currentState.copy(
                            subjects = topicsList,
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
            _homeUiState.update { it.copy(isLoadingCards = true, errorMessage = null) }
            try {

                val recentCardsList = cardsDbRepository.getAllCardsSorted(SortMode.NEWEST_FIRST)

                _homeUiState.update { currentState ->
                    currentState.copy(
                        recentCards = recentCardsList
                            .map { it.content }
                            .take(10),
                        isLoadingCards = false
                    )
                }
            } catch (e: Exception) {
                _homeUiState.update { currentState ->
                    currentState.copy(
                        isLoadingCards = false,
                        errorMessage = "Failed to load recent cards: ${e.message}"
                    )
                }
            }
        }
    }
}