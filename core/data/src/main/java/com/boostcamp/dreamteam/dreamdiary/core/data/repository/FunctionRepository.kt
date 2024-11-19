package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

class FunctionRepository(private val functions: FirebaseFunctions) {

    suspend fun helloWorld(): String {
        return try {
            val result = functions
                .getHttpsCallable("helloWorld")
                .call()
                .await()
            result.data as String
        } catch (e: Exception) {
            throw e // Handle exceptions as needed
        }
    }
}
