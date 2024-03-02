package dev.oblac.tdv.reporter

data class ReportExceptionData(
    val threadName: String,
    val threadId: String,
    val exceptions: List<String>
)
