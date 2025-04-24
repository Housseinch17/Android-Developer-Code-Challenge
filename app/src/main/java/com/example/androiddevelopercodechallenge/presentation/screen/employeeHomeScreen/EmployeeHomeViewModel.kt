package com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
    data class NavigateToEditEmployee(val employee: Result, val checked: Boolean) :
        EmployeeHomeActions

    data class UpdateLoader(val isLoading: Boolean) : EmployeeHomeActions
    data class UpdateEmployeeList(val newEmployeeList: List<Result>) : EmployeeHomeActions
    data class DeleteEmployee(val employee: Result) : EmployeeHomeActions
    data class DeleteEmployeeConfirmed(val employee: Result) : EmployeeHomeActions
    data object HideDialog : EmployeeHomeActions
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class EmployeeHomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    pager: Pager<Int, Result>,
) : ViewModel() {
    private val _employeeHomeUiState: MutableStateFlow<EmployeeHomeUiState> = MutableStateFlow(
        EmployeeHomeUiState()
    )
    val employeeUiState: StateFlow<EmployeeHomeUiState> = _employeeHomeUiState.asStateFlow()

    private val _employeeHomeDialogState: MutableStateFlow<EmployeeHomeDialogState> =
        MutableStateFlow(
            EmployeeHomeDialogState()
        )
    val employeeHomeDialogState: StateFlow<EmployeeHomeDialogState> =
        _employeeHomeDialogState.asStateFlow()

    private val _employeeHomeEvents: Channel<EmployeeHomeEvents> = Channel()
    val employeeHomeEvents = _employeeHomeEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "EmployeeHomeViewModel: Started")
        viewModelScope.launch {
            launch {
                readFromLocalDb()
            }
                getEmployee()
        }
    }

    val employeePagingFlow = pager.flow.cachedIn(viewModelScope)

    private suspend fun getEmployee(){
        _employeeHomeUiState
            .map { it.searchQuery }
            .debounce(300L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                employeePagingFlow.map { pagingData ->
                    if (query.isBlank()) {
                        pagingData
                    } else {
                        pagingData.filter { employee ->
                            employee.name.first.contains(query, ignoreCase = true) ||
                                    employee.name.last.contains(query, ignoreCase = true)
                        }
                    }
                }
            }
            .cachedIn(viewModelScope)
            .collect { filteredFlow ->
                _employeeHomeUiState.update { it.copy(filteredEmployeePagingFlow = flowOf(filteredFlow)) }
            }
    }




    fun onActions(employeeHomeActions: EmployeeHomeActions) {
        viewModelScope.launch {
            when (employeeHomeActions) {
                EmployeeHomeActions.NavigateToAddEmployee -> navigateToAddEmployee()

                is EmployeeHomeActions.OnSearchQueryChanged -> {
                    onSearchQueryChanged(query = employeeHomeActions.newQuery)
                }

                is EmployeeHomeActions.NavigateToEditEmployee -> navigateToEditEmployee(
                    employee = employeeHomeActions.employee,
                    checked = employeeHomeActions.checked
                )

                is EmployeeHomeActions.UpdateLoader -> updateLoader(isLoading = employeeHomeActions.isLoading)
                is EmployeeHomeActions.UpdateEmployeeList -> {
//                    updateEmployeeList(newEmployeeList = employeeHomeActions.newEmployeeList)
                }

                is EmployeeHomeActions.DeleteEmployee -> deleteEmployee(employee = employeeHomeActions.employee)

                is EmployeeHomeActions.DeleteEmployeeConfirmed -> deleteEmployeeConfirmed(employee = employeeHomeActions.employee)
                EmployeeHomeActions.HideDialog -> hideDialog()

            }
        }
    }

    private suspend fun readFromLocalDb() {
        try {
            localRepository.getAllResults().collect { newList ->
                Log.d("MyTag", "EmployeeHomeViewModel: readFromLocalDb: success: ${newList.size}")
                _employeeHomeUiState.update { newState ->
                    newState.copy(
                        employeeResult = newList,
                        filteredEmployeeResult = newList
                    )
                }
                filterEmployeeList(query = _employeeHomeUiState.value.searchQuery)
            }
        } catch (e: Exception) {
            Log.e("MyTag", "EmployeeHomeViewModel: readFromLocalDb: error: ${e.message}")
        }
    }

    private fun filterEmployeeList(query: String) {
        _employeeHomeUiState.update { newState ->
            val filteredEmployeeList = if (query.isBlank()) {
                newState.employeeResult
            } else {
                newState.employeeResult.filter { employee ->
                    employee.name.first.contains(query, ignoreCase = true) ||
                            employee.name.last.contains(query, ignoreCase = true)
                }
            }
            newState.copy(filteredEmployeeResult = filteredEmployeeList)
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


    private suspend fun navigateToAddEmployee() {
        _employeeHomeEvents.send(EmployeeHomeEvents.NavigateToAddEmployee)
    }

    private suspend fun deleteEmployeeConfirmed(employee: Result) {
//        Log.d("MyTag", "deleteEmployeeConfirmed: $employee")
        showDialogProgressBar()

        //delete from db
        localRepository.deleteResultsByEmail(email = employee.email)

        delay(500)

        _employeeHomeEvents.send(EmployeeHomeEvents.DeleteEmployeeConfirmed(deletedEmployee = _employeeHomeUiState.value.currentEmployee))
        hideDialog()
    }

    private fun deleteEmployee(employee: Result) {
//        Log.d("MyTag", "deleteEmployee() employee: $employee")
        _employeeHomeUiState.update { newState ->
            newState.copy(currentEmployee = employee)
        }
        _employeeHomeDialogState.update { newState ->
            newState.copy(showDialog = true)
        }
    }

    private fun hideDialog() {
        _employeeHomeDialogState.update { newState ->
            newState.copy(showDialog = false, dialogProgressBar = false)
        }
    }

    private fun showDialogProgressBar() {
        _employeeHomeDialogState.update { newState ->
            newState.copy(dialogProgressBar = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "EmployeeHomeViewModel: Cleared!")
    }
}