package com.sugardevs.flashcards.data.local.di

import android.content.Context
import androidx.room.Room
import com.sugardevs.flashcards.data.local.CardsDatabase
import com.sugardevs.flashcards.data.local.dao.CardDao
import com.sugardevs.flashcards.data.local.dao.ExamDao
import com.sugardevs.flashcards.data.local.dao.TopicDao
import com.sugardevs.flashcards.data.local.repository.CardsRepository
import com.sugardevs.flashcards.data.local.repository.ExamRepository
import com.sugardevs.flashcards.data.local.repository.TopicRepository
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
            ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCardDao(database: CardsDatabase) = database.cardDao()

    @Provides
    @Singleton
    fun provideTopicDao(database: CardsDatabase) = database.topicDao()

    @Provides
    @Singleton
    fun provideExamDao(database: CardsDatabase) = database.examDao()

    @Provides
    @Singleton
    fun provideCardsDbRepository(dao: CardDao) = CardsRepository(dao)

    @Provides
    @Singleton
    fun provideTopicRepository(dao: TopicDao) = TopicRepository(dao)

    @Provides
    @Singleton
    fun provideExamRepository(dao: ExamDao) = ExamRepository(dao)

}