package com.sugardevs.flashcards.data.auth.di

import com.sugardevs.flashcards.data.auth.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthDbModule {


    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return  AuthRepository()
    }
}