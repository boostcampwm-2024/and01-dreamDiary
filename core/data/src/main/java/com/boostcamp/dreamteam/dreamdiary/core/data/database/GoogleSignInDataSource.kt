package com.boostcamp.dreamteam.dreamdiary.core.data.database

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.boostcamp.dreamteam.dreamdiary.core.data.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class GoogleSignInDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(context.getString(R.string.google_client_id))
        .setAutoSelectEnabled(true)
        .build()
    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    suspend fun requestGoogleLogin(): GoogleIdTokenCredential? {
        return try {
            val response = CredentialManager.create(context).getCredential(
                request = request,
                context = context,
            )
            GoogleIdTokenCredential.createFrom(response.credential.data)
        } catch (e: GetCredentialException) {
            Timber.e("Unexpected type of credential")
            null
        }
    }

    suspend fun signOut() {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }
}
