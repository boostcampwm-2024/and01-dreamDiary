package com.boostcamp.dreamteam.dreamdiary.feature.auth.model

sealed class SignInState {
    data object NotSignIn : SignInState()

    data object Success : SignInState()

    data class Error(val message: String) : SignInState()
}
