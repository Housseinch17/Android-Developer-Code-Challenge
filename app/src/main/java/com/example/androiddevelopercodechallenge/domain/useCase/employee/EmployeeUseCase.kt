package com.example.androiddevelopercodechallenge.domain.useCase.employee

import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface EmployeeUseCase {
    suspend fun getEmployees(page: Int): ApiResponse
}