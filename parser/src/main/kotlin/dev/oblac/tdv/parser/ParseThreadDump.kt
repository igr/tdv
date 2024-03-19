package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.SystemThreadInfo
import dev.oblac.tdv.domain.ThreadDump
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ParseThreadDump : (String) -> ThreadDump {

	override fun invoke(threadDumpText: String): ThreadDump {
		val tdi = ThreadDumpIterator(threadDumpText)

		val date = LocalDateTime.parse(tdi.next(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
		val name = tdi.next().substringAfter("Full thread dump ").substringBefore(":")

		tdi.skip()

		tdi.peek().let {
			if (it == "Threads class SMR info:") {
				skipSMR(tdi)
			}
		}

		tdi.skip()

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
		}

		return ThreadDump(
			name,
			date,
			threads,
			sysThreads
		)
	}


	private fun skipSMR(tdi: ThreadDumpIterator) {
        fun detectSmr(line: String): Boolean {
            return line.startsWith("_java_thread_list=")
                || line.startsWith("_to_delete_list=")
                || line.startsWith("next-> ")
        }
		tdi.skip()

        while (true) {
            val nextLine = tdi.peek()
            if (!detectSmr(nextLine)) {
                break
            }
            tdi.skipUntilMatch("}")
            tdi.skip()
        }
	}

}
