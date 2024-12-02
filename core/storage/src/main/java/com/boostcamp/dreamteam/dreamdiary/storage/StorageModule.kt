package com.boostcamp.dreamteam.dreamdiary.storage

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface StorageModule {
    @Binds
    @Singleton
    fun bindsStorageManager(storageManager: AndroidStorageManager): StorageManager
}
