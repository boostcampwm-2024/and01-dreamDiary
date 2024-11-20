package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.convertToFirebaseData
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.DreamDiarySync
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.DreamDiarySyncResponse
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.toDreamDiarySync
import com.boostcamp.dreamteam.dreamdiary.core.model.Diary
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import javax.inject.Inject

class FunctionRepository @Inject constructor(
    private val functions: FirebaseFunctions,
) {

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

    suspend fun syncDreamDiaries(diaries: List<Diary>): List<DreamDiarySyncResponse> {
        val data = DreamDiarySync(
            list = diaries.map { it.toDreamDiarySync() }
        )
        val jsonData = Json.encodeToJsonElement(data).jsonObject.convertToFirebaseData()

        val response = functions
            .getHttpsCallable("needSync")
            .call(jsonData)
            .await()
            .data

        val syncs = mutableListOf<DreamDiarySyncResponse>()

        if (response != null) {
            val response = response as Map<String, Any>
            if (response["isSuccess"] == true) {
                val updatedVersions = response["updatedVersions"] as List<Map<String, Any>>
                for (updatedVersion in updatedVersions) {
                    val diaryId = updatedVersion["diaryId"] as String
                    val version = updatedVersion["version"] as Long
                    syncs.add(DreamDiarySyncResponse(
                        diaryId = diaryId,
                        version = version
                    ))
                }
            }
        }

        return syncs
    }
}
