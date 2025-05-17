package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Users(
    val limit: Int,
    val skip: Int,
    val total: Int,
    val users: List<User>
)