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
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.FunctionRepository
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SyncVersionRequest
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
    private val dreamDiaryDao: DreamDiaryDao,
    private val functionRepository: FunctionRepository,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                synchronizeVersion()
                Result.success()
            } catch (e: Exception) {
                Timber.e(e, "SynchronizationWorker")
                Result.failure()
            }
        }
    }

    private suspend fun synchronizeVersion() {
        val dreamDiariesWithVersion =
            dreamDiaryDao.getDreamDiaryVersion() + dreamDiaryDao.getDreamDiaryVersionInSynchronizing()

        val syncResponse = functionRepository.syncVersion(
            dreamDiariesWithVersion.map {
                SyncVersionRequest(
                    id = it.id,
                    version = it.currentVersion,
                )
            },
        )

        if (syncResponse != null) {
            for (i in syncResponse.needSyncDiaries) {
                val lines = dreamDiaryDao.setNeedSync(i.diaryId)
                if (lines == 0) {
                    dreamDiaryDao.insertSynchronizingDreamDiary(i.diaryId, "init")
                }
            }

            for (i in syncResponse.serverOnlyDiaries) {
                dreamDiaryDao.insertSynchronizingDreamDiary(i.diaryId, "init")
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
                    .build(),
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
