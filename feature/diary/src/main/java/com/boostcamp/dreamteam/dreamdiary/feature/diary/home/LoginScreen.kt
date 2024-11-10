package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R
import com.boostcamp.dreamteam.dreamdiary.feature.diary.home.model.LoginState

@Composable
fun LoginScreen(
    navigateToDiaryHomeScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.loginWithGoogle(result.data)
        }
    }

    when (loginState) {
        is LoginState.Success -> {
            navigateToDiaryHomeScreen()
        }
        is LoginState.Error -> {
            // TODO: 에러 처리
        }
        is LoginState.NotLogin -> {
            LoginScreenContent(
                onGitHubLogInClick = { /*TODO*/ },
                onGoogleLogInClick = {
                    val signInIntent = viewModel.getGoogleSignInIntent()
                    launcher.launch(signInIntent)
                },
                onNotLogInClick = navigateToDiaryHomeScreen,
            )
        }
    }
}

@Composable
private fun LoginScreenContent(
    onGitHubLogInClick: () -> Unit = {},
    onGoogleLogInClick: () -> Unit = {},
    onNotLogInClick: () -> Unit = {},
    modifier: Modifier = Modifier,
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
                    stringResource(R.string.login_dream_diary),
                    fontSize = 40.sp,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlineLoginButton(
                    onGitHubLogInClick,
                    R.drawable.github_icon,
                    R.string.login_github_icon,
                    R.string.login_github_login,
                    Modifier.fillMaxWidth(),
                )
                OutlineLoginButton(
                    onGoogleLogInClick,
                    R.drawable.google_icon,
                    R.string.login_google_icon,
                    R.string.login_google_login,
                    Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = stringResource(R.string.login_now_pass),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray,
                        textDecoration = TextDecoration.Underline,
                    ),
                    modifier = Modifier.clickable {
                        onNotLogInClick()
                    },
                )
            }
        }
    }
}

@Composable
private fun OutlineLoginButton(
    onLogInClick: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes iconDescription: Int,
    @StringRes loginText: Int,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onLogInClick,
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
            Text(stringResource(loginText))
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview() {
    DreamdiaryTheme {
        LoginScreenContent()
    }
}
