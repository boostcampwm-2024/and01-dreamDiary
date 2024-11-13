package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var user: FirebaseUser? = Firebase.auth.currentUser

    suspend fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
        user = auth.currentUser
    }

    suspend fun signOut() {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
        auth.signOut()
    }

    fun getUser(): String? {
        return user?.email
    }
}
