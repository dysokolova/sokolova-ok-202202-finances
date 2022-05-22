package ru.otus.otuskotlin.sokolova.finances.common.models

import java.time.LocalDateTime

data class FinsOperation(
    var userId: FinsUserId = FinsUserId.NONE,
    var description: String = "",
    var amount: Double = 0.0,
    var fromAccountId: FinsAccountId = FinsAccountId.NONE,
    var toAccountId: FinsAccountId = FinsAccountId.NONE,
    var operationDateTime:  LocalDateTime = LocalDateTime.MIN,
    val operationId: FinsOperationId = FinsOperationId.NONE,
)
