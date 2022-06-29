package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

data class FinsOperation(
    var userId: FinsUserId = FinsUserId.NONE,
    var description: String = "",
    var amount: Double = Double.NaN,
    var fromAccountId: FinsAccountId = FinsAccountId.NONE,
    var toAccountId: FinsAccountId = FinsAccountId.NONE,
    var operationDateTime:  Instant = Instant.NONE,
    var operationId: FinsOperationId = FinsOperationId.NONE,
)
