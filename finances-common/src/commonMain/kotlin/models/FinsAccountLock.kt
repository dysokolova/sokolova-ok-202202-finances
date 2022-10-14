package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FinsAccountLock(private val accountLockId: String) {
    fun asString() = accountLockId

    companion object {
        val NONE = FinsAccountLock("")
    }
}
