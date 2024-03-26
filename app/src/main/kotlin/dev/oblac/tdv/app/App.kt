package dev.oblac.tdv.app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.terminal.Terminal
import dev.oblac.tdv.analyzer.AnalyzeThreadDump
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
    val t = Terminal()

    val threadDumpName = threadDumpFile.fileName.toString()
    t.println("Generating report for ${(bold)(threadDumpName)}")

    val td = parseThreadDumpReport(threadDumpFile)
    val tda = AnalyzeThreadDump(td)

    val destination = Path.of("out", threadDumpName)
    Files.createDirectories(destination)

    GenerateReport(td, tda, threadDumpName).files.forEach {
        Files.writeString(destination.resolve(it.name), it.content)
    }
    t.println("Report generated:")
    t.println(brightGreen("./${destination}/index.html"))
    t.println("Done.")
}
