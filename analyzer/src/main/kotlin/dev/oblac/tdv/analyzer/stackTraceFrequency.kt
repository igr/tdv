package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.AppThreadInfo

data class StackTraceFrequency(
	val threadInfo: AppThreadInfo,  // thread-info of first stacktrace
	val count: Int
) {
    fun increment(): StackTraceFrequency {
        return copy(count = count + 1)
    }
}
