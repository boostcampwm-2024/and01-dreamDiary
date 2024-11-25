package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.convertToFirebaseData
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.FunctionsSyncVersionRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model.toDreamDiarySyncVersion
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionRequest
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionResponse
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
            list = syncVersions.map { it.toDreamDiarySyncVersion() },
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
}
