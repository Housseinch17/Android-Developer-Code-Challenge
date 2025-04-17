package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDataSource: EmployeeDataSource
) : EmployeeRepository {
    override suspend fun getEmployees(page: Int): ApiResponse {
        return employeeDataSource.getEmployees(page = page)
    }
}