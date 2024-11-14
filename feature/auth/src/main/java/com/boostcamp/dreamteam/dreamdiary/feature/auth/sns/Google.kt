package com.boostcamp.dreamteam.dreamdiary.feature.auth.sns

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class Google(
    private val applicationContext: Context,
) {
    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(applicationContext.getString(R.string.google_client_id))
        .setAutoSelectEnabled(true)
        .build()

    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    suspend fun signIn(context: Context): String {
        val credentialResponse = CredentialManager
            .create(context)
            .getCredential(
                context,
                request,
            )
        val googleIdToken = GoogleIdTokenCredential
            .createFrom(credentialResponse.credential.data)
            .idToken
        return googleIdToken
    }
}
