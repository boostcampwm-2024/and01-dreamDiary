package com.boostcamp.dreamteam.dreamdiary.community.write

sealed class CommunityWriteEvent {
    sealed class AddPost : CommunityWriteEvent() {
        class Success(val postId: String) : AddPost()

        data object Failure : AddPost()
    }
}
