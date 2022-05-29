package ru.otus.otuskotlin.sokolova.finances.common.models
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

data class FinsHistFilter(
    var fromDateTime: Instant = Instant.NONE,
    var toDateTime: Instant = Instant.NONE,
)
