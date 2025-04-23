package com.example.androiddevelopercodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.androiddevelopercodechallenge.presentation.navigation.NavRoot
import com.example.androiddevelopercodechallenge.presentation.theme.AndroidDeveloperCodeChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        //restore state if the application is not destroy just paused/stopped
        //avoid re-launching navigation from start destination(initial)
        //it will restore the last state
        val restoreState = savedInstanceState?.getBundle("nav_state")
        enableEdgeToEdge()
        setContent {
            AndroidDeveloperCodeChallengeTheme {
                navController = rememberNavController()
                navController.restoreState(restoreState)

                NavRoot(navHostController = navController)

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //saving state when app is in background
        if (::navController.isInitialized) {
            outState.putBundle("nav_state", navController.saveState())
        }
    }
}
