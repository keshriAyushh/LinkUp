package com.ayush.linkup.presentation.screen.host

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ayush.linkup.presentation.LocalSnackbarState
import com.ayush.linkup.presentation.navigation.BottomNavGraph
import com.ayush.linkup.presentation.navigation.LocalAuthNavigator
import com.ayush.linkup.utils.BtmRoute


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HostScreen() {
    val appNavController = rememberNavController()
    val authNavController = LocalAuthNavigator.current
    val snackbarHostState = LocalSnackbarState.current

    Scaffold(
        bottomBar = {
            BottomBar(appNavController)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
    ) {
        BottomNavGraph(
            appNavHostController = appNavController,
            authNavController = authNavController
        )
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val screens = listOf(
        BtmRoute.PostsScreen,
        BtmRoute.SearchScreen,
        BtmRoute.AddPostScreen,
        BtmRoute.NotificationsScreen,
        BtmRoute.ProfileScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(50.dp))
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navHostController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BtmRoute,
    currentDestination: NavDestination?,
    navHostController: NavController
) {

    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    NavigationBarItem(
        selected = isSelected,
        onClick = {
            navHostController.navigate(screen.route) {
                popUpTo(navHostController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = {
            if (screen.title == "Create") {
                Icon(
                    imageVector = Icons.Rounded.AddCircle,
                    contentDescription = "bottom_bar_icon",
                )
            } else {
                Icon(
                    painter = painterResource(id = screen.icon),
                    contentDescription = "bottom_bar_icon",
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = Color.White
        ),
        alwaysShowLabel = false
    )
}
