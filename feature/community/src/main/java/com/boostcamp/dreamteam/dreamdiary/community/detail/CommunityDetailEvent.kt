package com.boostcamp.dreamteam.dreamdiary.community.detail

sealed class CommunityDetailEvent {
    sealed class PostDelete : CommunityDetailEvent() {
        data object Success : PostDelete()
    }

    sealed class CommentAdd : CommunityDetailEvent() {
        data object Success : CommentAdd()
    }

    sealed class CommentDelete : CommunityDetailEvent() {
        data object Success : CommentDelete()
    }

    sealed class LikePost : CommunityDetailEvent() {
        data object Success : LikePost()

        data object Failure : LikePost()
    }
}
