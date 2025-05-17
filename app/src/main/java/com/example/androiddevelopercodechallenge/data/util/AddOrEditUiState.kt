package com.example.androiddevelopercodechallenge.data.util

import com.example.androiddevelopercodechallenge.data.model.User

sealed class AddOrEditUiState(
    open val user: User,
    open val selectedCountry: Country,
    open val isCountryExpanded: Boolean,
    open val isGenderExpanded: Boolean,
) {
    data class AddUserUiState(
        override val user: User = User(),
        override val selectedCountry: Country = Country(),
        override val isCountryExpanded: Boolean = false,
        override val isGenderExpanded: Boolean = false,
    ): AddOrEditUiState(
        user = user,
        selectedCountry = selectedCountry,
        isCountryExpanded = isCountryExpanded,
        isGenderExpanded = isGenderExpanded
    )

    data class EditUserUiState(
        val readOnly: Boolean = false,
        override val user: User = User(),
        override val selectedCountry: Country = Country(),
        override val isCountryExpanded: Boolean = false,
        override val isGenderExpanded: Boolean = false,
    ): AddOrEditUiState(
        user = user,
        selectedCountry = selectedCountry,
        isCountryExpanded = isCountryExpanded,
        isGenderExpanded = isGenderExpanded
    )

}