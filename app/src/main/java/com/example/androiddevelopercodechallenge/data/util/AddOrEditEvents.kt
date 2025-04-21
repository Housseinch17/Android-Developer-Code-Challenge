package com.example.androiddevelopercodechallenge.data.util

sealed interface AddOrEditEvents {

    sealed interface AddEmployeeEvents: AddOrEditEvents {
        data object AddEmployee : AddEmployeeEvents
        data class ShowMessage(val message: String) : AddEmployeeEvents
    }

    sealed interface EditEmployeeEvents: AddOrEditEvents {
        data object AddEmployee : EditEmployeeEvents
        data class ShowMessage(val message: String) : EditEmployeeEvents
    }
}