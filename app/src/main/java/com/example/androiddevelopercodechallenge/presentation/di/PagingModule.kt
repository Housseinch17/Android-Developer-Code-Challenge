package com.example.androiddevelopercodechallenge.presentation.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.paging.EmployeeRemoteMediator
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDAO
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object PagingModule {

    @Provides
    @Singleton
    fun providePager(
        employeeRepository: EmployeeRepository,
        localRepository: LocalRepository,
        database: ResultDataBase,
        resultDAO: ResultDAO,
    ): Pager<Int, Result> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 5, initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = EmployeeRemoteMediator(
                employeeRepository = employeeRepository,
                localRepository = localRepository,
                database = database
            ),
            pagingSourceFactory = {
                resultDAO.getPagingResults()
            }
        )
    }
}