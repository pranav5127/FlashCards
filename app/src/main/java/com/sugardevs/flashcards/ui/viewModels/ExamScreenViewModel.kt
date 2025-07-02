package com.sugardevs.flashcards.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sugardevs.flashcards.data.local.model.ExamEntity
import com.sugardevs.flashcards.data.local.repository.ExamRepository
import com.sugardevs.flashcards.ui.model.AnswerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private val _scores = MutableStateFlow<Map<String, Int>>(emptyMap())
    val scores: StateFlow<Map<String, Int>> = _scores.asStateFlow()

    var showDialog by mutableStateOf(false)
        private set

    var finalScoreText by mutableStateOf("")
        private set

    private val _answerResults = mutableStateOf<List<AnswerResult>>(emptyList())
    val answerResults: List<AnswerResult> get() = _answerResults.value

    fun submitExam(selectedAnswers: Map<Int, String>, topicId: String) {
        showScore(selectedAnswers, topicId)
        val score = _scores.value[topicId] ?: 0
        val total = _questionsByTopic.value.size
        finalScoreText = "Your score: $score / $total"
        showDialog = true
    }

    fun dismissDialog() {
        showDialog = false
    }

    fun showScore(selectedAnswers: Map<Int, String>, topicId: String) {
        val questionsForTopic = _questionsByTopic.value
        var correctCount = 0
        val results = mutableListOf<AnswerResult>()

        questionsForTopic.forEach { question ->
            val selectedAnswer = selectedAnswers[question.questionId]
            val correctAnswer = question.answer
            val isCorrect = selectedAnswer == correctAnswer
            if (isCorrect) correctCount++

            results.add(
                AnswerResult(
                    question = question.question,
                    selectedAnswer = selectedAnswer,
                    correctAnswer = correctAnswer,
                    isCorrect = isCorrect
                )
            )
        }

        _answerResults.value = results
        _scores.value = _scores.value + (topicId to correctCount)
    }

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
