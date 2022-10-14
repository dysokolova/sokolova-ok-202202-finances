package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationLock
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbOperationIdRequest (
    var userId: FinsUserId = FinsUserId.NONE,
    val operationId: FinsOperationId,
    val operationLock: FinsOperationLock = FinsOperationLock.NONE,
) {
    constructor(userId: FinsUserId, operation: FinsOperation): this(userId, operation.operationId, operation.operationLock)
}