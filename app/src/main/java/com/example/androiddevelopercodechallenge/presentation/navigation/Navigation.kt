package com.example.androiddevelopercodechallenge.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder.employeeNavGraph

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = Screen.EmployeeNavGraph,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(300)) + scaleIn(
                initialScale = 0.95f,
                animationSpec = tween(300)
            )
        },
                exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300)) + scaleOut(
                targetScale = 1.05f,
                animationSpec = tween(300)
            )
        },
    )
    {
        employeeNavGraph(navHostController = navHostController)
    }
}