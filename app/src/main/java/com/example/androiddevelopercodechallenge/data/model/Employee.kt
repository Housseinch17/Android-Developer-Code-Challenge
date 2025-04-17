package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Employee(
    val info: Info = Info(),
    val results: List<Result> = emptyList()
)