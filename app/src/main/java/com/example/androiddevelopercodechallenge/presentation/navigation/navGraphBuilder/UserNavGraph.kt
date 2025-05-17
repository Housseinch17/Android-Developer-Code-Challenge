package com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.AddUserEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.EditUserEvents
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.AddUserScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EditUserScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.UsersNavGraph
import com.example.androiddevelopercodechallenge.presentation.screen.addUserScreen.AddUserScreen
import com.example.androiddevelopercodechallenge.presentation.screen.addUserScreen.AddUserViewModel
import com.example.androiddevelopercodechallenge.presentation.screen.editUserScreen.EditUserScreen
import com.example.androiddevelopercodechallenge.presentation.screen.editUserScreen.EditUserViewModel
import com.example.androiddevelopercodechallenge.presentation.screen.userHomeScreen.UserHomeEvents
import com.example.androiddevelopercodechallenge.presentation.screen.userHomeScreen.UserHomeScreen
import com.example.androiddevelopercodechallenge.presentation.screen.userHomeScreen.UserHomeViewModel
import com.example.androiddevelopercodechallenge.presentation.theme.Background
import com.example.androiddevelopercodechallenge.presentation.theme.ComposableBackground
import com.example.androiddevelopercodechallenge.presentation.util.CustomNavType
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.typeOf

fun NavGraphBuilder.userNavGraph(
    navHostController: NavHostController
) {
    navigation<UsersNavGraph>(
        startDestination = Screen.UserHomeScreen,
    ) {

        //UserHomeScreen composable
        composable<Screen.UserHomeScreen> {
            val context = LocalContext.current

            val userHomeViewModel = hiltViewModel<UserHomeViewModel>()
            //collectAsStateWithLifecycle
            val userUiState by
            userHomeViewModel.userUiState.collectAsStateWithLifecycle()
            val userDialogState by
            userHomeViewModel.userHomeDialogState.collectAsStateWithLifecycle()

            LaunchedEffect(userHomeViewModel.userHomeEvents) {
                //collectLatest used when we want to cancel the last execution of a task
                //when new emission is emitted
                userHomeViewModel.userHomeEvents.collectLatest { event ->
                    when (event) {
                        UserHomeEvents.NavigateToAddUser -> navHostController.navigate(
                            AddUserScreen
                        )

                        is UserHomeEvents.NavigateToEditUser -> navHostController.navigate(
                            EditUserScreen(user = event.user, checked = event.checked)
                        )

                        is UserHomeEvents.DeleteUserConfirmed -> {
                            val message = context.getString(
                                R.string.user_has_been_deleted,
                                event.deleteUser.firstName,
                                event.deleteUser.lastName
                            )
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            UserHomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                state = userUiState,
                dialogState = userDialogState,
                onActions = userHomeViewModel::onActions,
            )
        }

        //AddUserScreen composable
        composable<AddUserScreen> { entry ->
            val addUserViewModel = hiltViewModel<AddUserViewModel>()
            val addUserUiState by
            addUserViewModel.addUserUiState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(addUserViewModel.addUserEvents) {
                addUserViewModel.addUserEvents.collectLatest { events ->
                    when (events) {
                        AddUserEvents.AddUser -> {
                            val message = context.getString(
                                R.string.user_added_message,
                                addUserUiState.user.firstName,
                                addUserUiState.user.lastName
                            )
                            //extension number not added in phone textField
                            //update it when saving object
                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()

                            navHostController.navigateUp()
                        }

                        is AddUserEvents.ShowMessage -> {
                            Log.d("MyTag", events.message)
                            Toast.makeText(
                                context,
                                events.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            AddUserScreen(
                modifier = Modifier
                    .fillMaxSize()
                    //padding for bottom bar
                    .navigationBarsPadding()
                    .background(color = ComposableBackground)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
                state = addUserUiState,
                onActions = addUserViewModel::onActions,
            )
        }


        //EditUserScreen composable
        composable<EditUserScreen>(
            typeMap = mapOf(
                typeOf<User>() to CustomNavType.user
            )
        ) { entry ->
            val editUserViewModel = hiltViewModel<EditUserViewModel>()
            val editUserUiState by
            editUserViewModel.editUserUiState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(editUserViewModel.editUserEvents) {
                editUserViewModel.editUserEvents.collectLatest { events ->
                    when (events) {
                        EditUserEvents.AddUser -> {
                            //extension number not added in phone textField
                            //update it when saving object
                            Toast.makeText(
                                context,
                                "${editUserUiState.user.firstName} ${editUserUiState.user.lastName} ${
                                    context.getString(
                                        R.string.successfully_updated
                                    )
                                }!",
                                Toast.LENGTH_SHORT
                            ).show()

                            navHostController.navigateUp()
                        }

                        is EditUserEvents.ShowMessage -> {
                            Log.d("MyTag", events.message)
                            Toast.makeText(
                                context,
                                events.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            EditUserScreen(
                modifier = Modifier
                    .fillMaxSize()
                    //padding for bottom bar
                    .navigationBarsPadding()
                    .background(color = ComposableBackground)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
                state = editUserUiState,
                onActions = editUserViewModel::onActions,
            )
        }
    }
}