package com.ayush.linkup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayush.linkup.presentation.screen.host.HostScreen
import com.ayush.linkup.presentation.screen.onboarding.OnboardingScreen
import com.ayush.linkup.utils.Constants.ROOT_ROUTE
import com.ayush.linkup.utils.Route

val LocalAuthNavigator = compositionLocalOf<NavHostController> {
    error("error")
}

@Composable
fun AppNavigation() {
    val authNavHostController = rememberNavController()
    CompositionLocalProvider(LocalAuthNavigator provides authNavHostController) {
        NavHost(
            navController = authNavHostController,
            route = ROOT_ROUTE,
            startDestination = Route.OnboardingScreen.route
        ) {
            composable(route = Route.OnboardingScreen.route) {
                OnboardingScreen()
            }
            authNavGraph(navController = authNavHostController)
            composable(route = Route.HostScreen.route) {
                HostScreen()
            }
        }
    }
}