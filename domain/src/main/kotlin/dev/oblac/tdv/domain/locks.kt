package dev.oblac.tdv.domain

@JvmInline
value class ObjectReference(private val value: String) {
    fun sameRef(other: ObjectReference): Boolean = value == other.value

    override fun toString(): String = value
}

sealed interface Lock {
    val value: ObjectReference
}

@JvmInline
value class Blocked(override val value: ObjectReference) : Lock {
    override fun toString(): String = "Blocked(${value})"

}
@JvmInline
value class Locked(override val value: ObjectReference) : Lock {
    override fun toString(): String = "Locked(${value})"
}
@JvmInline
value class Waiting(override val value: ObjectReference) : Lock {
    override fun toString(): String = "Waiting(${value})"
}
@JvmInline
value class Parked(override val value: ObjectReference) : Lock {
    override fun toString(): String = "Parked(${value})"
}
@JvmInline
value class Eliminated(override val value: ObjectReference) : Lock {
    override fun toString(): String = "Eliminated(${value})"
}
