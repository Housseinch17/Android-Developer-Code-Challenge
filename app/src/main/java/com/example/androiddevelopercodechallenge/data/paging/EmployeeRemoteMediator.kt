package com.example.androiddevelopercodechallenge.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EmployeeRemoteMediator
@Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val localRepository: LocalRepository,
    private val database: ResultDataBase,
) : RemoteMediator<Int, Result>() {

    val maxPage = 10
    var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Result>
    ): MediatorResult {
        return try {
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("EmployeeMediator","refresh")
                    currentPage = 1
                    1
                }
                LoadType.PREPEND -> {
                    Log.d("EmployeeMediator","prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    Log.d("EmployeeMediator","append")
                    if (currentPage > maxPage) {
                        Log.d("EmployeeMediator","currentPage = maxPage $currentPage")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    val nextPage = currentPage
                    nextPage
                }
            }

            Log.d("EmployeeMediator","pageToLoad: $pageToLoad")

            val response = employeeRepository.getEmployees(page = pageToLoad) as ApiResponse.Success
            val results = response.data.results

            Log.d("EmployeeMediator","results: ${results.size}")
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localRepository.deleteAll()
                }
                //currentPage increment here because when no internet connection
                //if the user clicks paging.retry() it will increment currentPage without loading data
                //but here only increment when data loaded and saved in local db
                currentPage++

                //insert data into room db
                localRepository.insertAllResults(results)
            }

            val endOfPaginationReached = pageToLoad >= maxPage || results.isEmpty()

            Log.d("EmployeeMediator","endOfPaginationReached: $endOfPaginationReached")

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
