package com.boostcamp.dreamteam.dreamdiary.community.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommunityListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CommunityListUiState())
    val state = _state.asStateFlow()
}