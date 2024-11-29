package com.boostcamp.dreamteam.dreamdiary.community.detail

sealed class CommunityDetailEvent {
    sealed class CommentAdd : CommunityDetailEvent() {
        data object Success : CommentAdd()
    }
}
