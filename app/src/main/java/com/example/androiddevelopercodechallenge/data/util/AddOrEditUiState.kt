package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.Result

sealed class AddOrEditUiState(
    open val employee: Result,
    open val selectedCountry: Country,
    open val isCountryExpanded: Boolean,
    open val isGenderExpanded: Boolean,
) {

    data class AddEmployeeUiState(
        override val employee: Result = Result(),
        override val selectedCountry: Country = Country(),
        override val isCountryExpanded: Boolean = false,
        override val isGenderExpanded: Boolean = false,
    ): AddOrEditUiState(
        employee = employee,
        selectedCountry = selectedCountry,
        isCountryExpanded = isCountryExpanded,
        isGenderExpanded = isGenderExpanded
    )

    data class EditEmployeeUiState(
        val readOnly: Boolean = false,
        override val employee: Result = Result(),
        override val selectedCountry: Country = Country(),
        override val isCountryExpanded: Boolean = false,
        override val isGenderExpanded: Boolean = false,
    ): AddOrEditUiState(
        employee = employee,
        selectedCountry = selectedCountry,
        isCountryExpanded = isCountryExpanded,
        isGenderExpanded = isGenderExpanded
    )

}