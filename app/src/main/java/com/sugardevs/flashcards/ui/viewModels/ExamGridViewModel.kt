package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import com.sugardevs.flashcards.data.local.repository.TopicRepository
import com.sugardevs.flashcards.ui.model.GridUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExamGridViewModel @Inject constructor(
    val examRepository: TopicRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(GridUiState())
    val uiState: StateFlow<GridUiState> = _uiState.asStateFlow()


}