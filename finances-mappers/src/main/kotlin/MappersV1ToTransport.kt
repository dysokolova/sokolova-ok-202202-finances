package ru.otus.otuskotlin.sokolova.finances.mappers.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions.UnknownFinsCommand
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

fun FinsContext.toTransport(): IResponse = when (val cmd = command) {
    FinsCommand.ACCOUNTCREATE -> toTransportAccountCreate()
    FinsCommand.ACCOUNTREAD -> toTransportAccountRead()
    FinsCommand.ACCOUNTUPDATE -> toTransportAccountUpdate()
    FinsCommand.ACCOUNTDELETE -> toTransportAccountDelete()
    FinsCommand.ACCOUNTSEARCH -> toTransportAccountSearch()
    FinsCommand.ACCOUNTHISTORY -> toTransportAccountHistory()
    FinsCommand.OPERATIONCREATE -> toTransportOperationCreate()
    FinsCommand.OPERATIONREAD -> toTransportOperationRead()
    FinsCommand.OPERATIONUPDATE -> toTransportOperationUpdate()
    FinsCommand.OPERATIONDELETE -> toTransportOperationDelete()
    FinsCommand.NONE -> throw UnknownFinsCommand(cmd)
}

fun FinsContext.toTransportAccountCreate() = AccountCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountRead() = AccountReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountUpdate() = AccountUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountDelete() = AccountDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
)

fun FinsContext.toTransportAccountSearch() = AccountSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    accounts = accountsResponse.toTransport()
)

fun FinsContext.toTransportAccountHistory() = AccountHistoryResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    operations = operationsResponse.toTransport()
)

fun FinsContext.toTransportOperationCreate() = OperationCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationRead() = OperationReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationUpdate() = OperationUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationDelete() = OperationDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    userId = this.userId.asString().takeIf { it.isNotBlank() },
    result = state.toTransport(),
    errors = errors.toTransportErrors(),
)

private fun FinsAccount.toTransport(): Account = Account(
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    amount = amount.takeIf { !it.isNaN() }?.toString(),
    accountId = accountId.takeIf { it != FinsAccountId.NONE }?.asString(),
    accountLock = accountLock.takeIf { it != FinsAccountLock.NONE }?.asString(),
)

private fun Account.toObj(): AccountObj = AccountObj(
    account = this,
)

fun List<FinsAccount>.toTransport(): List<AccountObj>? = this
    .map { it.toTransport() }
    .map { it.toObj() }
    .toList()
    .takeIf { it.isNotEmpty() }


private fun FinsOperation.toTransport(): Operation = Operation(
    description = description.takeIf { it.isNotBlank() },
    amount = amount.takeIf { !it.isNaN() }?.toString(),
    fromAccountId = fromAccountId.takeIf { it != FinsAccountId.NONE }?.asString(),
    toAccountId = toAccountId.takeIf { it != FinsAccountId.NONE }?.asString(),
    operationDateTime = operationDateTime.takeIf { it != Instant.NONE }?.toString(),
    operationId = operationId.takeIf { it != FinsOperationId.NONE }?.asString(),
    operationLock = operationLock.takeIf { it != FinsOperationLock.NONE }?.asString(),
)


private fun Operation.toObj(): OperationObj = OperationObj(
    operation = this,
)

@JvmName("toTransportFinsOperation")
fun List<FinsOperation>.toTransport(): List<OperationObj>? = this
    .map { it.toTransport() }
    .map { it.toObj() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun List<FinsError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransport() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun FinsError.toTransport() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun FinsState.toTransport(): ResponseResult? = when (this) {
    FinsState.RUNNING, FinsState.FINISHING -> ResponseResult.SUCCESS
    FinsState.FAILING -> ResponseResult.ERROR
    else -> null
}
