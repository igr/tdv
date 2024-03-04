package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.domain.ThreadName
import dev.oblac.tdv.domain.ThreadPoolInfo
import dev.oblac.tdv.domain.ThreadPoolName

object ResolveThreadPools : (ThreadDump) -> List<ThreadPoolInfo> {
    override fun invoke(td: ThreadDump): List<ThreadPoolInfo> {
        val map = mutableMapOf<ThreadPoolName, ThreadPoolInfo>()

        td.threads.forEach {
            val threadPoolName = resolveThreadPoolName(it.name)
            val element = map.computeIfAbsent(threadPoolName) {
                ThreadPoolInfo(threadPoolName, 0, setOf())
            }
            map[threadPoolName] =
                ThreadPoolInfo(threadPoolName, element.count + 1, element.threads + it)
        }

        return map.values.sortedByDescending { it.count }.toList()
    }

    private fun resolveThreadPoolName(name: ThreadName): ThreadPoolName {
        val threadName = name.toString().trim()
        return threadName
            .filter { it.isLetter() || it.isWhitespace() }
            .let { ThreadPoolName(it) }

    }
}
