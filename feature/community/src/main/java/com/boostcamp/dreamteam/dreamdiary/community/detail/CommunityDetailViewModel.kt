package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.ViewModel
import com.boostcamp.dreamteam.dreamdiary.community.model.PostDetailUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CommunityDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun onLikeClick(postId: String) {
        // TODO
    }
}
