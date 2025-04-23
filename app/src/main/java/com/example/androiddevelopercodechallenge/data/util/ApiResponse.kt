package com.example.androiddevelopercodechallenge.data.util

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
    data object IsLoading : ApiResponse<Nothing>()
}