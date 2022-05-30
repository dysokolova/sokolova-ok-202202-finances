package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlin.jvm.JvmInline
@JvmInline
value class FinsRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = FinsRequestId("")
    }
}
