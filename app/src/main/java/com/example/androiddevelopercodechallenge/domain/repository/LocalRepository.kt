package com.example.androiddevelopercodechallenge.domain.repository

import com.example.androiddevelopercodechallenge.data.model.User
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun insertAllUsers(users: List<User>)
    suspend fun clearAndResetDatabase()
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUsersById(id: Int)
    suspend fun getUsersCount(): Int
}