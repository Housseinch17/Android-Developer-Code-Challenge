package com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
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
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.AddEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EditEmployeeScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EmployeeDetailScreen
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen.EmployeeNavGraph
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeEvents
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeScreen
import com.example.androiddevelopercodechallenge.presentation.screen.employeeHomeScreen.EmployeeHomeViewModel
import com.example.androiddevelopercodechallenge.presentation.theme.Background
import com.example.androiddevelopercodechallenge.presentation.util.CustomNavType
import kotlinx.coroutines.flow.collectLatest
import kotlin.reflect.typeOf

fun NavGraphBuilder.employeeNavGraph(
    navHostController: NavHostController
) {
    navigation<EmployeeNavGraph>(
        startDestination = Screen.EmployeeHomeScreen
    ) {
        composable<Screen.EmployeeHomeScreen> {
            val context = LocalContext.current

            val employeeHomeViewModel = hiltViewModel<EmployeeHomeViewModel>()
            //collectAsStateWithLifecycle
            val employeeUiState =
                employeeHomeViewModel.employeeUiState.collectAsStateWithLifecycle()

            LaunchedEffect(employeeHomeViewModel.employeeHomeEvents) {
                //collectLatest used when we want to cancel the last execution of a task
                //when new emission is emitted
                employeeHomeViewModel.employeeHomeEvents.collectLatest { event ->
                    when (event) {
                        EmployeeHomeEvents.NavigateToAddEmployee -> navHostController.navigate(
                            AddEmployeeScreen
                        )

                        is EmployeeHomeEvents.NavigateToEmployeeDetail -> navHostController.navigate(
                            EmployeeDetailScreen(employee = event.employee)
                        )

                        is EmployeeHomeEvents.DeleteEmployeeConfirmed -> {
                            Toast.makeText(
                                context,
                                event.deletedEmployee.name.first + " " + event.deletedEmployee.name.last + " has been deleted!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            EmployeeHomeScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Background)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                state = employeeUiState.value,
                onActions = employeeHomeViewModel::onActions,
            )
        }

        composable<EmployeeDetailScreen>(
            typeMap = mapOf(
                typeOf<Result>() to CustomNavType.employee
            )
        ) { entry ->

            val args = entry.toRoute<EmployeeDetailScreen>()
            Log.d("MyTag", "args: ${args.employee}")
        }

        composable<AddEmployeeScreen> { entry ->

        }

        composable<EditEmployeeScreen>(
            typeMap = mapOf(
                typeOf<Result>() to CustomNavType.employee
            )
        ) { entry ->

            val args = entry.toRoute<EditEmployeeScreen>()

            Log.d("MyTag", "args: ${args.employee}")

        }
    }
}