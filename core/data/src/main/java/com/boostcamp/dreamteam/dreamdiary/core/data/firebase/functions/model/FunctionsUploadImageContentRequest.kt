package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import kotlinx.serialization.Serializable

@Serializable
data class FunctionsUploadImageContentRequest(
    val id: String,
    val path: String,
)
