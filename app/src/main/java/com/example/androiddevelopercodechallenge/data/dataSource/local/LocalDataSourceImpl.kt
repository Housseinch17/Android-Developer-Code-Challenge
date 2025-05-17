package com.example.androiddevelopercodechallenge.data.dataSource.local

import android.util.Log
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.data.roomDB.UserDaAO
import com.example.androiddevelopercodechallenge.presentation.di.AppModule.IoDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    @IoDispatcher
    private val coroutineDispatcher: CoroutineDispatcher,
    private val userDaAO: UserDaAO,
) : LocalDataSource {
    override fun getAllUsers(): Flow<List<User>> {
        return try {
            userDaAO.getAllUsers()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("MyTag", "LocalDataSourceImpl: insertAllUsers(): failed ${e.message}")
            emptyFlow()
        }
    }

    override suspend fun insertAllUsers(users: List<User>): Unit =
        withContext(coroutineDispatcher) {
            try {
                userDaAO.insertAllUsers(users = users)
                Log.d(
                    "MyTag",
                    "LocalDataSourceImpl: insertAllUsers(): successfully completed users: $users"
                )
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Log.e("MyTag", "LocalDataSourceImpl: insertAllUsers(): failed ${e.message}")
            }
        }

    override suspend fun clearAndResetDatabase(): Unit = withContext(coroutineDispatcher) {
        try {
            userDaAO.deleteAll()
            //reset UID
            userDaAO.resetUid()
            Log.d("MyTag", "LocalDataSourceImpl: clearAndResetDatabase(): successfully completed")
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("MyTag", "LocalDataSourceImpl: clearAndResetDatabase(): failed ${e.message}")
        }
    }

    override suspend fun addUser(user: User): Unit = withContext(coroutineDispatcher) {
        try {
            userDaAO.addUser(user = user)
            Log.d("MyTag", "LocalDataSourceImpl: addUser(): successfully completed")
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("MyTag", "LocalDataSourceImpl: addUser(): failed ${e.message}")
        }
    }

    override suspend fun updateUser(user: User): Unit = withContext(coroutineDispatcher) {
        try {
            userDaAO.updateUser(user = user)
            Log.d("MyTag", "LocalDataSourceImpl: updateUser(): successfully completed")
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("MyTag", "LocalDataSourceImpl: updateUser(): failed ${e.message}")
        }
    }

    override suspend fun getUsersCount(): Int = withContext(coroutineDispatcher) {
        return@withContext userDaAO.getUsersCount()
    }

    override suspend fun deleteUsersById(id: Int): Unit =
        withContext(coroutineDispatcher) {
            try {
                userDaAO.deleteUsersById(id = id)
                Log.d(
                    "MyTag",
                    "LocalDataSourceImpl: deleteUsersById(): successfully completed: id:$id"
                )
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                Log.e("MyTag", "LocalDataSourceImpl: deleteUsersById(): failed ${e.message}")
            }
        }
}