package com.ayush.linkup.utils

import androidx.annotation.DrawableRes
import com.ayush.linkup.R

sealed class BtmRoute(
    val route: String,
    @DrawableRes val icon: Int,
    val title: String
) {

    data object PostsScreen : BtmRoute(
        route = "posts",
        icon = R.drawable.post,
        title = "Posts"
    )

    data object DiscoverScreen : BtmRoute(
        route = "discover",
        icon = R.drawable.search,
        title = "Discover"
    )

    data object AddPostScreen : BtmRoute(
        route = "add_post",
        icon = R.drawable.search,
        title = "Create"
    )

    data object NotificationsScreen : BtmRoute(
        route = "notification",
        icon = R.drawable.notification,
        title = "Notifications"
    )

    data object ProfileScreen : BtmRoute(
        route = "profile",
        icon = R.drawable.profile,
        title = "Profile"
    )

}