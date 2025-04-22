package com.example.androiddevelopercodechallenge.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androiddevelopercodechallenge.presentation.util.Constants
import kotlinx.serialization.Serializable

@Entity(tableName = "results_table")
@Serializable
data class Result(
    @PrimaryKey
    val email: String = "",
    @Embedded
    val id: Id = Id(),
    @Embedded
    val name: Name = Name(),
    val phone: String = "",
    val gender: String = Constants.gender.first(),
    val cell: String = "",
    @Embedded
    val location: Location = Location(),
    @Embedded
    val picture: Picture = Picture(),
)