package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.dataSource.remote.UsersDataSource
import com.example.androiddevelopercodechallenge.data.model.Users
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val usersDataSource: UsersDataSource
) : UsersRepository {
    override suspend fun getUsers(limit: Int, skip: Int): ApiResponse<Users> {
        return usersDataSource.getUsers(limit = limit, skip = skip)
    }

    override suspend fun getTotalSize(limit: Int, skip: Int): Int {
        return usersDataSource.getTotalSize(limit = limit, skip = skip)
    }
}