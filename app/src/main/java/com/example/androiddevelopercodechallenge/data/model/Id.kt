package com.example.androiddevelopercodechallenge.data.model

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val name: String = "",
    val value: String? = ""
)
