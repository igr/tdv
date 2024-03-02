package dev.oblac.tdv.reporter

data class ReportCPUConsumingThread(
    val threadName: String,
    val threadId: String,
    val percentage: Float,
)
