package com.boostcamp.dreamteam.dreamdiary.core.synchronization

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class SynchronizationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Result.success()
            } catch (e: Exception) {
                Timber.e(e, "SynchronizationWorker")
                Result.failure()
            }
        }
    }
    companion object {
        const val UNIQUE_WORK_NAME = "SynchronizationWorker"

        fun initWorker(application: Application) {

            val hiltWorkerFactory = EntryPointAccessors.fromApplication(
                application,
                HiltWorkerFactoryEntryPoint::class.java,
            ).hiltWorkerFactory()

            WorkManager.initialize(
                application,
                Configuration.Builder()
                    .setWorkerFactory(hiltWorkerFactory)
                    .build()
            )
        }

        fun runSynchronizationWorker(context: Context) {
            val workManager = WorkManager.getInstance(context.applicationContext)

            val request = OneTimeWorkRequestBuilder<SynchronizationWorker>()
                .build()

            workManager
                .enqueueUniqueWork(UNIQUE_WORK_NAME, ExistingWorkPolicy.KEEP, request)
        }
    }
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface HiltWorkerFactoryEntryPoint {
    fun hiltWorkerFactory(): HiltWorkerFactory
}
