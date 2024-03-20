package dev.oblac.tdv.analyzer

data class TomcatAnalysis(
    val stats: ThreadDumpStats,
    val tomcatExecutors: ThreadDumpStats
)
