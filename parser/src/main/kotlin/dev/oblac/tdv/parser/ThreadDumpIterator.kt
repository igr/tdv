package dev.oblac.tdv.parser

interface ThreadDumpLocation {
    override fun toString(): String
}

internal class ThreadDumpIterator(threadDumpText: String) : Iterator<String>, ThreadDumpLocation {
	private val lines = threadDumpText.lines()
	private var i = 0

	override fun next(): String {
		return lines[i++]
	}

	override fun hasNext(): Boolean {
		return i < lines.size
	}

	fun peek() = lines[i]

	fun skip() {
		i++
	}
    fun skip(times: Int) {
        i += times
    }

	fun back() {
		i--
	}

	override fun toString(): String {
		return """
			|[tdi] ${i-1}: ${if (i == 0) "<START>" else lines[i-1]}
			|[tdi] $i: ${lines[i]}
			|[tdi] ${i+1}: ${lines[i+1]}
		""".trimMargin()
	}

	fun skipEmptyLines() {
		while (hasNext()) {
			val line = next()
			if (line.trim().isNotEmpty()) {
				back()
				break
			}
		}
	}
    fun skipNonEmptyLines() {
        while (hasNext()) {
            val line = next()
            if (line.trim().isEmpty()) {
                back()
                break
            }
        }
    }

	fun skipUntilMatch(match: String) {
		while (hasNext()) {
			val line = next()
			if (line == match) {
				back()
				break
			}
		}
	}

    fun skipLineIf(function: (String) -> Boolean) {
        if (function(peek())) {
            skip()
        }
    }

}
