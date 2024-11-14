package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val provider = OAuthProvider.newBuilder("github.com")

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    suspend fun signInWithGitHub(activityContext: Context) {
        auth.startActivityForSignInWithProvider(activityContext as Activity, provider.build()).await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun getUserEmail(): String? {
        return auth.currentUser?.email
    }
}
