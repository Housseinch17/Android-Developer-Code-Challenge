package com.example.androiddevelopercodechallenge.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.androiddevelopercodechallenge.presentation.navigation.Navigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navHostController: NavHostController
) {
    Scaffold { innerPadding->
        Navigation(
            modifier = Modifier
                .fillMaxSize()
                //padding for status bar
                .padding(top = innerPadding.calculateTopPadding()),
            navHostController = navHostController
        )
    }
}