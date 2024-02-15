package com.ayush.linkup.di

import com.ayush.linkup.data.repository.AuthRepositoryImpl
import com.ayush.linkup.data.repository.impl.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
}