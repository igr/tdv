package dev.oblac.tdv.parser

import org.junit.jupiter.api.Test

class AppHeaderTest {

    @Test
    fun testHeader1() {
        val header = """"AssetDeletionJobService" #194 prio=5 os_prio=0 cpu=477.76ms elapsed=20779.97s tid=0x00007f7167e48c10 nid=0x3a038b waiting on condition  [0x00007f6f455d3000]"""
        AppHeader.parse(header, tdl).getOrThrow().apply {
            assert(name == "AssetDeletionJobService")
            assert(tid == "0x00007f7167e48c10")
            assert(nid == "0x3a038b")
            assert(prio == 5)
            assert(osPrio == 0)
            assert(daemon == false)
            assert(cpu == "477.76ms")
            assert(elapsed == "20779.97s")
            assert(hexValue == "0x00007f6f455d3000")
            assert(state == "waiting on condition")
        }
    }
    @Test
    fun testHeader2() {
        val header = """"CLI Requests Server" #23 [24835] daemon prio=5 os_prio=31 cpu=0.78ms elapsed=191.02s tid=0x000000012c825c00 nid=24835 runnable  [0x0000000172152000]"""
        AppHeader.parse(header, tdl).getOrThrow().apply {
            assert(name == "CLI Requests Server")
            assert(tid == "0x000000012c825c00")
            assert(nid == "24835")
            assert(prio == 5)
            assert(osPrio == 31)
            assert(daemon == true)
            assert(cpu == "0.78ms")
            assert(elapsed == "191.02s")
            assert(hexValue == "0x0000000172152000")
            assert(state == "runnable")
        }
    }

}
