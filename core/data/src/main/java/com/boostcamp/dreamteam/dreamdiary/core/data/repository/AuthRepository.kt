package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val provider = OAuthProvider.newBuilder("github.com")

    fun firebaseSignOut() {
        auth.signOut()
    }

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    suspend fun signInWithGitHub(activityContext: Context) {
        auth.startActivityForSignInWithProvider(activityContext as Activity, provider.build()).await()
    }

    suspend fun snsSignOut() {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    fun getUserEmail(): String? {
        auth.currentUser?.reload()
        return auth.currentUser?.email
    }

    fun getSignInProvider(): String? {
        val user = auth.currentUser
        if (user != null) {
            for (profile in user.providerData) {
                when (profile.providerId) {
                    "google.com" -> return "Google"
                    "github.com" -> return "GitHub"
                }
            }
        }
        return null
    }
}
