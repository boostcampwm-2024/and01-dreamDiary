package com.boostcamp.dreamteam.dreamdiary.core.data.repository

import androidx.credentials.GetCredentialRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.database.GoogleSignInDataSource
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
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

    fun getSignWithGoogleRequest(): GetCredentialRequest {
        return googleSignInDataSource.getSignInRequest()
    }

    suspend fun signInWithGoogle(account: GoogleIdTokenCredential): Result<Unit> {
        try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).await()
            user = auth.currentUser
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(Exception("Google sign-in failed"))
        }
    }

    suspend fun signOutWithGoogle() {
        googleSignInDataSource.signOut()
        auth.signOut()
    }
}
