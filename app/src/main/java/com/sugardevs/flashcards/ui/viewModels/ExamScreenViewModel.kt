package com.sugardevs.flashcards.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.model.ExamEntity
import com.sugardevs.flashcards.data.local.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamScreenViewModel @Inject constructor(
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _currentQuestion = MutableStateFlow<ExamEntity?>(null)
    val currentQuestion: StateFlow<ExamEntity?> = _currentQuestion.asStateFlow()

    private val _questionsByTopic = MutableStateFlow<List<ExamEntity>>(emptyList())
    val questionsByTopic: StateFlow<List<ExamEntity>> = _questionsByTopic.asStateFlow()

    private val _allQuestions = MutableStateFlow<List<ExamEntity>>(emptyList())
    val allQuestions: StateFlow<List<ExamEntity>> = _allQuestions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun insertQuestion(examEntity: ExamEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                examRepository.insertQuestion(examEntity)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error inserting question: ${e.message}"
            }
        }
    }

       fun loadQuestionsByTopicId(topicId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            examRepository.getQuestionsByTopicId(topicId)
                .catch { e ->
                    _isLoading.value = false
                    _errorMessage.value = "Error loading questions for topic $topicId: ${e.message}"
                }
                .collect { questions ->
                    _isLoading.value = false
                    _questionsByTopic.value = questions
                }
        }
    }

    fun loadQuestionById(questionId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val question = examRepository.getQuestionById(questionId)
                _currentQuestion.value = question
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error loading question $questionId: ${e.message}"
            }
        }
    }

    fun loadAllQuestions() {
        viewModelScope.launch {
            _isLoading.value = true
            examRepository.getAllQuestions()
                .catch { e ->
                    _isLoading.value = false
                    _errorMessage.value = "Error loading all questions: ${e.message}"
                }
                .collect { questions ->
                    _isLoading.value = false
                    _allQuestions.value = questions
                }
        }
    }

    fun updateQuestion(examEntity: ExamEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                examRepository.updateQuestion(examEntity)
                loadQuestionById(examEntity.id)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error updating question: ${e.message}"
            }
        }
    }

    fun deleteQuestionById(questionId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                examRepository.deleteQuestionById(questionId)
                if (_currentQuestion.value?.id == questionId) {
                    _currentQuestion.value = null
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting question $questionId: ${e.message}"
            }
        }
    }

    fun deleteQuestionsByTopicId(topicId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                examRepository.deleteQuestionsByTopicId(topicId)
                if (_questionsByTopic.value.any { it.topicId == topicId }) {
                    _questionsByTopic.value = emptyList()
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error deleting questions for topic $topicId: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}