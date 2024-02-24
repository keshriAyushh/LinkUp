package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.User
import com.ayush.linkup.utils.State
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(userId: String): Flow<State<User>>
    fun getCurrentUserId(): String?
}