package com.example.androiddevelopercodechallenge.presentation.screen.addUserScreen

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.AddUserEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState.AddUserUiState
import com.example.androiddevelopercodechallenge.data.util.Country
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val localRepository: LocalRepository,
) : ViewModel() {
    private val _addUserUiState: MutableStateFlow<AddUserUiState> =
        MutableStateFlow(AddUserUiState())
    val addUserUiState: StateFlow<AddUserUiState> = _addUserUiState.asStateFlow()

    private val _addUserEvents: Channel<AddUserEvents> = Channel()
    val addUserEvents = _addUserEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "AddUserViewModel: created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "AddUserViewModel: cleared")
    }

    fun onActions(addUserActions: AddOrEditActions) {
        when (addUserActions) {
            is AddOrEditActions.UpdateEmail -> updateEmail(email = addUserActions.email)
            is AddOrEditActions.UpdateFirstName -> updateFirstName(firstName = addUserActions.firstName)
            is AddOrEditActions.UpdateLastName -> updateLastName(lastName = addUserActions.lastName)
            is AddOrEditActions.UpdatePhoneNumber -> updatePhoneNumber(phoneNumber = addUserActions.phoneNumber)
            AddOrEditActions.OnCountryExpand -> onCountryExpand()
            is AddOrEditActions.UpdateSelectedCountry -> updateSelectedCountry(selectedCountry = addUserActions.country)
            AddOrEditActions.OnGenderExpand -> onGenderExpand()
            is AddOrEditActions.UpdateSelectedGender -> updateSelectedGender(selectedGender = addUserActions.gender)
            is AddOrEditActions.AddOrSaveUser -> {
                viewModelScope.launch {
                    addUser()
                }
            }

            else -> {}
        }
    }

    private suspend fun addUser() {
        val state = _addUserUiState.value
        val user = state.user
//        Log.d("MyTag", "$user")
        if (user.firstName.isNotBlank() && user.lastName.isNotBlank() &&
            user.email.isNotBlank() && user.phone.isNotBlank()
        ) {
            if (Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
                try {
                    val extension = _addUserUiState.value.selectedCountry.extension
                    localRepository.addUser(user = user.copy(phone = extension + " " + user.phone))
                    resetState()
                    _addUserEvents.send(AddUserEvents.AddUser)
                } catch (e: Exception) {
                    _addUserEvents.send(AddUserEvents.ShowMessage(message = "${e.message}"))
                }
            } else {
                _addUserEvents.send(AddUserEvents.ShowMessage(message = "Please use valid email!"))
            }
        } else {
            _addUserEvents.send(AddUserEvents.ShowMessage(message = "Required fields are empty!"))
        }
    }

    private fun updateSelectedGender(selectedGender: String) {
        _addUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(gender = selectedGender))
        }
    }

    private fun updateSelectedCountry(selectedCountry: Country) {
        _addUserUiState.update { newState ->
            newState.copy(selectedCountry = selectedCountry)
        }
    }

    private fun updateFirstName(firstName: String) {
        _addUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(firstName = firstName))
        }
    }

    private fun updateLastName(lastName: String) {
        _addUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(lastName = lastName))
        }
    }

    private fun updateEmail(email: String) {
        _addUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(email = email))
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        if (phoneNumber.isEmpty() || phoneNumber.all { it.isDigit() }) {
            _addUserUiState.update { newState ->
                newState.copy(user = newState.user.copy(phone = phoneNumber))
            }
        }
    }

    private fun onCountryExpand() {
        _addUserUiState.update { newState ->
            val isExpanded = newState.isCountryExpanded
            newState.copy(isCountryExpanded = !isExpanded)
        }
    }

    private fun onGenderExpand() {
        _addUserUiState.update { newState ->
            val isExpanded = newState.isGenderExpanded
            newState.copy(isGenderExpanded = !isExpanded)
        }
    }

    private fun resetState() {
        _addUserUiState.value = AddUserUiState()
    }
}