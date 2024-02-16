package com.ayush.linkup.data.utils

sealed class StorageState {

    data class Success(val data: String): StorageState()
    data class Failure(val data: String): StorageState()
    data object Cancelled: StorageState()

}