package com.boostcamp.dreamteam.dreamdiary.core.data.database

import android.content.Context
import android.content.Intent
import com.boostcamp.dreamteam.dreamdiary.core.data.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class GoogleSignInDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_client_id))
            .requestEmail()
            .build(),
    )

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun handleSignInResult(data: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            val account = task.await()
            account
        } catch (e: Exception) {
            Timber.e(e, "Google Sign-in failed")
            null
        }
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.d("User log out successfully.")
            } else {
                Timber.e("log out failed.")
            }
        }
    }
}
