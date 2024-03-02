package dev.oblac.tdv.reporter

import java.time.LocalDateTime

data class ReportFile(
	val name: String,
	val content: String
)

data class Report(
	val files: List<ReportFile>,
	val dateTime: LocalDateTime = LocalDateTime.now(),
)
data class ResourceFile(val name: String) {
    private fun content(): String = object {}.javaClass.getResource("/template/$name")?.readText()
        ?: throw IllegalStateException("$name not found")

    fun toReportFile() = ReportFile(name, content())
}
