package com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.CommentRepository
import javax.inject.Inject

class SendCommentNotificationUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
) {
    suspend operator fun invoke(
        uid: String,
        title: String,
        content: String,
    ) {
        commentRepository.sendCommentNotification(uid, title, content)
    }
}
