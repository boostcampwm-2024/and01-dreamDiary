package com.boostcamp.dreamteam.dreamdiary.feature.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.SignInUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotSignIn)
    val signInState = _signInState.asStateFlow()

    fun getGoogleSignInIntent(): Intent {
        return signInUseCase.getGoogleSignIntent()
    }

    fun signInWithGoogle(data: Intent?) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { signInUseCase(data) }
            result.onSuccess {
                _signInState.value = SignInState.Success
            }.onFailure {
                _signInState.value = SignInState.Error("Google sign-in failed")
            }
        }
    }
}
