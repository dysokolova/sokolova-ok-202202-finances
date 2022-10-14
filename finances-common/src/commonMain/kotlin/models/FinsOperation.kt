package ru.otus.otuskotlin.sokolova.finances.common.models

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

data class FinsOperation(
    var description: String = "",
    var amount: Double = Double.NaN,
    var fromAccountId: FinsAccountId = FinsAccountId.NONE,
    var toAccountId: FinsAccountId = FinsAccountId.NONE,
    var operationDateTime:  Instant = Instant.NONE,
    var operationId: FinsOperationId = FinsOperationId.NONE,
    var operationLock: FinsOperationLock = FinsOperationLock.NONE,
){
    fun deepCopy(
    ) = FinsOperation(
        description = this@FinsOperation.description,
        amount = this@FinsOperation.amount,
        fromAccountId = this@FinsOperation.fromAccountId,
        toAccountId = this@FinsOperation.toAccountId,
        operationDateTime = this@FinsOperation.operationDateTime,
        operationId = this@FinsOperation.operationId,
        operationLock = this@FinsOperation.operationLock
    )
}


