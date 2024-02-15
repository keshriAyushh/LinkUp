package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.impl.AuthRepository
import com.ayush.linkup.data.utils.AuthState
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun signIn(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)
            val authFlow = auth
                .signInWithEmailAndPassword(email, password)
                .toFlow()
                .collect {
                    when (it) {
                        AuthState.Cancelled -> {
                            emit(State.Success(false))
                        }

                        is AuthState.Failure -> {
                            emit(State.Error(it.message ?: ERR))
                        }

                        AuthState.Success -> {
                            emit(State.Success(true))
                        }
                    }
                }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }

    override fun signUp(user: User): Flow<State<Boolean>> = flow {
        try {
            var isAuthSuccess = false
            emit(State.None)
            emit(State.Loading)
            auth
                .createUserWithEmailAndPassword(user.email, user.password)
                .toFlow()
                .collect {
                    isAuthSuccess = when (it) {
                        AuthState.Cancelled -> {
                            false
                        }

                        is AuthState.Failure -> {
                            false
                        }

                        AuthState.Success -> {

                            true
                        }
                    }
                }

            if(isAuthSuccess) {
                firestore.collection(USER_COLLECTION)
                    .add(user.copy(timestamp = System.currentTimeMillis(), userId = auth.currentUser?.uid!!))
                    .toFlow()
                    .collect {
                        when(it) {
                            AuthState.Cancelled -> {
                                emit(State.Success(false))
                            }
                            is AuthState.Failure -> {
                                emit(State.Error(it.message))
                            }
                            AuthState.Success -> {
                                emit(State.Success(true))
                            }
                        }
                    }

            }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    private fun <T> Task<T>.toFlow(): Flow<AuthState> = callbackFlow {
        addOnSuccessListener {
            trySend(AuthState.Success)
            close()
        }
        addOnFailureListener {
            trySend(AuthState.Failure(it.message ?: ERR))
            close()
        }
        addOnCanceledListener {
            trySend(AuthState.Cancelled)
            close()
        }
        awaitClose {
            this.cancel()
        }
    }
}