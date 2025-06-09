package com.sugardevs.flashcards.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sugardevs.flashcards.data.local.model.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestion(examEntity: ExamEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestions(questions: List<ExamEntity>)

    @Query("SELECT * FROM exam WHERE topicId = :topicId ORDER BY createdAt ASC")
    fun getQuestionsByTopicId(topicId: String): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exam WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): ExamEntity?

    @Update
    suspend fun updateQuestion(examEntity: ExamEntity)

    @Query("SELECT * FROM exam ORDER BY createdAt ASC")
    fun getAllQuestions(): Flow<List<ExamEntity>>

    @Query("DELETE FROM exam WHERE id = :questionId")
    suspend fun deleteQuestionById(questionId: Int)

    @Query("DELETE FROM exam WHERE topicId = :topicId")
    suspend fun deleteQuestionsByTopicId(topicId: String)
}