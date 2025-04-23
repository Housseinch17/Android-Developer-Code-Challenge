package com.example.androiddevelopercodechallenge.data.dataSource

import androidx.paging.PagingSource
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

    override fun getPagingResults(): PagingSource<Int, Result> {
       return localDataSource.getPagingResults()
    }

    override fun getAllResults(): Flow<List<Result>> {
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

    override suspend fun deleteAll() {
        localDataSource.deleteAll()
    }
}