package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AppTest0001 {

    companion object {
        private lateinit var tda: ThreadDumpAnalysis
        @BeforeAll
        @JvmStatic
        fun setup() {
            tda = process("../issues/0001-threaddump.txt.gz")
        }
    }
    @Test
    fun testStats() {
        assertEquals(34, tda.stats.all.totalThreads)

        assertEquals(13, tda.stats.all.runnableThreads)
        assertEquals(12, tda.stats.app.runnableThreads)
        assertEquals(1, tda.stats.sys.runnableThreads)

        assertEquals(10, tda.stats.all.waitingThreads)
        assertEquals(9, tda.stats.app.waitingThreads)
        assertEquals(1, tda.stats.sys.waitingThreads)

        assertEquals(11, tda.stats.all.timedWaitingThreads)
        assertEquals(11, tda.stats.app.timedWaitingThreads)
        assertEquals(0, tda.stats.sys.timedWaitingThreads)
    }
}
