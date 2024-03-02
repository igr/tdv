package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.LockRef

data class BlockPair(val blocker: LockRef, val blocked: LockRef)

data class BlockNode(
    val blocker: LockRef,
    val blocked: MutableList<BlockNode> = mutableListOf(),
    var totalBlocked: Int = 0
)

class BlockTree {
    val root = mutableListOf<BlockNode>()

    /**
     * Adds a block pair to the tree.
     */
    fun add(blockPair: BlockPair) {
        val node = findRootNodeWithSameBlocker(blockPair.blocker)
        when {
            node != null -> node.blocked.add(BlockNode(blockPair.blocked))
            else -> root.add(
                BlockNode(
                    blockPair.blocker,
                    mutableListOf(BlockNode(blockPair.blocked))
                )
            )
        }
    }

    fun finalize(): BlockTree {
        // check if the root elements can be merged in the tree
        val toRemove = mutableListOf<BlockNode>()

        root.forEach { rootNode ->
            // create a list of all root children without the current root node
            val list = root.filter { it != rootNode }.flatMap { it.blocked }.toList()
            findNodeWithSameBlockersThread(rootNode.blocker, list)?.let { n ->
                n.blocked.addAll(rootNode.blocked)
                toRemove.add(rootNode)
            }
        }
        root.removeAll(toRemove)

        // calculate the cumulative blocked count
        fun calculateBlockedCount(node: BlockNode): Int {
            node.totalBlocked = node.blocked.size + if (node.blocked.isEmpty()) 0 else node.blocked.sumOf { calculateBlockedCount(it) }
            return node.totalBlocked
        }
        root.forEach { it.totalBlocked = calculateBlockedCount(it) }

        // sort root nodes by total blocked count descending
        root.sortByDescending { it.totalBlocked }

        return this
    }


    private fun findRootNodeWithSameBlocker(blocker: LockRef): BlockNode? {
        return root.find { it.blocker.lock == blocker.lock }
    }

    private fun findNodeWithSameBlockersThread(blocker: LockRef, nodes: List<BlockNode>): BlockNode? {
        return nodes.find { it.blocker.thread.tid == blocker.thread.tid } ?:
        if (nodes.isEmpty()) null else findNodeWithSameBlockersThread(blocker, nodes.flatMap { it.blocked }.toList())
    }

}
