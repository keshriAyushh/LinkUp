package com.ayush.linkup.data.repository.impl

import android.net.Uri
import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.utils.TaskState
import com.ayush.linkup.data.utils.toFlow
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.POST_COLLECTION
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : PostRepository {

    override fun addPost(post: Post): Flow<State<Boolean>> = flow {
//        try {
        emit(State.None)
        emit(State.Loading)

        val postId = firestore.collection(POST_COLLECTION).document().id

        val currentUser = firestore.collection(USER_COLLECTION)
            .document(auth.currentUser?.uid!!)
            .get()
            .await()
            .toObject(User::class.java) ?: User()

        val randomFileName = UUID.randomUUID().toString().split("-")[0].trim()
        if (post.media != null) {
            val uploadTask = storage.reference.child("image/${post.postedBy}/$randomFileName")
                .putFile(Uri.parse(post.media))
                .await()

            val uri = storage
                .reference
                .child("image/${post.postedBy}/$randomFileName")
                .downloadUrl
                .await()
                .toString()

            firestore.collection(POST_COLLECTION)
                .document(postId)
                .set(
                    post.copy(
                        postId = postId,
                        postedAt = System.currentTimeMillis(),
                        postedBy = currentUser.userId,
                        postedByName = currentUser.name,
                        postedByPfp = currentUser.pfp,
                        media = uri,
                        mediaFileName = randomFileName
                    )
                )
                .await()
        } else {
            firestore.collection(POST_COLLECTION)
                .document(postId)
                .set(
                    post.copy(
                        postId = postId,
                        postedAt = System.currentTimeMillis(),
                        postedBy = currentUser.userId,
                        postedByName = currentUser.name,
                        postedByPfp = currentUser.pfp,
                    )
                )
                .await()
        }

        emit(State.Success(true))
    }

    override fun getPost(postId: String): Flow<State<Post>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            val post = firestore.collection(POST_COLLECTION)
                .document(postId)
                .get()
                .await()
                .toObject(Post::class.java) ?: Post()

            emit(State.Success(post))

        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }

    override fun getAllPosts(): Flow<State<List<Post>>> = callbackFlow {
        try {
            trySend(State.None)
            trySend(State.Loading)

            firestore.collection(POST_COLLECTION)
                .orderBy("postedAt", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    error?.let {
                        this.trySend(State.Error(error.message ?: ERR))
                        this.close(it)
                    }
                    value?.let {
                        val posts = it.toObjects(Post::class.java)
                        this.trySend(State.Success(posts))
                    }
                }
        } catch (e: Exception) {
            trySend(State.Error(e.message ?: ERR))
        }

        awaitClose {
            channel.close()
        }
    }


    override fun deletePost(post: Post): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            if (post.mediaFileName != "") {
                storage
                    .reference
                    .child("image/${post.postedBy}/${post.mediaFileName}")
                    .delete()
            }

            firestore.collection(POST_COLLECTION)
                .document(post.postId)
                .delete()
                .toFlow()
                .collect {
                    when (it) {
                        TaskState.Cancelled -> {
                            emit(State.Success(false))
                        }

                        is TaskState.Failure -> {
                            emit(State.Error(it.message))
                        }

                        TaskState.Success -> {
                            emit(State.Success(true))
                        }
                    }
                }

        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }
}