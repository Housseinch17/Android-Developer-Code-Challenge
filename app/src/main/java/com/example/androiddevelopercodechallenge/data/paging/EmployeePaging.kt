package com.example.androiddevelopercodechallenge.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase

class EmployeePaging(
    private val employeeUseCase: EmployeeUseCase,
    private val localUseCase: LocalUseCase,
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

            val employeeList = response.employee.results
            if(employeeList.isNotEmpty()){
                //delete old saved because the api is generating random users always
                //with same parameter page = 1 & results = 20 always different response
                localUseCase.deleteAll()
                //insert response(list<Result>) into database
                localUseCase.insertAllResults(results = response.employee.results)
            }
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