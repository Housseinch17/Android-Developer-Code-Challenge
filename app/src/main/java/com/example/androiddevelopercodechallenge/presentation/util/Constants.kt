package com.example.androiddevelopercodechallenge.presentation.util
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.Country

object Constants {
    val gender = listOf<String>("male", "female")

    val countryList: Map<String, Country> = mapOf(
        "Lebanon" to Country(countryFlag = R.drawable.lebanon, extension = "+961"),
        "France" to Country(countryFlag = R.drawable.france, extension = "+33"),
        "United Arab Emirates" to Country(countryFlag = R.drawable.emirate, extension = "+971"),
        "Denmark" to Country(countryFlag = R.drawable.denmark, extension = "+45"),
    )

}