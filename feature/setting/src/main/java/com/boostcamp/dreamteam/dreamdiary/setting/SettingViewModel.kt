package com.boostcamp.dreamteam.dreamdiary.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _email = MutableStateFlow<String?>(authRepository.getUserEmail())
    val email = _email.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.emailFlow.collect { newEmail ->
                _email.value = newEmail
            }
        }
    }

    fun signOut() {
        authRepository.firebaseSignOut()
        viewModelScope.launch {
            authRepository.snsSignOut()
        }
        _email.value = null
    }

    fun getSignInProvider(): String? {
        return authRepository.getSignInProvider()
    }
}
