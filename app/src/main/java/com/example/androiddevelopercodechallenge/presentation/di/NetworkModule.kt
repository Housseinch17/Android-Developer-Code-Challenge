package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://randomuser.me/"

    //slow internet can take time to connect to server or to receive response which leading to error
    val client = OkHttpClient.Builder()
        //time that can wait for server connection
        .connectTimeout(20, TimeUnit.SECONDS)
        //time that can wait for server response
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}