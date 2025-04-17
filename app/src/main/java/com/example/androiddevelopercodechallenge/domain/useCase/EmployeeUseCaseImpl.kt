package com.example.androiddevelopercodechallenge.domain.useCase

import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeUseCaseImpl @Inject constructor(
    private val employeeRepository: EmployeeRepository
): EmployeeUseCase{
    //we usually use UseCases when repository is used in multiple places
    //or when we need to combine multiple repositories to get single business logic
    override suspend fun getEmployees(page: Int): ApiResponse {
        return employeeRepository.getEmployees(page = page)
    }
}