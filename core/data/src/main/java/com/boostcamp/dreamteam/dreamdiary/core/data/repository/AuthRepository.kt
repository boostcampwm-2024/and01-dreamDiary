package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import com.boostcamp.dreamteam.dreamdiary.core.data.database.GoogleSignInDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val googleSignInDataSource: GoogleSignInDataSource,
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var user: FirebaseUser? = null

    suspend fun signInWithGoogle(): Result<Unit> {
        val account = googleSignInDataSource.requestGoogleLogin()
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            user = auth.currentUser
            return Result.success(Unit)
        } else {
            return Result.failure(Exception("Google sign-in failed"))
        }
    }

    suspend fun signOutWithGoogle() {
        googleSignInDataSource.signOut()
        auth.signOut()
    }
}
