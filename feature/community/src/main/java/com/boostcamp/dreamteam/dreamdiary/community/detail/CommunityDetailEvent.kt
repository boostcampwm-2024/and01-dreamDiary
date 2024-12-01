package com.boostcamp.dreamteam.dreamdiary.community.detail

sealed class CommunityDetailEvent {
    sealed class CommentAdd : CommunityDetailEvent() {
        data object Success : CommentAdd()
    }

    sealed class LikePost : CommunityDetailEvent() {
        data object Success : LikePost()

        data object Fail : LikePost()
    }
}
