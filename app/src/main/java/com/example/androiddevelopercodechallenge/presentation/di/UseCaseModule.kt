package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCaseImpl
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideEmployeeUseCase(employeeRepository: EmployeeRepository): EmployeeUseCase {
        return EmployeeUseCaseImpl(employeeRepository = employeeRepository)
    }

    @Singleton
    @Provides
    fun provideLocalUseCase(localRepository: LocalRepository): LocalUseCase {
        return LocalUseCaseImpl(
            localRepository =
                localRepository
        )
    }

}