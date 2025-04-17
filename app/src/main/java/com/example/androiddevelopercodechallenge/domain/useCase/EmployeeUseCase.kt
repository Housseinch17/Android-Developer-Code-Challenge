package com.example.androiddevelopercodechallenge.domain.useCase

import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface EmployeeUseCase {
    suspend fun getEmployees(page: Int): ApiResponse
}