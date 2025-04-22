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
import androidx.navigation.toRoute
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.util.AddOrEditActions
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.AddEmployeeEvents
import com.example.androiddevelopercodechallenge.data.util.AddOrEditEvents.EditEmployeeEvents
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.AddEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EditEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EmployeeNavGraph
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.AddEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen.AddEmployeeViewModel
import com.example.androiddevelopercodechallenge.presentation.screen.editEmployeeScreen.EditEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.screen.editEmployeeScreen.EditEmployeeViewModel
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeEvents
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeScreen
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeViewModel
import com.example.androiddevelopercodechallenge.presentation.theme.Background
import com.example.androiddevelopercodechallenge.presentation.theme.ComposableBackground
import com.example.androiddevelopercodechallenge.presentation.util.CustomNavType
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.typeOf

fun NavGraphBuilder.employeeNavGraph(
    navHostController: NavHostController
) {
    navigation<EmployeeNavGraph>(
        startDestination = Screen.EmployeeHomeScreen,
    ) {

        //EmployeeHomeScreen composable
        composable<Screen.EmployeeHomeScreen> {
            val context = LocalContext.current

            val employeeHomeViewModel = hiltViewModel<EmployeeHomeViewModel>()
            //collectAsStateWithLifecycle
            val employeeUiState by
            employeeHomeViewModel.employeeUiState.collectAsStateWithLifecycle()
            val employeeDialogState by
            employeeHomeViewModel.employeeHomeDialogState.collectAsStateWithLifecycle()

            LaunchedEffect(employeeHomeViewModel.employeeHomeEvents) {
                //collectLatest used when we want to cancel the last execution of a task
                //when new emission is emitted
                employeeHomeViewModel.employeeHomeEvents.collectLatest { event ->
                    when (event) {
                        EmployeeHomeEvents.NavigateToAddEmployee -> navHostController.navigate(
                            AddEmployeeScreen
                        )

                        is EmployeeHomeEvents.NavigateToEditEmployee -> navHostController.navigate(
                            EditEmployeeScreen(employee = event.employee, checked = event.checked)
                        )

                        is EmployeeHomeEvents.DeleteEmployeeConfirmed -> {
                            Toast.makeText(
                                context,
                                event.deletedEmployee.name.first + " " + event.deletedEmployee.name.last + " " + context.getString(
                                    R.string.has_been_deleted
                                ) + "!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            EmployeeHomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                state = employeeUiState,
                dialogState = employeeDialogState,
                onActions = employeeHomeViewModel::onActions,
            )
        }

        //AddEmployeeScreen composable
        composable<AddEmployeeScreen> { entry ->
            val addEmployeeViewModel = hiltViewModel<AddEmployeeViewModel>()
            val addEmployeeUiState by
            addEmployeeViewModel.addEmployeeUiState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            LaunchedEffect(addEmployeeViewModel.addEmployeeEvents) {
                addEmployeeViewModel.addEmployeeEvents.collectLatest { events ->
                    when (events) {
                        AddEmployeeEvents.AddEmployee -> {
                            //extension number not added in phone textField
                            //update it when saving object
                            Toast.makeText(
                                context,
                                "${addEmployeeUiState.employee.name.first} ${addEmployeeUiState.employee.name.last} ${
                                    context.getString(
                                        R.string.successfully_added
                                    )
                                }!",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d(
                                "MyTag",
                                "here: ${addEmployeeUiState.employee.copy(phone = addEmployeeUiState.selectedCountry.extension + " " + addEmployeeUiState.employee.phone)}"
                            )

                            navHostController.navigateUp()
                        }

                        is AddEmployeeEvents.ShowMessage -> {
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

            AddEmployeeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    //padding for bottom bar
                    .navigationBarsPadding()
                    .background(color = ComposableBackground)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
                state = addEmployeeUiState,
                onActions = addEmployeeViewModel::onActions,
            )
        }


        //EditEmployeeScreen composable
        composable<EditEmployeeScreen>(
            typeMap = mapOf(
                typeOf<Result>() to CustomNavType.employee
            )
        ) { entry ->

            val args = entry.toRoute<EditEmployeeScreen>()

            val editEmployeeViewModel = hiltViewModel<EditEmployeeViewModel>()
            val editEmployeeUiState by
            editEmployeeViewModel.editEmployeeUiState.collectAsStateWithLifecycle()


            Log.d("MyTag","args: ${args.checked}")
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                Log.d("MyTag", "args: ${args.employee}")
                editEmployeeViewModel.onActions(
                    AddOrEditActions.EditEmployeeActions.UpdateEmployee(
                        employee = args.employee,
                        checked = args.checked
                    )
                )
            }

            LaunchedEffect(editEmployeeViewModel.editEmployeeEvents) {
                editEmployeeViewModel.editEmployeeEvents.collectLatest { events ->
                    when (events) {
                        EditEmployeeEvents.AddEmployee -> {
                            //extension number not added in phone textField
                            //update it when saving object
                            Toast.makeText(
                                context,
                                "${editEmployeeUiState.employee.name.first} ${editEmployeeUiState.employee.name.first} ${
                                    context.getString(
                                        R.string.successfully_updated
                                    )
                                }!",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d(
                                "MyTag",
                                "here: ${editEmployeeUiState.employee.copy(phone = editEmployeeUiState.selectedCountry.extension + " " + editEmployeeUiState.employee.phone)}"
                            )

                            navHostController.navigateUp()
                        }

                        is EditEmployeeEvents.ShowMessage -> {
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
            EditEmployeeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    //padding for bottom bar
                    .navigationBarsPadding()
                    .background(color = ComposableBackground)
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
                state = editEmployeeUiState,
                onActions = editEmployeeViewModel::onActions,
            )
        }
    }
}