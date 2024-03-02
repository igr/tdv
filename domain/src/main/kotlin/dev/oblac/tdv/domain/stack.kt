package dev.oblac.tdv.domain

@JvmInline
value class ClassName(private val value: String) {
    override fun toString() = value
    fun isJavaException() = value == "java.lang.Throwable" || value == "java.lang.Exception"
    fun isAnyException() = value.contains("Throwable") || value.contains("Exception")
}

@JvmInline
value class MethodName(private val value: String) {
    override fun toString() = value
}

@JvmInline
value class FileName(private val value: String) {
    override fun toString() = value
}

@JvmInline
value class FileLine(private val value: Int) {
    fun isNA(): Boolean = value == -1
    override fun toString(): String {
        return "$value"
    }

}

@JvmInline
value class ModuleName(private val value: String)

@JvmInline
value class ModuleVersion(private val value: String) {
	companion object {
        val NA: ModuleVersion = ModuleVersion("")
    }
}


sealed class StackFrame(
    open val className: ClassName,
    open val methodName: MethodName,
    open val fileName: FileName,
    open val fileLine: FileLine,
) {
    val locks: MutableList<Lock> = mutableListOf()
    fun fileName(): FileName = fileName
}

data class MethodStackFrame(
    override val className: ClassName,
    override val methodName: MethodName,
    override val fileName: FileName,
    override val fileLine: FileLine,
) : StackFrame(className, methodName, fileName, fileLine) {
    override fun toString(): String {
        return if (fileLine.isNA()) "    at $className.$methodName($fileName)"
        else "    at $className.$methodName($fileName:$fileLine)"
    }
}

data class ModuleMethodStackFrame(
    override val className: ClassName,
    override val methodName: MethodName,
    val moduleName: ModuleName,
    val moduleVersion: ModuleVersion,
    override val fileName: FileName,
    override val fileLine: FileLine,
) : StackFrame(className, methodName, fileName, fileLine) {
    override fun toString(): String {
        return if (fileLine.isNA()) "    at $className.$methodName($moduleName@$moduleVersion/Unknown Source)"
        else "    at $className.$methodName($moduleName@$moduleVersion/$fileName:$fileLine)"
    }
}
