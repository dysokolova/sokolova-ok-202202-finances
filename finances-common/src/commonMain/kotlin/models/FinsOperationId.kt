package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlin.jvm.JvmInline
@JvmInline
value class FinsOperationId(private val operationId: String) {
    fun asString() = operationId

    companion object {
        val NONE = FinsOperationId("")
    }
}
