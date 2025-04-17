package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val cell: String,
    val dob: Dob,
    val email: String,
    val gender: String,
    val location: Location,
    val login: Login,
    val name: Name,
    val nat: String,
    val phone: String,
    val picture: Picture,
    val registered: Registered
)