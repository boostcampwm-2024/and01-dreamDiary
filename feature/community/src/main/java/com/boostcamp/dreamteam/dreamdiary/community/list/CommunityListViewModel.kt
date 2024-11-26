package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.boostcamp.dreamteam.dreamdiary.community.model.toPostUi
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.AddCommunityPostUseCase
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor(
    private val addCommunityPostUseCase: AddCommunityPostUseCase,
    private val getCommunityPostUseCase: GetCommunityPostUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CommunityListUiState())
    val state = _state.asStateFlow()


    fun addCommunityPost() {
        // TODO
        viewModelScope.launch {
            addCommunityPostUseCase(
                author = "author",
                title = "title",
                content = "content",
            )
        }
    }

    val posts = getCommunityPostUseCase()
        .map { pagingData ->
            pagingData.map { it.toPostUi() } // CommunityDreamPost -> PostUi 변환
        }
        .cachedIn(viewModelScope)


//    fun getPosts() {
//        viewModelScope.launch {
//            getCommunityPostUseCase()
//                .cachedIn(viewModelScope) // Paging 캐싱
//                .collectLatest { pagingData ->
//                    _posts.value = pagingData
//                }
//        }
//    }
}
