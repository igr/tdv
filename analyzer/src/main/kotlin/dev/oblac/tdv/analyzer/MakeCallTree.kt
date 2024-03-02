package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.ThreadDump

object MakeCallTree : (ThreadDump) -> CallTreeNode {
    override fun invoke(td: ThreadDump): CallTreeNode {
        return invertThreadDump(td)
    }

    private fun invertThreadDump(td: ThreadDump): CallTreeNode {
        val root = CallTreeNode.root()

        td.threads.forEach { thread ->
            val reversedStackTrace = thread.stackTrace.reversed().toList()

            var node = root
            reversedStackTrace.forEach {
                val signature = if (it.fileLine.isNA()) {
                    """${it.className}.${it.methodName}(${it.fileName})"""
                } else {
                    """${it.className}.${it.methodName}(${it.fileName}:${it.fileLine})"""
                }
                node = node.find(signature)
            }
        }

        root.count = root.children.sumOf { it.count }

        fun sort(node: CallTreeNode) {
            node.children.sortByDescending { it.count }
            node.children.forEach { sort(it) }
        }
        sort(root)
        return root
    }
}
