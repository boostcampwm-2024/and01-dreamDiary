package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.model

sealed class SignInState {
    object NotSignIn : SignInState()

    object Success : SignInState()

    data class Error(val message: String) : SignInState()
}
