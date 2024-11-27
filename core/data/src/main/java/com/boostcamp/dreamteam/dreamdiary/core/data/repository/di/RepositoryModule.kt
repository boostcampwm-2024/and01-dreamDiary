package com.boostcamp.dreamteam.dreamdiary.core.data.repository.di

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DefaultDreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsDreamDiaryRepository(dreamDiaryRepository: DefaultDreamDiaryRepository): DreamDiaryRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseFunctions(): FirebaseFunctions {
            val functions = FirebaseFunctions.getInstance("asia-northeast3")

            return functions
        }

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage {
            val storage = FirebaseStorage.getInstance()

            return storage
        }

        @Provides
        @Singleton
        fun provideFirebaseFirestore(): FirebaseFirestore {
            val firestore = FirebaseFirestore.getInstance()

            return firestore
        }
    }
}
