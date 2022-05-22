package ru.otus.otuskotlin.sokolova.finances.mappers.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.DATE_TIME_FORMATTER
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions.UnknownFinsCommand

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
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountRead() = AccountReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountUpdate() = AccountUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    account = accountResponse.toTransport()
)

fun FinsContext.toTransportAccountDelete() = AccountDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

fun FinsContext.toTransportAccountSearch() = AccountSearchResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    accounts = accountsResponse.toTransport()
)

fun FinsContext.toTransportAccountHistory() = AccountHistoryResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    operations = operationsResponse.toTransport()
)

fun FinsContext.toTransportOperationCreate() = OperationCreateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationRead() = OperationReadResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationUpdate() = OperationUpdateResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
    operation = operationResponse.toTransport()
)

fun FinsContext.toTransportOperationDelete() = OperationDeleteResponse(
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == FinsState.RUNNING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    errors = errors.toTransportErrors(),
)

private fun FinsAccount.toTransport(): Account = Account(
   userId = userId.takeIf { it != FinsUserId.NONE }?.asString(),
    name = name.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    accountId = accountId.takeIf { it != FinsAccountId.NONE }?.asString(),
)

private fun Account.toObj(): AccountObj = AccountObj(
    account = this,
)

fun List<FinsAccount>.toTransport(): List<AccountObj>? = this
    .map { it.toTransport() }
    .map {it.toObj()}
    .toList()
    .takeIf { it.isNotEmpty() }


private fun FinsOperation.toTransport(): Operation = Operation(
    userId = userId.takeIf { it != FinsUserId.NONE }?.asString(),
    description = description.takeIf { it.isNotBlank() },
    amount = description.takeIf { it.isNotBlank() },
    fromAccountId = fromAccountId.takeIf { it != FinsAccountId.NONE }?.asString(),
    toAccountId = toAccountId.takeIf { it != FinsAccountId.NONE }?.asString(),
    operationDateTime = operationDateTime.format(DATE_TIME_FORMATTER).takeIf { it.isNotBlank() },
    operationId = operationId.takeIf { it != FinsOperationId.NONE }?.asString(),
)


private fun Operation.toObj(): OperationObj = OperationObj(
    operation = this,
)

@JvmName("toTransportFinsOperation")
fun List<FinsOperation>.toTransport(): List<OperationObj>? = this
    .map { it.toTransport() }
    .map {it.toObj()}
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
