package dev.oblac.tdv.parser

import dev.oblac.tdv.domain.*

internal object ParseStackTrace : (ThreadDumpIterator) -> List<StackFrame> {
    private val HEX_REF_REGEX = """<(0x[0-9a-fA-F]+)>""".toRegex()

	override fun invoke(tdi: ThreadDumpIterator): List<StackFrame> {
		val stackTrace = mutableListOf<StackFrame>()

		while (tdi.hasNext()) {
			val line = tdi.next().trim()
            when {
                line.isEmpty() -> {
                    tdi.back()
                    break
                }
                line == "No compile task" -> {
                    break
                }
                line.startsWith("Compiling:") -> {      // todo GC compiler thread
                    break
                }
            }

            val stackFrame = parseStackFrameLine(line.substring(3), tdi)    // "at " prefix

            while (true) {
                val lockLine = tdi.peek().trim()
                if (!lockLine.startsWith("- ")) {
                    break
                }
                val lock = parseLockLine(lockLine.substring(2), tdi)
                stackFrame.locks.add(lock)
                tdi.next()
            }

			stackTrace.add(stackFrame)
		}
		return stackTrace
	}

    private fun parseStackFrameLine(
        line: String,
        tdi: ThreadDumpIterator,
    ): StackFrame {
        val leftOfBracket = line.substringBefore("(")
        val className = leftOfBracket.substringBeforeLast(".")
        val methodName = leftOfBracket.substringAfterLast(".")

        val source = line.substringAfter("(").substringBefore(")")


        val stackFrame = when {
            source.contains(':') -> {
                val fileName = source.substringBeforeLast(":")
                val lineNumber = source.substringAfterLast(":")
                MethodStackFrame(
                    ClassName(className),
                    MethodName(methodName),
                    FileName(fileName),
                    FileLine(lineNumber.toInt())
                )
            }
            source.contains('@') -> {
                val module = source.substringBeforeLast("/")
                val moduleName = module.substringBeforeLast("@")
                val moduleVersion = module.substringAfterLast("@")
                val file = source.substringAfterLast("/")
                val fileName = if (file == "Unknown Source" || file == "Native Method") file else file.substringBeforeLast(":")
                val fileLine = if (file == fileName) -1 else file.substringAfterLast(":").toInt()
                ModuleMethodStackFrame(
                    ClassName(className),
                    MethodName(methodName),
                    ModuleName(moduleName),
                    ModuleVersion(moduleVersion),
                    FileName(fileName),
                    FileLine(fileLine)
                )
            }
            !source.contains('@') && source.contains('/') -> {
                val moduleName = source.substringBeforeLast("/")
                val file = source.substringAfterLast("/")
                val fileName = if (file == "Unknown Source" || file == "Native Method") file else file.substringBeforeLast(":")
                val fileLine = if (file == fileName) -1 else file.substringAfterLast(":").toInt()
                ModuleMethodStackFrame(
                    ClassName(className),
                    MethodName(methodName),
                    ModuleName(moduleName),
                    ModuleVersion.NA,
                    FileName(fileName),
                    FileLine(fileLine)
                )
            }
            source == "Unknown Source" || source == "Native Method" -> {
                MethodStackFrame(
                    ClassName(className),
                    MethodName(methodName),
                    FileName(source),
                    FileLine(-1)
                )
            }
            else -> throw ParserException("Could not parse stack frame: $line", tdi)
        }
        return stackFrame
    }

    private fun parseLockLine(lockLine: String, tdi: ThreadDumpLocation): Lock {
        return when {
            lockLine.startsWith("waiting to lock") -> {
                val matchResult = HEX_REF_REGEX.find(lockLine)
                val objectReference = matchResult?.groups?.get(1)?.value!!
                Blocked(ObjectReference(objectReference))
            }
            lockLine.startsWith("waiting to re-lock") -> {  // Enhancement from JDK 12, but helps readability
                val matchResult = HEX_REF_REGEX.find(lockLine)
                val objectReference = matchResult?.groups?.get(1)?.value!!
                Blocked(ObjectReference(objectReference))
            }
            lockLine.startsWith("waiting on") -> {
                val matchResult = HEX_REF_REGEX.find(lockLine)
                val objectReference = if (matchResult == null) "NA" else matchResult.groups[1]?.value!!
                Waiting(ObjectReference(objectReference))
            }
            lockLine.startsWith("locked") -> {
                val matchResult = HEX_REF_REGEX.find(lockLine)
                val objectReference = matchResult?.groups?.get(1)?.value!!
                Locked(ObjectReference(objectReference))
            }
            lockLine.startsWith("parking to wait for") -> {
                val matchResult = HEX_REF_REGEX.find(lockLine)
                val objectReference = matchResult?.groups?.get(1)?.value!!
                Parked(ObjectReference(objectReference))
            }
            lockLine.startsWith("eliminated") -> {
                val matchResult = HEX_REF_REGEX.find(lockLine)
                if (matchResult?.groups == null) {
                    val l = lockLine.substringAfter("<").substringBefore(">").trim()
                    Eliminated(ObjectReference(l))
                } else {
                    val objectReference = matchResult.groups.get(1)?.value!!
                    Eliminated(ObjectReference(objectReference))
                }
            }
            else -> {
                throw ParserException("Could not parse lock line: $lockLine", tdi)}
        }
    }
}
