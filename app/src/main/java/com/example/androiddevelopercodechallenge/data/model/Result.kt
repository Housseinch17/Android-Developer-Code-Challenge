package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val cell: String = "",
    val email: String = "",
    val gender: String = "",
    val location: Location = Location(),
    val name: Name = Name(),
    val id: Id = Id(),
    val nat: String = "",
    val phone: String = "",
    val picture: Picture = Picture(),
)