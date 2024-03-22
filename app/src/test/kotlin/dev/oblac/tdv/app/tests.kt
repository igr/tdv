package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.AnalyzeThreadDump
import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import java.nio.file.Path

fun process(fileName: String): ThreadDumpAnalysis {
    val td = parseThreadDumpReport(Path.of(fileName))
    return AnalyzeThreadDump(td)
}
