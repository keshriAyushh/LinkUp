package com.ayush.linkup.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ayush.linkup.data.repository.AuthRepository
import com.ayush.linkup.data.repository.CommentRepository
import com.ayush.linkup.data.repository.DataStoreRepository
import com.ayush.linkup.data.repository.DiscoverRepository
import com.ayush.linkup.data.repository.PostRepository
import com.ayush.linkup.data.repository.UserRepository
import com.ayush.linkup.data.repository.impl.AuthRepositoryImpl
import com.ayush.linkup.data.repository.impl.CommentRepositoryImpl
import com.ayush.linkup.data.repository.impl.DataStoreRepositoryImpl
import com.ayush.linkup.data.repository.impl.DiscoverRepositoryImpl
import com.ayush.linkup.data.repository.impl.PostRepositoryImpl
import com.ayush.linkup.data.repository.impl.UserRepositoryImpl
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
    @Singleton
    fun providesStorage() = FirebaseStorage.getInstance()

    @Provides
    fun providesAuthRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(firestore, auth)

    @Provides
    fun providesPostsRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage,
        @ApplicationContext context: Context
    ): PostRepository = PostRepositoryImpl(
        auth = auth,
        firestore = firestore,
        storage = storage,
        context = context
    )

    @Provides
    fun providesUserRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage
    ): UserRepository =
        UserRepositoryImpl(firestore = firestore, auth = auth, storage = storage)

    @Provides
    fun providesDataStoreRepository(
        @ApplicationContext context: Context
    ): DataStoreRepository = DataStoreRepositoryImpl(context)

    @Provides
    fun providesCommentRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): CommentRepository = CommentRepositoryImpl(auth, firestore)

    @Provides
    fun providesDiscoverRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): DiscoverRepository = DiscoverRepositoryImpl(auth, firestore)

}