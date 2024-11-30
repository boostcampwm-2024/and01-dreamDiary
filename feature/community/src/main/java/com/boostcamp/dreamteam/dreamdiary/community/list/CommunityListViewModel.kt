package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.PostUi
import com.boostcamp.dreamteam.dreamdiary.community.model.toPostUi
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostsUseCase
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase,
    private val togglePostLikeUseCase: TogglePostLikeUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _event = Channel<CommunityListEvent>(64)
    val event = _event.receiveAsFlow()

    private val toggledLikes = MutableStateFlow<Set<String>>(emptySet())
    private val toggleLike = Channel<String>()
    private val toggleLikeFlow = toggleLike.consumeAsFlow()
        .onEach {
            togglePostLikeInternal(it)
        }

    val posts = getCommunityPostsUseCase()
        .map { pagingData ->
            pagingData.map { it.toPostUi() }
        }
        .cachedIn(viewModelScope)
        .combine(toggledLikes) { pagingData, toggledLikesSet ->
            pagingData.map { postUi ->
                if (toggledLikesSet.contains(postUi.id)) {
                    postUi.copy(
                        isLiked = !postUi.isLiked,
                    )
                } else {
                    postUi
                }
            }
        }

    init {
        toggleLikeFlow.launchIn(viewModelScope)
    }

    fun togglePostLike(postUi: PostUi) {
        viewModelScope.launch {
            toggleLike.trySend(postUi.id)
        }
    }

    fun notSignIn(): Boolean {
        return authRepository.getUserEmail() == null
    }

    // 내부 함수: 실제로 좋아요 토글 처리
    private suspend fun togglePostLikeInternal(postId: String) {
        val copiedToggledLikes = toggledLikes.value.toSet()
        try {
            toggledLikes.update { currentSet ->
                if (currentSet.contains(postId)) {
                    currentSet - postId
                } else {
                    currentSet + postId
                }
            }
            togglePostLikeUseCase(postId)
        } catch (e: Exception) {
            // 실패하면 복원하기
            toggledLikes.value = copiedToggledLikes
            _event.trySend(CommunityListEvent.LikePost.Failure)
            Timber.e(e, "Failed to toggle like for post $postId")
        }
    }
}
