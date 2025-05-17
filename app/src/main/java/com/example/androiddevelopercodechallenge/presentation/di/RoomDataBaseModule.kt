package com.example.androiddevelopercodechallenge.presentation.di

import android.app.Application
import androidx.room.Room
import com.example.androiddevelopercodechallenge.data.roomDB.UserDaAO
import com.example.androiddevelopercodechallenge.data.roomDB.UserDataBase
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
    fun provideUserDataBase(app: Application): UserDataBase {
        return Room.databaseBuilder(app, UserDataBase::class.java, "CodeChallenge")
            .build()
    }

    @Singleton
    @Provides
    fun provideResultDAO(userDataBase: UserDataBase): UserDaAO {
        return userDataBase.userDAO()
    }
}