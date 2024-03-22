package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.Locked
import dev.oblac.tdv.domain.Parked
import dev.oblac.tdv.domain.ThreadDump

object FindMissingLocks : (ThreadDump) -> MissingLocks {
    override fun invoke(td: ThreadDump): MissingLocks {
        val threads = td.threads

        val allLocks = threads
            .map { it.stackTrace }
            .flatten()
            .filter { it.locks.isNotEmpty() }
            .flatMap { it.locks }


        val parkedLocksMissingLocked = allLocks
            .asSequence()
            .filterIsInstance<Parked>()
            .filter { parkedLock ->
                allLocks.none { it is Locked && it.value.sameRef(parkedLock.value) }
            }
            .groupBy { it.value }
            .map {
                MissingLock(
                    it.key,
                    it.value
                )
            }
            .sortedByDescending { it.locks.size }
            .toList()

        return MissingLocks(
            parked = parkedLocksMissingLocked
        )
    }
}
