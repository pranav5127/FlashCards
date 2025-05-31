package com.sugardevs.flashcards.data.local.di

import android.content.Context
import androidx.room.Room
import com.sugardevs.flashcards.data.local.CardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CardsDbModule {

    @Provides
    @Singleton
    fun provideCardsDatabase(@ApplicationContext context: Context): CardsDatabase {
        return Room.databaseBuilder(
            context,
            CardsDatabase::class.java,
            "cards_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCardDao(database: CardsDatabase) = database.cardDao()

}