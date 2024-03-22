package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.*

internal object CalculateAppStats: (Collection<AppThreadInfo>) -> ThreadDumpStats {
    override fun invoke(threads: Collection<AppThreadInfo>): ThreadDumpStats {
        val totalThreads = threads.size

        val blockedThreads = threads.count { it.state == ThreadState.BLOCKED }
        val newThreads = threads.count { it.state == ThreadState.NEW }
        val waitingThreads =
            threads.count { it.state == ThreadState.WAITING }
        val runnableThreads =
            threads.count { it.state == ThreadState.RUNNABLE }
        val timedWaitingThreads = threads.count { it.state == ThreadState.TIMED_WAITING }
        val terminatedThreads = threads.count { it.state == ThreadState.TERMINATED }

        val daemonCount = threads.count { it.daemon == Daemon.DAEMON }
        val daemonCountPercent = percent(daemonCount, totalThreads)
        val nonDaemonCount =
            threads.count { it.daemon == Daemon.NOT_DAEMON }
        val nonDaemonCountPercent = percent(nonDaemonCount, totalThreads)
        val gcWorkerThreadsCount = 0
        val gcConcurrentThreadsCount = 0
        val gcRefineThreads = 0
        val gcTotalThreads = 0

        return ThreadDumpStats(
            totalThreads,
            blockedThreads,
            percent(blockedThreads, totalThreads),
            newThreads,
            percent(newThreads, totalThreads),
            waitingThreads,
            percent(waitingThreads, totalThreads),
            runnableThreads,
            percent(runnableThreads, totalThreads),
            timedWaitingThreads,
            percent(timedWaitingThreads, totalThreads),
            terminatedThreads,
            percent(terminatedThreads, totalThreads),
            daemonCount,
            daemonCountPercent,
            nonDaemonCount,
            nonDaemonCountPercent,
            gcTotalThreads = gcTotalThreads,
            gcWorkerThreads = gcWorkerThreadsCount,
            percent(gcWorkerThreadsCount, gcTotalThreads),
            gcConcurrentThreads = gcConcurrentThreadsCount,
            percent(gcConcurrentThreadsCount, gcTotalThreads),
            gcRefineThreads = gcRefineThreads,
            percent(gcRefineThreads, gcTotalThreads),
        )
    }

    private fun percent(blockedThreads: Int, totalThreads: Int): Float {
        return blockedThreads.toFloat() / totalThreads * 100
    }
}

internal object CalculateSystemStats : (Collection<SystemThreadInfo>) -> ThreadDumpStats {
    override fun invoke(systemThreads: Collection<SystemThreadInfo>): ThreadDumpStats {
        val totalThreads = systemThreads.size
        val blockedThreads = 0
        val newThreads = 0
        val waitingThreads = systemThreads.count { it.state == ThreadState.WAITING }
        val runnableThreads = systemThreads.count { it.state == ThreadState.RUNNABLE }
        val timedWaitingThreads = 0
        val terminatedThreads = 0

        val daemonCount = 0
        val daemonCountPercent = 0f
        val nonDaemonCount = systemThreads.size
        val nonDaemonCountPercent = percent(nonDaemonCount, totalThreads)
        val gcWorkerThreadsCount = systemThreads.count { it.name.toString().startsWith("GC Thread#") }
        val gcConcurrentThreadsCount = systemThreads.count { it.name.toString().startsWith("G1 Conc#") }
        val gcRefineThreads = systemThreads.count { it.name.toString().startsWith("G1 Refine#") }
        val gcTotalThreads = gcWorkerThreadsCount + gcConcurrentThreadsCount + gcRefineThreads

        return ThreadDumpStats(
            totalThreads,
            blockedThreads,
            percent(blockedThreads, totalThreads),
            newThreads,
            percent(newThreads, totalThreads),
            waitingThreads,
            percent(waitingThreads, totalThreads),
            runnableThreads,
            percent(runnableThreads, totalThreads),
            timedWaitingThreads,
            percent(timedWaitingThreads, totalThreads),
            terminatedThreads,
            percent(terminatedThreads, totalThreads),
            daemonCount,
            daemonCountPercent,
            nonDaemonCount,
            nonDaemonCountPercent,
            gcTotalThreads = gcTotalThreads,
            gcWorkerThreads = gcWorkerThreadsCount,
            percent(gcWorkerThreadsCount, gcTotalThreads),
            gcConcurrentThreads = gcConcurrentThreadsCount,
            percent(gcConcurrentThreadsCount, gcTotalThreads),
            gcRefineThreads = gcRefineThreads,
            percent(gcRefineThreads, gcTotalThreads),
        )
    }

    private fun percent(blockedThreads: Int, totalThreads: Int): Float {
        return blockedThreads.toFloat() / totalThreads * 100
    }

}

/**
 * Calculate ALL stats.
 */
internal object CalculateStats : (ThreadDump) -> ThreadDumpAllStats {
    override fun invoke(td: ThreadDump): ThreadDumpAllStats {
        val appStats = CalculateAppStats(td.threads)
        val systemStats = CalculateSystemStats(td.systemThreads)

        val totalThreads = appStats.totalThreads + systemStats.totalThreads
        val blockedThreads = appStats.blockedThreads + systemStats.blockedThreads
        val newThreads = appStats.newThreads + systemStats.newThreads
        val waitingThreads = appStats.waitingThreads + systemStats.waitingThreads
        val runnableThreads = appStats.runnableThreads + systemStats.runnableThreads

        val timedWaitingThreads = appStats.timedWaitingThreads + systemStats.timedWaitingThreads
        val terminatedThreads = appStats.terminatedThreads + systemStats.terminatedThreads

        val daemonCount = appStats.daemonCount + systemStats.daemonCount
        val daemonCountPercent = percent(daemonCount, totalThreads)
        val nonDaemonCount = appStats.nonDaemonCount + systemStats.nonDaemonCount
        val nonDaemonCountPercent = percent(nonDaemonCount, totalThreads)
        val gcWorkerThreadsCount = appStats.gcWorkerThreads + systemStats.gcWorkerThreads
        val gcConcurrentThreadsCount = appStats.gcConcurrentThreads + systemStats.gcConcurrentThreads
        val gcRefineThreads = appStats.gcRefineThreads + systemStats.gcRefineThreads
        val gcTotalThreads = appStats.gcTotalThreads + systemStats.gcTotalThreads

        val allStats = ThreadDumpStats(
            totalThreads,
            blockedThreads,
            percent(blockedThreads, totalThreads),
            newThreads,
            percent(newThreads, totalThreads),
            waitingThreads,
            percent(waitingThreads, totalThreads),
            runnableThreads,
            percent(runnableThreads, totalThreads),
            timedWaitingThreads,
            percent(timedWaitingThreads, totalThreads),
            terminatedThreads,
            percent(terminatedThreads, totalThreads),
            daemonCount,
            daemonCountPercent,
            nonDaemonCount,
            nonDaemonCountPercent,
            gcTotalThreads = gcTotalThreads,
            gcWorkerThreads = gcWorkerThreadsCount,
            percent(gcWorkerThreadsCount, gcTotalThreads),
            gcConcurrentThreads = gcConcurrentThreadsCount,
            percent(gcConcurrentThreadsCount, gcTotalThreads),
            gcRefineThreads = gcRefineThreads,
            percent(gcRefineThreads, gcTotalThreads),
        )

        return ThreadDumpAllStats(
            sys = systemStats,
            app = appStats,
            all = allStats,
        )
    }

    private fun percent(blockedThreads: Int, totalThreads: Int): Float {
        return blockedThreads.toFloat() / totalThreads * 100
    }

}
