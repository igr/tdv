package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.domain.ThreadName

object AnalyzeTomcat: (ThreadDump) -> TomcatAnalysis {
    override fun invoke(threadDump: ThreadDump): TomcatAnalysis {
        val tomcatThreads = DetectTomcatThreads(threadDump)
        return TomcatAnalysis(
            stats = CalculateAppStats(tomcatThreads),
            tomcatExecutors = CalculateAppStats(tomcatThreads.filter { isTomcatExecutorPool(it.name) })
        )
    }

    private fun isTomcatExecutorPool(threadName: ThreadName) = threadName.contains("http-nio") && threadName.contains("exec")
}
