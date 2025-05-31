package com.sugardevs.flashcards.data.local.repository

import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.model.CardEntity
import javax.inject.Inject

class CardsDbRepository @Inject constructor(private val dao: CardDao) {

    suspend fun getCardsByTopicId(topicId: String): List<String> {
        return dao.getCardsByTopicId(topicId).map { it.content }
    }

    suspend fun saveCards(topicId: String, points: List<String>) {
        val cardEntities = points.map { point ->
            CardEntity(topicId = topicId, content = point)
        }
        dao.upsertCards(cardEntities)

    }
}