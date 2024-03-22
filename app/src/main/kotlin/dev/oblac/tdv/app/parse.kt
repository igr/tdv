package dev.oblac.tdv.app

import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.parser.ParseThreadDump
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream

fun parseThreadDumpReport(threadDumpFile: Path): ThreadDump {
    val threadDumpName = threadDumpFile.fileName.toString()
    val tdContent = if (threadDumpName.endsWith(".gz")) {
        Files.newInputStream(threadDumpFile).use { gzipStream ->
            GZIPInputStream(gzipStream).bufferedReader().readText()
        }
    } else {
        Files.readString(threadDumpFile)
    }

    return ParseThreadDump(tdContent)
}
