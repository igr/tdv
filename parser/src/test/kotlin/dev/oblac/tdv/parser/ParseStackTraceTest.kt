package dev.oblac.tdv.parser

import org.junit.jupiter.api.Test

class ParseStackTraceTest {

    @Test
    fun testParseStackTrace() {
        val stackTrace = "\n" +
            "\tat java.lang.Object.wait(java.base@11.0.20/Native Method)\n" +
            "\t- waiting on <no object reference available>\n" +
            "\tat java.lang.ref.ReferenceQueue.remove(java.base@11.0.20/ReferenceQueue.java:155)\n" +
            "\t- waiting to re-lock in wait() <0x00007f28a50d1550> (a java.lang.ref.ReferenceQueue\$Lock)\n" +
            "\tat java.lang.ref.ReferenceQueue.remove(java.base@11.0.20/ReferenceQueue.java:176)\n" +
            "\tat java.lang.ref.Finalizer\$FinalizerThread.run(java.base@11.0.20/Finalizer.java:170)\n"

        ParseStackTrace(ThreadDumpIterator(stackTrace))
    }
}
