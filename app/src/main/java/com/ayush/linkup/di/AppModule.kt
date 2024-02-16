package com.ayush.linkup.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ayush.linkup.data.repository.impl.AuthRepositoryImpl
import com.ayush.linkup.data.repository.AuthRepository
import com.ayush.linkup.utils.NetworkObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @RequiresApi(Build.VERSION_CODES.N)
    @Provides
    @Singleton
    fun providesNetworkObserver(@ApplicationContext context: Context) = NetworkObserver(context)

    @Provides
    @Singleton
    fun providesFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesAuth() = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firestore, auth)

    @Provides
    @Singleton
    fun providesStorage() = FirebaseStorage.getInstance()
}