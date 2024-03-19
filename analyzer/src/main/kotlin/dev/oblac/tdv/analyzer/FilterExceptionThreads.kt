package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.ThreadDump

object FilterExceptionThreads: (ThreadDump) -> List<AppThreadInfo> {
    override fun invoke(td: ThreadDump): List<AppThreadInfo> {
        return td.threads
            .filter { it.stackTrace.any { st -> st.className.isJavaException() } }
    }
}
