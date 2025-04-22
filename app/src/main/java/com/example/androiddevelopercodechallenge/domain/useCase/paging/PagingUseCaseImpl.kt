package com.example.androiddevelopercodechallenge.domain.useCase.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.paging.EmployeeRemoteMediator
import com.example.androiddevelopercodechallenge.data.roomDB.ResultDataBase
import com.example.androiddevelopercodechallenge.domain.useCase.employee.EmployeeUseCase
import com.example.androiddevelopercodechallenge.domain.useCase.local.LocalUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PagingUseCaseImpl @Inject constructor(
    private val employeeUseCase: EmployeeUseCase,
    private val localUseCase: LocalUseCase,
    private val database: ResultDataBase
): PagingUseCase {
//    @OptIn(ExperimentalPagingApi::class)
//    override fun getEmployeesPager(): Flow<PagingData<Result>> {
//        return Pager(
//            config = PagingConfig(pageSize = 20, prefetchDistance = 8, initialLoadSize = 20),
//            remoteMediator = EmployeeRemoteMediator(employeeUseCase, localUseCase, database),
//            pagingSourceFactory = { localUseCase.getPagingResults() }
//        ).flow
//    }
}