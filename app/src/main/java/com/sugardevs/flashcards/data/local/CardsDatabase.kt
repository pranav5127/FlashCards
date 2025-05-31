package com.sugardevs.flashcards.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.model.CardEntity

@Database(entities = [CardEntity::class], version = 1, exportSchema = false)
abstract class CardsDatabase : RoomDatabase() {
   abstract fun cardDao(): CardDao
}