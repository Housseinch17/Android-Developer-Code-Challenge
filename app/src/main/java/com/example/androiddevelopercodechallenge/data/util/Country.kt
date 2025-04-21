package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.presentation.util.Constants

data class Country(
    val countryFlag: Int = Constants.countryList.entries.first().value.countryFlag,
    val extension: String = Constants.countryList.entries.first().value.extension
)