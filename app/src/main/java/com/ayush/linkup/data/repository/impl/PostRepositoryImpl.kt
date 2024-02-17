package com.ayush.linkup.data.repository.impl

import com.ayush.linkup.data.model.Post
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.utils.TaskState
import com.ayush.linkup.data.utils.toFlow
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.POST_COLLECTION
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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : PostRepository {

    override fun addPost(post: Post): Flow<State<Boolean>> = flow {
        try {
//            var isUploadSuccess = false
            emit(State.None)
            emit(State.Loading)

            val postId = firestore.collection(POST_COLLECTION).document().id

            firestore.collection(POST_COLLECTION)
                .document(postId)
                .set(
                    post.copy(
                        postId = postId,
                        postedAt = System.currentTimeMillis(),
                        postedBy = auth.currentUser?.uid!!
                    )
                )
                .await()

            emit(State.Success(true))
//            val uniqueMediaName = UUID.randomUUID().toString().trim().split("-")[0]
//
//            val storageRef = storage.reference.child("images/$uniqueMediaName.jpg")
//
//            val postId = firestore.collection(POST_COLLECTION).document().id
//
//            storageRef.putFile(Uri.parse(post.media))
//                .toFlow()
//                .collect {
//                    isUploadSuccess = when (it) {
//                        TaskState.Cancelled -> {
//                            false
//                        }
//
//                        is TaskState.Failure -> {
//                            false
//                        }
//
//                        TaskState.Success -> {
//                            true
//                        }
//
//                        else -> {
//                            false
//                        }
//                    }
//                }

//            if (isUploadSuccess) {
//                var downloadUrl: String? = null
//                storageRef.downloadUrl.toUriFlow().collect {
//                    when (it) {
//                        StorageState.Cancelled -> {
//                            downloadUrl = null
//                        }
//
//                        is StorageState.Failure -> {
//                            downloadUrl = null
//                        }
//
//                        is StorageState.Success -> {
//                            downloadUrl = it.data
//                        }
//                    }
//                }
//                if (downloadUrl != null) {
//                    firestore.collection(POST_COLLECTION)
//                        .document(postId)
//                        .set(
//                            post.copy(
//                                postedAt = System.currentTimeMillis(),
//                                uniqueMediaName = uniqueMediaName,
//                                downloadUrl = downloadUrl!!,
//                                postedBy = auth.currentUser?.uid!!
//                            )
//                        )
//                        .toFlow()
//                        .collect {
//                            when (it) {
//                                TaskState.Cancelled -> {
//                                    emit(State.Success(false))
//                                }
//
//                                is TaskState.Failure -> {
//                                    emit(State.Error(it.message))
//                                }
//
//                                TaskState.Success -> {
//                                    emit(State.Success(true))
//                                }
//                            }
//
//                        }
//                } else {
//                    emit(State.Success(false))
//                }

        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
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

    override fun deletePost(postId: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            firestore.collection(POST_COLLECTION)
                .document(postId)
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