package com.example.androiddevelopercodechallenge.data.dataSource

import com.example.androiddevelopercodechallenge.data.dataSource.remote.EmployeeDataSource
import com.example.androiddevelopercodechallenge.data.model.Employee
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDataSource: EmployeeDataSource
) : EmployeeRepository {
    override suspend fun getEmployees(page: Int): ApiResponse<Employee> {
        return employeeDataSource.getEmployees(page = page)
    }
}