package dev.oblac.tdv.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import dev.oblac.tdv.analyzer.AnalyzeThreadDump
import dev.oblac.tdv.parser.ParseThreadDump
import dev.oblac.tdv.reporter.GenerateReport
import java.nio.file.Files
import java.nio.file.Path

class RunCommand : CliktCommand() {
    val threadDumpFile: Path by argument(
        "thread-dump-file-name",
        help = "The file containing the thread dump"
    ).path()

    override fun run() {
        generateThreadDumpReport(threadDumpFile)
    }
}

fun main(args: Array<String>) {
    RunCommand().main(args)
}

internal fun generateThreadDumpReport(threadDumpFile: Path) {
    val threadDumpName = threadDumpFile.fileName.toString()

    println("Generating report for $threadDumpName")

    val tdContent = Files.readString(threadDumpFile)
    val td = ParseThreadDump(tdContent)
    val tda = AnalyzeThreadDump(td)

    GenerateReport(td, tda, threadDumpName).files.forEach {
        Files.writeString(Path.of("out", it.name), it.content)
    }
    println("Report for $threadDumpName generated")
}
