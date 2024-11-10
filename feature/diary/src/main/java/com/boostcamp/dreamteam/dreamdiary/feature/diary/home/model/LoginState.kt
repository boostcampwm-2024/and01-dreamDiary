package com.boostcamp.dreamteam.dreamdiary.feature.diary.home.model

sealed class LoginState {
    object NotLogin : LoginState()

    object Success : LoginState()

    data class Error(val message: String) : LoginState()
}
