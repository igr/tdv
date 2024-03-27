package dev.oblac.tdv.parser

import org.junit.jupiter.api.Test

class JvmHeaderTest {

    @Test
    fun testJvmHeader1() {
        val header =
            """"GC Thread#0" os_prio=0 cpu=293286.37ms elapsed=25563.14s tid=0x00007f0b5c07d250 nid=0x3ee480 runnable"""
        JvmHeader.parse(header, tdl).getOrThrow().apply {
            assert(name == "GC Thread#0")
            assert(tid == "0x00007f0b5c07d250")
            assert(nid == "0x3ee480")
            assert(osPrio == 0)
            assert(cpu == "293286.37ms")
            assert(elapsed == "25563.14s")
            assert(state == "runnable")
        }
    }

    @Test
    fun testJvmHeader2() {
        val header =
            """"VM Thread" os_prio=31 cpu=92.86ms elapsed=191.47s tid=0x000000013c70ea90 nid=19203 runnable"""
        JvmHeader.parse(header, tdl).getOrThrow().apply {
            assert(name == "VM Thread")
            assert(tid == "0x000000013c70ea90")
            assert(nid == "19203")
            assert(osPrio == 31)
            assert(cpu == "92.86ms")
            assert(elapsed == "191.47s")
            assert(state == "runnable")
        }
    }
}
