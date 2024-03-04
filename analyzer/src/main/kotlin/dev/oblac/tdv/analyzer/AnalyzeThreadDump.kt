package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump

object AnalyzeThreadDump : (ThreadDump) -> ThreadDumpAnalysis {
	override fun invoke(td: ThreadDump): ThreadDumpAnalysis {
        return ThreadDumpAnalysis(
            td,
            stats = CalculateStats(td),
            blockTree = DetectBlocks(td),
            callTree = MakeCallTree(td),
            exceptions = FilterExceptionThreads(td),
            maxCpuThreads = FindTopCpuThreads(td, 10),
            uniqueStackTraces = GroupUniqueStackTraces(td),
            threadPools = ResolveThreadPools(td),
        )
	}
}
