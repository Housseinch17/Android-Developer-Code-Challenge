package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSource
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
): LocalRepository {
    override fun getAllUsers(): Flow<List<User>> {
        return localDataSource.getAllUsers()
    }

    override suspend fun insertAllUsers(users: List<User>) {
        localDataSource.insertAllUsers(users = users)
    }

    override suspend fun clearAndResetDatabase() {
        localDataSource.clearAndResetDatabase()
    }

    override suspend fun addUser(user: User) {
        localDataSource.addUser(user = user)
    }

    override suspend fun updateUser(user: User) {
        localDataSource.updateUser(user = user)
    }

    override suspend fun deleteUsersById(id: Int) {
        localDataSource.deleteUsersById(id = id)
    }

    override suspend fun getUsersCount(): Int {
        return localDataSource.getUsersCount()
    }
}