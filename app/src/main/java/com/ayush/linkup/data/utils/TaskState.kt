package com.ayush.linkup.data.utils

sealed class TaskState {
    data object Success : TaskState()
    data class Failure(val message: String): TaskState()
    data object Cancelled: TaskState()
}

