package dev.oblac.tdv.reporter

import dev.oblac.tdv.domain.AppThreadInfo

data class ReportThreadStackFreq(
    val state: String,
    val count: Int,
    val stackTrace: List<String>,
)

data class ReportThreadStack(
    val name: String,
    val threadId: String,
    val state: String,
    val stackTrace: List<String>,
) {
    companion object {
        fun of(threadInfo: AppThreadInfo) = ReportThreadStack(
            threadInfo.name.toString(),
            threadInfo.tid.toString(),
            threadInfo.state.toString(),
            threadInfo.stackTrace.flatMap { st ->
                listOf(st.toString().trim()) + st.locks.map { it.toString() }
            }
        )
    }
}
