package com.ayush.linkup.data.utils

sealed class AuthState {

    data object Success : AuthState()
    data class Failure(val message: String): AuthState()
    data object Cancelled: AuthState()
}