package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import com.boostcamp.dreamteam.dreamdiary.community.model.toPostUi
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostsUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetPostIsLikeUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.TogglePostLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase,
    private val togglePostLikeUseCase: TogglePostLikeUseCase,
    private val getPostIsLikeUseCase: GetPostIsLikeUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _event = Channel<CommunityListEvent>(64)
    val event = _event.receiveAsFlow()

    private val toggledLikes = MutableStateFlow<Map<String, Boolean>>(emptyMap())

    private val toggleLike = Channel<String>()
    private val toggleLikeFlow = toggleLike.consumeAsFlow()
        .onEach {
            togglePostLikeInternal(it)
        }

    val posts = getCommunityPostsUseCase()
        .map { pagingData -> pagingData.map { it.toPostUi() } }
        .cachedIn(viewModelScope)
        .combine(toggledLikes) { pagingData, toggledLikesMap ->
            pagingData.map { postUi ->
                postUi.applyToggleLike(toggledLikesMap[postUi.id])
            }
        }

    private fun PostUi.applyToggleLike(toggleIsLike: Boolean?): PostUi {
        return toggleIsLike?.let { toggled ->
            if (toggled != isLiked) {
                val adjustLikeCount = likeCount + if (toggled) 1 else -1
                copy(isLiked = toggled, likeCount = adjustLikeCount)
            } else {
                this
            }
        } ?: this
    }

    init {
        toggleLikeFlow.launchIn(viewModelScope)
    }

    fun togglePostLike(postUi: PostUi) {
        toggleLike.trySend(postUi.id)
    }

    fun notSignIn(): Boolean {
        return authRepository.getUserEmail() == null
    }

    // 내부 함수: 실제로 좋아요 토글 처리
    private suspend fun togglePostLikeInternal(postId: String) {
        val previousToggledLikes = toggledLikes.value.toMap()
        try {
            // 현재 상태 가져오기
            val currentIsLiked = toggledLikes.value[postId] ?: getCurrentIsLiked(postId)
            val newIsLiked = !currentIsLiked
            toggledLikes.update { currentMap ->
                currentMap + (postId to newIsLiked)
            }
            togglePostLikeUseCase(postId)
        } catch (e: Exception) {
            // 실패 시 이전 상태로 복원
            toggledLikes.value = previousToggledLikes
            _event.trySend(CommunityListEvent.LikePost.Failure)
            Timber.e(e, "Failed to toggle like for post $postId")
        }
    }

    private suspend fun getCurrentIsLiked(postId: String): Boolean {
        return getPostIsLikeUseCase(postId)
    }
}
