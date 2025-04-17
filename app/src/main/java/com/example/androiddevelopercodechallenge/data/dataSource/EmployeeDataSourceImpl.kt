package com.example.androiddevelopercodechallenge.data.dataSource

import android.util.Log
import com.example.androiddevelopercodechallenge.data.api.ApiService
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class EmployeeDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    @Named("Dispatchers_IO") private val coroutineDispatcher: CoroutineDispatcher
) : EmployeeDataSource {
    override suspend fun getEmployees(page: Int): ApiResponse = withContext(coroutineDispatcher) {
        try {
            val employee = apiService.getEmployees(page = page)
            Log.d("MyTag", "EmployeeDataSourceImpl: getEmployees(): success: $employee")
            return@withContext ApiResponse.Success(employee = employee)
        } catch (e: Exception) {
            Log.d("MyTag", "EmployeeDataSourceImpl: getEmployees(): error: ${e.message}")
            return@withContext ApiResponse.Error(message = e.message.toString())
        }
    }
}