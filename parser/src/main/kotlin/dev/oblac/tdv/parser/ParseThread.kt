package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.*

internal object ParseThread : (ThreadDumpIterator) -> ThreadInfo {

	override fun invoke(tdi: ThreadDumpIterator): ThreadInfo {
		val headerLine = tdi.next()
		val detectLine = headerLine.substringAfter("\"").substringAfter("\"")

        return if (detectLine.contains("#")) {
            parseAppThread(headerLine, tdi)
        } else {
            parseSystemThread(headerLine, tdi)
        }
	}

	private fun parseAppThread(
		headerLine: String,
		tdi: ThreadDumpIterator,
	): AppThreadInfo {

        val header = AppHeader.parse(headerLine, tdi).getOrThrow()
        val threadState = ParseThreadState(tdi)
		val stackTrace = ParseStackTrace(tdi)

		return AppThreadInfo(
            name = ThreadName(header.name),
            number = ThreadNo(header.number.toInt()),
            daemon = Daemon.of(header.daemon),
            priority = ThreadPriority.of(header.prio),
            osPriority = OsPriority(header.osPrio),
            cpu = Cpu.of(header.cpu),
            elapsed = Elapsed.of(header.elapsed),
            tid = ThreadId(header.tid),
            nid = NativeId(header.nid),
            state = threadState,
            stackTrace = stackTrace
        )
	}

	private fun parseSystemThread(
        headerLine: String,
        tdi: ThreadDumpIterator,
	): SystemThreadInfo {
		val header = JvmHeader.parse(headerLine, tdi).getOrThrow()

        return SystemThreadInfo(
            name = ThreadName(header.name),
            osPriority = OsPriority(header.osPrio),
            cpu = Cpu.of(header.cpu),
            elapsed = Elapsed.of(header.elapsed),
            tid = ThreadId(header.tid),
            nid = NativeId(header.nid),
            state = ParseSystemThreadStatus(header.state, tdi)
        )
	}
}
