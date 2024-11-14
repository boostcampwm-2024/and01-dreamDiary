package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    data object MyDream : BottomNavItem(
        route = "my_dream",
        icon = Icons.Outlined.Hotel,
        label = R.string.icon_text_my_dream,
    )

    data object Community : BottomNavItem(
        route = "community",
        icon = Icons.AutoMirrored.Outlined.Comment,
        label = R.string.icon_text_community,
    )

    data object Setting : BottomNavItem(
        route = "settings",
        icon = Icons.Outlined.Settings,
        label = R.string.icon_text_settings,
    )
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val isSelected: Boolean,
    val onClick: () -> Unit,
)

@Composable
fun HomeBottomNavigation(items: List<NavigationItem>) {
    BottomAppBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (item.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                label = { Text(stringResource(item.labelRes)) },
                selected = item.isSelected,
                onClick = item.onClick,
            )
        }
    }
}
