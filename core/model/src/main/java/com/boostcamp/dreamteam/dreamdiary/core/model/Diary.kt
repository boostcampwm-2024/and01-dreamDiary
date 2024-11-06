package com.boostcamp.dreamteam.dreamdiary.core.model

data class Diary(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val images: List<String> = listOf(),
    val labels: List<Label> = listOf(),
)
