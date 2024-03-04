package dev.oblac.tdv.reporter

import dev.oblac.tdv.analyzer.ThreadDumpAnalysis
import dev.oblac.tdv.domain.ThreadDump
import dev.oblac.tdv.reporter.pebble.JsonWriterPebbleExtension
import dev.oblac.tdv.reporter.pebble.StringContainsOneOf
import io.pebbletemplates.pebble.PebbleEngine
import java.io.StringWriter


object GenerateReport : (ThreadDump, ThreadDumpAnalysis, String) -> Report {
    override fun invoke(td: ThreadDump, tda: ThreadDumpAnalysis, name: String): Report {
        val engine = PebbleEngine.Builder()
            .extension(JsonWriterPebbleExtension())
            .extension(StringContainsOneOf())
            .build()
        val compiledTemplate = engine.getTemplate("template/report.pebble")

        val context: MutableMap<String, Any> = HashMap()

        context["td"] =
            td
        context["stats"] =
            tda.stats
        context["blockTree"] =
            tda.blockTree.root
                .map { ReportBlockNode.fromBlockNode(it) }
                .toList()
        context["explain"] =
            tda.blockTree.root
                .map { BlockExplanation.fromLockRef(it.blocker, it.totalBlocked) }
                .toList()
        context["flamegraph"] =
            FlameNode.fromCallTree(tda.callTree)
        context["callgraph"] =
            renderCallTreeGraph(tda.callTree)
        context["exceptions"] =
            tda.exceptions
                .map {
                    ReportExceptionData(
                        it.name.toString(),
                        it.tid.toString(),
                        it.stackTrace.filter { it.className.isAnyException() }
                            .map { it.className.toString() }
                            .distinct().toList(),
                        it.stackTrace.map { it.toString() }.toList()
                    )
                }
        context["cpuc"] =
            tda.maxCpuThreads.map {
                ReportCPUConsumingThread(
                    it.thread.name.toString(),
                    it.thread.tid.toString(),
                    it.percentage
                )
            }
        context["usts"] =
            tda.uniqueStackTraces
                .takeWhile { it.count >= 10 }
//                .take(10)
                .map {
                    ReportThreadStackFreq(it.threadInfo.name.toString(),
                        it.count,
                        it.threadInfo.stackTrace.map { it.toString() })
                }

        val writer = StringWriter()
        compiledTemplate.evaluate(writer, context)

        return Report(
            listOf(
                ReportFile("report-${name}.html", writer.toString()),
                ResourceFile("style.css").toReportFile(),
                ResourceFile("canvasjs.min.js").toReportFile(),
                ResourceFile("d3.v7.min.js").toReportFile(),
                ResourceFile("d3-flamegraph.min.css").toReportFile(),
                ResourceFile("d3-flamegraph.min.js").toReportFile(),
                ResourceFile("charts.js").toReportFile(),
                ResourceFile("tree.js").toReportFile(),
                ResourceFile("expand-collapse.svg").toReportFile()
            )
        )
    }
}
