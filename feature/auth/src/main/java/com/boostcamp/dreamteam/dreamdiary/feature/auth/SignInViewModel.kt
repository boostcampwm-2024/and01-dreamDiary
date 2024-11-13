package com.boostcamp.dreamteam.dreamdiary.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotSignIn)
    val signInState = _signInState.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            try {
                authRepository.signInWithGoogle(idToken)
                _signInState.value = SignInState.Success
            } catch (e: Exception) {
                _signInState.value = SignInState.Error("Google sign-in failed")
            }
        }
    }
}
