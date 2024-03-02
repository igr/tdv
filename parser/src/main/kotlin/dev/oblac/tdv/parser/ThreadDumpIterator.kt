package dev.oblac.tdv.parser

internal class ThreadDumpIterator(threadDumpText: String) : Iterator<String> {
	private val lines = threadDumpText.lines()
	private var i = 1

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

	fun back() {
		i--
	}

	override fun toString(): String {
		return """
			|[tdi] ${i-1}: ${lines[i-1]}
			|[tdi] $i: ${lines[i]}
			|[tdi] ${i+1}: ${lines[i+1]}
		""".trimMargin()
	}

	fun skipEmptyLines() {
		while (hasNext()) {
			val line = next()
			if (line.isNotEmpty()) {
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

}
