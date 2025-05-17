package com.example.androiddevelopercodechallenge.domain.repository

import com.example.androiddevelopercodechallenge.data.model.Users
import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface UsersRepository {
    suspend fun getUsers(limit: Int, skip: Int): ApiResponse<Users>
    suspend fun getTotalSize(limit: Int, skip: Int): Int
}