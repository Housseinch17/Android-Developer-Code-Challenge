package com.example.androiddevelopercodechallenge.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = Roboto_Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = Title
    ),
    titleMedium = TextStyle(
        fontFamily = Roboto_Semi_bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Title,
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto_Regular,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Body
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto_Regular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Label
    ),
    labelSmall = TextStyle(
        fontFamily = Roboto_Regular,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        color = Label
    )
)