package com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.Country
import com.example.androiddevelopercodechallenge.presentation.util.Constants.numberRegex
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

sealed interface AddEmployeeEvents {
    data object AddEmployee : AddEmployeeEvents
    data class ShowMessage(val message: String) : AddEmployeeEvents
}

sealed interface AddEmployeeActions {
    data class UpdateFirstName(val firstName: String) : AddEmployeeActions
    data class UpdateLastName(val lastName: String) : AddEmployeeActions
    data class UpdateEmail(val email: String) : AddEmployeeActions
    data class UpdatePhoneNumber(val phoneNumber: String) : AddEmployeeActions
    data object OnCountryExpand : AddEmployeeActions
    data class UpdateSelectedCountry(val country: Country) : AddEmployeeActions
    data object OnGenderExpand : AddEmployeeActions
    data class UpdateSelectedGender(val gender: String) : AddEmployeeActions
    data object AddEmployee : AddEmployeeActions
}

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    //not recommended to use context in viewmodel
    //but since it's applicationContext can work if really needed
    @ApplicationContext private val context: Context

) : ViewModel() {
    private val _addEmployeeUiState: MutableStateFlow<AddEmployeeUiState> =
        MutableStateFlow(AddEmployeeUiState())
    val addEmployeeUiState: StateFlow<AddEmployeeUiState> = _addEmployeeUiState.asStateFlow()

    private val _addEmployeeEvents: Channel<AddEmployeeEvents> = Channel()
    val addEmployeeEvents = _addEmployeeEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "AddEmployeeViewModel: created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "AddEmployeeViewModel: cleared")
    }

    fun onActions(addEmployeeActions: AddEmployeeActions) {
        when (addEmployeeActions) {
            is AddEmployeeActions.UpdateEmail -> updateEmail(email = addEmployeeActions.email)
            is AddEmployeeActions.UpdateFirstName -> updateFirstName(firstName = addEmployeeActions.firstName)
            is AddEmployeeActions.UpdateLastName -> updateLastName(lastName = addEmployeeActions.lastName)
            is AddEmployeeActions.UpdatePhoneNumber -> updatePhoneNumber(phoneNumber = addEmployeeActions.phoneNumber)
            AddEmployeeActions.OnCountryExpand -> onCountryExpand()
            is AddEmployeeActions.UpdateSelectedCountry -> updateSelectedCountry(selectedCountry = addEmployeeActions.country)
            AddEmployeeActions.OnGenderExpand -> onGenderExpand()
            is AddEmployeeActions.UpdateSelectedGender -> updateSelectedGender(selectedGender = addEmployeeActions.gender)
            is AddEmployeeActions.AddEmployee -> {
                viewModelScope.launch {
                    addEmployee()
                }
            }
        }
    }

    private suspend fun addEmployee() {
        val state = _addEmployeeUiState.value
        val employee = state.employee
        Log.d("MyTag","$employee")
        if (employee.name.first.isNotBlank() && employee.name.last.isNotBlank() &&
            employee.email.isNotBlank() && employee.phone.isNotBlank()
        ) {
            _addEmployeeEvents.send(AddEmployeeEvents.AddEmployee)
            resetState()
        } else {
            val requiredFields =
                context.getString(R.string.required_fields) + " " + context.getString(R.string.are_empty) + "!"
            _addEmployeeEvents.send(AddEmployeeEvents.ShowMessage(message = requiredFields))
        }
    }

    private fun updateSelectedGender(selectedGender: String) {
        _addEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(gender = selectedGender))
        }
    }

    private fun updateSelectedCountry(selectedCountry: Country) {
        _addEmployeeUiState.update { newState ->
            newState.copy(selectedCountry = selectedCountry)
        }
    }

    private fun updateFirstName(firstName: String) {
        _addEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(name = newState.employee.name.copy(first = firstName)))
        }
    }

    private fun updateLastName(lastName: String) {
        _addEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(name = newState.employee.name.copy(last = lastName)))
        }
    }

    private fun updateEmail(email: String) {
        _addEmployeeUiState.update { newState ->
            newState.copy(employee = newState.employee.copy(email = email))
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        if (phoneNumber.matches(numberRegex)) {
            _addEmployeeUiState.update { newState ->
                newState.copy(employee = newState.employee.copy(phone = phoneNumber))
            }
        }
    }

    private fun onCountryExpand() {
        _addEmployeeUiState.update { newState ->
            val isExpanded = newState.isCountryExpanded
            newState.copy(isCountryExpanded = !isExpanded)
        }
    }

    private fun onGenderExpand() {
        _addEmployeeUiState.update { newState ->
            val isExpanded = newState.isGenderExpanded
            newState.copy(isGenderExpanded = !isExpanded)
        }
    }

    private fun resetState(){
        _addEmployeeUiState.value = AddEmployeeUiState()
    }
}