package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.AppThreadInfo
import dev.oblac.tdv.domain.SystemThreadInfo
import dev.oblac.tdv.domain.ThreadDump

object ParseThreadDump : (String) -> ThreadDump {

	override fun invoke(threadDumpText: String): ThreadDump {
		val tdi = ThreadDumpIterator(threadDumpText)
        val line0 = tdi.peek()

        // header
        if (line0.endsWith(':')) {
            // Header line with date and name, looks like this:
            // 3801084:
            // 2024-03-20 17:49:29
            // Full thread dump OpenJDK 64-Bit Server VM (17.0.10+7-LTS mixed mode, sharing):
            // <empty line>
            tdi.skip(4)
        }

		tdi.peek().let {
			if (it == "Threads class SMR info:") {
				skipSMR(tdi)
			}
		}

		tdi.skipEmptyLines()

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
                tdi.skip(2)
            }
            tdi.skipEmptyLines()
		}

		return ThreadDump(
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

    /**
     * Parses the date line if it exists.
     */
    private fun isLineWithDate(line: String): Boolean {
        return try {
            parseDateTime(line)
            true
        } catch (e: Exception) {
            false
        }
    }

}
