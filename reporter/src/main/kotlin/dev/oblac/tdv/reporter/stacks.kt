package dev.oblac.tdv.reporter

data class ReportThreadStackFreq(
    val state: String,
    val count: Int,
    val stackTrace: List<String>
)
