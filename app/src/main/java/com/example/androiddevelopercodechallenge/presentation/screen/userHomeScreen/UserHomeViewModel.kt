package com.example.androiddevelopercodechallenge.presentation.screen.userHomeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UserHomeEvents {
    data object NavigateToAddUser : UserHomeEvents
    data class NavigateToEditUser(val user: User, val checked: Boolean) :
        UserHomeEvents

    data class DeleteUserConfirmed(val deleteUser: User) : UserHomeEvents
}

sealed interface UserHomeActions {
    data object NavigateToAddUser : UserHomeActions
    data class OnSearchQueryChanged(val newQuery: String) : UserHomeActions
    data class NavigateToEditUser(val user: User, val checked: Boolean) : UserHomeActions
    data object OnQueryClear: UserHomeActions

    data class UpdateLoader(val isLoading: Boolean) : UserHomeActions
    data class DeleteUser(val user: User) : UserHomeActions
    data class DeleteUserConfirmed(val user: User) : UserHomeActions
    data object HideDialog : UserHomeActions
    data class UpdateLoadStateAppendError(val isShowed: Boolean) : UserHomeActions
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
@HiltViewModel
class UserHomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    pager: Pager<Int, User>,
) : ViewModel() {
    private val _userHomeUiState: MutableStateFlow<UserHomeUiState> = MutableStateFlow(
        UserHomeUiState()
    )
    val userUiState: StateFlow<UserHomeUiState> = _userHomeUiState.asStateFlow()

    private val _userHomeDialogState: MutableStateFlow<UserHomeDialogState> =
        MutableStateFlow(
            UserHomeDialogState()
        )
    val userHomeDialogState: StateFlow<UserHomeDialogState> =
        _userHomeDialogState.asStateFlow()

    private val _userHomeEvents: Channel<UserHomeEvents> = Channel()
    val userHomeEvents = _userHomeEvents.receiveAsFlow()

    val userPagingFlow = pager.flow.cachedIn(viewModelScope)


    //search will load new users
    // Now your Paging flow
//    val userPagingFlow = _userHomeUiState.map {
//        it.searchQuery
//    }.debounce(300L)
//        .distinctUntilChanged()
//        .flatMapLatest { query ->
//            Pager(
//                config = PagingConfig(
//                    pageSize = 20,
//                    prefetchDistance = 4,
//                    initialLoadSize = 20,
//                    enablePlaceholders = false
//                ),
//                remoteMediator = UsersRemoteMediator(
//                    usersRepository = usersRepository,
//                    localRepository = localRepository,
//                    database = dataBase
//                ),
//                pagingSourceFactory = {
//                    dataBase.userDAO().getPagingUsersByName(searchQuery = query)
//                }
//            ).flow
//        }
//        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            Log.d("MyTag", "UserHomeViewModel: Started")
            getUser()
            readFromLocalDb()
        }
    }

    fun onActions(userHomeActions: UserHomeActions) {
        viewModelScope.launch {
            when (userHomeActions) {
                UserHomeActions.NavigateToAddUser -> navigateToAddUser()

                is UserHomeActions.OnSearchQueryChanged -> {
                    onSearchQueryChanged(query = userHomeActions.newQuery)
                }

                is UserHomeActions.NavigateToEditUser -> navigateToEditUser(
                    user = userHomeActions.user,
                    checked = userHomeActions.checked
                )

                is UserHomeActions.UpdateLoader -> updateLoader(isLoading = userHomeActions.isLoading)

                is UserHomeActions.DeleteUser -> deleteUser(user = userHomeActions.user)

                is UserHomeActions.DeleteUserConfirmed -> deleteUserConfirmed(user = userHomeActions.user)
                UserHomeActions.HideDialog -> hideDialog()
                is UserHomeActions.UpdateLoadStateAppendError -> updateLoadStateAppendError(isShowed = userHomeActions.isShowed)
                UserHomeActions.OnQueryClear -> onClearQuery()
            }
        }
    }

    private fun updateLoadStateAppendError(isShowed: Boolean) {
        _userHomeUiState.update { newState ->
            newState.copy(loadStateAppendError = isShowed)
        }
    }

    private  fun getUser() {
        Log.d("MyTag","getUser: entered")
        _userHomeUiState.update { newState->
            newState.copy(userPagingFlow = userPagingFlow)
        }
    }

    private suspend fun readFromLocalDb() {
        try {
            localRepository.getAllUsers().collect { userList ->
                Log.d("MyTag", "UserHomeViewModel: readFromLocalDb: success: ${userList.size}")
                _userHomeUiState.update { newState ->
                    newState.copy(
                        userResult = userList,
                        filteredUserResult = userList
                    )
                }
                filterUserList(query = _userHomeUiState.value.searchQuery)
            }
        } catch (e: Exception) {
            Log.e("MyTag", "UserHomeViewModel: readFromLocalDb: error: ${e.message}")
        }
    }

    private fun filterUserList(query: String) {
        _userHomeUiState.update { newState ->
            val filteredUserList = if (query.isBlank()) {
                newState.userResult
            } else {
                newState.userResult.filter { user ->
                    user.firstName.contains(query, ignoreCase = true) ||
                            user.lastName.contains(query, ignoreCase = true)
                }
            }
            newState.copy(filteredUserResult = filteredUserList)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _userHomeUiState.update { newState ->
            newState.copy(searchQuery = query)
        }
        filterUserList(query = query)
    }

    fun onClearQuery(){
        _userHomeUiState.update { newState->
            newState.copy(searchQuery = "")
        }
        filterUserList(query = "")
    }

    private fun updateLoader(isLoading: Boolean) {
        _userHomeUiState.update { newState ->
            newState.copy(isLoading = isLoading)
        }
    }

    private suspend fun navigateToEditUser(user: User, checked: Boolean) {
        _userHomeEvents.send(
            UserHomeEvents.NavigateToEditUser(
                user = user,
                checked = checked
            )
        )
    }


    private suspend fun navigateToAddUser() {
        _userHomeEvents.send(UserHomeEvents.NavigateToAddUser)
    }

    private suspend fun deleteUserConfirmed(user: User) {
        Log.d("MyTag", "deleteUserConfirmed: $user")
        showDialogProgressBar()

        //delete from db
        localRepository.deleteUsersById(id = user.id)

        delay(500)

        _userHomeEvents.send(UserHomeEvents.DeleteUserConfirmed(deleteUser = _userHomeUiState.value.currentUser))
        hideDialog()
    }

    private fun deleteUser(user: User) {
        Log.d("MyTag", "deleteUser() user: $user")
        _userHomeUiState.update { newState ->
            newState.copy(currentUser = user)
        }
        _userHomeDialogState.update { newState ->
            newState.copy(showDialog = true)
        }
    }

    private fun hideDialog() {
        _userHomeDialogState.update { newState ->
            newState.copy(showDialog = false, dialogProgressBar = false)
        }
    }

    private fun showDialogProgressBar() {
        _userHomeDialogState.update { newState ->
            newState.copy(dialogProgressBar = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MyTag", "UserHomeViewModel: Cleared!")
    }
}