package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.ThreadState

internal object ParseSystemThreadStatus : (String, ThreadDumpLocation) -> ThreadState {

    override fun invoke(status: String, tdi: ThreadDumpLocation): ThreadState {
        return when {
            status == "runnable" -> ThreadState.RUNNABLE
            status == "waiting on condition" -> ThreadState.WAITING
            else -> throw ParserException("Unknown thread status: $status", tdi)
        }
    }
}
