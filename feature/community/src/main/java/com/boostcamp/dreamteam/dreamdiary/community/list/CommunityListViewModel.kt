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
import kotlinx.coroutines.flow.map
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

    // todo : uIstate에 넣기
    private val toggledLikes = MutableStateFlow<Set<String>>(emptySet())

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

    fun togglePostLike(postUi: PostUi) {
        viewModelScope.launch {
            try {
                togglePostLikeUseCase(postUi.id)
                toggledLikes.update { currentSet ->
                    if (currentSet.contains(postUi.id)) {
                        currentSet - postUi.id
                    } else {
                        currentSet + postUi.id
                    }
                }
            } catch (e: Exception) {
                _event.trySend(CommunityListEvent.LikePost.Failure)
                Timber.e(e, "Failed to toggle like for post ${postUi.id}")
            }
        }
    }

    fun notSignIn(): Boolean {
        return authRepository.getUserEmail() == null
    }
}
