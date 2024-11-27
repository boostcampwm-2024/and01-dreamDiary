package com.boostcamp.dreamteam.dreamdiary.core.data.firebase.functions.model

import kotlinx.serialization.Serializable

@Serializable
data class FunctionsDownloadTextContentRequest(
    val id: String,
)
