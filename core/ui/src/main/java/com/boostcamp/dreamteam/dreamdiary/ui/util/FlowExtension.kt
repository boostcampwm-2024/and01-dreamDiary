package com.boostcamp.dreamteam.dreamdiary.ui.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.util.concurrent.atomic.AtomicLong

fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> =
    channelFlow {
        val lastEmissionTime = AtomicLong(0L)
        collect { value ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime.get() >= windowDuration) {
                lastEmissionTime.set(currentTime)
                trySend(value).isSuccess
            }
        }
    }
