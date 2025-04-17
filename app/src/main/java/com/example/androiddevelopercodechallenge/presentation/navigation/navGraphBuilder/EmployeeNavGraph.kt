package com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.androiddevelopercodechallenge.data.model.Employee
import com.example.androiddevelopercodechallenge.presentation.navigation.Screen
import com.example.androiddevelopercodechallenge.presentation.util.CustomNavType
import kotlin.reflect.typeOf

fun NavGraphBuilder.employeeNavGraph(
    navHostController: NavHostController
) {
    navigation<Screen.EmployeeNavGraph>(
        startDestination = Screen.EmployeeHomeScreen
    ) {
        composable<Screen.EmployeeHomeScreen> { entry ->

        }

        composable<Screen.EmployeeDetailScreen>(
            typeMap = mapOf(
                typeOf<List<Employee>>() to CustomNavType.employee
            )
        ) { entry ->

        }

        composable<Screen.AddEmployeeScreen> { entry ->

        }

        composable<Screen.EditEmployeeScreen>(
            typeMap = mapOf(
                typeOf<List<Employee>>() to CustomNavType.employee
            )
        ) { entry ->

        }
    }
}