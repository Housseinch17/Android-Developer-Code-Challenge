package com.example.androiddevelopercodechallenge.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder.employeeNavGraph

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.EmployeeNavGraph
    ) {
        employeeNavGraph(navHostController = navHostController)
    }
}