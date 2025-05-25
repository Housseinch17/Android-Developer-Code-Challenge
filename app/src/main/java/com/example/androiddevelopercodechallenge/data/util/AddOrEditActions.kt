package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.User

sealed interface AddOrEditActions {

    data class UpdateFirstName(val firstName: String) : AddOrEditActions
    data class UpdateLastName(val lastName: String) : AddOrEditActions
    data class UpdateEmail(val email: String) : AddOrEditActions
    data class UpdatePhoneNumber(val phoneNumber: String) : AddOrEditActions
    data object OnCountryExpand : AddOrEditActions
    data class UpdateSelectedCountry(val country: Country) : AddOrEditActions
    data object OnGenderExpand : AddOrEditActions
    data class UpdateSelectedGender(val gender: String) : AddOrEditActions
    data object AddOrSaveUser : AddOrEditActions

    sealed interface AddUserActions: AddOrEditActions

    sealed interface EditUserActions: AddOrEditActions {
        data class OnCheck(val checked: Boolean): EditUserActions
        data class UpdateUser(val user: User, val checked: Boolean):  EditUserActions
    }
}