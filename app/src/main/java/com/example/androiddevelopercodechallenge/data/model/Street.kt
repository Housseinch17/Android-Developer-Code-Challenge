package com.example.androiddevelopercodechallenge.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Street(
    @SerializedName("name")
    val streetName: String = "",
    val number: Int = 0
)