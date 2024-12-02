package com.boostcamp.dreamteam.dreamdiary.feature

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

fun <T> Flow<T>.flowWithStarted(
    subscriptionCount: StateFlow<Int>,
    started: SharingStarted,
): Flow<T> {
    return started.command(subscriptionCount)
        .flatMapLatest {
            when (it) {
                SharingCommand.START -> {
                    this
                }

                SharingCommand.STOP, SharingCommand.STOP_AND_RESET_REPLAY_CACHE -> {
                    emptyFlow()
                }
            }
        }
}
