package com.example.androiddevelopercodechallenge.domain.repository

import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertAllResults(results: List<Result>)
    //live update
    suspend fun getAllResults(): Flow<List<Result>>
    suspend fun addResult(result: Result)
    suspend fun updateResult(result: Result)
    suspend fun deleteResultsByEmail(email: String)
}