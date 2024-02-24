package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.Post
import com.ayush.linkup.utils.State
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun addPost(post: Post): Flow<State<Boolean>>
    fun getPost(postId: String): Flow<State<Post>>
    fun getAllPosts(): Flow<State<List<Post>>>
    fun deletePost(post: Post): Flow<State<Boolean>>
    fun updateLike(post: Post, liked: Boolean)

}