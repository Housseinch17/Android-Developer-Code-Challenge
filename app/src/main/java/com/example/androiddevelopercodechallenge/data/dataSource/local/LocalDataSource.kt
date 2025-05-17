package com.example.androiddevelopercodechallenge.data.dataSource.local

import com.example.androiddevelopercodechallenge.data.model.User
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllUsers(): Flow<List<User>>
    suspend fun insertAllUsers(users: List<User>)
    suspend fun clearAndResetDatabase()
    suspend fun addUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun getUsersCount(): Int
    suspend fun deleteUsersById(id: Int)
}