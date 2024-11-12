package com.boostcamp.dreamteam.dreamdiary.core.data.database

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.boostcamp.dreamteam.dreamdiary.core.data.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleSignInDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(context.getString(R.string.google_client_id))
        .setAutoSelectEnabled(true)
        .build()
    private val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    fun getSignInRequest(): GetCredentialRequest {
        return request
    }

    suspend fun signOut() {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }
}
