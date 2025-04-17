package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.example.androiddevelopercodechallenge.data.dataSource.EmployeeDataSource
import com.example.androiddevelopercodechallenge.data.dataSource.EmployeeDataSourceImpl
import com.example.androiddevelopercodechallenge.data.dataSource.EmployeeRepositoryImpl
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
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
        apiService: ApiService,
        @Named("Dispatchers_IO") coroutineDispatcher: CoroutineDispatcher
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
}