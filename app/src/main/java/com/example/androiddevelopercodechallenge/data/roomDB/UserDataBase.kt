package com.example.androiddevelopercodechallenge.data.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androiddevelopercodechallenge.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDAO(): UserDaAO
}