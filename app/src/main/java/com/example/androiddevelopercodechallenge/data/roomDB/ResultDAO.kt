package com.example.androiddevelopercodechallenge.data.roomDB

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androiddevelopercodechallenge.data.model.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllResults(results: List<Result>)

    @Query("SELECT * FROM results_table")
    fun getPagingResults(): PagingSource<Int, Result>

    @Query("SELECT * FROM results_table")
    fun getAllResults(): Flow<List<Result>>

    @Query("DELETE FROM results_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addResult(result: Result)

    //will update depending on primaryKey email
    @Update
    suspend fun updateResult(result: Result)

    //will delete depending on email
    @Query("DELETE FROM results_table WHERE email = :email")
    suspend fun deleteResultsByEmail(email: String)
}
