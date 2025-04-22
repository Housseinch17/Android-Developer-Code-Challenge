package com.example.androiddevelopercodechallenge.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDAO
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDataBaseModule {

    @Singleton
    @Provides
    fun provideResultDataBase(app: Application): ResultDataBase {
        return Room.databaseBuilder(app, ResultDataBase::class.java, "CodeChallenge")
            .build()
    }

    @Singleton
    @Provides
    fun provideResultDAO(resultDataBase: ResultDataBase): ResultDAO {
        return resultDataBase.resultDAO()
    }
}