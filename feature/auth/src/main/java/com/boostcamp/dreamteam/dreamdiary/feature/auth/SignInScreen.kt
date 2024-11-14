package com.boostcamp.dreamteam.dreamdiary.feature.auth

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInErrorMessage
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInEvent
import com.boostcamp.dreamteam.dreamdiary.feature.auth.model.SignInState
import com.boostcamp.dreamteam.dreamdiary.feature.auth.sns.Google
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onPassClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val signInState by viewModel.signInState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val google = Google(context.applicationContext)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            when (it) {
                is SignInEvent.GoogleSignInSuccess -> {
                    Toast.makeText(context, context.getString(R.string.signIn_google_success), Toast.LENGTH_SHORT).show()
                }
                is SignInEvent.GitHubSignInSuccess -> {
                    Toast.makeText(context, context.getString(R.string.signIn_github_success), Toast.LENGTH_SHORT).show()
                }
                is SignInEvent.OnPass -> {
                    Toast.makeText(context, context.getString(R.string.signIn_onPass_success), Toast.LENGTH_SHORT).show()
                }
                is SignInEvent.SignInFailure -> {
                    when (it.signInErrorMessage) {
                        SignInErrorMessage.GOOGLE_SIGN_IN_FAIL -> {
                            Toast.makeText(context, context.getString(R.string.signIn_google_fail), Toast.LENGTH_SHORT).show()
                        }
                        SignInErrorMessage.GITHUB_SIGN_IN_FAIL -> {
                            Toast.makeText(context, context.getString(R.string.signIn_github_fail), Toast.LENGTH_SHORT).show()
                        }
                        SignInErrorMessage.UNKNOWN_ERROR -> {
                            Toast.makeText(context, context.getString(R.string.signIn_unkown_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    when (signInState) {
        is SignInState.Success -> {
            onSignInSuccess()
        }

        is SignInState.OnPass -> {
            onPassClick()
        }

        is SignInState.NotSignIn -> {
            SignInScreenContent(
                onGitHubSignInClick = {
                    viewModel.signInWithGitHub(context)
                },
                onGoogleSignInClick = {
                    scope.launch {
                        try {
                            val googleIdToken = google.signIn(context)
                            viewModel.signInWithGoogle(googleIdToken)
                        } catch (e: Exception) {
                            Timber.e(e)
                        }
                    }
                },
                onNotSignInClick = {
                    viewModel.onPass()
                },
            )
        }
    }
}

@Composable
private fun SignInScreenContent(
    modifier: Modifier = Modifier,
    onGitHubSignInClick: () -> Unit = {},
    onGoogleSignInClick: () -> Unit = {},
    onNotSignInClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 50.dp)
                .padding(top = 150.dp)
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(130, 211, 224)),
                )
                Text(
                    stringResource(R.string.signIn_dream_diary),
                    fontSize = 40.sp,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlineSignInButton(
                    onGitHubSignInClick,
                    R.drawable.github_icon,
                    R.string.signIn_github_icon,
                    R.string.signIn_github_login,
                    Modifier.fillMaxWidth(),
                )
                OutlineSignInButton(
                    onGoogleSignInClick,
                    R.drawable.google_icon,
                    R.string.signIn_google_icon,
                    R.string.signIn_google_login,
                    Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.signIn_now_pass),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        textDecoration = TextDecoration.Underline,
                    ),
                    modifier = Modifier.clickable {
                        onNotSignInClick()
                    },
                )
            }
        }
    }
}

@Composable
private fun OutlineSignInButton(
    onSignInClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes iconDescription: Int,
    @StringRes signInText: Int,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onSignInClick,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(iconDescription),
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(signInText))
        }
    }
}

@Preview
@Composable
private fun SignInScreenContentPreview() {
    DreamdiaryTheme {
        SignInScreenContent()
    }
}
