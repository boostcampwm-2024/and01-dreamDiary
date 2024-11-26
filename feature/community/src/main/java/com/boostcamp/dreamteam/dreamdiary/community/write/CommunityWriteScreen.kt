package com.boostcamp.dreamteam.dreamdiary.community.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme

@Composable
fun CommunityWriteScreen(
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommunityWriteViewModel = hiltViewModel(),
) {
    CommunityWriteScreenContent(
        topAppbarParams = CommunityWriteTopAppbarState(
            onClickBack = onClickBack,
            onClickSave = viewModel::onClickSave,
        ),
    )
}

@Composable
private fun CommunityWriteScreenContent(
    topAppbarParams: CommunityWriteTopAppbarState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CommunityWriteTopAppbar(params = topAppbarParams)
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {}
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityWriteTopAppbar(
    params: CommunityWriteTopAppbarState,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text(text = "공유 일기 작성") },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = params.onClickBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로 가기",
                )
            }
        },
        actions = {
            IconButton(
                onClick = params.onClickSave,
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "공유하기",
                )
            }
        },
    )
}

private data class CommunityWriteTopAppbarState(
    val onClickBack: () -> Unit,
    val onClickSave: () -> Unit,
)

@Preview
@Composable
private fun CommunityWriteScreenContentPreview() {
    DreamdiaryTheme {
        CommunityWriteScreenContent(
            topAppbarParams = CommunityWriteTopAppbarState(
                onClickBack = {},
                onClickSave = {},
            ),
        )
    }
}
