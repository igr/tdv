package dev.oblac.tdv.reporter

data class ReportThreadStackFreq(
    val state: String,
    val count: Int,
    val stackTrace: List<String>
)

data class ReportThreadStack(
    val name: String,
    val threadId: String,
    val state: String,
    val stackTrace: List<String>
)
