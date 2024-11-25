package com.boostcamp.dreamteam.dreamdiary.community.model

data class UserUi(
    val id: String,
    val username: String,
    val profileImageUrl: String,
)

internal val userUiPreview1 = UserUi(
    id = "1",
    username = "User 1",
    profileImageUrl = "https://picsum.photos/200/300",
)

internal val userUiPreview2 = UserUi(
    id = "2",
    username = "User 2",
    profileImageUrl = "https://picsum.photos/200/300",
)

internal val userUiPreview3 = UserUi(
    id = "3",
    username = "User 3",
    profileImageUrl = "https://picsum.photos/200/300",
)

internal val usersUiPreview = listOf(
    userUiPreview1,
    userUiPreview2,
    userUiPreview3,
)
