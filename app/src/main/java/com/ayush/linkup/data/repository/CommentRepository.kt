package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.Comment
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.utils.State
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun addComment(comment: Comment): Flow<State<Boolean>>
    fun getAllComments(postId: String): Flow<State<List<Comment>>>
    fun deleteComment(comment: Comment): Flow<State<Boolean>>
    fun updateComments(value: Int, post: Post)
}