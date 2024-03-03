package com.ayush.linkup.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String = "",
    val name: String = "",
    val bio: String = "",
    val dob: String = "",
    val timestamp: Long = 0L,
    val pfp: String = "",
    val email: String = "",
    val password: String = "",
    val noOfFriends: Int = 0,
    val friends: String = "",
    val userName: String = "",
    val likedPosts: List<String> = emptyList(),
    val posts: List<String> = emptyList()
)
