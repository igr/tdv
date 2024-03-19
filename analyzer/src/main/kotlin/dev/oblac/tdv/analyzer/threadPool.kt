package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadPoolInfo

data class ThreadPool(
    val info: ThreadPoolInfo,
    val stats: ThreadDumpStats
)
