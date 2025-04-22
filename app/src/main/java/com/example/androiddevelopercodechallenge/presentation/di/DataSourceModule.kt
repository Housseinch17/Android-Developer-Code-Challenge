package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.example.androiddevelopercodechallenge.data.dataSource.remote.EmployeeDataSource
import com.example.androiddevelopercodechallenge.data.dataSource.remote.EmployeeDataSourceImpl
import com.example.androiddevelopercodechallenge.data.dataSource.EmployeeRepositoryImpl
import com.example.androiddevelopercodechallenge.data.dataSource.LocalRepositoryImpl
import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSource
import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSourceImpl
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDAO
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    //scoped to application lifecycle
    //single instance created across application
    @Singleton
    @Provides
    fun provideEmployeeDataSource(
        @Named("Dispatchers_IO") coroutineDispatcher: CoroutineDispatcher,
        apiService: ApiService
    ): EmployeeDataSource {
        return EmployeeDataSourceImpl(
            apiService = apiService,
            coroutineDispatcher = coroutineDispatcher
        )
    }

    @Singleton
    @Provides
    fun provideEmployeeRepository(
        employeeDataSource: EmployeeDataSource
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(
            employeeDataSource = employeeDataSource
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @Named("Dispatchers_IO") coroutineDispatcher: CoroutineDispatcher,
        resultDAO: ResultDAO,
    ): LocalDataSource {
        return LocalDataSourceImpl(
            coroutineDispatcher = coroutineDispatcher,
            resultDAO = resultDAO
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