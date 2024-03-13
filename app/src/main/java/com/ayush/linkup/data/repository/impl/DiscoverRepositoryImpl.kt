package com.ayush.linkup.data.repository.impl

import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.DiscoverRepository
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : DiscoverRepository {
    override fun getCurrentUserId(): String {
        return auth.currentUser?.uid!!
    }

    override fun getAllUsers(): Flow<State<List<User>>> = callbackFlow {
        try {

            trySend(State.None)
            trySend(State.Loading)

            firestore.collection(USER_COLLECTION)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        trySend(State.Error(error.message ?: ERR))
                        this.close()
                    }
                    if (value != null) {
                        val users = value.toObjects(User::class.java)
                        trySend(State.Success(users))
                    }
                }
        } catch (e: Exception) {
            trySend(State.Error(e.message ?: ERR))
        }

        awaitClose {
            this.cancel()
        }
    }

    override fun searchUsers(user: String): Flow<State<List<User>>> = callbackFlow {
//        try {
//            trySend(State.None)
//            trySend(State.Loading)
//
//            firestore.collection(USER_COLLECTION)
//                .whereGreaterThanOrEqualTo(
//                    Filter.equalTo()
//                )
//                .addSnapshotListener { value, error ->
//                    if(error != null) {
//                        trySend(State.Error(error.message ?: ERR))
//                        this.close()
//                    }
//                    if(value != null) {
//                        val users = value.toObjects(User::class.java)
//                        trySend(State.Success(users))
//                    }
//                }
//
//        } catch (e: Exception) {
//            trySend(State.Error(e.message ?: ERR))
//        }
        awaitClose {
            this.cancel()
        }
    }
}