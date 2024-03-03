package com.ayush.linkup.utils

sealed class Route(val route: String) {
    data object LoginScreen : Route("login")
    data object SignupScreen : Route("signup")
    data object ForgotPassScreen : Route("forgot_password")
    data object HostScreen : Route("host")
    data object AddPostScreen : Route("add_post")
    data object OnboardingScreen : Route("onboarding")
    data object CompleteProfileScreen : Route("complete_profile")
}