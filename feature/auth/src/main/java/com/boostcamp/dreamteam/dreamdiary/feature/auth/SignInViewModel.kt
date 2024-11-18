package com.boostcamp.dreamteam.dreamdiary.feature.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.AuthRepository
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInErrorMessage
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInEvent
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {
    private val _signInState = MutableStateFlow<SignInState>(SignInState.NotSignIn)
    val signInState = _signInState.asStateFlow()

    private val _event = Channel<SignInEvent>(64)
    val event = _event.receiveAsFlow()

    init {
        Timber.d("${authRepository.getUserEmail()}")

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
                _event.trySend(SignInEvent.GoogleSignInSuccess)
            } catch (e: Exception) {
                Timber.e(e)
                _event.trySend(
                    SignInEvent.SignInFailure(SignInErrorMessage.GOOGLE_SIGN_IN_FAIL),
                )
            }
        }
    }

    fun signInWithGitHub(context: Context) {
        viewModelScope.launch {
            try {
                authRepository.signInWithGitHub(context)
                _signInState.value = SignInState.Success
                _event.trySend(SignInEvent.GitHubSignInSuccess)
            } catch (e: Exception) {
                Timber.e(e)
                _event.trySend(
                    SignInEvent.SignInFailure(SignInErrorMessage.GITHUB_SIGN_IN_FAIL),
                )
            }
        }
    }

    fun onPass() {
        sharedPreferences.edit().putBoolean("onPass", true).apply()
        _signInState.value = SignInState.OnPass
        _event.trySend(SignInEvent.OnPass)
    }
}
