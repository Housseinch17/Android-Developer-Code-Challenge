package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.example.androiddevelopercodechallenge.data.dataSource.LocalRepositoryImpl
import com.example.androiddevelopercodechallenge.data.dataSource.UsersRepositoryImpl
import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSource
import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSourceImpl
import com.example.androiddevelopercodechallenge.data.dataSource.remote.UsersDataSource
import com.example.androiddevelopercodechallenge.data.dataSource.remote.UsersDataSourceImpl
import com.example.androiddevelopercodechallenge.data.roomDB.UserDaAO
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import com.example.androiddevelopercodechallenge.domain.repository.UsersRepository
import com.example.androiddevelopercodechallenge.presentation.di.AppModule.IoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    //scoped to application lifecycle
    //single instance created across application
    @Singleton
    @Provides
    fun provideUsersDataSource(
        @IoDispatcher  coroutineDispatcher: CoroutineDispatcher,
        apiService: ApiService
    ): UsersDataSource {
        return UsersDataSourceImpl(
            apiService = apiService,
            coroutineDispatcher = coroutineDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        usersDataSource: UsersDataSource
    ): UsersRepository {
        return UsersRepositoryImpl(
            usersDataSource = usersDataSource
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @IoDispatcher  coroutineDispatcher: CoroutineDispatcher,
        userDaAO: UserDaAO,
    ): LocalDataSource {
        return LocalDataSourceImpl(
            coroutineDispatcher = coroutineDispatcher,
            userDaAO = userDaAO
        )
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        localDataSource: LocalDataSource
    ): LocalRepository {
        return LocalRepositoryImpl(localDataSource = localDataSource)
    }
}