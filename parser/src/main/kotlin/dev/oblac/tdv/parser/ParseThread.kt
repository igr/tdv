package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.*

internal object ParseThread : (ThreadDumpIterator) -> ThreadInfo {

	private val JVM_THREAD_HEADER_PARSE_REGEX =
		"""^"(?<name>.+?)" #(?<number>\d+)(?: (?<daemon>daemon))? prio=(?<prio>\d+) os_prio=(?<osPrio>\d+) cpu=(?<cpu>[0-9a-z.]+) elapsed=(?<elapsed>[0-9a-z.]+) tid=(?<tid>0x[\da-f]+) nid=(?<nid>0x[\da-f]+) (?<status>.+) \[(?<hexValue>0x[\da-f]+)\]$""".toRegex()

	private val SYSTEM_THREAD_HEADER_PARSE_REGEX =
		"""^"(?<name>.+?)" os_prio=(?<osPrio>\d+) cpu=(?<cpu>[0-9a-z.]+) elapsed=(?<elapsed>[0-9a-z.]+) tid=(?<tid>0x[\da-f]+) nid=(?<nid>0x[\da-f]+) (?<status>.+)$""".toRegex()

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
		val header = JVM_THREAD_HEADER_PARSE_REGEX.find(headerLine)?.groups
			?: throw IllegalStateException("Invalid JVM thread header: $headerLine")

		val threadName = ThreadName(header["name"]!!.value)
		val threadNumber = ThreadNo(header["number"]!!.value.toInt())
		val threadDaemon = Daemon.of((header["daemon"] != null))
		val threadPriority = ThreadPriority.of(header["prio"]!!.value.toInt())
		val osPriority = OsPriority(header["osPrio"]?.value?.toInt() ?: 0)
		val cpu = Cpu.of(header["cpu"]!!.value)
		val elapsed = Elapsed.of(header["elapsed"]!!.value)
		val tid = ThreadId(header["tid"]!!.value)
		val nid = NativeId(header["nid"]!!.value)
		val threadState = ParseThreadState(tdi)

		val stackTrace = ParseStackTrace(tdi)

		return AppThreadInfo(
			name = threadName,
			number = threadNumber,
			daemon = threadDaemon,
			priority = threadPriority,
			osPriority = osPriority,
			cpu = cpu,
			elapsed = elapsed,
			tid = tid,
			nid = nid,
			state = threadState,
			stackTrace = stackTrace
		)
	}

	private fun parseSystemThread(
		headerLine: String,
		tdi: ThreadDumpIterator,
	): SystemThreadInfo {
		val header = SYSTEM_THREAD_HEADER_PARSE_REGEX.find(headerLine)?.groups
			?: throw IllegalStateException("Invalid system thread header: $headerLine at\n$tdi")

		val threadName = ThreadName(header["name"]!!.value)
		val osPriority = OsPriority(header["osPrio"]?.value?.toInt() ?: 0)
		val cpu = Cpu.of(header["cpu"]!!.value)
		val elapsed = Elapsed.of(header["elapsed"]!!.value)
		val tid = ThreadId(header["tid"]!!.value)
		val nid = NativeId(header["nid"]!!.value)

        val threadState = ParseSystemThreadStatus(header["status"]!!.value.trim())
		return SystemThreadInfo(
			name = threadName,
			osPriority = osPriority,
			cpu = cpu,
			elapsed = elapsed,
			tid = tid,
			nid = nid,
            state = threadState
		)
	}
}
