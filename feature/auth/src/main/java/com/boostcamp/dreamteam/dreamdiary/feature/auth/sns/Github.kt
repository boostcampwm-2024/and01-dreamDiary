package com.boostcamp.dreamteam.dreamdiary.feature.auth.sns

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

class Github {
    private val githubSignInUri = Uri.Builder().scheme("https").authority("github.com")
        .appendPath("login")
        .appendPath("oauth")
        .appendPath("authorize")
        .appendQueryParameter("client_id", "")
        .build()

    fun signIn(context: Context) {
        CustomTabsIntent.Builder().build().also {
            it.launchUrl(context, githubSignInUri)
        }
    }

}
