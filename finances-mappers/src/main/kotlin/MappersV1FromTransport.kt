package ru.otus.otuskotlin.sokolova.finances.mappers.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions.UnknownRequestClass
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE

fun FinsContext.fromTransport(request: IRequest) = when(request){
    is AccountCreateRequest -> fromTransport(request)
    is AccountReadRequest   -> fromTransport(request)
    is AccountUpdateRequest -> fromTransport(request)
    is AccountDeleteRequest -> fromTransport(request)
    is AccountSearchRequest -> fromTransport(request)
    is AccountHistoryRequest -> fromTransport(request)
    is OperationCreateRequest -> fromTransport(request)
    is OperationReadRequest   -> fromTransport(request)
    is OperationUpdateRequest -> fromTransport(request)
    is OperationDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toUserId() = this?.let { FinsUserId(it) } ?: FinsUserId.NONE
private fun String?.toAccountId() = this?.let { FinsAccountId(it) } ?: FinsAccountId.NONE
private fun String?.toOperationId() = this?.let { FinsOperationId(it) } ?: FinsOperationId.NONE
private fun AccountId?.toAccountWithId() = FinsAccount(accountId = this?.accountId.toAccountId())
private fun OperationId?.toOperationWithId() = FinsOperation(operationId = this?.operationId.toOperationId())
private fun IRequest?.requestId() = this?.requestId?.let { FinsRequestId(it) } ?: FinsRequestId.NONE

private fun Debug?.transportToWorkMode(): FinsWorkMode = when(this?.mode) {
    RequestDebugMode.PROD -> FinsWorkMode.PROD
    RequestDebugMode.TEST -> FinsWorkMode.TEST
    RequestDebugMode.STUB -> FinsWorkMode.STUB
    null -> FinsWorkMode.PROD
}

private fun Debug?.transportToStubCase(): FinsStubs = when(this?.stub) {
    RequestDebugStubs.SUCCESS -> FinsStubs.SUCCESS
    RequestDebugStubs.NOT_FOUND -> FinsStubs.NOT_FOUND
    RequestDebugStubs.BAD_USER_ID -> FinsStubs.BAD_USER_ID
    RequestDebugStubs.BAD_NAME -> FinsStubs.BAD_NAME
    RequestDebugStubs.BAD_DESCRIPTION -> FinsStubs.BAD_DESCRIPTION
    RequestDebugStubs.BAD_ACCOUNT_ID -> FinsStubs.BAD_ACCOUNT_ID
    RequestDebugStubs.BAD_AMOUNT -> FinsStubs.BAD_AMOUNT
    RequestDebugStubs.BAD_FROM_ACCOUNT_ID -> FinsStubs.BAD_FROM_ACCOUNT_ID
    RequestDebugStubs.BAD_TO_ACCOUNT_ID -> FinsStubs.BAD_TO_ACCOUNT_ID
    RequestDebugStubs.BAD_OPERATION_DATE_TIME -> FinsStubs.BAD_OPERATION_DATE_TIME
    RequestDebugStubs.BAD_OPERATION_ID -> FinsStubs.BAD_OPERATION_ID
    RequestDebugStubs.CANNOT_DELETE -> FinsStubs.CANNOT_DELETE
    RequestDebugStubs.BAD_SEARCH_FILTER -> FinsStubs.BAD_SEARCH_FILTER
    RequestDebugStubs.BAD_FROM_DATE_TIME -> FinsStubs.BAD_FROM_DATE_TIME
    RequestDebugStubs.BAD_TO_DATE_TIME -> FinsStubs.BAD_TO_DATE_TIME
    null -> FinsStubs.NONE
}

fun FinsContext.fromTransport(request: AccountCreateRequest) {
    command = FinsCommand.ACCOUNTCREATE
    requestId = request.requestId()
    accountRequest = request.account?.toInternal() ?: FinsAccount()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountReadRequest) {
    command = FinsCommand.ACCOUNTREAD
    requestId = request.requestId()
    accountRequest = request.account.toAccountWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountUpdateRequest) {
    command = FinsCommand.ACCOUNTUPDATE
    requestId = request.requestId()
    accountRequest = request.account?.toInternal() ?: FinsAccount()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountDeleteRequest) {
    command = FinsCommand.ACCOUNTDELETE
    requestId = request.requestId()
    accountRequest = request.account.toAccountWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountSearchRequest) {
    command = FinsCommand.ACCOUNTSEARCH
    requestId = request.requestId()
    accountFilterRequest = request.searchFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountHistoryRequest) {
    command = FinsCommand.ACCOUNTHISTORY
    requestId = request.requestId()
    accountRequest = request.account.toAccountWithId()
    accountHistoryRequest = request.historyFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationCreateRequest) {
    command = FinsCommand.OPERATIONCREATE
    requestId = request.requestId()
    operationRequest = request.operation?.toInternal() ?: FinsOperation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationReadRequest) {
    command = FinsCommand.OPERATIONREAD
    requestId = request.requestId()
    operationRequest = request.operation.toOperationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationUpdateRequest) {
    command = FinsCommand.OPERATIONUPDATE
    requestId = request.requestId()
    operationRequest = request.operation?.toInternal() ?: FinsOperation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationDeleteRequest) {
    command = FinsCommand.OPERATIONDELETE
    requestId = request.requestId()
    operationRequest = request.operation.toOperationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SearchFilterObj?.toInternal(): FinsSrchFilter = FinsSrchFilter(
    searchFilter = this?.searchFilter ?: ""
)
private fun HistoryFilterObj?.toInternal(): FinsHistFilter = FinsHistFilter(
    fromDateTime = this?.fromDateTime?.let { Instant.parse(it) } ?:Instant.NONE,
    toDateTime = this?.toDateTime?.let { Instant.parse(it) } ?:Instant.NONE,
)
private fun Account.toInternal(): FinsAccount = FinsAccount(
    userId = this.userId.toUserId(),
    description = this.description ?: "",
    name = this.name ?: "",
    amount = this.amount?.toDouble() ?: 0.0,
    accountId = this.accountId.toAccountId(),
)
private fun AccountData.toInternal(): FinsAccount = FinsAccount(
    userId = this.userId.toUserId(),
    name = this.name ?: "",
    description = this.description ?: "",
    amount = this.amount?.toDouble() ?: 0.0,
)
private fun Operation.toInternal(): FinsOperation = FinsOperation(
    userId = this.userId.toUserId(),
    description = this.description ?: "",
    amount = this.amount?.toDouble() ?: 0.0,
    fromAccountId = this.fromAccountId.toAccountId(),
    toAccountId = this.toAccountId.toAccountId(),
    operationDateTime = this.operationDateTime?.let { Instant.parse(it) } ?:Instant.NONE,
    operationId = this.operationId.toOperationId(),
)
private fun OperationData.toInternal(): FinsOperation = FinsOperation(
    userId = this.userId.toUserId(),
    description = this.description ?: "",
    amount = (this.amount)?.toDouble() ?: 0.0,
    fromAccountId = this.fromAccountId.toAccountId(),
    toAccountId = this.toAccountId.toAccountId(),
    operationDateTime = this.operationDateTime?.let { Instant.parse(it) } ?:Instant.NONE,
)