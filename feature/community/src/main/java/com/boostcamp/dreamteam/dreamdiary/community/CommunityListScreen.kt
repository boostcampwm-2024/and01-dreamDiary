package com.boostcamp.dreamteam.dreamdiary.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.boostcamp.dreamteam.dreamdiary.community.model.DiaryUi
import com.boostcamp.dreamteam.dreamdiary.designsystem.theme.DreamdiaryTheme
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavItem
import com.boostcamp.dreamteam.dreamdiary.ui.HomeBottomNavigation
import com.boostcamp.dreamteam.dreamdiary.ui.toNavigationItem

@Composable
fun CommunityListScreen(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    CommunityListScreenContent(
        onNavigateToDiary = onNavigateToDiary,
        onNavigateToSetting = onNavigateToSetting,
        onDiaryClick = onDiaryClick,
        modifier = modifier,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CommunityListScreenContent(
    onNavigateToDiary: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onDiaryClick: (DiaryUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationItems = listOf(
        HomeBottomNavItem.MyDream.toNavigationItem(
            onClick = onNavigateToDiary,
        ),
        HomeBottomNavItem.Community.toNavigationItem(
            onClick = { /* no-op */ },
            isSelected = true,
        ),
        HomeBottomNavItem.Setting.toNavigationItem(
            onClick = onNavigateToSetting,
        ),
    )

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.community_title)) },
            )
        },
        bottomBar = {
            HomeBottomNavigation(items = navigationItems)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
        ) {
            // TODO:
        }
    }
}

@Preview
@Composable
private fun CommunityListScreenContentPreview() {
    DreamdiaryTheme {
        CommunityListScreenContent(
            onNavigateToDiary = { },
            onNavigateToSetting = { },
            onDiaryClick = { },
        )
    }
}
