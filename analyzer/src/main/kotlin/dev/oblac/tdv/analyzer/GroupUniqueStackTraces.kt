package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump

object GroupUniqueStackTraces : (ThreadDump) -> List<StackTraceFrequency> {
    override fun invoke(td: ThreadDump): List<StackTraceFrequency> {
        val map = mutableMapOf<String, StackTraceFrequency>()

        td.threads.forEach { thread ->
            val stackTraceUid = thread.stackTraceUid.value
            map.computeIfAbsent(stackTraceUid) { StackTraceFrequency(thread, 0) }.also { it ->
                map[stackTraceUid] = it.increment()
            }
        }

        return map.values.sortedByDescending { it.count }.toList()
    }
}
