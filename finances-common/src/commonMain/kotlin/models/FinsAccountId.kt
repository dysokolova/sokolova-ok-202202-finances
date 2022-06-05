package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class FinsAccountId(private val accountId: String) {
    fun asString() = accountId

    companion object {
        val NONE = FinsAccountId("")
    }
}
