package com.example.androiddevelopercodechallenge.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase

@OptIn(ExperimentalPagingApi::class)
class EmployeeRemoteMediator(
    private val employeeUseCase: EmployeeUseCase,
    private val localUseCase: LocalUseCase,
    private val database: ResultDataBase,
) : RemoteMediator<Int, Result>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Result>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                // You can compute the next page based on your logic
                val currentSize = state.pages.sumOf { it.data.size }
                (currentSize / state.config.pageSize) + 1
            }
        }

        return try {
            val response = employeeUseCase.getEmployees(page = page) as ApiResponse.Success
            val results = response.employee.results

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localUseCase.deleteAll()
                }
                localUseCase.insertAllResults(results)
            }

            MediatorResult.Success(endOfPaginationReached = results.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
