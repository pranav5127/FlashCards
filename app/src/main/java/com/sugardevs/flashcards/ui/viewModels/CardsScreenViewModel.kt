package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.ui.model.CardsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CardsScreenViewModel : ViewModel() {
    private val _cardsUiState = MutableStateFlow(CardsUiState())
    val cardsUiState: StateFlow<CardsUiState> = _cardsUiState.asStateFlow()

    init {
        loadCards()
    }

    private fun loadCards() {
        viewModelScope.launch {

            try {
                val fetchedCards = listOf(
                    "The cards will look like this.",
                    "Explanation with Example",
                    "Real-world Application",
                    "Card4",
                    "Card5",
                    "Card6"
                )
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
            } else {
                it
            }
        }
    }

    fun prevCard() {
        _cardsUiState.value = _cardsUiState.value.let {
            if (it.currentCardIndex > 0) {
                it.copy(currentCardIndex = it.currentCardIndex - 1)
            } else {
                it
            }
        }
    }
}
