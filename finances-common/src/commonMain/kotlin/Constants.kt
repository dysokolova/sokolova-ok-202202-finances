package ru.otus.otuskotlin.sokolova.finances.common

import kotlinx.datetime.Instant


private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE

//fun Instant.parse(parseString: String?) = if (parseString != null) Instant.parse(parseString) else INSTANT_NONE