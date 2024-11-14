package com.boostcamp.dreamteam.dreamdiary.feature.auth.model

sealed class SignInEvent {
    data object GoogleSignInSuccess : SignInEvent()

    data object GitHubSignInSuccess : SignInEvent()

    data object OnPass : SignInEvent()

    data class SignInFailure(val signInErrorMessage: SignInErrorMessage) : SignInEvent()
}

enum class SignInErrorMessage {
    GOOGLE_SIGN_IN_FAIL,
    GITHUB_SIGN_IN_FAIL,
    UNKNOWN_ERROR,
}
