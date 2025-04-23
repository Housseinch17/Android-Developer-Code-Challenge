package com.example.androiddevelopercodechallenge.presentation.util
import com.example.androiddevelopercodechallenge.R
import com.example.androiddevelopercodechallenge.data.util.Country

object Constants {

    //static results
    //usually fetched from api but here we use it as constant
    const val RESULTS = 20

    val gender = listOf<String>("male", "female")

    val numberRegex = Regex("^\\d+$")

    //if list is small it will show below dropDownMenu
    //if list is big it will show above dropDownMenu
    val countryList: Map<String, Country> = mapOf(
        "Lebanon" to Country(countryFlag = R.drawable.lebanon, extension = "+961"),
        "France" to Country(countryFlag = R.drawable.france, extension = "+33"),
        "United Arab Emirates" to Country(countryFlag = R.drawable.emirate, extension = "+971"),
        "Denmark" to Country(countryFlag = R.drawable.denmark, extension = "+45"),
    )

}