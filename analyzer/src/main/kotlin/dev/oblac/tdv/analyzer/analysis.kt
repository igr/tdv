package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.JVMThreadInfo
import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.domain.ThreadPoolInfo

data class ThreadDumpAnalysis(
    val threadDump: ThreadDump,
    val stats: ThreadDumpStats,
    val blockTree: BlockTree,
    val callTree: CallTreeNode,
    val exceptions: List<JVMThreadInfo>,
    val maxCpuThreads: List<CpuConsumingThread>,
    val uniqueStackTraces: List<StackTraceFrequency>,
    val threadPools: List<ThreadPoolInfo>
)
