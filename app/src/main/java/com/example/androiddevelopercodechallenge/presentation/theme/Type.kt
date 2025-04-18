package com.example.androiddevelopercodechallenge.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
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
    bodyMedium = TextStyle(
        fontFamily = Roboto_Regular,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Body
    ),
    labelSmall = TextStyle(
        fontFamily = Roboto_Regular,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        color = Label
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)