package com.boostcamp.dreamteam.dreamdiary.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

internal class AndroidStorageManager @Inject constructor(
    @ApplicationContext private val context: Context,
) : StorageManager {

    override fun getFullPath(path: String): String {
        return File(context.filesDir, path).path
    }
}
