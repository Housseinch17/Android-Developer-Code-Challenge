package com.example.androiddevelopercodechallenge.domain.useCase.local

import androidx.paging.PagingSource
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow

interface LocalUseCase {
    suspend fun insertAllResults(results: List<Result>)
//    fun getPagingResults(): PagingSource<Int, Result>
    suspend fun addResult(result: Result)
    suspend fun updateResult(result: Result)
    suspend fun deleteResultsByEmail(email: String)
    suspend fun deleteAll()
    //live update
    suspend fun getAllResults(): Flow<List<Result>>
}