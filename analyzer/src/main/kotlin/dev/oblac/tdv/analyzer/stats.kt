package dev.oblac.tdv.analyzer

data class ThreadDumpStats(
    val totalThreads: Int,
    val blockedThreads: Int,
    val blockedThreadsPercent: Float,
    val newThreads: Int,
    val newThreadsPercent: Float,
    val waitingThreads: Int,
    val waitingThreadsPercent: Float,
    val runnableThreads: Int,
    val runnableThreadsPercent: Float,
    val timedWaitingThreads: Int,
    val timedWaitingThreadsPercent: Float,
    val terminatedThreads: Int,
    val terminatedThreadsPercent: Float,
    val daemonCount: Int,
    val daemonCountPercent: Float,
    val nonDaemonCount: Int,
    val nonDaemonCountPercent: Float,
    val gcTotalThreads: Int,
    val gcWorkerThreads: Int,
    val gcWorkerThreadsPercent: Float,
    val gcConcurrentThreads: Int,
    val gcConcurrentThreadsPercent: Float,
    val gcRefineThreads: Int,
    val gcRefineThreadsPercent: Float,
)

/**
 * Holds the thread dump stats for the system, application, and all threads.
 */
data class ThreadDumpAllStats(
    val sys: ThreadDumpStats,
    val app: ThreadDumpStats,
    val all: ThreadDumpStats,
)
