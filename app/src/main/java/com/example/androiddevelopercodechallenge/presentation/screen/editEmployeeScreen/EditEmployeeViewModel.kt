package com.example.androiddevelopercodechallenge.presentation.screen.editEmployeeScreen

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.EditEmployeeEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState.EditEmployeeUiState
import com.example.androiddevelopercodechallenge.data.util.Country
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEmployeeViewModel @Inject constructor(    //not recommended to use context in viewmodel
    //but since it's applicationContext can work if really needed
    @ApplicationContext private val context: Context,
    private val localUseCase: LocalUseCase,
) : ViewModel() {
    private val _editEmployeeUiState: MutableStateFlow<EditEmployeeUiState> =
        MutableStateFlow(EditEmployeeUiState())
    val editEmployeeUiState: StateFlow<EditEmployeeUiState> = _editEmployeeUiState.asStateFlow()

    private val _editEmployeeEvents: Channel<EditEmployeeEvents> = Channel()
    val editEmployeeEvents = _editEmployeeEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "EditEmployeeViewModel: created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "EditEmployeeViewModel: cleared")
    }

    fun onActions(editEmployeeActions: AddOrEditActions) {
        when (editEmployeeActions) {
            is AddOrEditActions.UpdateEmail -> updateEmail(email = editEmployeeActions.email)
            is AddOrEditActions.UpdateFirstName -> updateFirstName(firstName = editEmployeeActions.firstName)
            is AddOrEditActions.UpdateLastName -> updateLastName(lastName = editEmployeeActions.lastName)
            is AddOrEditActions.UpdatePhoneNumber -> updatePhoneNumber(phoneNumber = editEmployeeActions.phoneNumber)
            AddOrEditActions.OnCountryExpand -> onCountryExpand()
            is AddOrEditActions.UpdateSelectedCountry -> updateSelectedCountry(selectedCountry = editEmployeeActions.country)
            AddOrEditActions.OnGenderExpand -> onGenderExpand()
            is AddOrEditActions.UpdateSelectedGender -> updateSelectedGender(selectedGender = editEmployeeActions.gender)
            is AddOrEditActions.AddOrSaveEmployee -> {
                viewModelScope.launch {
                    saveEmployee()
                }
            }

            is AddOrEditActions.EditEmployeeActions.OnCheck -> onCheck(readOnly = editEmployeeActions.checked)
            is AddOrEditActions.EditEmployeeActions.UpdateEmployee -> updateEmployee(
                employee = editEmployeeActions.employee,
                checked = editEmployeeActions.checked
            )
        }

    }

    private fun updateEmployee(employee: Result, checked: Boolean) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = employee, readOnly = checked)
        }
    }

    private suspend fun saveEmployee() {
        val state = _editEmployeeUiState.value
        val employee = state.employee
        Log.d("MyTag", "$employee")
        if (employee.name.first.isNotBlank() && employee.name.last.isNotBlank() &&
            employee.email.isNotBlank() && employee.phone.isNotBlank()
        ) {
            if (Patterns.EMAIL_ADDRESS.matcher(employee.email).matches()) {
                try {
                    val extension = _editEmployeeUiState.value.selectedCountry.extension
                    localUseCase.updateResult(result = employee.copy(phone = extension+" "+employee.phone))
                    resetState()
                    _editEmployeeEvents.send(EditEmployeeEvents.AddEmployee)
                }catch (e: Exception){
                    _editEmployeeEvents.send(EditEmployeeEvents.ShowMessage(message = "${e.message}"))
                }
            } else {
                val validEmail = context.getString(R.string.please_use_valid_email) + "!"
                _editEmployeeEvents.send(EditEmployeeEvents.ShowMessage(message = validEmail))
            }
        } else {
            val requiredFields =
                context.getString(R.string.required_fields) + " " + context.getString(R.string.are_empty) + "!"
            _editEmployeeEvents.send(EditEmployeeEvents.ShowMessage(message = requiredFields))
        }
    }

    private fun onCheck(readOnly: Boolean) {
        _editEmployeeUiState.update { newState ->
            newState.copy(readOnly = readOnly)
        }
    }

    private fun updateSelectedGender(selectedGender: String) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(gender = selectedGender))
        }
    }

    private fun updateSelectedCountry(selectedCountry: Country) {
        _editEmployeeUiState.update { newState ->
            newState.copy(selectedCountry = selectedCountry)
        }
    }

    private fun updateFirstName(firstName: String) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(name = newState.employee.name.copy(first = firstName)))
        }
    }

    private fun updateLastName(lastName: String) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(name = newState.employee.name.copy(last = lastName)))
        }
    }

    private fun updateEmail(email: String) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(email = email))
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        _editEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(phone = phoneNumber))
        }
    }

    private fun onCountryExpand() {
        _editEmployeeUiState.update { newState ->
            val isExpanded = newState.isCountryExpanded
            newState.copy(isCountryExpanded = !isExpanded)
        }
    }

    private fun onGenderExpand() {
        _editEmployeeUiState.update { newState ->
            val isExpanded = newState.isGenderExpanded
            newState.copy(isGenderExpanded = !isExpanded)
        }
    }

    private fun resetState() {
        _editEmployeeUiState.value = EditEmployeeUiState()
    }
}