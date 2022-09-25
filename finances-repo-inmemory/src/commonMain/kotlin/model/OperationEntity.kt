package ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.model

import kotlinx.datetime.toInstant
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*


data class OperationEntity (

    var userId: String = "",
    var description: String? = null,
    var amount: String? = null,
    var fromAccountId: String? = null,
    var toAccountId: String? = null,
    var operationDateTime: String? = null,
    var operationId: String? = null,
    var operationLock: String? = null,
        ){
    fun toInternal() = FinsOperation (
        description = description?: "",
        amount = amount?.toDouble() ?: Double.NaN,
        fromAccountId = fromAccountId?.let { FinsAccountId(it) }?: FinsAccountId.NONE,
        toAccountId = toAccountId?.let { FinsAccountId(it) }?: FinsAccountId.NONE,
        operationDateTime = operationDateTime?.toInstant() ?: Instant.NONE,
        operationId = operationId?.let { FinsOperationId(it) }?: FinsOperationId.NONE,
        operationLock = operationLock?.let { FinsOperationLock(it) } ?: FinsOperationLock.NONE,

        )
    constructor(userId: FinsUserId, model: FinsOperation) : this(
        userId = userId.asString(),
        description = model.description.takeIf { it.isNotBlank() },
        amount = model.amount.toString().takeIf { it.isNotBlank() },
        fromAccountId = model.fromAccountId.asString().takeIf { it.isNotBlank() },
        toAccountId = model.toAccountId.asString().takeIf { it.isNotBlank() },
        operationDateTime = model.operationDateTime.toString().takeIf { it.isNotBlank() },
        operationId = model.operationId.asString().takeIf { it.isNotBlank() },
        operationLock = model.operationLock.asString().takeIf { it.isNotBlank() },
    )
}