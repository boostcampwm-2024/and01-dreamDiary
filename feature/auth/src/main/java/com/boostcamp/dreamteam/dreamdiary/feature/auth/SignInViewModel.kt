package com.boostcamp.dreamteam.dreamdiary.feature.auth

import android.content.SharedPreferences
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
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotSignIn)
    val signInState = _signInState.asStateFlow()

    init {
        if (authRepository.getUserEmail() != null) {
            _signInState.value = SignInState.Success
        } else if (sharedPreferences.getBoolean("onPass", false)) {
            _signInState.value = SignInState.OnPass
        }
    }

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

    fun onPass() {
        sharedPreferences.edit().putBoolean("onPass", true).apply()
        _signInState.value = SignInState.OnPass
    }
}
