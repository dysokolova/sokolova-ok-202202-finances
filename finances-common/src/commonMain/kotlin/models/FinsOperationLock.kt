package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlin.jvm.JvmInline
@JvmInline
value class FinsOperationLock(private val operationLock: String) {
    fun asString() = operationLock

    companion object {
        val NONE = FinsOperationLock("")
    }
}
