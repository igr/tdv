package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.Lock
import dev.oblac.tdv.domain.ObjectReference

data class MissingLock(
    val missingLock: ObjectReference,
    // list of locks that are somehow blocked/parked
    val locks: List<Lock>
)
data class MissingLocks(
    val parked: List<MissingLock>
)
