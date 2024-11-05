package com.boostcamp.dreamteam.dreamdiary.core.model

data class Diary(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val images: List<String>,
    val labels: List<Label>,
)
