package com.example.androiddevelopercodechallenge.domain.repository

import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface EmployeeRepository {
    suspend fun getEmployees(page: Int): ApiResponse
}