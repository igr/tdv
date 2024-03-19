package dev.oblac.tdv.reporter

import dev.oblac.tdv.analyzer.BlockNode
import dev.oblac.tdv.domain.LockRef
import dev.oblac.tdv.domain.Locked
import dev.oblac.tdv.domain.ThreadName

data class BlockExplanation(
    val className: String,
    val methodName: String,
    val fileLine: Int,
    val blocking: Int,
    val threadName: String,
    val threadId: String,
    val stuckClassName: String,
    val stuckMethodName: String,
    val stuckLine: Int,
    val locksCount: Int,
    val stackTrace: List<String>
) {
    companion object {
        fun fromLockRef(blocker: LockRef, blocking: Int): BlockExplanation {
            return BlockExplanation(
                className = blocker.stackTraceElement.className.toString(),
                methodName = blocker.stackTraceElement.methodName.toString(),
                fileLine = blocker.stackTraceElement.fileLine.toString().toInt(),
                blocking = blocking,
                threadName = blocker.thread.name.toString(),
                threadId = blocker.thread.tid.toString(),
                stuckClassName = blocker.thread.stackTrace[0].className.toString(),
                stuckMethodName = blocker.thread.stackTrace[0].methodName.toString(),
                stuckLine = blocker.thread.stackTrace[0].fileLine.toString().toInt(),
                locksCount = blocker.thread.stackTrace.flatMap { it.locks }.count { it is Locked },
                stackTrace = blocker.thread.stackTrace.map {
                    it.toString() +
                        if (it.locks.isEmpty()) "" else "<br>" + it.locks.joinToString("<br>") { it.toString() }

                }
            )
        }
    }
}

data class ReportBlockNode(
    val name: ThreadName,
    val popup: String,
    val children: List<ReportBlockNode>
) {
    companion object {
        fun fromBlockNode(blockNode: BlockNode): ReportBlockNode {
            return ReportBlockNode(
                name = blockNode.blocker.thread.name,
                popup = blockNode.blocker.thread.name.toString() + "\n" +
                        blockNode.blocker.thread.stackTrace[0].toString(),
                children = blockNode.blocked.map { fromBlockNode(it) }
            )
        }
    }
}
