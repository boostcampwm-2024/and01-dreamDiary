package com.boostcamp.dreamteam.dreamdiary.community.list

sealed class CommunityListEvent {
    sealed class LikePost : CommunityListEvent() {
        data object Success : LikePost()

        data object Failure : LikePost()
    }
}
