package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.Result

sealed interface AddOrEditActions {

    data class UpdateFirstName(val firstName: String) : AddEmployeeActions
    data class UpdateLastName(val lastName: String) : AddEmployeeActions
    data class UpdateEmail(val email: String) : AddEmployeeActions
    data class UpdatePhoneNumber(val phoneNumber: String) : AddEmployeeActions
    data object OnCountryExpand : AddEmployeeActions
    data class UpdateSelectedCountry(val country: Country) : AddEmployeeActions
    data object OnGenderExpand : AddEmployeeActions
    data class UpdateSelectedGender(val gender: String) : AddEmployeeActions
    data object AddOrSaveEmployee : AddEmployeeActions

    sealed interface AddEmployeeActions: AddOrEditActions

    sealed interface EditEmployeeActions: AddOrEditActions {
        data class OnCheck(val checked: Boolean): EditEmployeeActions
        data class UpdateEmployee(val employee: Result, val checked: Boolean):  EditEmployeeActions
    }
}