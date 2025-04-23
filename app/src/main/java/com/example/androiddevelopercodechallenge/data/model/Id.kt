package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Id(
    val name: String = "",
    val value: String? = ""
)
