package com.example.androiddevelopercodechallenge.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androiddevelopercodechallenge.presentation.util.Constants
import kotlinx.serialization.Serializable

@Entity(tableName = "users_table")
@Serializable
data class User(
    @PrimaryKey(autoGenerate = true)
    //uid = 0 or null otherwise room db will not generate a new uid and all of the users
    //will have the same uid
    val uid: Int = 0,
    //can't use id as primary key because we can't update the api
    //each user added will be replaced/removed when the new user fetched with this id
    val id: Int = 1,
    val email: String = "",
    val lastName: String = "",
    val firstName: String = "",
    val gender: String = Constants.gender.first(),
    val image: String = "https://th.bing.com/th/id/OIP.-n6mzk6Qt5Tu-I3ek1It1gHaHa?w=196&h=196&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    val phone: String = "",
    @Embedded
    val address: Address = Address(),
)