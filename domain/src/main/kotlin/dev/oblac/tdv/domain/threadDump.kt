package dev.oblac.tdv.domain

import java.time.LocalDateTime

data class LockRef(
    val thread: JVMThreadInfo,
    val stackTraceElement: StackFrame,
    val lock: Lock,
)

data class ThreadDump(
	val name: String,
	val date: LocalDateTime,
	val threads: List<JVMThreadInfo>,
	val systemThreads: List<SystemThreadInfo>,
)
