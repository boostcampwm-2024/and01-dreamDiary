package com.boostcamp.dreamteam.dreamdiary.core.data.repository.di

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DefaultDreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.FunctionRepository
import com.google.firebase.functions.FirebaseFunctions
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
        fun provideFirebaseFunctions(): FirebaseFunctions = FirebaseFunctions.getInstance()

        @Provides
        @Singleton
        fun provideFunctionRepository(functions: FirebaseFunctions): FunctionRepository {
            return FunctionRepository(functions)
        }
    }
}
