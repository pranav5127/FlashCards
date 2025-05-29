package com.sugardevs.flashcards.data.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sugardevs.flashcards.data.network.api.ApiService
import com.sugardevs.flashcards.data.network.repository.CardsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://192.168.1.8:9999/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideCardRepository(apiService: ApiService): CardsRepository =
        CardsRepository(apiService)
}