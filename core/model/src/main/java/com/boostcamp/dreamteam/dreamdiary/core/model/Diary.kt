package com.boostcamp.dreamteam.dreamdiary.core.model

import java.time.Instant

data class Diary(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
    val sleepStartAt: Instant = Instant.now(),
    val sleepEndAt: Instant = Instant.now(),
    val images: List<String> = listOf(),
    val labels: List<Label> = listOf(),
)
