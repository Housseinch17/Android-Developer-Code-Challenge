package com.example.androiddevelopercodechallenge.presentation.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.paging.EmployeeRemoteMediator
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDAO
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("Dispatchers_IO")
    fun provideDispatchersIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providePager(
        employeeUseCase: EmployeeUseCase,
        localUseCase: LocalUseCase,
        database: ResultDataBase,
        resultDAO: ResultDAO,
    ): Pager<Int, Result> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 5, initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = EmployeeRemoteMediator(
                employeeUseCase = employeeUseCase,
                localUseCase = localUseCase,
                database = database
            ),
            pagingSourceFactory = {
                resultDAO.getPagingResults()
            }
        )
    }

}