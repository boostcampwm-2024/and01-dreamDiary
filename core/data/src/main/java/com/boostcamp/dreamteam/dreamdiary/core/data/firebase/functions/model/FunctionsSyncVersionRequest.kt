package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionRequest
import kotlinx.serialization.Serializable

@Serializable
data class FunctionsSyncVersionRequest(
    val list: List<IdAndVersion>,
) {
    @Serializable
    data class IdAndVersion(
        val diaryId: String,
        val version: String,
    )
}

fun SyncVersionRequest.toFunctionsRequest(): FunctionsSyncVersionRequest.IdAndVersion {
    return FunctionsSyncVersionRequest.IdAndVersion(
        diaryId = this.id,
        version = this.version,
    )
}
