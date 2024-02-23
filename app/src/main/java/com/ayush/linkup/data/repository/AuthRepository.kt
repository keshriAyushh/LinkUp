package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.User
import com.ayush.linkup.utils.State
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signIn(email: String, password: String): Flow<State<Boolean>>
    fun signUp(user: User): Flow<State<Boolean>>
    fun isUserSignedIn(): Boolean
    fun signOut()
    fun forgotPassword(email: String): Flow<State<Boolean>>
}