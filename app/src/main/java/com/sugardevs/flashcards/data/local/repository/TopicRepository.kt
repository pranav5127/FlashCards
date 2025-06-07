package com.sugardevs.flashcards.data.local.repository

import com.sugardevs.flashcards.data.local.dao.TopicDao
import com.sugardevs.flashcards.data.local.model.TopicEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TopicRepository @Inject constructor(private val dao: TopicDao) {

    fun getAllTopics() = dao.getAllTopics()


    suspend fun getAllCardsNewestFirstFlow(): Flow<List<TopicEntity>> = dao.getAllCardsNewestFirstFlow()

    suspend fun insertTopic(topic: TopicEntity) = dao.insertTopic(topic)

    suspend fun ensureTopicExists(topicId: String,topicName: String): TopicEntity {
        var topic = dao.getTopicByName(topicName)
        if(topic == null){
            topic = TopicEntity(id = topicId, name = topicName)
            dao.insertTopic(topic)
        }
        return topic
    }

    suspend fun deleteTopic(topicId: String) = dao.deleteTopicById(topicId)

    suspend fun updateTopic(topic: TopicEntity) = dao.updateTopic(topic)
}