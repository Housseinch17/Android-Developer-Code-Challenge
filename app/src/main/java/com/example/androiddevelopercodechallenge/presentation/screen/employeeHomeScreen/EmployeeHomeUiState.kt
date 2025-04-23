package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import androidx.paging.PagingData
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class EmployeeHomeUiState(
    val filteredEmployeePagingFlow : Flow<PagingData<Result>> = emptyFlow(),
    val employeeResult: List<Result> = emptyList(),
    val filteredEmployeeResult: List<Result> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val currentEmployee: Result = Result(),
    val enableButtons: Boolean = true,

)

data class EmployeeHomeDialogState(
    val showDialog: Boolean = false,
    val dialogProgressBar: Boolean = false,
)
