package com.boostcamp.dreamteam.dreamdiary.core.data.repository.di

import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DefaultDreamDiaryRepository
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.DreamDiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun bindsDreamDiaryRepository(dreamDiaryRepository: DefaultDreamDiaryRepository): DreamDiaryRepository
}
