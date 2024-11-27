package com.boostcamp.dreamteam.dreamdiary.community.detail

import androidx.lifecycle.ViewModel
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.community.GetCommunityPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val getCommunityPostUseCase: GetCommunityPostUseCase
) : ViewModel() {

}
