package com.ayush.linkup.data.repository.impl

import android.util.Log
import com.ayush.linkup.data.model.Comment
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.CommentRepository
import com.ayush.linkup.utils.Constants
import com.ayush.linkup.utils.Constants.COMMENTS_COLLECTION
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.POST_COLLECTION
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CommentRepository {
    override fun addComment(comment: Comment): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            val commentId = firestore.collection(POST_COLLECTION)
                .document(comment.onPost)
                .collection(COMMENTS_COLLECTION)
                .document().id

            val user = firestore.collection(USER_COLLECTION)
                .document(auth.currentUser?.uid!!)
                .get()
                .await()
                .toObject(User::class.java) ?: User()

            firestore.collection(POST_COLLECTION)
                .document(comment.onPost)
                .collection(COMMENTS_COLLECTION)
                .document(commentId)
                .set(
                    comment.copy(
                        commentedBy = auth.currentUser?.uid!!,
                        commentedAt = System.currentTimeMillis(),
                        commentId = commentId,
                        userName = user.name,
                        userPfp = user.pfp
                    )
                )
                .await()

            emit(State.Success(true))
        } catch (e: Exception) {
            emit(State.Error(e.localizedMessage ?: Constants.ERR))
        }
    }

    override fun getAllComments(postId: String): Flow<State<List<Comment>>> = callbackFlow {
        try {
            trySend(State.None)
            trySend(State.Loading)

            firestore.collection(POST_COLLECTION)
                .document(postId)
                .collection(COMMENTS_COLLECTION)
                .orderBy("commentedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        trySend(State.Error(error.message ?: Constants.ERR))
                        this.cancel()
                    }
                    value?.let {
                        val comments = it.toObjects(Comment::class.java)
                        trySend(State.Success(comments))
                    }
                }
        } catch (e: Exception) {
            trySend(State.Error(e.message ?: Constants.ERR))
        }
        awaitClose {
            this.cancel()
        }
    }

    override fun deleteComment(comment: Comment): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            firestore.collection(POST_COLLECTION)
                .document(comment.onPost)
                .collection(COMMENTS_COLLECTION)
                .document(comment.commentId)
                .delete()
                .await()

            emit(State.Success(true))

        } catch (e: Exception) {
            emit(State.Error(e.message ?: Constants.ERR))
        }
    }

    override fun updateComments(value: Int, post: Post) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                firestore.collection(POST_COLLECTION)
                    .document(post.postId)
                    .update("comments", (post.comments + value))
                    .await()
            }
        } catch (e: Exception) {
            Log.d("commentUpdateError", e.message ?: ERR)
        }
    }
}