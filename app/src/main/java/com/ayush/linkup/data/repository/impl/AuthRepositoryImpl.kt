package com.ayush.linkup.data.repository.impl

import com.ayush.linkup.data.model.User
import com.ayush.linkup.data.repository.AuthRepository
import com.ayush.linkup.data.utils.TaskState
import com.ayush.linkup.data.utils.toFlow
import com.ayush.linkup.utils.Constants.ERR
import com.ayush.linkup.utils.Constants.USER_COLLECTION
import com.ayush.linkup.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun isUserSignedIn() = auth.currentUser?.uid != null

    override fun signIn(email: String, password: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)
            val authFlow = auth
                .signInWithEmailAndPassword(email, password)
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
                        TaskState.Cancelled -> {
                            false
                        }

                        is TaskState.Failure -> {
                            false
                        }

                        TaskState.Success -> {
                            true
                        }
                    }
                }

            if(isAuthSuccess) {
                val userId = auth.currentUser?.uid!!
                firestore.collection(USER_COLLECTION)
                    .document(userId)
                    .set(user.copy(timestamp = System.currentTimeMillis(), userId = userId))
                    .toFlow()
                    .collect {
                        when(it) {
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

            }
        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }

    override fun forgotPassword(email: String): Flow<State<Boolean>> = flow {
        try {
            emit(State.None)
            emit(State.Loading)

            var isSuccess = false

            auth.sendPasswordResetEmail(email)
                .toFlow()
                .collect {
                    isSuccess = when (it) {
                        TaskState.Cancelled -> {
                            false
                        }

                        is TaskState.Failure -> {
                            false
                        }

                        TaskState.Success -> {
                            true
                        }
                    }
                }

            emit(State.Success(isSuccess))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: ERR))
        }
    }

    override fun signOut() {
        auth.signOut()
    }


}