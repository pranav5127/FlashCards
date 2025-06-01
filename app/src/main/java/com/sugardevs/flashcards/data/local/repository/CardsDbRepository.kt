package com.sugardevs.flashcards.data.local.repository

import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.model.CardEntity
import com.sugardevs.flashcards.utils.SortMode
import javax.inject.Inject

class CardsDbRepository @Inject constructor(private val dao: CardDao) {

    suspend fun getCardsByTopicId(topicId: String): List<String> {
        return dao.getCardsByTopicId(topicId).map { it.content }
    }

    suspend fun getAllCardsSorted(sortMode: SortMode): List<CardEntity> {
        return when (sortMode) {
            SortMode.NEWEST_FIRST -> dao.getAllCardsNewestFirst()
            SortMode.OLDEST_FIRST -> dao.getAllCardsOldestFirst()
            SortMode.ALPHABETICAL -> dao.getAllCardsAlphabetical()
            SortMode.REVERSE_ALPHABETICAL -> dao.getAllCardsReverseAlphabetical()
        }
    }


    suspend fun saveCards(topicId: String, points: List<String>) {
        val cardEntities = points.map { point ->
            CardEntity(topicId = topicId, content = point)
        }
        dao.upsertCards(cardEntities)

    }
    suspend fun deleteCardsByTopicId(topicId: String) {
        dao.deleteCardsByTopicId(topicId)
    }

}