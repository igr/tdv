package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AppTest0005 {

    companion object {
        private lateinit var tda: ThreadDumpAnalysis
        @BeforeAll
        @JvmStatic
        fun setup() {
            tda = process("../issues/0005-tdump.txt.gz")
        }
    }

    @Test
    fun testStats() {
        assertEquals(54, tda.stats.all.totalThreads)
    }
}
