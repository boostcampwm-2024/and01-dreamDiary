package com.boostcamp.dreamteam.dreamdiary.feature.auth

import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInState
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotSignIn)
    val signInState = _signInState.asStateFlow()

    fun getSignInWithGoogleRequest(): GetCredentialRequest {
        return authRepository.getSignWithGoogleRequest()
    }

    suspend fun signInWithGoogle(account: GoogleIdTokenCredential) {
        val result = authRepository.signInWithGoogle(account)
        result.onSuccess {
            _signInState.value = SignInState.Success
        }.onFailure {
            _signInState.value = SignInState.Error("Google sign-in failed")
        }
    }
}
