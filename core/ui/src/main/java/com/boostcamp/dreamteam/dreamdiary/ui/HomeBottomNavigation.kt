package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.annotation.StringRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

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
