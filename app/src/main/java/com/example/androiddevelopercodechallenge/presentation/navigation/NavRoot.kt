package com.example.androiddevelopercodechallenge.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.androiddevelopercodechallenge.presentation.navigation.navGraphBuilder.userNavGraph

@Composable
fun NavRoot(
    navHostController: NavHostController
) {
    Scaffold { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                //padding for status bar
                .padding(top = innerPadding.calculateTopPadding()),
            navController = navHostController,
            startDestination = Screen.UsersNavGraph,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, // Or Right for the opposite direction
                    animationSpec = tween(700, easing = EaseOut)
                ) + fadeIn(animationSpec = tween(400)) + scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(400)
                ) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, // Adds the diagonal movement
                    animationSpec = tween(700, easing = EaseOut)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left, // Match the enter direction
                    animationSpec = tween(700, easing = EaseIn)
                ) + fadeOut(animationSpec = tween(400)) + scaleOut(
                    targetScale = 1.1f,
                    animationSpec = tween(400)
                ) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, // Match the enter direction
                    animationSpec = tween(700, easing = EaseIn)
                )
            },
        )
        {
            userNavGraph(navHostController = navHostController)
        }
    }
}