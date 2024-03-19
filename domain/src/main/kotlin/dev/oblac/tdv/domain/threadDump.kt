package dev.oblac.tdv.domain

import java.time.LocalDateTime

data class LockRef(
    val thread: AppThreadInfo,
    val stackTraceElement: StackFrame,
    val lock: Lock,
)

data class ThreadDump(
    val name: String,
    val date: LocalDateTime,
    val threads: List<AppThreadInfo>,
    val systemThreads: List<SystemThreadInfo>,
)
