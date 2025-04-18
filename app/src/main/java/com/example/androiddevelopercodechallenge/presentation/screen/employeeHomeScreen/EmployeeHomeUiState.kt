package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import androidx.paging.PagingData
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class EmployeeHomeUiState(
    val employeePagingData: Flow<PagingData<Result>> = emptyFlow(),
    val employeeList: List<Result> = emptyList<Result>(),
    val filteredEmployeeList: List<Result> = emptyList<Result>(),
    val searchQuery: String = "",
    val resultList: List<Result> = emptyList<Result>(),
    val isLoading: Boolean = true,
    val showDialog: Boolean = false,
    val currentEmployee: Result = Result(),
    val dialogProgressBar: Boolean = false
)
