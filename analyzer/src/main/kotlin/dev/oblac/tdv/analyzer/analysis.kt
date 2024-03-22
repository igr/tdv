package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.ThreadDump

data class ThreadDumpAnalysis(
    val threadDump: ThreadDump,
    val stats: ThreadDumpAllStats,
    val blockTree: BlockTree,
    val callTree: CallTreeNode,
    val exceptions: List<AppThreadInfo>,
    val maxCpuThreads: List<CpuConsumingThread>,
    val uniqueStackTraces: List<StackTraceFrequency>,
    val threadPools: List<ThreadPool>,
    val tomcatAnalysis: TomcatAnalysis,
)
