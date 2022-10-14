package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbOperationRequest (
    var userId: FinsUserId = FinsUserId.NONE,
    val operation: FinsOperation
)