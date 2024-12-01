package com.boostcamp.dreamteam.dreamdiary.core.synchronization

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.boostcamp.dreamteam.dreamdiary.core.data.database.dao.DreamDiaryDao
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.ImageEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.SynchronizingDreamDiaryEntity
import com.boostcamp.dreamteam.dreamdiary.core.data.database.model.TextEntity
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
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
                if (functionRepository.updateCurrentUID()) {
                    Timber.d("remove all sync data")
                    removeAllSyncData()
                }

                synchronizeVersion()
                synchronizeDreamDiaries()
                uploadContents()
                downloadContents()
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

    private suspend fun downloadContents() {
        dreamDiaryDao.removeSynchronizingDreamDiaryIfVersionNotEquals()

        val ids = dreamDiaryDao.getSynchronizingDreamDiaryIds()
        for (id in ids) {
            dreamDiaryDao.moveToDreamDiaryIfSynced(id)
        }

        val notDownloadedContents = dreamDiaryDao.getSynchronizingDreamDiaries().flatMap { diary ->
            parseNeedDownloadContent(diary)
        }

        for (content in notDownloadedContents) {
            try {
                when (content) {
                    is NotDownloadedContent.Image -> {
                        val image = functionRepository.downloadImage(content.id)
                        if (image != null) {
                            dreamDiaryDao.insertImage(
                                imageEntity = ImageEntity(
                                    id = content.id,
                                    path = image.path,
                                ),
                            )
                            dreamDiaryDao.moveToDreamDiaryIfSynced(content.diaryId)
                        }
                    }

                    is NotDownloadedContent.Text -> {
                        val text = functionRepository.downloadText(content.id)
                        if (text != null) {
                            dreamDiaryDao.insertText(
                                textEntity = TextEntity(
                                    id = content.id,
                                    text = text.text,
                                ),
                            )
                            dreamDiaryDao.moveToDreamDiaryIfSynced(content.diaryId)
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "$content 다운로드 중 에러")
            }
        }
    }

    private suspend fun parseNeedDownloadContent(synchronizingDreamDiaryEntity: SynchronizingDreamDiaryEntity): List<NotDownloadedContent> {
        val body = synchronizingDreamDiaryEntity.body
        val diaryContents = mutableListOf<NotDownloadedContent>()

        val parsingDiaryContent = body.split(":")
        var index = 0

        while (index < parsingDiaryContent.size) {
            if (parsingDiaryContent[index] == "text") {
                index += 1
                val id = parsingDiaryContent[index]
                val textEntity = dreamDiaryDao.getText(id)
                if (textEntity == null) {
                    diaryContents.add(
                        NotDownloadedContent.Text(id, synchronizingDreamDiaryEntity.id),
                    )
                }
            } else if (parsingDiaryContent[index] == "image") {
                index += 1
                val id = parsingDiaryContent[index]
                val imageEntity = dreamDiaryDao.getImage(id)
                if (imageEntity == null) {
                    diaryContents.add(
                        NotDownloadedContent.Image(id, synchronizingDreamDiaryEntity.id),
                    )
                }
            } else {
                index += 1
            }
        }
        return diaryContents
    }

    private suspend fun removeAllSyncData() {
        dreamDiaryDao.removeSyncData()
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

        fun getWorkInfo(context: Context): Flow<SyncWorkState> {
            val workManager = WorkManager.getInstance(context.applicationContext)

            return workManager.getWorkInfosForUniqueWorkFlow(UNIQUE_WORK_NAME)
                .transform {
                    if (it.size > 0) {
                        emit(it.last())
                    }
                }.map {
                    if (it.state == WorkInfo.State.RUNNING) {
                        SyncWorkState.RUNNING
                    } else {
                        SyncWorkState.IDLE
                    }
                }
        }
    }
}

@InstallIn(SingletonComponent::class)
@EntryPoint
interface HiltWorkerFactoryEntryPoint {
    fun hiltWorkerFactory(): HiltWorkerFactory
}
