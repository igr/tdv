package dev.oblac.tdv.domain

data class LockRef(
    val thread: AppThreadInfo,
    val stackTraceElement: StackFrame,
    val lock: Lock,
)

data class ThreadDump(
    val threads: List<AppThreadInfo>,
    val systemThreads: List<SystemThreadInfo>,
)
