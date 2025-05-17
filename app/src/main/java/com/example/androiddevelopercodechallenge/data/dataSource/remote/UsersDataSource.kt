package com.example.androiddevelopercodechallenge.data.dataSource.remote

import com.example.androiddevelopercodechallenge.data.model.Users
import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface UsersDataSource {
    suspend fun getUsers(limit: Int, skip: Int): ApiResponse<Users>
    suspend fun getTotalSize(limit: Int, skip: Int): Int
}