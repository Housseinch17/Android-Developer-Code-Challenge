package com.example.androiddevelopercodechallenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    @Embedded
    val street: Street = Street(),
    val city: String = "",
    val country: String = "",
)