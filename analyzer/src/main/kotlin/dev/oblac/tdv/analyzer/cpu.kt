package dev.oblac.tdv.analyzer

import dev.oblac.tdv.domain.AppThreadInfo

data class CpuConsumingThread(
	val thread: AppThreadInfo,
	val percentage: Float
)
