package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveAuthToken(token: String?) {
        Log.d("AuthRepository", "${token}")
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
}
