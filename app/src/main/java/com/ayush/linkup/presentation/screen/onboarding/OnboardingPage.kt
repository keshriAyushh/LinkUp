package com.ayush.linkup.presentation.screen.onboarding

import androidx.annotation.DrawableRes

sealed class OnboardingPage(
    @DrawableRes val image: Int?,
    val title: String,
    val description: String
) {
    data object First : OnboardingPage(
        image = null,
        title = "first",
        description = "first desc"
    )

    data object Second : OnboardingPage(
        image = null,
        title = "second",
        description = "second desc"
    )

    data object Third : OnboardingPage(
        image = null,
        title = "third",
        description = "third desc"
    )
}