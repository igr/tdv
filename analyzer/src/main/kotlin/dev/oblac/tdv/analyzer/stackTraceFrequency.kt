package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.JVMThreadInfo

data class StackTraceFrequency(
    val threadInfo: JVMThreadInfo,  // thread-info of first stacktrace
    val count: Int
) {
    fun increment(): StackTraceFrequency {
        return copy(count = count + 1)
    }
}
