package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AppTest20230908190546 {
    companion object {
        private lateinit var tda: ThreadDumpAnalysis
        @BeforeAll
        @JvmStatic
        fun setup() {
            tda = process("../in/Thread.print.20230908190546")
        }
    }

    @Test
    fun testStats() {
        val stats = tda.stats
        assertEquals(2608, stats.all.totalThreads)
        assertEquals(11, stats.all.blockedThreads)
        assertEquals(1312, stats.all.timedWaitingThreads)
        assertEquals(228, stats.all.waitingThreads)
        assertEquals(0, stats.all.newThreads)
        assertEquals(872, stats.all.runnableThreads)
        assertEquals(185, stats.all.terminatedThreads)
        assertEquals(2422, stats.all.daemonCount)
        assertEquals(186, stats.all.nonDaemonCount)
    }
    @Test
    fun testBlockTree() {
        val blockTree = tda.blockTree
        assertEquals(3, blockTree.root.size)

        val root1 = blockTree.root[0]
        assertEquals(9, root1.totalBlocked)
        assertEquals(7, root1.blocked.size)

        val root2 = blockTree.root[1]
        assertEquals(1, root2.totalBlocked)

        val root3 = blockTree.root[2]
        assertEquals(1, root3.totalBlocked)
    }

    @Test
    fun testCallTree() {
        val callTree = tda.callTree
        assertEquals(13, callTree.children.size)
        assertEquals(2371, callTree.count)

        val child1 = callTree.children[0]
        assertEquals(1228, child1.count)

        val child2 = callTree.children[1]
        assertEquals(929, child2.count)

        val child3 = callTree.children[2]
        assertEquals(178, child3.count)

        val child4 = callTree.children[3]
        assertEquals(23, child4.count)

        val child5 = callTree.children[4]
        assertEquals(3, child5.count)
    }
    @Test
    fun testExceptions() {
        val exceptions = tda.exceptions
        assertEquals(1, exceptions.size)
        assertEquals("Assessment Runner 458", exceptions[0].name.toString())
    }
    @Test
    fun MaxCPU() {
        val maxCpuThreads = tda.maxCpuThreads
        assertEquals(20, maxCpuThreads.size)
    }
}
