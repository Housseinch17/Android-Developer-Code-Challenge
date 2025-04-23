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
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EmployeeRemoteMediator
@Inject constructor(
    private val employeeUseCase: EmployeeUseCase,
    private val localUseCase: LocalUseCase,
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
                    Log.d("EmployeeMediator","Refresh")
                    currentPage
                }
                LoadType.PREPEND -> {
                    Log.d("EmployeeMediator","PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    //lastItem is always most recommended
                    //but in this example api can return null Result() even if not fetched all
                    //val lastItem = state.lastItemOrNull()
                    Log.d("EmployeeMediator","APPEND")

                    if (currentPage == maxPage) {
                        Log.d("EmployeeMediator","currentPage = maxPage")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    val nextPage = currentPage
                    Log.d("EmployeeMediator","nextPage :$nextPage")
                    nextPage
                }
            }

            Log.d("EmployeeMediator", "Loading page: $pageToLoad")

            val response = employeeUseCase.getEmployees(page = pageToLoad) as ApiResponse.Success
            val results = response.employee.results

            Log.d("EmployeeMediator", "results: $results")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d("EmployeeMediator", "LoadType.REFRESH")
                    localUseCase.deleteAll()
                }
                Log.d("EmployeeMediator", "out LoadType.REFRESH")
                //currentPage increment here because when no internet connection
                //if the user clicks paging.retry() it will increment currentPage without loading data
                //but here only increment when data loaded and saved in local db
                currentPage++
                localUseCase.insertAllResults(results)
            }

            val endOfPaginationReached = pageToLoad >= maxPage

            Log.d(
                "EmployeeMediator",
                "Loaded ${results.size} results, endOfPagination: $endOfPaginationReached"
            )

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
