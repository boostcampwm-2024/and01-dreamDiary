package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class HomeBottomNavItem(
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    data object MyDream : HomeBottomNavItem(
        icon = Icons.Outlined.Hotel,
        label = R.string.icon_text_my_dream,
    )

    data object Community : HomeBottomNavItem(
        icon = Icons.AutoMirrored.Outlined.Comment,
        label = R.string.icon_text_community,
    )

    data object Setting : HomeBottomNavItem(
        icon = Icons.Outlined.Settings,
        label = R.string.icon_text_settings,
    )
}
