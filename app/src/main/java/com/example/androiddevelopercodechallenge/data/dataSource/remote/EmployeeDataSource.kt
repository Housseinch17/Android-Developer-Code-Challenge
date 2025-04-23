package com.example.androiddevelopercodechallenge.data.dataSource.remote

import com.example.androiddevelopercodechallenge.data.model.Employee
import com.example.androiddevelopercodechallenge.data.util.ApiResponse

interface EmployeeDataSource {
    suspend fun getEmployees(page: Int): ApiResponse<Employee>
}