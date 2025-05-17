package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.User

sealed interface AddOrEditActions {

    data class UpdateFirstName(val firstName: String) : AddUserActions
    data class UpdateLastName(val lastName: String) : AddUserActions
    data class UpdateEmail(val email: String) : AddUserActions
    data class UpdatePhoneNumber(val phoneNumber: String) : AddUserActions
    data object OnCountryExpand : AddUserActions
    data class UpdateSelectedCountry(val country: Country) : AddUserActions
    data object OnGenderExpand : AddUserActions
    data class UpdateSelectedGender(val gender: String) : AddUserActions
    data object AddOrSaveUser : AddUserActions

    sealed interface AddUserActions: AddOrEditActions

    sealed interface EditUserActions: AddOrEditActions {
        data class OnCheck(val checked: Boolean): EditUserActions
        data class UpdateUser(val user: User, val checked: Boolean):  EditUserActions
    }
}