package com.example.androiddevelopercodechallenge.presentation.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.data.paging.UsersRemoteMediator
import com.example.androiddevelopercodechallenge.data.roomDB.UserDaAO
import com.example.androiddevelopercodechallenge.data.roomDB.UserDataBase
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import com.example.androiddevelopercodechallenge.domain.repository.UsersRepository
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
        usersRepository: UsersRepository,
        localRepository: LocalRepository,
        database: UserDataBase,
        userDaAO: UserDaAO,
    ): Pager<Int, User> {
        return Pager(
            config = PagingConfig(
                pageSize = 20, prefetchDistance = 4, initialLoadSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = UsersRemoteMediator(
                usersRepository = usersRepository,
                localRepository = localRepository,
                database = database
            ),
            pagingSourceFactory = {
                userDaAO.getPagingUsers()
            }
        )
    }
}