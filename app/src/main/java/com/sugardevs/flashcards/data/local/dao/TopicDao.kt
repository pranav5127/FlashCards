package com.sugardevs.flashcards.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sugardevs.flashcards.data.local.model.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTopic(topicDao: TopicEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTopics(topics: List<TopicEntity>)

    @Update
    suspend fun updateTopic(topic: TopicEntity)

    @Query("SELECT * FROM topics ORDER BY name ASC")
    fun getAllTopics(): Flow<List<TopicEntity>>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: String): TopicEntity?

    @Query("SELECT * FROM topics ORDER BY createdAt DESC")
     fun getAllCardsNewestFirstFlow(): Flow<List<TopicEntity>>

    @Query("DELETE FROM topics WHERE id = :topicId")
    suspend fun deleteTopicById(topicId: String)

    @Query("SELECT * FROM topics WHERE name = :name LIMIT 1")
    suspend fun getTopicByName(name: String): TopicEntity?

}