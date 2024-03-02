package dev.oblac.tdv.reporter

import dev.oblac.tdv.analyzer.CallTreeNode

data class FlameNode(
    val name: String,
    val value: Int,
    val children: List<FlameNode>
) {
    companion object {
        fun fromCallTree(callTree: CallTreeNode): FlameNode {
            return FlameNode(
                name = callTree.signature,
                value = callTree.count,
                children = callTree.children.map { fromCallTree(it) }
            )
        }
    }
}
