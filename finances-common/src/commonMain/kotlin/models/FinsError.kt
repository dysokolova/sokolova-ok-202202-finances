package ru.otus.otuskotlin.sokolova.finances.common.models

data class FinsError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: FinsErrorLevels = FinsErrorLevels.ERROR,
    val exception: Throwable? = null,
)
