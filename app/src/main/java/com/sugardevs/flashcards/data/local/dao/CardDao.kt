package com.sugardevs.flashcards.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sugardevs.flashcards.data.local.model.CardEntity

@Dao
interface CardDao {

    @Query("SELECT * FROM cards WHERE topicId = :topicId")
    suspend fun getCardsByTopicId(topicId: String): List<CardEntity>

    @Upsert
    suspend fun upsertCards(cards: List<CardEntity>)

}