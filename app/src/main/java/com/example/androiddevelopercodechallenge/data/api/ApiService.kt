package com.example.androiddevelopercodechallenge.data.api

import com.example.androiddevelopercodechallenge.data.model.Employee
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getEmployees(
        @Query("page") page: Int,
        @Query("results") results: Int = 20,
    ): Employee

}