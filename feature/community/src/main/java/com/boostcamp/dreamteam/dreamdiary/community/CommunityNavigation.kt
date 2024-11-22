package com.boostcamp.dreamteam.dreamdiary.community

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.boostcamp.dreamteam.dreamdiary.community.list.CommunityListScreen
import kotlinx.serialization.Serializable

@Serializable
data object CommunityGraph {
    @Serializable
    data object CommunityListRoute

    @Serializable
    data class CommunityDetailRoute(
        val id: String,
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
                onNavigateToDiary = onDiaryClick,
                onNavigateToSetting = onSettingClick,
                onDiaryClick = { diaryId -> navController.navigateToCommunityDetail(diaryId) },
            )
        }
        composable<CommunityGraph.CommunityDetailRoute> { backStackEntry ->
            val id = backStackEntry.id
            Text(text = "CommunityDetailScreen: $id", modifier = Modifier.padding(16.dp))
        }
    }
}

private fun NavController.navigateToCommunityDetail(
    id: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        route = CommunityGraph.CommunityDetailRoute(id),
        navOptions = navOptions,
    )
}
