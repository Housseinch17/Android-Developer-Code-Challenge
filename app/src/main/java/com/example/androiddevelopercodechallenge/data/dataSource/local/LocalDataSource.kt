package com.example.androiddevelopercodechallenge.data.dataSource.local

import com.example.androiddevelopercodechallenge.data.model.Id
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertAllResults(results: List<Result>)
    //live update
    suspend fun getAllResults(): Flow<List<Result>>
    suspend fun addResult(result: Result)
    suspend fun updateResult(result: Result): Int
    suspend fun deleteResultsByEmail(email: String): Int
}