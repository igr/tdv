package dev.oblac.tdv.reporter

data class ReportThreadStackFreq(
    val name: String,
    val tid: String,
    val state: String,
    val count: Int,
    val stackTrace: List<String>
)
