package com.example.androiddevelopercodechallenge.presentation.di

import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import com.example.androiddevelopercodechallenge.domain.repository.EmployeeRepository
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCaseImpl
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCaseImpl
import com.example.androiddevelopercodechallenge.domain.useCase.paging.PagingUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.paging.PagingUseCaseImpl
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

    @Singleton
    @Provides
    fun providePagingUseCase(
        employeeUseCase: EmployeeUseCase,
        localUseCase: LocalUseCase,
        resultDataBase: ResultDataBase
    ): PagingUseCase {
        return PagingUseCaseImpl(
            employeeUseCase = TODO(),
            localUseCase = TODO(),
            database = TODO()
        )
    }

}