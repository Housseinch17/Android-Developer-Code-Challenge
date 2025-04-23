package com.example.androiddevelopercodechallenge.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Picture(
    val large: String = "https://th.bing.com/th/id/OIP.-n6mzk6Qt5Tu-I3ek1It1gHaHa?w=196&h=196&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    val medium: String = "https://th.bing.com/th/id/OIP.-n6mzk6Qt5Tu-I3ek1It1gHaHa?w=196&h=196&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    val thumbnail: String = "https://th.bing.com/th/id/OIP.Od4m4w455EEToOQDKESqvgHaFJ?w=286&h=198&c=7&r=0&o=5&dpr=1.3&pid=1.7"
)