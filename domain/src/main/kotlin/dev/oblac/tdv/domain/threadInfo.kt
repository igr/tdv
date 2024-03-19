package dev.oblac.tdv.domain

sealed interface ThreadInfo

data class AppThreadInfo(
	val name: ThreadName,
	val daemon: Daemon,
	val number: ThreadNo,
	val priority: ThreadPriority,
	val osPriority: OsPriority,
	val cpu: Cpu,
	val elapsed: Elapsed,
	val tid: ThreadId,
	val nid: NativeId,
	val state: ThreadState,
	val stackTrace: List<StackFrame>,
) : ThreadInfo {
    val stackTraceUid = lazy {
        state.name + stackTrace.joinToString { it.toString().trim() }
    }
}

data class SystemThreadInfo(
    val name: ThreadName,
    val osPriority: OsPriority,
    val cpu: Cpu,
    val elapsed: Elapsed,
    val tid: ThreadId,
    val nid: NativeId,
    val state: ThreadState,
) : ThreadInfo

data class ThreadPoolInfo(
    val name: ThreadPoolName,
    val count: Int,
    val threads: Set<AppThreadInfo>
)
