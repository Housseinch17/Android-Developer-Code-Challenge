package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface EmployeeDataSource {
    suspend fun getEmployees(page: Int): ApiResponse
}