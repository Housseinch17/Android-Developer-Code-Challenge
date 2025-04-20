package com.example.androiddevelopercodechallenge.data.api

import com.example.androiddevelopercodechallenge.data.model.Employee
import com.example.androiddevelopercodechallenge.presentation.util.Constants.RESULTS
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getEmployees(
        @Query("page") page: Int,
        @Query("results") results: Int = RESULTS,
    ): Employee

}