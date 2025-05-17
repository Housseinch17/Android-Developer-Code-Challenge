package com.example.androiddevelopercodechallenge.data.api

import com.example.androiddevelopercodechallenge.data.model.Users
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users/")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Users
}