package ru.otus.otuskotlin.sokolova.finances.common

import kotlinx.datetime.Instant
import java.time.format.DateTimeFormatter

val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE