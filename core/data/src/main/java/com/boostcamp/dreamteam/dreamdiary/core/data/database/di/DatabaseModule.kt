package com.boostcamp.dreamteam.dreamdiary.core.data.database.di

import android.content.Context
import androidx.room.Room
import com.boostcamp.dreamteam.dreamdiary.core.data.database.DreamDiaryDatabase
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext applicationContext: Context,
    ): DreamDiaryDatabase {
        return Room.databaseBuilder(
            applicationContext,
            DreamDiaryDatabase::class.java,
            "dream-diary-database",
        ).build()
    }

    @Provides
    fun provideDreamDiaryDao(dreamDiaryDatabase: DreamDiaryDatabase): DreamDiaryDao {
        return dreamDiaryDatabase.dreamDiaryDao()
    }
}
