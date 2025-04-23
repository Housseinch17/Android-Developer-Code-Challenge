package com.example.androiddevelopercodechallenge.domain.repository

import androidx.paging.PagingSource
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun insertAllResults(results: List<Result>)
    //live update
    fun getAllResults(): Flow<List<Result>>
    fun getPagingResults(): PagingSource<Int, Result>
    suspend fun addResult(result: Result)
    suspend fun updateResult(result: Result)
    suspend fun deleteResultsByEmail(email: String)
    suspend fun deleteAll()
    suspend fun getResultsCount(): Int
}