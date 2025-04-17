package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val page: Int = 0,
    val results: Int = 0,
    val seed: String = "",
    val version: String = ""
)