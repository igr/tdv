package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.*

internal object DetectBlocks : (ThreadDump) -> BlockTree {

    override fun invoke(td: ThreadDump): BlockTree {
        val tree = BlockTree()

        td.threads.forEach { thread ->
            if (thread.state == ThreadState.BLOCKED) {
                thread.forEachLock { lockRef ->
                    when (lockRef.lock) {
                        is Blocked -> findWhoBlockedMe(lockRef, td)
                        else -> emptyList()
                    }
                    .forEach { blockPair ->
                        tree.add(blockPair)
                    }
                }
            }
        }

        return tree.finalize()
    }

    private fun findWhoBlockedMe(lockRef: LockRef, td: ThreadDump): MutableList<BlockPair> {
//        println("BLOCKED THREAD LOCK REF: ${lockRef.stackTraceElement} ${lockRef.lock.value}")
        val results = mutableListOf<BlockPair>()

        td.findLockWithSameRef(lockRef.lock.value) { blockedLockRef ->
            if (blockedLockRef == lockRef) {
                return@findLockWithSameRef
            }

            // one thread may obtain the locks multiple times that
            // blocks the same thread
            results.find { blockedLockRef.thread.tid == it.blocker.thread.tid }?.let {
                return@findLockWithSameRef
            }

            if (blockedLockRef.lock is Locked)  {
//                println("\tBLOCKED BY: ${blockedLockRef.stackTraceElement} ${blockedLockRef.lock.value}")
                results.add(BlockPair(blockedLockRef, lockRef))
            }
        }
        return results
    }
}
