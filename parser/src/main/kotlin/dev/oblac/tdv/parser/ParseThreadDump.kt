package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.SystemThreadInfo
import dev.oblac.tdv.domain.ThreadDump

object ParseThreadDump : (String) -> ThreadDump {

	override fun invoke(threadDumpText: String): ThreadDump {
		val tdi = ThreadDumpIterator(threadDumpText)

        SkipToThreads(tdi)

		val threads = mutableListOf<AppThreadInfo>()
		val sysThreads = mutableListOf<SystemThreadInfo>()

		// parse threads
		while (tdi.hasNext()) {
			if (tdi.peek().startsWith("JNI")) {
				break;
			}
			ParseThread(tdi).run {
				when (this) {
					is AppThreadInfo -> threads.add(this)
					is SystemThreadInfo -> sysThreads.add(this)
				}
			}

			tdi.skipEmptyLines()
            if (tdi.peek().contains("Locked ownable synchronizers:")) { // todo: see what to do whit this
                tdi.skipNonEmptyLines()
            }
            tdi.skipEmptyLines()
		}

		return ThreadDump(
			threads,
			sysThreads
		)
	}
}
