package ru.otus.otuskotlin.sokolova.finances.common.models

@JvmInline
value class FinsRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FinsRequestId("")
    }
}
