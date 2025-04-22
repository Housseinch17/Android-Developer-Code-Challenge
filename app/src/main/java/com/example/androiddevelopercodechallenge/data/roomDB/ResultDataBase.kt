package com.example.androiddevelopercodechallenge.data.roomDB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androiddevelopercodechallenge.data.model.Result

@Database(
    entities = [Result::class],
    version = 1,
    exportSchema = false
)
abstract class ResultDataBase: RoomDatabase() {
    abstract fun resultDAO(): ResultDAO
}