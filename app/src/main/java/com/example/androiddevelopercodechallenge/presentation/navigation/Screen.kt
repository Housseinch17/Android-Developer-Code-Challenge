package com.example.androiddevelopercodechallenge.presentation.navigation

import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    //navigation graph
    @Serializable
    data object EmployeeNavGraph: Screen

    //composable screens

    @Serializable
    data object EmployeeHomeScreen: Screen

    @Serializable
    data object AddEmployeeScreen: Screen

    @Serializable
    data class EditEmployeeScreen(val employee: Result = Result(), val checked: Boolean = false): Screen

}