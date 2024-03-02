package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump

object FindTopCpuThreads: (ThreadDump, Int) -> List<CpuConsumingThread> {
    override fun invoke(td: ThreadDump, count: Int): List<CpuConsumingThread> {
        val totalTime = td.threads.map { it.cpu.toMillis() }.sumOf { it }
        return td.threads
            .sortedByDescending { it.cpu.toMillis() }
            .take(count)
            .map { CpuConsumingThread(
                it, it.cpu.toMillis() * 100.0f / totalTime)}
    }
}
