package com.example.androiddevelopercodechallenge.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.androiddevelopercodechallenge.data.model.User
import com.example.androiddevelopercodechallenge.data.roomDB.UserDataBase
import com.example.androiddevelopercodechallenge.data.util.ApiResponse
import com.example.androiddevelopercodechallenge.domain.repository.LocalRepository
import com.example.androiddevelopercodechallenge.domain.repository.UsersRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UsersRemoteMediator @Inject constructor(
    private val usersRepository: UsersRepository,
    private val localRepository: LocalRepository,
    private val database: UserDataBase,
) : RemoteMediator<Int, User>() {
    val limit = 20
    var currentPage = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        //total to reach to end
        val totalSize = usersRepository.getTotalSize(limit = limit, skip = 0)
        return try {
            val pageToLoad = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("UserMediator", "refresh")
                    currentPage = 0
                    0
                }

                LoadType.PREPEND -> {
                    Log.d("UserMediator", "prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    Log.d("UserMediator", "append")
                    val lastItem = state.lastItemOrNull()
                    Log.d("UserMediator","lastItem: $lastItem")
                    if (currentPage > totalSize) {
                        Log.d("UserMediator", "totalSize is: $totalSize")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    val nextPage = currentPage
                    nextPage
                }
            }

            Log.d("UserMediator", "pageToLoad: $pageToLoad")

            val response =
                usersRepository.getUsers(limit = limit, skip = pageToLoad) as ApiResponse.Success
            val users = response.data.users

            Log.d("UserMediator", "users: ${users.size}")

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localRepository.clearAndResetDatabase()
                }

                //currentPage increment here because when no internet connection
                //if the user clicks paging.retry() it will increment currentPage without loading data
                //but here only increment when data loaded and saved in local db
                currentPage += 20

                //insert data into room db
                localRepository.insertAllUsers(users = users)
            }

            val endOfPaginationReached = currentPage >= totalSize || users.isEmpty()

            Log.d("UserMediator", "endOfPaginationReached: $endOfPaginationReached")

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
