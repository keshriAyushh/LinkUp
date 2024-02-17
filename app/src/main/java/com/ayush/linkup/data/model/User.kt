package com.ayush.linkup.data.model

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
    val userName: String = ""
)
