package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.Employee

sealed class ApiResponse {
    data class Success(val employee: Employee): ApiResponse()
    data class Error(val message: String): ApiResponse()
    object Loading: ApiResponse()
}