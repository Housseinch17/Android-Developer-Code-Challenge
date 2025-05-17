package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val address: String = "",
    val city: String = "",
    val postalCode: String = "",
    val state: String = "",
    val stateCode: String = ""
)