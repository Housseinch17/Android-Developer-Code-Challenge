package com.example.androiddevelopercodechallenge.data.dataSource.local

import android.util.Log
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class LocalDataSourceImpl @Inject constructor(
    @Named("DispatchersIO")
    private val coroutineDispatcher: CoroutineDispatcher,
    private val resultDAO: ResultDAO,
) : LocalDataSource {
    override suspend fun insertAllResults(results: List<Result>): Unit =
        withContext(coroutineDispatcher) {
            try {
                resultDAO.insertAllResults(results = results)
                Log.d("MyTag", "LocalDataSourceImpl: insertAllResults(): successfully completed")
            } catch (e: Exception) {
                Log.e("MyTag", "LocalDataSourceImpl: insertAllResults(): failed ${e.message}")
            }
        }

    override suspend fun getAllResults(): Flow<List<Result>> = withContext(coroutineDispatcher) {
        try {
            Log.d("MyTag", "LocalDataSourceImpl: getAllResults(): successfully completed")
            resultDAO.getAllResults()
        } catch (e: Exception) {
            Log.e("MyTag", "LocalDataSourceImpl: getAllResults(): failed ${e.message}")
            emptyFlow()
        }
    }

    override suspend fun addResult(result: Result): Unit = withContext(coroutineDispatcher) {
        try {
            resultDAO.addResult(result = result)
            Log.d("MyTag", "LocalDataSourceImpl: addResult(): successfully completed")
        } catch (e: Exception) {
            Log.e("MyTag", "LocalDataSourceImpl: addResult(): failed ${e.message}")
        }
    }

    override suspend fun updateResult(result: Result) = withContext(coroutineDispatcher) {
        try {
            resultDAO.updateResult(result = result)
            Log.d("MyTag", "LocalDataSourceImpl: updateResult(): successfully completed")
        } catch (e: Exception) {
            Log.e("MyTag", "LocalDataSourceImpl: updateResult(): failed ${e.message}")
        }
    }

    override suspend fun deleteResultsByEmail(email: String) = withContext(coroutineDispatcher) {
        try {
            resultDAO.deleteResultsByEmail(email = email)
            Log.d("MyTag", "LocalDataSourceImpl: deleteResultsById(): successfully completed: email:$email")
        } catch (e: Exception) {
            Log.e("MyTag", "LocalDataSourceImpl: deleteResultsById(): failed ${e.message}")
        }
    }
}