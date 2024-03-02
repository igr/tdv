package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.JVMThreadInfo
import dev.oblac.tdv.domain.ThreadDump

object FilterExceptionThreads: (ThreadDump) -> List<JVMThreadInfo> {
    override fun invoke(td: ThreadDump): List<JVMThreadInfo> {
        return td.threads
            .filter { it.stackTrace.any { st -> st.className.isJavaException() } }
    }
}
