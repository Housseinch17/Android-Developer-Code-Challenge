package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.paging.EmployeePaging
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EmployeeHomeEvents {
    data object NavigateToAddEmployee : EmployeeHomeEvents
    data class NavigateToEmployeeDetail(val employee: Result) : EmployeeHomeEvents
    data class DeleteEmployeeConfirmed(val deletedEmployee: Result) : EmployeeHomeEvents
}

sealed interface EmployeeHomeActions {
    data object NavigateToAddEmployee : EmployeeHomeActions
    data class OnSearchQueryChanged(val newQuery: String) : EmployeeHomeActions
    data object ClearQuery : EmployeeHomeActions
    data class NavigateToEmployeeDetail(val employee: Result) : EmployeeHomeActions
    data class UpdateLoader(val isLoading: Boolean) : EmployeeHomeActions
    data class UpdateEmployeeList(val newEmployeeList: List<Result>) : EmployeeHomeActions
    data class DeleteEmployee(val employee: Result) : EmployeeHomeActions
    data object DeleteEmployeeConfirmed : EmployeeHomeActions
    data object HideDialog : EmployeeHomeActions
    data class EnableButtons(val enableButtons: Boolean) : EmployeeHomeActions
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class EmployeeHomeViewModel @Inject constructor(
    private val employeeUseCase: EmployeeUseCase
) : ViewModel() {
    private val _employeeHomeUiState: MutableStateFlow<EmployeeHomeUiState> = MutableStateFlow(
        EmployeeHomeUiState()
    )
    val employeeUiState: StateFlow<EmployeeHomeUiState> = _employeeHomeUiState.asStateFlow()

    private val _employeeHomeEvents: Channel<EmployeeHomeEvents> = Channel()
    val employeeHomeEvents = _employeeHomeEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "EmployeeHomeViewModel: Started")
        getEmployee()
    }

    fun onActions(employeeHomeActions: EmployeeHomeActions) {
        viewModelScope.launch {
            when (employeeHomeActions) {
                EmployeeHomeActions.NavigateToAddEmployee -> navigateToAddEmployee()
                is EmployeeHomeActions.OnSearchQueryChanged -> onSearchQueryChanged(query = employeeHomeActions.newQuery)
                EmployeeHomeActions.ClearQuery -> clearQuery()
                is EmployeeHomeActions.NavigateToEmployeeDetail -> navigateToEmployeeDetail(employee = employeeHomeActions.employee)
                is EmployeeHomeActions.UpdateLoader -> updateLoader(isLoading = employeeHomeActions.isLoading)
                is EmployeeHomeActions.UpdateEmployeeList -> updateEmployeeList(newEmployeeList = employeeHomeActions.newEmployeeList)
                is EmployeeHomeActions.DeleteEmployee -> deleteEmployee(employee = employeeHomeActions.employee)
                EmployeeHomeActions.DeleteEmployeeConfirmed -> deleteEmployeeConfirmed()
                EmployeeHomeActions.HideDialog -> hideDialog()
                is EmployeeHomeActions.EnableButtons -> enableButtons(enableButtons = employeeHomeActions.enableButtons)
            }
        }
    }

    fun getEmployee() {
        val employeePager = Pager(
            //here pageSize how many items i have in each page
            //prefetchDistance when to load new items for example here 3 so im saying
            //when we reach the last 3 items load new page
            //initialLoadSize how many items i want  to load initial it's usually preferred to use same as pageSize
            PagingConfig(pageSize = 20, prefetchDistance = 3, initialLoadSize = 20)
        ) {
            EmployeePaging(
                employeeUseCase = employeeUseCase,
            )
        }.flow.cachedIn(viewModelScope)

        viewModelScope.launch {
            _employeeHomeUiState.update { newState ->
                newState.copy(employeePagingData = employeePager)
            }
        }
    }

    private fun updateEmployeeList(newEmployeeList: List<Result>) {
        Log.d("MyTag", "EmployeeHomeViewModel: updateEmployeeList()")
        _employeeHomeUiState.update { newState ->
            newState.copy(
                employeeList = newEmployeeList,
                filteredEmployeeList = newEmployeeList,
            )
        }
        //filterEmployeeList again when the composable recompose after onStart/onResume (navigating back to EmployeeHomeScreen)
        filterEmployeeList(query = _employeeHomeUiState.value.searchQuery)
    }

    private fun enableButtons(enableButtons: Boolean) {
        _employeeHomeUiState.update { newState ->
            newState.copy(enableButtons = enableButtons)
        }
    }

    private fun filterEmployeeList(query: String) {
        _employeeHomeUiState.update { newState ->
            val filteredEmployeeList = if (query.isBlank()) {
                newState.employeeList
            } else {
                newState.employeeList.filter { employee ->
                    employee.name.first.contains(query, ignoreCase = true) ||
                            employee.name.last.contains(query, ignoreCase = true)
                }
            }
            newState.copy(filteredEmployeeList = filteredEmployeeList)
        }
    }


    fun onSearchQueryChanged(query: String) {
        _employeeHomeUiState.update { newState ->
            newState.copy(searchQuery = query)
        }
        filterEmployeeList(query = query)
    }

    private fun updateLoader(isLoading: Boolean) {
        _employeeHomeUiState.update { newState ->
            newState.copy(isLoading = isLoading)
        }
    }

    private suspend fun navigateToEmployeeDetail(employee: Result) {
        _employeeHomeEvents.send(EmployeeHomeEvents.NavigateToEmployeeDetail(employee = employee))
    }

    private fun clearQuery() {
//        searchQueryPaging.value = ""
        _employeeHomeUiState.update { newState ->
            newState.copy(searchQuery = "")
        }
        filterEmployeeList(query = "")
    }

    private suspend fun navigateToAddEmployee() {
        _employeeHomeEvents.send(EmployeeHomeEvents.NavigateToAddEmployee)
    }

    private suspend fun deleteEmployeeConfirmed() {
        showDialogProgressBar(dialogProgressBar = true)
        delay(500)
        _employeeHomeEvents.send(EmployeeHomeEvents.DeleteEmployeeConfirmed(deletedEmployee = _employeeHomeUiState.value.currentEmployee))
        showDialogProgressBar(dialogProgressBar = false)
        hideDialog()
    }

    private fun deleteEmployee(employee: Result) {
        _employeeHomeUiState.update { newState ->
            newState.copy(currentEmployee = employee, showDialog = true)
        }
    }

    private fun hideDialog() {
        _employeeHomeUiState.update { newState ->
            newState.copy(showDialog = false)
        }
    }

    private fun showDialogProgressBar(dialogProgressBar: Boolean) {
        _employeeHomeUiState.update { newState ->
            newState.copy(dialogProgressBar = dialogProgressBar)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "EmployeeHomeViewModel: Cleared!")
    }
}