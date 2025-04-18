package com.example.androiddevelopercodechallenge.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCase

class EmployeePaging(
    private val employeeUseCase: EmployeeUseCase,
//    private val searchQuery: String,
) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        Log.d("MyTag", "EmployeePaging: getRefreshKey(): ${state.anchorPosition}")
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        //1 since page = 0 is showing Info(page = 1)
        val page = params.key ?: 1
        Log.d("MyTag", "EmployeePaging: load(): nextPage: $page")
        return try {
            val response = employeeUseCase.getEmployees(page = page) as ApiResponse.Success

//            //to be used with query api if exists
//            val filteredResults = if (searchQuery.isNotBlank()) {
//                //query api for search to be replaced
//                response.employee.results.filter { result ->
//                    result.name.first.contains(searchQuery, ignoreCase = true) ||
//                            result.name.last.contains(searchQuery, ignoreCase = true)
//                }
//            } else {
//                response.employee.results
//            }

            LoadResult.Page(
                data = response.employee.results,
                prevKey = null,
                nextKey = if (response.employee.results.isNotEmpty()) page.inc() else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}