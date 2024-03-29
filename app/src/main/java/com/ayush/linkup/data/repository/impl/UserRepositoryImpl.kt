package com.ayush.linkup.data.repository.impl

import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.UserRepository
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : UserRepository {

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun getUser(userId: String): Flow<State<User>> = callbackFlow {
        try {
            trySend(State.None)
            trySend(State.Loading)

            firestore.collection(USER_COLLECTION)
                .document(userId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        trySend(State.Error(error.message ?: ERR))
                        this.close()
                    }
                    value?.let {
                        val user = it.toObject(User::class.java) ?: User()
                        trySend(State.Success(user))
                    }
                }
        } catch (e: Exception) {
            trySend(State.Error(e.message ?: ERR))
        }

        awaitClose {
            channel.close()
        }
    }

    override fun deleteUser(userId: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                firestore.collection(USER_COLLECTION)
                    .document(userId)
                    .delete()
                    .await()

                auth.currentUser?.delete()?.await()

                storage.reference.child("image/${userId}")
                    .delete()
                    .await()
            }
        } catch (e: Exception) {

        }
    }
}