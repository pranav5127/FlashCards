package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.repository.CardsDbRepository
import com.sugardevs.flashcards.ui.model.HomeUiState
import com.sugardevs.flashcards.utils.SortMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val cardsRepository: CardsDbRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadRecentCards()
    }

    private fun loadRecentCards() {
        viewModelScope.launch {
            val topics = cardsRepository.getAllCardsSorted(sortMode = SortMode.NEWEST_FIRST)

            val sortedCards = cardsRepository.getAllCardsSorted(SortMode.NEWEST_FIRST)
            _homeUiState.value = _homeUiState.value.copy(
                recentCards = sortedCards.map { it.content },
                subjects = topics.map {it.topicId}


            )
        }
    }
}