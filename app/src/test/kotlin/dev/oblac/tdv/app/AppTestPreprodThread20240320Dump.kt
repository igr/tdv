package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class AppTestPreprodThread20240320Dump {

    companion object {
        private lateinit var tda: ThreadDumpAnalysis
        @BeforeAll
        @JvmStatic
        fun setup() {
            tda = process("../in/preprod-Thread-20240320.dump")
        }
    }

    @Test
    fun testMissingLocks() {
        assertEquals(62, tda.missingLocks.parked.size)
        assertEquals("0x0000000410b9f828", tda.missingLocks.parked[0].missingLock.toString())
        assertEquals(974, tda.missingLocks.parked[0].locks.size)
    }

}
