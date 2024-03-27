package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.ThreadState
import dev.oblac.tdv.domain.ThreadSubStatus

internal object ParseThreadState: (ThreadDumpIterator) -> ThreadState {

	private val THREAD_STATUS_PARSE_REGEX =
		"""^java\.lang\.Thread\.State: (?<state>.+)$""".toRegex()

	override fun invoke(tdi: ThreadDumpIterator): ThreadState {
		val stateLine = tdi.next().trim()
		val state = THREAD_STATUS_PARSE_REGEX.find(stateLine)?.groups?.get("state")?.value ?: throw ParserException("Invalid thread status: $stateLine", tdi)
		val split = state.split(" ")

		val threadState = ThreadState.valueOf(split[0])
		val subState = split.getOrNull(1)?.let { ThreadSubStatus(it) }
		return threadState
	}
}
