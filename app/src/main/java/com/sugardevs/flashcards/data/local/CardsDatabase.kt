package com.sugardevs.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.dao.TopicDao
import com.sugardevs.flashcards.data.local.model.CardEntity
import com.sugardevs.flashcards.data.local.model.TopicEntity

@Database(entities = [CardEntity::class, TopicEntity::class], version = 2, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {
   abstract fun cardDao(): CardDao
   abstract fun topicDao(): TopicDao
}