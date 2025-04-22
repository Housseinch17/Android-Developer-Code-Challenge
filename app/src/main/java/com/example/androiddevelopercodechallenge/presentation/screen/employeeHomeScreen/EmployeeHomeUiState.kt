package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import androidx.paging.PagingData
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class EmployeeHomeUiState(
    val employeePagingData: Flow<PagingData<Result>> = emptyFlow(),
    val employeeList: List<Result> = emptyList<Result>(),
    val filteredEmployeeList: List<Result> = emptyList<Result>(),
    val localEmployeeResults: List<Result> = emptyList<Result>(),
    val filteredLocalEmployeeResults: List<Result> = emptyList<Result>(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val showLocalDialog: Boolean = false,
    val localIsLoading: Boolean = false,
    val currentEmployee: Result = Result(),
    val localDialogProgressBar: Boolean = false,
    val enableButtons: Boolean = true,
    val isPagingQuery: Boolean = true,
)

data class EmployeeHomeDialogState(
    val showDialog: Boolean = false,
    val dialogProgressBar: Boolean = false,
)
