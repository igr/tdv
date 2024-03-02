package dev.oblac.tdv.analyzer

data class CallTreeNode(
    val signature: String,
    val children: MutableList<CallTreeNode> = mutableListOf(),
    var count: Int = 0,
) {

    fun find(signature: String): CallTreeNode {
        val found = children.find { it.signature == signature }
        return if (found != null) {
            found.count++
            found
        } else {
            val newNode = CallTreeNode(signature)
            children.add(newNode)
            newNode.count++
            newNode
        }
    }
    companion object {
        fun root() = CallTreeNode("root")
    }
}
