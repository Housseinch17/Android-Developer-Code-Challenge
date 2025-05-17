package com.example.androiddevelopercodechallenge.presentation.screen.userHomeScreen

import androidx.paging.PagingData
import com.example.androiddevelopercodechallenge.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class UserHomeUiState(
    val userPagingFlow: Flow<PagingData<User>> = emptyFlow(),
    val userResult: List<User> = emptyList(),
    val filteredUserResult: List<User> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val currentUser: User = User(),
    val loadStateAppendError: Boolean = false,
)

data class UserHomeDialogState(
    val showDialog: Boolean = false,
    val dialogProgressBar: Boolean = false,
)
