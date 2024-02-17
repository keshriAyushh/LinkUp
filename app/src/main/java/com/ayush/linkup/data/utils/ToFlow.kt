package com.ayush.linkup.data.utils

import android.net.Uri
import com.ayush.linkup.utils.Constants
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <T> Task<T>.toFlow(): Flow<TaskState> = callbackFlow {
    addOnSuccessListener {
        trySend(TaskState.Success)
        close()
    }
    addOnFailureListener {
        trySend(TaskState.Failure(it.message ?: Constants.ERR))
        close()
    }
    addOnCanceledListener {
        trySend(TaskState.Cancelled)
        close()
    }
    awaitClose {
        this.cancel()
    }
}

fun Task<Uri>.toUriFlow(): Flow<StorageState> = callbackFlow {
    addOnSuccessListener {
        trySend(StorageState.Success(it.toString()))
        close()
    }
    addOnFailureListener {
        trySend(StorageState.Failure(it.message ?: Constants.ERR))
        close()
    }
    addOnCanceledListener {
        trySend(StorageState.Cancelled)
        close()
    }

    awaitClose {
        this.cancel()
    }
}
