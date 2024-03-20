package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.ClassName
import dev.oblac.tdv.domain.ThreadDump

object DetectTomcatThreads: (ThreadDump) -> List<AppThreadInfo> {
    override fun invoke(td: ThreadDump): List<AppThreadInfo> {
        return td.threads.filter {
            it.stackTrace.any { st -> isTomcat(st.className) }
        }
    }
    private fun isTomcat(className: ClassName) =
        className.contains("org.apache.catalina") || className.contains("org.apache.tomcat")

}
