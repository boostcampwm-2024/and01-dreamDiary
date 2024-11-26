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
import com.boostcamp.dreamteam.dreamdiary.core.model.synchronization.SynchronizeDreamDiaryRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant

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
                synchronizeDreamDiaries()
                uploadContents()
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

    private suspend fun synchronizeDreamDiaries() {
        val synchronizingDreamDiaryNeedData = dreamDiaryDao.getSynchronizingDreamDiaryNeedData()
        val dreamDiaryNeedSync = dreamDiaryDao.getDreamDiaryNeedSync()

        val synchronizeDreamDiaries =
            synchronizingDreamDiaryNeedData.map { it.toRequest() } + dreamDiaryNeedSync.map { it.toRequest() }

        for (dreamDiary in synchronizeDreamDiaries) {
            for (diaryContent in dreamDiary.diaryContents) {
                when (diaryContent) {
                    is SynchronizeDreamDiaryRequest.ContentId.Image -> {
                        dreamDiaryDao.insertSynchronizingContentWhenNotDone(diaryContent.id, dreamDiary.id, "image")
                    }
                    is SynchronizeDreamDiaryRequest.ContentId.Text -> {
                        dreamDiaryDao.insertSynchronizingContentWhenNotDone(diaryContent.id, dreamDiary.id, "text")
                    }
                }
            }

            val response = functionRepository.synchronizeDreamDiary(dreamDiary)

            if (response != null) {
                val newDiary = response.newDiary
                val updateDiary = response.updateDiary
                val deletedDiary = response.deletedDiary
                if (deletedDiary != null) {
                    if (deletedDiary.deleted) {
                        dreamDiaryDao.deleteDreamDiaryHard(dreamDiary.id)
                        continue
                    }
                }

                if (newDiary != null) {
                    dreamDiaryDao.insertSynchronizingDreamDiaryAndUpdateVersion(
                        id = dreamDiary.id,
                        title = newDiary.title,
                        body = newDiary.content,
                        labels = newDiary.labels,
                        createdAt = Instant.ofEpochMilli(newDiary.createdAt),
                        updatedAt = Instant.ofEpochMilli(newDiary.updatedAt),
                        sleepStartAt = Instant.ofEpochMilli(newDiary.sleepStartAt),
                        sleepEndAt = Instant.ofEpochMilli(newDiary.sleepEndAt),
                        version = response.currentVersion,
                    )
                } else if (updateDiary != null) {
                    dreamDiaryDao.insertSynchronizingDreamDiaryAndUpdateVersion(
                        id = dreamDiary.id,
                        title = updateDiary.title,
                        body = updateDiary.content,
                        labels = updateDiary.labels,
                        createdAt = Instant.ofEpochMilli(updateDiary.createdAt),
                        updatedAt = Instant.ofEpochMilli(updateDiary.updatedAt),
                        sleepStartAt = Instant.ofEpochMilli(updateDiary.sleepStartAt),
                        sleepEndAt = Instant.ofEpochMilli(updateDiary.sleepEndAt),
                        version = response.currentVersion,
                    )
                } else {
                    dreamDiaryDao.updateDreamDiarySyncVersionAndCurrentVersion(dreamDiary.id, response.currentVersion)
                }
            }
        }
    }

    private suspend fun uploadContents() {
        val needUploadContents = dreamDiaryDao.getNeedSynchronizingContents()
        for (content in needUploadContents) {
            if (content.type == "text") {
                val text = dreamDiaryDao.getText(content.id)
                if (text != null) {
                    functionRepository.uploadText(text.id, text.text)
                    dreamDiaryDao.setSynchronizingContentDone(content.id)
                }
            } else if (content.type == "image") {
                val image = dreamDiaryDao.getImage(content.id)
                if (image != null) {
                    functionRepository.uploadImage(image.id, image.path)
                    dreamDiaryDao.setSynchronizingContentDone(content.id)
                }
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
