package ru.otus.otuskotlin.sokolova.finances.common.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class FinsHistFilter(
    var fromDateTime: LocalDateTime = LocalDateTime.MIN,
    var toDateTime: LocalDateTime = LocalDateTime.MIN,
)
