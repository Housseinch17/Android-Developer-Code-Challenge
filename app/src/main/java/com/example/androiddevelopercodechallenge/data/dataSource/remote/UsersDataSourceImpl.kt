package com.example.androiddevelopercodechallenge.data.dataSource.remote

import android.util.Log
import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.example.androiddevelopercodechallenge.data.model.Users
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.presentation.di.AppModule.IoDispatcher
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : UsersDataSource {
    override suspend fun getUsers(limit: Int, skip: Int): ApiResponse<Users> = withContext(coroutineDispatcher) {
        try {
            val users = apiService.getUsers(limit = limit, skip = skip)
            Log.d("MyTag", "UserDataSourceImpl: getUsers(): success: $users")
            return@withContext ApiResponse.Success(data = users)
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            Log.e("MyTag", "UserDataSourceImpl: getUsers(): error: $e")
            return@withContext ApiResponse.Error(message = e.message.toString())
        }
    }

    override suspend fun getTotalSize(limit: Int, skip: Int): Int = withContext(coroutineDispatcher){
        try {
            val users = apiService.getUsers(limit = limit, skip = skip)
            val totalSize = users.total
            Log.d("MyTag", "UserDataSourceImpl: getTotalSize(): success: $totalSize")
            return@withContext totalSize
        } catch (e: Exception) {
            if(e is CancellationException){
                throw e
            }
            Log.e("MyTag", "UserDataSourceImpl: getTotalSize(): error: $e")
            return@withContext 0
        }
    }
}