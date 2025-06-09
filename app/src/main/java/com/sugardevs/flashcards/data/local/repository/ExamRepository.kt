package com.sugardevs.flashcards.data.local.repository

import com.sugardevs.flashcards.data.local.dao.ExamDao
import com.sugardevs.flashcards.data.local.model.ExamEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExamRepository @Inject constructor(private val dao: ExamDao) {

    suspend fun insertQuestion(examEntity: ExamEntity) {
        dao.insertQuestion(examEntity)
    }

    suspend fun insertQuestions(questions: List<ExamEntity>) {
        dao.insertQuestions(questions)
    }

    fun getQuestionsByTopicId(topicId: String): Flow<List<ExamEntity>> {
        return dao.getQuestionsByTopicId(topicId)
    }

    suspend fun getQuestionById(questionId: Int): ExamEntity? {
        return dao.getQuestionById(questionId)
    }

    fun getAllQuestions(): Flow<List<ExamEntity>> {
        return dao.getAllQuestions()
    }

    suspend fun updateQuestion(examEntity: ExamEntity) {
        dao.updateQuestion(examEntity)
    }

    suspend fun deleteQuestionById(questionId: Int) {
        dao.deleteQuestionById(questionId)
    }

    suspend fun deleteQuestionsByTopicId(topicId: String) {
        dao.deleteQuestionsByTopicId(topicId)
    }
}