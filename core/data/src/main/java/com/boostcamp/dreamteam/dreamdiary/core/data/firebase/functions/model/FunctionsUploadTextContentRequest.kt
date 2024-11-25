package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import kotlinx.serialization.Serializable

@Serializable
data class FunctionsUploadTextContentRequest(
    val id: String,
    val text: String,
)
