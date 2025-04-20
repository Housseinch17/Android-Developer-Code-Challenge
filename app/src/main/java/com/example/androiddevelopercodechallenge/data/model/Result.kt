package com.example.androiddevelopercodechallenge.data.model

import com.example.androiddevelopercodechallenge.presentation.util.Constants
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val cell: String = "",
    val email: String = "",
    val gender: String = Constants.gender.first(),
    val location: Location = Location(),
    val name: Name = Name(),
    val id: Id = Id(),
    val nat: String = "",
    val phone: String = "",
    val picture: Picture = Picture(),
)