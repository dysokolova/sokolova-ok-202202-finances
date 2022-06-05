package ru.otus.otuskotlin.sokolova.finances.common.models

import java.time.LocalDateTime

data class FinsOperation(
    var userId: FinsUserId = FinsUserId.NONE,
    var description: String = "",
    var amount: Double = Double.NaN,
    var fromAccountId: FinsAccountId = FinsAccountId.NONE,
    var toAccountId: FinsAccountId = FinsAccountId.NONE,
    var operationDateTime:  LocalDateTime = LocalDateTime.MIN,
    var operationId: FinsOperationId = FinsOperationId.NONE,
)
