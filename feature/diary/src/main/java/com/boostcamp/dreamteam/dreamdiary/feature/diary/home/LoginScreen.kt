package com.boostcamp.dreamteam.dreamdiary.feature.diary.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boostcamp.dreamteam.dreamdiary.feature.diary.R

@Composable
fun LoginScreen(
    onGitHubLogInClick: () -> Unit,
    onGoogleLogInClick: () -> Unit,
    onNotLogInClick: () -> Unit,
) {
    LoginScreenContent(
        onGitHubLogInClick = onGitHubLogInClick,
        onGoogleLogInClick = onGoogleLogInClick,
        onNotLogInClick = onNotLogInClick,
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    onGitHubLogInClick: () -> Unit = {},
    onGoogleLogInClick: () -> Unit = {},
    onNotLogInClick: () -> Unit = {},
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
                .padding(top = 150.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                color = Color(130, 211, 224),
                shape = RoundedCornerShape(10.dp),
            ) {
            }
            Text(
                stringResource(R.string.login_dream_diary),
                fontSize = 40.sp,
            )
            Spacer(modifier = Modifier.height(350.dp))
            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = onGitHubLogInClick,
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.github_icon),
                        contentDescription = stringResource(R.string.login_github_icon),
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.login_github_login))
                }
            }

            OutlinedButton(
                modifier = modifier.fillMaxWidth(),
                onClick = onGoogleLogInClick,
                shape = RoundedCornerShape(8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.google_icon),
                        contentDescription = stringResource(R.string.login_google_icon),
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.login_google_login))
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.login_now_pass),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.Underline,
                ),
                modifier = Modifier.clickable {
                    onNotLogInClick()
                },
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview() {
    // TODO: 테마 적용
    MaterialTheme {
        LoginScreenContent()
    }
}
