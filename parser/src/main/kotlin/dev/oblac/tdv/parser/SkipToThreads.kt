package dev.oblac.tdv.parser

/**
 * The whole purpose of this function is to skip
 * the bunch of lines that are not related to threads.
 */
internal object SkipToThreads : (ThreadDumpIterator) -> Unit {
    override fun invoke(tdi: ThreadDumpIterator) {
        tdi.skipEmptyLines()

        // Try if the there is no headers
        if (AppHeader.parse(tdi.peek(), tdi).isSuccess) {
            return
        }

        // we want to skip all kind of header lines
        tdi.skipNonEmptyLines()
        tdi.skipEmptyLines()

        // skip all SMR info
        tdi.peek().let {
            if (it == "Threads class SMR info:") {
                skipSMR(tdi)
            }
        }

        tdi.skipEmptyLines()
    }

    private fun skipSMR(tdi: ThreadDumpIterator) {
        fun detectSmr(line: String): Boolean {
            return line.startsWith("_java_thread_list=")
                || line.startsWith("_to_delete_list=")
                || line.startsWith("next-> ")
        }
        tdi.skip()

        while (true) {
            val nextLine = tdi.peek()
            if (!detectSmr(nextLine)) {
                break
            }
            tdi.skipUntilMatch("}")
            tdi.skip()
        }
    }

}
