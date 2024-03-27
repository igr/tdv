package dev.oblac.tdv.parser

data class AppHeader(
    val name: String,
    val number: String,
    val tid: String,
    val nid: String,
    val prio: Int,
    val osPrio: Int,
    val daemon: Boolean,
    val cpu: String,
    val elapsed: String,
    val hexValue: String,
    val state: String,
) {
    companion object {
        fun parse(line: String, tdi: ThreadDumpLocation): Result<AppHeader> {
            return try {
                Result.success(parseAppHeader(line))
            } catch (e: IllegalStateException) {
                Result.failure(ParserException(e.message ?: "Error parsing app thread line", tdi))
            }
        }
    }
}

private fun parseAppHeader(line: String): AppHeader {
    val split = splitThreadLine(line)
    val name = split[0]
    val state = split[1]

    var tid: String? = null
    var nid: String? = null
    var daemon = false
    var prio: Int? = null
    var osPrio: Int? = null
    var cpu: String? = null
    var elapsed: String? = null
    var hexValue: String? = null
    var nid2: String? = null
    var number: String? = null

    for (element in split.slice(2 until split.size)) {
        when {
            element == "daemon" -> {
                daemon = true
            }

            element.startsWith("#") -> {
                number = element.removePrefix("#")
            }

            element.startsWith("tid=") -> {
                tid = element.removePrefix("tid=")
            }

            element.startsWith("nid=") -> {
                nid = element.removePrefix("nid=")
            }

            element.startsWith("prio=") -> {
                prio = element.removePrefix("prio=").toInt()
            }

            element.startsWith("os_prio=") -> {
                osPrio = element.removePrefix("os_prio=").toInt()
            }

            element.startsWith("cpu=") -> {
                cpu = element.removePrefix("cpu=")
            }

            element.startsWith("elapsed=") -> {
                elapsed = element.removePrefix("elapsed=")
            }

            element.startsWith("[0x") -> {
                hexValue = element.removePrefix("[").removeSuffix("]")
            }

            element.startsWith("[") -> {
                nid2 = element.removePrefix("[").removeSuffix("]")
            }

            else -> {
                throw IllegalStateException("Unknown element in thread header: $element")
            }
        }
    }

    if (nid2 != null && nid2 != nid) {
        throw IllegalStateException("Nid mismatch: $nid != $nid2")
    }

    return AppHeader(
        name = name,
        number = required(number, "number"),
        tid = required(tid, "tid"),
        nid = required(nid, "nid"),
        prio = required(prio, "prio"),
        osPrio = required(osPrio, "os_prio"),
        daemon = daemon,
        cpu = required(cpu, "cpu"),
        elapsed = required(elapsed, "elapsed"),
        hexValue = required(hexValue, "hexValue"),
        state = state
    )
}

data class JvmHeader(
    val name: String,
    val tid: String,
    val nid: String,
    val osPrio: Int,
    val cpu: String,
    val elapsed: String,
    val state: String,
) {
    companion object {
        fun parse(line: String, tdi: ThreadDumpLocation): Result<JvmHeader> {
            return try {
                Result.success(parseJvmHeader(line))
            } catch (e: IllegalStateException) {
                Result.failure(ParserException(e.message ?: "Error parsing jvm thread line", tdi))
            }
        }
    }
}

private fun parseJvmHeader(line: String): JvmHeader {
    val split = splitThreadLine(line)
    val name = split[0]
    val state = split[1]

    var tid: String? = null
    var nid: String? = null
    var osPrio: Int? = null
    var cpu: String? = null
    var elapsed: String? = null
    var nid2: String? = null

    for (element in split.slice(2 until split.size)) {
        when {
            element.startsWith("tid=") -> {
                tid = element.removePrefix("tid=")
            }

            element.startsWith("nid=") -> {
                nid = element.removePrefix("nid=")
            }

            element.startsWith("os_prio=") -> {
                osPrio = element.removePrefix("os_prio=").toInt()
            }

            element.startsWith("cpu=") -> {
                cpu = element.removePrefix("cpu=")
            }

            element.startsWith("elapsed=") -> {
                elapsed = element.removePrefix("elapsed=")
            }

            element.startsWith("[") -> {
                nid2 = element.removePrefix("[").removeSuffix("]")
            }

            else -> {
                throw IllegalStateException("Unknown element in thread header: $element")
            }
        }
    }

    if (nid2 != null && nid2 != nid) {
        throw IllegalStateException("Nid mismatch: $nid != $nid2")
    }

    return JvmHeader(
        name = name,
        tid = required(tid, "tid"),
        nid = required(nid, "nid"),
        osPrio = required(osPrio, "os_prio"),
        cpu = required(cpu, "cpu"),
        elapsed = required(elapsed, "elapsed"),
        state = state
    )
}
private fun <T> required(value: T?, field: String): T =
    value ?: throw IllegalStateException("$field required")

private fun splitThreadLine(line: String): Array<String> {
    if (!line.startsWith("\"")) {
        throw IllegalStateException("Thread name should start with \": $line")
    }
    // thread name
    val nameQuoteIndex = line.indexOf("\"", 1)
    if (nameQuoteIndex == -1) {
        throw IllegalStateException("Thread name should end with \": $line")
    }

    // thread state
    if (!line.endsWith("]")) {
        // SYSTEM threads do not have hex value, state is at the end of the line
        val startOfState = line.indexOf(' ', line.indexOf("nid="))
        if (startOfState == -1) {
            throw IllegalStateException("Thread state not detected: $line")
        }
        return arrayOf(
            line.substring(1, nameQuoteIndex),
            line.substring(startOfState).trim()
        ) +
            line.substring(
                nameQuoteIndex + 1,
                startOfState
            ).trim().split(" ").toTypedArray()
    }

    // APP threads

    val endOfState = line.lastIndexOf('[', line.length - 2)
    if (endOfState == -1) {
        throw IllegalStateException("Thread hex should be in square brackets: $line")
    }
    val startOfState = line.indexOf(' ', line.indexOf("nid="))
    if (startOfState == -1) {
        throw IllegalStateException("Thread state not detected: $line")
    }

    return arrayOf(
        line.substring(1, nameQuoteIndex),
        line.substring(startOfState, endOfState).trim()
    ) +
        (line.substring(
            nameQuoteIndex + 1,
            startOfState
        ) + line.substring(endOfState - 1)).trim().split(" ").toTypedArray()
}
