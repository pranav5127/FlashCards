package com.sugardevs.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.dao.ExamDao
import com.sugardevs.flashcards.data.local.dao.TopicDao
import com.sugardevs.flashcards.data.local.model.CardEntity
import com.sugardevs.flashcards.data.local.model.ExamEntity
import com.sugardevs.flashcards.data.local.model.TopicEntity
import com.sugardevs.flashcards.utils.Converters

@Database(entities = [CardEntity::class, TopicEntity::class, ExamEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CardsDatabase : RoomDatabase() {
   abstract fun cardDao(): CardDao
   abstract fun topicDao(): TopicDao
   abstract fun examDao(): ExamDao
}