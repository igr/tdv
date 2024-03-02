package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.JVMThreadInfo

data class CpuConsumingThread(
    val thread: JVMThreadInfo,
    val percentage: Float
)
