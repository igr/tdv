package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.JVMThreadInfo
import dev.oblac.tdv.domain.LockRef
import dev.oblac.tdv.domain.ObjectReference
import dev.oblac.tdv.domain.ThreadDump

fun ThreadDump.findLockWithSameRef(objectRef: ObjectReference, consumer: (LockRef) -> Unit) {
    threads.forEach { thread ->
        thread.stackTrace.forEach { stackTraceElement ->
            stackTraceElement.locks.forEach { lock ->
                if (lock.value.sameRef(objectRef)) {
                    consumer(LockRef(thread, stackTraceElement, lock))
                }
            }
        }
    }
}

fun JVMThreadInfo.forEachLock(consumer: (LockRef) -> Unit) {
    stackTrace.forEach { stackTraceElement ->
        stackTraceElement.locks.forEach { lock ->
            consumer(LockRef(this, stackTraceElement, lock))
        }
    }
}
