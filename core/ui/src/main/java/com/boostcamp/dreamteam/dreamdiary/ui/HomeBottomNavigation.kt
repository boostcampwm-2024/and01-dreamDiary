package com.boostcamp.dreamteam.dreamdiary.ui

import androidx.annotation.StringRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

data class NavigationItem(
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    @StringRes val labelRes: Int,
    val onClick: () -> Unit,
    val isSelected: Boolean,
)

fun HomeBottomNavItem.toNavigationItem(
    onClick: () -> Unit,
    isSelected: Boolean = false,
) = NavigationItem(
    icon = icon,
    selectedIcon = selectedIcon,
    labelRes = label,
    onClick = onClick,
    isSelected = isSelected,
)

@ExperimentalMaterial3Api
@Composable
fun HomeBottomNavigation(
    items: List<NavigationItem>,
    modifier: Modifier = Modifier,
    scrollBehavior: BottomAppBarScrollBehavior? = null,
) {
    BottomAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (item.isSelected) item.selectedIcon else item.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(text = stringResource(item.labelRes)) },
                onClick = item.onClick,
                selected = item.isSelected,
            )
        }
    }
}
