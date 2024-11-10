package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.database.GoogleLogInDataSource
import com.boostcamp.dreamteam.dreamdiary.core.domain.usecase.LoginUseCase
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLogInDataSource: GoogleLogInDataSource,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.NotLogin)
    val loginState = _loginState.asStateFlow()

    fun getGoogleSignInIntent(): Intent {
        return googleLogInDataSource.getSignInIntent()
    }

    fun loginWithGoogle(data: Intent?) {
        viewModelScope.launch {
            val result = loginUseCase(data)
            result.onSuccess {
                _loginState.value = LoginState.Success
            }.onFailure {
                _loginState.value = LoginState.Error("Google sign-in failed")
            }
        }
    }
}
