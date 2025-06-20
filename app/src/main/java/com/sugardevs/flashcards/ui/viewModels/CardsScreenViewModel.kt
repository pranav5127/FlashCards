package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.CardsRepository
import com.sugardevs.flashcards.ui.model.CardsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsScreenViewModel @Inject constructor(
    private val repository: CardsRepository
) : ViewModel() {

    private val _cardsUiState = MutableStateFlow(CardsUiState())
    val cardsUiState: StateFlow<CardsUiState> = _cardsUiState.asStateFlow()

    private var currentTopicId: String? = null

    private val _topicSearchResults = MutableStateFlow<List<String>>(emptyList())
    val topicSearchResults: StateFlow<List<String>> = _topicSearchResults.asStateFlow()

    fun loadCards(topicId: String) {
        currentTopicId = topicId
        viewModelScope.launch {
            _cardsUiState.value = _cardsUiState.value.copy(isLoading = true)
            try {
                val fetchedCards = repository.getCardsByTopicId(topicId)
                _cardsUiState.value = CardsUiState(
                    isLoading = false,
                    cards = fetchedCards,
                    currentCardIndex = 0,
                    error = null
                )
            } catch (e: Exception) {
                _cardsUiState.value = CardsUiState(
                    isLoading = false,
                    cards = emptyList(),
                    currentCardIndex = -1,
                    error = e.message
                )
            }
        }
    }

    fun nextCard() {
        _cardsUiState.value = _cardsUiState.value.let {
            if (it.currentCardIndex < it.cards.lastIndex) {
                it.copy(currentCardIndex = it.currentCardIndex + 1)
            } else it
        }
    }

    fun prevCard() {
        _cardsUiState.value = _cardsUiState.value.let {
            if (it.currentCardIndex > 0) {
                it.copy(currentCardIndex = it.currentCardIndex - 1)
            } else it
        }
    }

    fun searchTopics(query: String) {
        viewModelScope.launch {
            _topicSearchResults.value = repository.searchTopics(query)
        }
    }
}
