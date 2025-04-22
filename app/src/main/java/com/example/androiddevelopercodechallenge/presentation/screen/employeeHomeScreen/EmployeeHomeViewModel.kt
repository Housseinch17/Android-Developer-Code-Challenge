package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.paging.EmployeePaging
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface EmployeeHomeEvents {
    data object NavigateToAddEmployee : EmployeeHomeEvents
    data class NavigateToEditEmployee(val employee: Result, val checked: Boolean) :
        EmployeeHomeEvents

    data class DeleteEmployeeConfirmed(val deletedEmployee: Result) : EmployeeHomeEvents
}

sealed interface EmployeeHomeActions {
    data object NavigateToAddEmployee : EmployeeHomeActions
    data class OnSearchQueryChanged(val newQuery: String) : EmployeeHomeActions
    data object ClearQuery : EmployeeHomeActions
    data class NavigateToEditEmployee(val employee: Result, val checked: Boolean) :
        EmployeeHomeActions

    data class UpdateLoader(val isLoading: Boolean) : EmployeeHomeActions
    data class UpdateEmployeeList(val newEmployeeList: List<Result>) : EmployeeHomeActions
    data class DeleteEmployee(val employee: Result) : EmployeeHomeActions
    data class DeleteLocalEmployee(val employee: Result) : EmployeeHomeActions
    data class DeleteEmployeeConfirmed(val employee: Result) : EmployeeHomeActions
    data class DeleteLocalEmployeeConfirmed(val employee: Result) : EmployeeHomeActions
    data object HideDialog : EmployeeHomeActions
    data object HideLocalDialog: EmployeeHomeActions
    data class EnableButtons(val enableButtons: Boolean) : EmployeeHomeActions

    data object GetAllResults : EmployeeHomeActions
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class EmployeeHomeViewModel @Inject constructor(
    private val employeeUseCase: EmployeeUseCase,
    private val localUseCase: LocalUseCase,
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
                is EmployeeHomeActions.NavigateToEditEmployee -> navigateToEditEmployee(
                    employee = employeeHomeActions.employee,
                    checked = employeeHomeActions.checked
                )

                is EmployeeHomeActions.UpdateLoader -> updateLoader(isLoading = employeeHomeActions.isLoading)
                is EmployeeHomeActions.UpdateEmployeeList -> updateEmployeeList(newEmployeeList = employeeHomeActions.newEmployeeList)
                is EmployeeHomeActions.DeleteEmployee -> deleteEmployee(employee = employeeHomeActions.employee)
                is EmployeeHomeActions.DeleteLocalEmployee -> deleteLocalEmployee(employee = employeeHomeActions.employee)
                is EmployeeHomeActions.DeleteEmployeeConfirmed -> deleteEmployeeConfirmed(employee = employeeHomeActions.employee)
                EmployeeHomeActions.HideDialog -> hideDialog()
                is EmployeeHomeActions.EnableButtons -> enableButtons(enableButtons = employeeHomeActions.enableButtons)
                EmployeeHomeActions.GetAllResults -> getAllResults()
                is EmployeeHomeActions.DeleteLocalEmployeeConfirmed -> deleteLocalEmployeeConfirmed(employee = employeeHomeActions.employee)
                EmployeeHomeActions.HideLocalDialog -> hideLocalDialog()
            }
        }
    }

    fun getEmployee() {
        val employeePager = Pager(
            //here pageSize how many items i have in each page
            //prefetchDistance when to load new items for example here 3 so im saying
            //when we reach the last 3 items load new page
            //initialLoadSize how many items i want  to load initial it's usually preferred to use same as pageSize
            //here 8 since it's gridlayout 2 columns and size is small so need to fetch before
            PagingConfig(pageSize = 20, prefetchDistance = 8, initialLoadSize = 20)
        ) {
            EmployeePaging(
                employeeUseCase = employeeUseCase,
                localUseCase = localUseCase
            )
        }.flow.cachedIn(viewModelScope)

        viewModelScope.launch {
            _employeeHomeUiState.update { newState ->
                newState.copy(employeePagingData = employeePager)
            }
        }
    }

    private suspend fun getAllResults() {
        //local results list
        val results = localUseCase.getAllResults()
        results.collect { result ->
            //show local shimmer
            _employeeHomeUiState.update { newState ->
                newState.copy(localIsLoading = true)
            }
            if (result.isNotEmpty()) {
                _employeeHomeUiState.update { newState ->
                    newState.copy(localEmployeeResults = result)
                }
            }

            //hide local shimmer
            _employeeHomeUiState.update { newState ->
                newState.copy(localIsLoading = false)
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
        Log.d("MyTag","EmployeeHomeViewModel enabledButtons()")
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

    private suspend fun navigateToEditEmployee(employee: Result, checked: Boolean) {
        _employeeHomeEvents.send(
            EmployeeHomeEvents.NavigateToEditEmployee(
                employee = employee,
                checked = checked
            )
        )
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

    private suspend fun deleteLocalEmployeeConfirmed(employee: Result) {
        showLocalDialogProgressBar()
        Log.d("MyTag","employeeUId: ${employee.email}")
        try {
            localUseCase.deleteResultsByEmail(email = employee.email)
            delay(500)
            _employeeHomeEvents.send(EmployeeHomeEvents.DeleteEmployeeConfirmed(deletedEmployee = _employeeHomeUiState.value.currentEmployee))
        }catch (e: Exception){
            Log.e("MyTag","EmployeeHomeViewModel: deleteLocalEmployee: error: ${e.message}" )
        }
        hideLocalDialog()
    }

    private suspend fun deleteEmployeeConfirmed(employee: Result) {
        Log.d("MyTag","deleteEmployeeConfirmed: $employee")
        showDialogProgressBar()
        val employeePagingData = _employeeHomeUiState.value.employeePagingData
        val newEmployeePagingData = employeePagingData.map { pagingData ->
            pagingData.filter { it != employee }
        }

        //delete from db
        localUseCase.deleteResultsByEmail(email = employee.email)

        delay(500)
        _employeeHomeUiState.update { newState ->
            newState.copy(employeePagingData = newEmployeePagingData)
        }
        _employeeHomeEvents.send(EmployeeHomeEvents.DeleteEmployeeConfirmed(deletedEmployee = _employeeHomeUiState.value.currentEmployee))
        hideDialog()
    }

    private fun deleteEmployee(employee: Result) {
        Log.d("MyTag","employee: $employee now")
        _employeeHomeUiState.update { newState ->
            newState.copy(currentEmployee = employee, showDialog = true)
        }
    }

    private fun deleteLocalEmployee(employee: Result) {
        Log.d("MyTag","employee: $employee now")
        _employeeHomeUiState.update { newState ->
            newState.copy(currentEmployee = employee, showLocalDialog = true)
        }
    }
    private fun hideLocalDialog() {
        _employeeHomeUiState.update { newState ->
            newState.copy(showLocalDialog = false, localDialogProgressBar = false)
        }
    }

    private fun showLocalDialogProgressBar() {
        _employeeHomeUiState.update { newState ->
            newState.copy(localDialogProgressBar = true)
        }
    }
    private fun hideDialog() {
        _employeeHomeUiState.update { newState ->
            newState.copy(showDialog = false, dialogProgressBar = false)
        }
    }

    private fun showDialogProgressBar() {
        _employeeHomeUiState.update { newState ->
            newState.copy(dialogProgressBar = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "EmployeeHomeViewModel: Cleared!")
    }
}