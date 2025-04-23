package com.example.androiddevelopercodechallenge.domain.useCase.local

import androidx.paging.PagingSource
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalUseCaseImpl @Inject constructor(
    private val localRepository: LocalRepository
) : LocalUseCase {

    override suspend fun insertAllResults(results: List<Result>) {
        localRepository.insertAllResults(results = results)
    }

    override fun getPagingResults(): PagingSource<Int, Result> {
        return localRepository.getPagingResults()
    }

    override fun getAllResults(): Flow<List<Result>> {
        return localRepository.getAllResults()
    }


    override suspend fun addResult(result: Result) {
        localRepository.addResult(result = result)
    }

    override suspend fun updateResult(result: Result) {
        localRepository.updateResult(result = result)
    }

    override suspend fun deleteResultsByEmail(emai: String) {
        localRepository.deleteResultsByEmail(email = emai)
    }

    override suspend fun deleteAll() {
        localRepository.deleteAll()
    }
}