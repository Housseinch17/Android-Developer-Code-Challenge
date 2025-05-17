package com.example.androiddevelopercodechallenge.presentation.navigation

import com.example.androiddevelopercodechallenge.data.model.User
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    //navigation graph
    @Serializable
    data object UsersNavGraph: Screen

    //composable screens

    @Serializable
    data object UserHomeScreen: Screen

    @Serializable
    data object AddUserScreen: Screen

    @Serializable
    data class EditUserScreen(val user: User = User(id = 1), val checked: Boolean = false): Screen

}