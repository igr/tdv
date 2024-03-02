package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AppTest20240216120818 {

    companion object {
        private lateinit var tda: ThreadDumpAnalysis
        @BeforeAll
        @JvmStatic
        fun setup() {
            tda = process("../in/Thread.print.20240216120818")
        }
    }

    @Test
    fun testStats() {
        val stats = tda.stats
        Assertions.assertEquals(2579, stats.totalThreads)
        Assertions.assertEquals(356, stats.blockedThreads)
        Assertions.assertEquals(571, stats.timedWaitingThreads)
        Assertions.assertEquals(177, stats.waitingThreads)
        Assertions.assertEquals(0, stats.newThreads)
        Assertions.assertEquals(1424, stats.runnableThreads)
        Assertions.assertEquals(51, stats.terminatedThreads)
        Assertions.assertEquals(2306, stats.daemonCount)
        Assertions.assertEquals(273, stats.nonDaemonCount)
    }

    @Test
    fun testBlockTree() {
        val blockTree = tda.blockTree
        Assertions.assertEquals(31, blockTree.root.size)

        val root1 = blockTree.root[0]
        Assertions.assertEquals(244, root1.totalBlocked)

        val root2 = blockTree.root[1]
        Assertions.assertEquals(52, root2.totalBlocked)

        val root3 = blockTree.root[2]
        Assertions.assertEquals(6, root3.totalBlocked)

        val root4 = blockTree.root[3]
        Assertions.assertEquals(6, root4.totalBlocked)

        val root5 = blockTree.root[4]
        Assertions.assertEquals(6, root5.totalBlocked)

        val root6 = blockTree.root[5]
        Assertions.assertEquals(4, root6.totalBlocked)
    }

    @Test
    fun testCallTree() {
        val callTree = tda.callTree
        Assertions.assertEquals(15, callTree.children.size)
        Assertions.assertEquals(2459, callTree.count)

        val child1 = callTree.children[0]
        Assertions.assertEquals(1225, child1.count)

        val child2 = callTree.children[1]
        Assertions.assertEquals(914, child2.count)

        val child3 = callTree.children[2]
        Assertions.assertEquals(270, child3.count)

        val child4 = callTree.children[3]
        Assertions.assertEquals(35, child4.count)
    }

    @Test
    fun testUniqueStackThreads() {
        val uniqueStackThreads = tda.uniqueStackTraces

        assertEquals(295, uniqueStackThreads[0].count)
        assertEquals(104, uniqueStackThreads[1].count)
        assertEquals(103, uniqueStackThreads[2].count)
        assertEquals(69, uniqueStackThreads[3].count)
        assertEquals(62, uniqueStackThreads[4].count)
        assertEquals(53, uniqueStackThreads[5].count)
        assertEquals(52, uniqueStackThreads[6].count)
        assertEquals(51, uniqueStackThreads[7].count)
        assertEquals(49, uniqueStackThreads[8].count)
        assertEquals(45, uniqueStackThreads[9].count)
    }

}
