package com.example.androiddevelopercodechallenge.presentation.screen.editUserScreen

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.EditUserEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditUiState.EditUserUiState
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
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _editUserUiState: MutableStateFlow<EditUserUiState> =
        MutableStateFlow(EditUserUiState())
    val editUserUiState: StateFlow<EditUserUiState> = _editUserUiState.asStateFlow()

    private val _editUserEvents: Channel<EditUserEvents> = Channel()
    val editUserEvents = _editUserEvents.receiveAsFlow()

    init {
        Log.d("MyTag", "EditUserViewModel: created")
        val userJson: String = savedStateHandle.get<String>("user") ?: "()"
        val user: User = Json.decodeFromString(userJson)
        val checked: Boolean = savedStateHandle.get<Boolean>("checked") == true
        if (user != User()) {
            updateUser(user = user, checked = checked)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "EditUserViewModel: cleared")
    }

    fun onActions(editUserActions: AddOrEditActions) {
        when (editUserActions) {
            is AddOrEditActions.UpdateEmail -> updateEmail(email = editUserActions.email)
            is AddOrEditActions.UpdateFirstName -> updateFirstName(firstName = editUserActions.firstName)
            is AddOrEditActions.UpdateLastName -> updateLastName(lastName = editUserActions.lastName)
            is AddOrEditActions.UpdatePhoneNumber -> updatePhoneNumber(phoneNumber = editUserActions.phoneNumber)
            AddOrEditActions.OnCountryExpand -> onCountryExpand()
            is AddOrEditActions.UpdateSelectedCountry -> updateSelectedCountry(selectedCountry = editUserActions.country)
            AddOrEditActions.OnGenderExpand -> onGenderExpand()
            is AddOrEditActions.UpdateSelectedGender -> updateSelectedGender(selectedGender = editUserActions.gender)
            is AddOrEditActions.AddOrSaveUser -> {
                viewModelScope.launch {
                    saveUser()
                }
            }

            is AddOrEditActions.EditUserActions.OnCheck -> onCheck(readOnly = editUserActions.checked)
            is AddOrEditActions.EditUserActions.UpdateUser -> updateUser(
                user = editUserActions.user,
                checked = editUserActions.checked
            )
        }

    }

    private fun updateUser(user: User, checked: Boolean) {
        _editUserUiState.update { newState ->
            newState.copy(user = user, readOnly = checked)
        }
    }

    private suspend fun saveUser() {
        val state = _editUserUiState.value
        val user = state.user
//        Log.d("MyTag", "$user")
        if (user.firstName.isNotBlank() && user.lastName.isNotBlank() &&
            user.email.isNotBlank() && user.phone.isNotBlank()
        ) {
            if (Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
                try {
                    val extension = _editUserUiState.value.selectedCountry.extension
                    localRepository.updateUser(user = user.copy(phone = extension + " " + user.phone))
                    resetState()
                    _editUserEvents.send(EditUserEvents.AddUser)
                } catch (e: Exception) {
                    _editUserEvents.send(EditUserEvents.ShowMessage(message = "${e.message}"))
                }
            } else {
                _editUserEvents.send(EditUserEvents.ShowMessage(message = "Please use valid email!"))
            }
        } else {
            _editUserEvents.send(EditUserEvents.ShowMessage(message = "Required fields are empty!"))
        }
    }

    private fun onCheck(readOnly: Boolean) {
        _editUserUiState.update { newState ->
            newState.copy(readOnly = readOnly)
        }
    }

    private fun updateSelectedGender(selectedGender: String) {
        _editUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(gender = selectedGender))
        }
    }

    private fun updateSelectedCountry(selectedCountry: Country) {
        _editUserUiState.update { newState ->
            newState.copy(selectedCountry = selectedCountry)
        }
    }

    private fun updateFirstName(firstName: String) {
        _editUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(firstName = firstName))
        }
    }

    private fun updateLastName(lastName: String) {
        _editUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(lastName = lastName))
        }
    }

    private fun updateEmail(email: String) {
        _editUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(email = email))
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        _editUserUiState.update { newState ->
            newState.copy(user = newState.user.copy(phone = phoneNumber))
        }
    }

    private fun onCountryExpand() {
        _editUserUiState.update { newState ->
            val isExpanded = newState.isCountryExpanded
            newState.copy(isCountryExpanded = !isExpanded)
        }
    }

    private fun onGenderExpand() {
        _editUserUiState.update { newState ->
            val isExpanded = newState.isGenderExpanded
            newState.copy(isGenderExpanded = !isExpanded)
        }
    }

    private fun resetState() {
        _editUserUiState.value = EditUserUiState()
    }
}