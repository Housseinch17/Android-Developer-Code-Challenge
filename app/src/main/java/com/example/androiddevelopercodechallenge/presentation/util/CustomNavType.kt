package com.example.androiddevelopercodechallenge.presentation.util

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.androiddevelopercodechallenge.data.model.Employee
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {

    val employee = object : NavType<Employee>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): Employee? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Employee {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Employee): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Employee) {
            bundle.putString(key,Json.encodeToString(value))
        }
    }

}