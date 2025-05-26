package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import com.sugardevs.flashcards.data.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()
}