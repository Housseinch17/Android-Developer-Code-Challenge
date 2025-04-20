package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.presentation.util.Constants

val country = Constants.countryList.entries.first()

data class Country(
    val countryFlag: Int = country.value.countryFlag,
    val extension: String = country.value.extension
)
