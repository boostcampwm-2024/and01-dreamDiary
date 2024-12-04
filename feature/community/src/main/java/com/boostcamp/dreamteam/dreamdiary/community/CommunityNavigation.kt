package com.boostcamp.dreamteam.dreamdiary.community

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.community.detail.CommunityDetailScreen
import com.boostcamp.dreamteam.dreamdiary.community.list.CommunityListScreen
import com.boostcamp.dreamteam.dreamdiary.community.list.CommunityListViewModel
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
    onGoToSignInClick: () -> Unit,
) {
    navigation<CommunityGraph>(
        startDestination = CommunityGraph.CommunityListRoute,
    ) {
        composable<CommunityGraph.CommunityListRoute> { entry ->
            val viewModel: CommunityListViewModel = hiltViewModel()
            val deletedPostId = entry.savedStateHandle.get<String>("deletedPostId")
            LaunchedEffect(deletedPostId) {
                viewModel.setDeletedPostId(deletedPostId)
                entry.savedStateHandle["deletedPostId"] = null
            }
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
                goToSignInClick = onGoToSignInClick,
            )
        }
        composable<CommunityGraph.CommunityDetailRoute> {
            CommunityDetailScreen(
                onClickBack = { deletedPostId ->
                    navController.previousBackStackEntry?.savedStateHandle?.set("deletedPostId", deletedPostId)
                    navController.popBackStack()
                },
            )
        }
        composable<CommunityGraph.CommunityWriteRoute> {
            CommunityWriteScreen(
                onClickBack = { navController.popBackStack() },
                onAddPostSuccess = { postId ->
                    navController.navigateToCommunityDetail(
                        diaryId = postId,
                        navOptions = navOptions {
                            popUpTo<CommunityGraph.CommunityWriteRoute> {
                                inclusive = true
                            }
                            launchSingleTop = true
                        },
                    )
                },
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
