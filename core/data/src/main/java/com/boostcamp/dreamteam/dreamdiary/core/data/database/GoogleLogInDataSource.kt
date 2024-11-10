package com.boostcamp.dreamteam.dreamdiary.core.data.database

import android.content.Context
import android.content.Intent
import android.util.Log
import com.boostcamp.dreamteam.dreamdiary.core.data.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoogleLogInDataSource @Inject constructor(
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

    suspend fun handleSignInResult(data: Intent?): String? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            val account = task.await()
            account.idToken
        } catch (e: ApiException) {
            Log.e("GoogleLogin", "Google sign-in failed", e)
            null
        }
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("GoogleLogInDataSource", "User signed out successfully.")
            } else {
                Log.e("GoogleLogInDataSource", "Sign out failed.")
            }
        }
    }
}
