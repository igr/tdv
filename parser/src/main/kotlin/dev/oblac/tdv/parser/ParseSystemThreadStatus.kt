package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.ThreadState

internal object ParseSystemThreadStatus : (String) -> ThreadState {

    override fun invoke(status: String): ThreadState {
        return when {
            status == "runnable" -> ThreadState.RUNNABLE
            status == "waiting on condition" -> ThreadState.WAITING
            else -> throw IllegalStateException("Unknown thread status: $status")
        }
    }
}
