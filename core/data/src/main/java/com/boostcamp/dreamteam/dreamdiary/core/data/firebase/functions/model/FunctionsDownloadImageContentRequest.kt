package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import kotlinx.serialization.Serializable

@Serializable
data class FunctionsDownloadImageContentRequest(
    val id: String,
)
