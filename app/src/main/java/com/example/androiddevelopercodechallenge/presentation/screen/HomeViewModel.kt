package com.example.androiddevelopercodechallenge.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.androiddevelopercodechallenge.data.paging.EmployeePaging
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.useCase.EmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val employeeUseCase: EmployeeUseCase
): ViewModel() {
    private val _list = MutableStateFlow(ApiResponse.Loading)
    val list = _list.asStateFlow()

    val resultsPager = Pager(
        //here pageSize how many items i have in each page
        //prefetchDistance when to load new items for example here 5 so im saying
        //when we reach the last 5 items load new page
        //initialLoadSize how many items i want  to load initial
        PagingConfig(pageSize = 20, prefetchDistance = 5, initialLoadSize = 20)
    ) {
        EmployeePaging(employeeUseCase = employeeUseCase)
    }.flow.cachedIn(viewModelScope)

}