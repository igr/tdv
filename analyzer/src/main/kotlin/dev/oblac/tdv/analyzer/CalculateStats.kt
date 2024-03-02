package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.Daemon
import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.domain.ThreadState

internal object CalculateStats : (ThreadDump) -> ThreadDumpStats {
    override fun invoke(td: ThreadDump): ThreadDumpStats {
        val totalThreads = td.threads.size + td.systemThreads.size

        val blockedThreads = td.threads.count { it.state == ThreadState.BLOCKED }
        val newThreads = td.threads.count { it.state == ThreadState.NEW }
        val waitingThreads =
            td.threads.count { it.state == ThreadState.WAITING } + td.systemThreads.count { it.state == ThreadState.WAITING }
        val runnableThreads =
            td.threads.count { it.state == ThreadState.RUNNABLE } + td.systemThreads.count { it.state == ThreadState.RUNNABLE }
        val timedWaitingThreads = td.threads.count { it.state == ThreadState.TIMED_WAITING }
        val terminatedThreads = td.threads.count { it.state == ThreadState.TERMINATED }

        val daemonCount = td.threads.count{ it.daemon == Daemon.DAEMON }
        val daemonCountPercent = percent(daemonCount, totalThreads)
        val nonDaemonCount = td.threads.count { it.daemon == Daemon.NOT_DAEMON } + td.systemThreads.size
        val nonDaemonCountPercent = percent(nonDaemonCount, totalThreads)
        val gcWorkerThreadsCount = td.systemThreads.count { it.name.toString().startsWith("GC Thread#") }
        val gcConcurrentThreadsCount = td.systemThreads.count { it.name.toString().startsWith("G1 Conc#") }
        val gcRefineThreads = td.systemThreads.count { it.name.toString().startsWith("G1 Refine#") }
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
