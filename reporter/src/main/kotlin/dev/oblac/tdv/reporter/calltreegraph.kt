package dev.oblac.tdv.reporter

import dev.oblac.tdv.analyzer.CallTreeNode

fun renderCallTreeGraph(callTree: CallTreeNode): String {
    return """
        <ul class="tree">
            <li>
                <details open>
                <summary>[<b>${callTree.count}</b>] Root</summary>
                ${renderDetails(callTree.children)}
                </details>
            </li>
        </ul>
    """.trimIndent()
}

private fun renderDetails(children: MutableList<CallTreeNode>): String {
    val listOfItems = children.joinToString(separator = "\n") {
        if (it.children.isEmpty()) {
            """
            <li>[<b>${it.count}</b>] ${it.signature}</li>
            """.trimIndent()
        } else {
            """
            <li>
                <details>
                <summary>[<b>${it.count}</b>] ${it.signature}</summary>
                ${renderDetails(it.children)}
                </details>
            </li>
            """.trimIndent()
        }
    }
    return "<ul>$listOfItems</ul>"
}
