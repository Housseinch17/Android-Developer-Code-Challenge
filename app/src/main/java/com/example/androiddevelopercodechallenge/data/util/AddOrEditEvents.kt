package com.example.androiddevelopercodechallenge.data.util

sealed interface AddOrEditEvents {

    sealed interface AddUserEvents: AddOrEditEvents {
        data object AddUser : AddUserEvents
        data class ShowMessage(val message: String) : AddUserEvents
    }

    sealed interface EditUserEvents: AddOrEditEvents {
        data object AddUser : EditUserEvents
        data class ShowMessage(val message: String) : EditUserEvents
    }
}