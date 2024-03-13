package com.ayush.linkup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ayush.linkup.presentation.screen.app.discover.DiscoverScreen
import com.ayush.linkup.presentation.screen.app.notifications.NotificationScreen
import com.ayush.linkup.presentation.screen.app.posts.AddPostScreen
import com.ayush.linkup.presentation.screen.app.posts.PostsScreen
import com.ayush.linkup.presentation.screen.app.profile.ProfileScreen
import com.ayush.linkup.presentation.screen.app.profile.UserProfileScreen
import com.ayush.linkup.utils.BtmRoute
import com.ayush.linkup.utils.Constants.APP_ROUTE
import com.ayush.linkup.utils.Route

val LocalAppNavigator = compositionLocalOf<NavHostController> {
    error("error")
}

@Composable
fun BottomNavGraph(
    authNavController: NavController,
    appNavHostController: NavHostController
) {
    CompositionLocalProvider(LocalAppNavigator provides appNavHostController) {
        NavHost(
            navController = appNavHostController,
            startDestination = BtmRoute.PostsScreen.route,
            route = APP_ROUTE
        ) {
            composable(route = BtmRoute.PostsScreen.route) {
                PostsScreen()
            }
            composable(route = BtmRoute.DiscoverScreen.route) {
                DiscoverScreen()
            }
            composable(route = BtmRoute.NotificationsScreen.route) {
                NotificationScreen()
            }
            composable(route = Route.AddPostScreen.route) {
                AddPostScreen()
            }
            composable(route = BtmRoute.ProfileScreen.route) {
                ProfileScreen({
                    authNavController.navigate(Route.LoginScreen.route) {
                        popUpTo(Route.LoginScreen.route) {
                            inclusive = true
                        }
                    }
                })
            }
            composable(
                route = "${Route.UserProfileScreen.route}/{posterId}/{currentUserId}",
                arguments = listOf(
                    navArgument("posterId") {
                        type = NavType.StringType
                    },
                    navArgument("currentUserId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                UserProfileScreen(
                    currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: "null",
                    userId = backStackEntry.arguments?.getString("posterId") ?: "null"
                )
            }
        }
    }
}