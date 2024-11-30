package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.toPostUi
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostsUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.TogglePostLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val getCommunityPostsUseCase: GetCommunityPostsUseCase,
    private val toggleLikeUseCase: TogglePostLikeUseCase,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val posts = getCommunityPostsUseCase()
        .map { pagingData ->
            pagingData.map { it.toPostUi() } // CommunityDreamPost -> PostUi 변환
        }
        .cachedIn(viewModelScope)

    fun togglePostLike(postId: String) {
        viewModelScope.launch {
            try {
                toggleLikeUseCase(postId)
            } catch (e: Exception) {
                Timber.e(e, "Failed to toggle like for post $postId")
            }
        }
    }

    fun notSignIn(): Boolean {
        return authRepository.getUserEmail() == null
    }
}
