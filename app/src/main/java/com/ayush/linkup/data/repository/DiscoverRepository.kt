package com.ayush.linkup.data.repository

import com.ayush.linkup.data.model.User
import com.ayush.linkup.utils.State
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    fun getAllUsers(): Flow<State<List<User>>>
    fun getCurrentUserId(): String
    fun searchUsers(user: String): Flow<State<List<User>>>
}