package dev.oblac.tdv.reporter

data class ReportThreadGroup(
    val name: String,
    val count: Int,
    val runnable: Int,
    val blocked: Int,
    val waiting: Int,
    val timedWaiting: Int,
)
