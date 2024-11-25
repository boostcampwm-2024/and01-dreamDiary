package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.convertToFirebaseData
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.FunctionsSyncVersionRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.toFunctionsRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionResponse
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SynchronizeDreamDiaryRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SynchronizeDreamDiaryResponse
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import timber.log.Timber
import javax.inject.Inject

class FunctionRepository @Inject constructor(
    private val functions: FirebaseFunctions,
) {
    suspend fun syncVersion(syncVersions: List<SyncVersionRequest>): SyncVersionResponse? {
        val data = FunctionsSyncVersionRequest(
            list = syncVersions.map { it.toFunctionsRequest() },
        )
        val jsonData = Json.encodeToJsonElement(data).jsonObject.convertToFirebaseData()

        val response = functions
            .getHttpsCallable("versionCheck")
            .call(jsonData)
            .await()
            .data

        Timber.d("syncVersion response $response")

        val needSyncDiaries = mutableListOf<SyncVersionResponse.NeedSyncDiary>()
        val serverOnlyDiaries = mutableListOf<SyncVersionResponse.NeedSyncDiary>()

        if (response != null) {
            val response = response as Map<String, Any>
            if (response["isSuccess"] == true) {
                val needSyncDiariesResponse = response["needSyncDiaries"] as List<String>
                for (diaryIdNeedSync in needSyncDiariesResponse) {
                    needSyncDiaries.add(SyncVersionResponse.NeedSyncDiary(diaryIdNeedSync))
                }

                val serverOnlyDiariesResponse = response["serverOnlyDiaries"] as List<String>
                for (diaryIdNeedSync in serverOnlyDiariesResponse) {
                    serverOnlyDiaries.add(SyncVersionResponse.NeedSyncDiary(diaryIdNeedSync))
                }
            }
            return SyncVersionResponse(
                needSyncDiaries = needSyncDiaries,
                serverOnlyDiaries = serverOnlyDiaries,
            )
        }

        return null
    }

    suspend fun synchronizeDreamDiary(diary: SynchronizeDreamDiaryRequest): SynchronizeDreamDiaryResponse? {
        val diarySync = diary.toFunctionsRequest()
        val jsonData = Json.encodeToJsonElement(diarySync).jsonObject.convertToFirebaseData()

        val response = functions
            .getHttpsCallable("needSync")
            .call(jsonData)
            .await()
            .data

        Timber.d("needSync response $response")

        if (response != null) {
            val response = response as Map<String, Any>
            if (response["isSuccess"] == true) {
                val newDiary = if ("newDiary" in response) {
                    val newDiaryResponse = response["newDiary"] as Map<String, Any>
                    SynchronizeDreamDiaryResponse.NewDiary(
                        createdAt = newDiaryResponse["createdAt"] as Long,
                        labels = newDiaryResponse["labels"] as List<String>,
                        sleepEndAt = newDiaryResponse["sleepEndAt"] as Long,
                        sleepStartAt = newDiaryResponse["sleepStartAt"] as Long,
                        title = newDiaryResponse["title"] as String,
                        updatedAt = newDiaryResponse["updatedAt"] as Long,
                        content = newDiaryResponse["content"] as String
                    )
                } else {
                    null
                }
                val updateDiary = if ("updateDiary" in response) {
                    val updateDiaryResponse = response["updateDiary"] as Map<String, Any>
                    SynchronizeDreamDiaryResponse.UpdateDiary(
                        createdAt = updateDiaryResponse["createdAt"] as Long,
                        labels = updateDiaryResponse["labels"] as List<String>,
                        sleepEndAt = updateDiaryResponse["sleepEndAt"] as Long,
                        sleepStartAt = updateDiaryResponse["sleepStartAt"] as Long,
                        title = updateDiaryResponse["title"] as String,
                        updatedAt = updateDiaryResponse["updatedAt"] as Long,
                        content = updateDiaryResponse["content"] as String
                    )
                } else {
                    null
                }
                val deletedDiary = if ("deletedDiary" in response) {
                    val deletedDiaryResponse = response["deletedDiary"] as Map<String, Any>
                    SynchronizeDreamDiaryResponse.DeletedDiary(
                        deleted = deletedDiaryResponse["deleted"] as Boolean
                    )
                } else {
                    null
                }
                return SynchronizeDreamDiaryResponse(
                    currentVersion = response["currentVersion"] as String,
                    newDiary = newDiary,
                    updateDiary = updateDiary,
                    deletedDiary = deletedDiary,
                )
            }
        }
        return null
    }
}
