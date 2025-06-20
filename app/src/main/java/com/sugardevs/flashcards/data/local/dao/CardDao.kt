package com.sugardevs.flashcards.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sugardevs.flashcards.data.local.model.CardEntity

@Dao
interface CardDao {

    @Query("SELECT * FROM cards WHERE topicId = :topicId")
    suspend fun getCardsByTopicId(topicId: String): List<CardEntity>

    @Query("SELECT DISTINCT topicId FROM cards WHERE topicId LIKE '%' || :query || '%'")
    suspend fun searchTopics(query: String): List<String>

    @Query("SELECT * FROM cards ORDER BY createdAt DESC")
    suspend fun getAllCardsNewestFirst(): List<CardEntity>

    @Query("SELECT * FROM cards ORDER BY createdAt ASC")
    suspend fun getAllCardsOldestFirst(): List<CardEntity>

    @Query("SELECT * FROM cards ORDER BY content ASC")
    suspend fun getAllCardsAlphabetical(): List<CardEntity>

    @Query("SELECT * FROM cards ORDER BY content DESC")
    suspend fun getAllCardsReverseAlphabetical(): List<CardEntity>

    @Upsert
    suspend fun upsertCards(cards: List<CardEntity>)

    @Query("DELETE FROM cards WHERE topicId = :topicId")
    suspend fun deleteCardsByTopicId(topicId: String)
}
