package com.example.androiddevelopercodechallenge.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET
    suspend fun getEmployees(
        @Query("page") page: Int,
        @Query("results") results: Int = 20,
    )

}