package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.dataSource.local.LocalDataSource
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
): LocalRepository {
    override suspend fun insertAllResults(results: List<Result>) {
        localDataSource.insertAllResults(results = results)
    }

    override suspend fun getAllResults(): Flow<List<Result>> {
        return localDataSource.getAllResults()
    }

    override suspend fun addResult(result: Result) {
        localDataSource.addResult(result = result)
    }

    override suspend fun updateResult(result: Result) {
        localDataSource.updateResult(result = result)
    }

    override suspend fun deleteResultsByEmail(email: String) {
        localDataSource.deleteResultsByEmail(email = email)
    }
}