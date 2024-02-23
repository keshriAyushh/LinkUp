package com.ayush.linkup.data.model

data class Post(
    val postId: String = "",
    val media: String? = null,
    val text: String = "",
    val postedBy: String = "",
    val postedAt: Long = 0L,
    val uniqueMediaName: String = "",
    val downloadUrl: String = "",
    var isLiked: Boolean = false,
    val postedByPfp: String = "",
    val postedByName: String = "",
    val mediaFileName: String = ""
)
