package com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.AddEmployeeEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState.AddEmployeeUiState
import com.example.androiddevelopercodechallenge.data.util.Country
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
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

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    //not recommended to use context in viewmodel
    //but since it's applicationContext can work if really needed
    @ApplicationContext private val context: Context,
    private val localUseCase: LocalUseCase,

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

    fun onActions(addEmployeeActions: AddOrEditActions) {
        when (addEmployeeActions) {
            is AddOrEditActions.UpdateEmail -> updateEmail(email = addEmployeeActions.email)
            is AddOrEditActions.UpdateFirstName -> updateFirstName(firstName = addEmployeeActions.firstName)
            is AddOrEditActions.UpdateLastName -> updateLastName(lastName = addEmployeeActions.lastName)
            is AddOrEditActions.UpdatePhoneNumber -> updatePhoneNumber(phoneNumber = addEmployeeActions.phoneNumber)
            AddOrEditActions.OnCountryExpand -> onCountryExpand()
            is AddOrEditActions.UpdateSelectedCountry -> updateSelectedCountry(selectedCountry = addEmployeeActions.country)
            AddOrEditActions.OnGenderExpand -> onGenderExpand()
            is AddOrEditActions.UpdateSelectedGender -> updateSelectedGender(selectedGender = addEmployeeActions.gender)
            is AddOrEditActions.AddOrSaveEmployee -> {
                viewModelScope.launch {
                    addEmployee()
                }
            }

            else -> {}
        }
    }

    private suspend fun addEmployee() {
        val state = _addEmployeeUiState.value
        val employee = state.employee
        Log.d("MyTag","$employee")
        if (employee.name.first.isNotBlank() && employee.name.last.isNotBlank() &&
            employee.email.isNotBlank() && employee.phone.isNotBlank()
        ) {
            if(Patterns.EMAIL_ADDRESS.matcher(employee.email).matches()) {
                try {
                    val extension = _addEmployeeUiState.value.selectedCountry.extension
                    localUseCase.addResult(result = employee.copy(phone = extension+" "+employee.phone))
                    resetState()
                    _addEmployeeEvents.send(AddEmployeeEvents.AddEmployee)
                }catch (e: Exception){
                    _addEmployeeEvents.send(AddEmployeeEvents.ShowMessage(message = "${e.message}"))
                }
            }else{
                val validEmail = context.getString(R.string.please_use_valid_email) +"!"
                _addEmployeeEvents.send(AddEmployeeEvents.ShowMessage(message = validEmail))
            }
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
        if (phoneNumber.isEmpty() || phoneNumber.matches(numberRegex)) {
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