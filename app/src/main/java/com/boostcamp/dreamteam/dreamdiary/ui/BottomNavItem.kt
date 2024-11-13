package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.boostcamp.dreamteam.dreamdiary.R

enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    MY_DREAM(
        route = "my_dream",
        icon = Icons.Outlined.Hotel,
        label = R.string.icon_text_my_dream,
    ),
    COMMUNITY(
        route = "community",
        icon = Icons.AutoMirrored.Outlined.Comment,
        label = R.string.icon_text_community,
    ),
    SETTINGS(
        route = "settings",
        icon = Icons.Outlined.Settings,
        label = R.string.icon_text_settings,
    ),
}
