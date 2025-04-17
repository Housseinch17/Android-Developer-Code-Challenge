package com.example.androiddevelopercodechallenge.presentation.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.androiddevelopercodechallenge.presentation.navigation.Navigation

@Composable
fun MainScreen(
    navHostController: NavHostController
) {

    Scaffold { innerPadding->
        Navigation(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            navHostController = navHostController
        )
    }

}