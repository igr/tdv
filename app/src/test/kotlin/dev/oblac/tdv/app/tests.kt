package dev.oblac.tdv.app

import dev.oblac.tdv.analyzer.AnalyzeThreadDump
import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import dev.oblac.tdv.parser.ParseThreadDump
import java.nio.file.Files
import java.nio.file.Path

fun process(fileName: String): ThreadDumpAnalysis {
    val tdContent = Files.readString(Path.of(fileName))
    val td = ParseThreadDump(tdContent)
    return AnalyzeThreadDump(td)
}
