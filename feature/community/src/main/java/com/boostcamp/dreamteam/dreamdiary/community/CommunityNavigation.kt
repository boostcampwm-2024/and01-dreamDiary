package com.boostcamp.dreamteam.dreamdiary.community

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.community.detail.CommunityDetailScreen
import com.boostcamp.dreamteam.dreamdiary.community.list.CommunityListScreen
import com.boostcamp.dreamteam.dreamdiary.community.write.CommunityWriteScreen
import kotlinx.serialization.Serializable

@Serializable
data object CommunityGraph {
    @Serializable
    data object CommunityListRoute

    @Serializable
    data class CommunityDetailRoute(
        val id: String,
    )

    @Serializable
    data class CommunityWriteRoute(
        val diaryId: String? = null,
    )
}

fun NavGraphBuilder.communityGraph(
    navController: NavHostController,
    onDiaryClick: () -> Unit,
    onSettingClick: () -> Unit,
) {
    navigation<CommunityGraph>(
        startDestination = CommunityGraph.CommunityListRoute,
    ) {
        composable<CommunityGraph.CommunityListRoute> {
            CommunityListScreen(
                onClickFab = { navController.navigateToCommunityWrite() },
                onNavigateToDiary = onDiaryClick,
                onNavigateToSetting = onSettingClick,
                onDiaryClick = { diaryId ->
                    navController.navigateToCommunityDetail(
                        diaryId = diaryId,
                        navOptions = navOptions { launchSingleTop = true },
                    )
                },
            )
        }
        composable<CommunityGraph.CommunityDetailRoute> {
            CommunityDetailScreen(
                onClickBack = { navController.popBackStack() },
            )
        }
        composable<CommunityGraph.CommunityWriteRoute> {
            CommunityWriteScreen(
                onClickBack = { navController.popBackStack() },
            )
        }
    }
}

fun NavController.navigateToCommunityWrite(
    diaryId: String? = null,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = CommunityGraph.CommunityWriteRoute(diaryId),
        navOptions = navOptions,
    )
}

private fun NavController.navigateToCommunityDetail(
    diaryId: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = CommunityGraph.CommunityDetailRoute(diaryId),
        navOptions = navOptions,
    )
}
