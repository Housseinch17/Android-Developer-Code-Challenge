package com.example.androiddevelopercodechallenge.presentation.screen.addEmployeeScreen

import com.example.androiddevelopercodechallenge.data.model.Result
import com.example.androiddevelopercodechallenge.data.util.Country

data class AddEmployeeUiState(
    val employee: Result = Result(),
    val selectedCountry: Country = Country(),
    val isCountryExpanded: Boolean = false,
    val isGenderExpanded: Boolean = false,
)
