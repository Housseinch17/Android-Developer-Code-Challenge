package com.example.androiddevelopercodechallenge.data.roomDB

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.androiddevelopercodechallenge.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDaAO {
    @Upsert
    suspend fun insertAllUsers(users: List<User>)

    //Flow because we need live updates
    //to handle search locally 
    @Query("SELECT * FROM users_table")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users_table")
    fun getPagingUsers(): PagingSource<Int, User>

    @Query("SELECT COUNT(*) FROM users_table")
    suspend fun getUsersCount(): Int

    //not used yet because when searching the api will fetch more data until many queries found
    @Query("SELECT * FROM Users_table WHERE firstName LIKE '%' || :searchQuery || '%' OR lastName LIKE '%' || :searchQuery || '%'")
    fun getPagingUsersByName(searchQuery: String): PagingSource<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    //will delete depending on email
    @Query("DELETE FROM Users_table WHERE id = :id")
    suspend fun deleteUsersById(id: Int)

    @Query("DELETE FROM Users_table")
    suspend fun deleteAll()

    //reset uid after deleting table
    @Query("DELETE FROM sqlite_sequence WHERE name = 'users_table'")
    suspend fun resetUid()

    suspend fun clearAndResetDatabase() {
        deleteAll()      // Delete all users
        resetUid()       // Reset the auto-increment counter
    }
}