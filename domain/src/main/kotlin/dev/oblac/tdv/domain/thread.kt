package dev.oblac.tdv.domain

import kotlin.time.Duration
import kotlin.time.DurationUnit

@JvmInline
value class ThreadNo(private val value: Int) {
    override fun toString() = value.toString()
}

@JvmInline
value class ThreadName(private val value: String) {
    override fun toString() = value
    fun contains(substring: String) = value.contains(substring)
}

@JvmInline
value class ThreadId(private val value: String) {
    override fun toString() = value
}

@JvmInline
value class NativeId(private val value: String) {
    override fun toString() = value
}

enum class Daemon {
    DAEMON,
    NOT_DAEMON;
    companion object {
        fun of(value: Boolean) = if (value) DAEMON else NOT_DAEMON
    }

}

@JvmInline
value class Cpu(private val value: Duration) {
	companion object {
		fun of(value: String) = Cpu(Duration.parse(value))
	}
    fun toMillis() = value.toLong(DurationUnit.MILLISECONDS)
}

@JvmInline
value class OsPriority(private val value: Int)

@JvmInline
value class Elapsed(private val value: Duration) {
	companion object {
		fun of(value: String) = Elapsed(Duration.parse(value))
	}
}

enum class ThreadPriority(val priority: Int) {
	P1(1),
	P2(2),
	P3(3),
	P4(4),
	P5(5),
	P6(6),
	P7(7),
	P8(8),
	P9(9),
	P10(10);

	companion object {
		fun of(i: Int) = ThreadPriority.entries.first { it.priority == i }
		fun default() = P5
	}
}

enum class ThreadState {
	NEW,
	RUNNABLE,
	BLOCKED,
	WAITING,
	TIMED_WAITING,
	TERMINATED
}

@JvmInline
value class ThreadSubStatus(private val value: String)

@JvmInline
value class ThreadPoolName(private val value: String) {
    override fun toString() = value
}
