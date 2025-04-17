package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val city: String = "",
    val country: String = "",
    val state: String = "",
    val street: Street = Street(),
)