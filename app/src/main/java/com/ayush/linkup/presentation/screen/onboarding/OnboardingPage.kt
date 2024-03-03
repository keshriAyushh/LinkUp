package com.ayush.linkup.presentation.screen.onboarding

import androidx.annotation.DrawableRes
import com.ayush.linkup.R

sealed class OnboardingPage(
    @DrawableRes val image: Int?,
    val title: String,
    val description: String
) {
    data object First : OnboardingPage(
        image = R.drawable.screen1,
        title = "Welcome to Link Up!",
        description = "Your social hub for staying connected, sharing moments, and discovering new connections."
    )

    data object Second : OnboardingPage(
        image = R.drawable.screen2,
        title = "Explore Your Feed",
        description = "Discover what others are up to so that you always stay in the loop."
    )

    data object Third : OnboardingPage(
        image = R.drawable.screen3,
        title = "Connect Through Direct Messaging",
        description = "Stay in touch with friends through seamless direct messaging. Share media and messages instantly."
    )

    data object Fourth : OnboardingPage(
        image = R.drawable.screen4,
        title = "Start Linking Up!",
        description = "Connect, share, and engage with Link Up. Your social journey begins now."
    )
}