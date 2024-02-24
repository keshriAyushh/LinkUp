package com.ayush.linkup.data.model

data class Comment(
    val commentId: String = "",
    val commentedBy: String = "",
    val commentedAt: Long = 0L,
    val comment: String = "",
    val onPost: String = "",
    val userPfp: String = "",
    val userName: String = ""
)
