package ru.otus.otuskotlin.sokolova.finances.mappers.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.exceptions.UnknownRequestClass
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.biz.errors.doubleValidationError
import ru.otus.otuskotlin.sokolova.finances.biz.errors.instantValidationError
import ru.otus.otuskotlin.sokolova.finances.common.NONE

fun FinsContext.fromTransport(request: IRequest) = when (request) {
    is AccountCreateRequest -> fromTransport(request)
    is AccountReadRequest -> fromTransport(request)
    is AccountUpdateRequest -> fromTransport(request)
    is AccountDeleteRequest -> fromTransport(request)
    is AccountSearchRequest -> fromTransport(request)
    is AccountHistoryRequest -> fromTransport(request)
    is OperationCreateRequest -> fromTransport(request)
    is OperationReadRequest -> fromTransport(request)
    is OperationUpdateRequest -> fromTransport(request)
    is OperationDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toUserId() = this?.let { FinsUserId(it) } ?: FinsUserId.NONE
private fun String?.toAccountId() = this?.let { FinsAccountId(it) } ?: FinsAccountId.NONE
private fun String?.toAccountLock() = this?.let { FinsAccountLock(it) } ?: FinsAccountLock.NONE
private fun String?.toOperationId() = this?.let { FinsOperationId(it) } ?: FinsOperationId.NONE
private fun String?.toOperationLock() = this?.let { FinsOperationLock(it) } ?: FinsOperationLock.NONE
private fun AccountId?.toAccountWithId() = FinsAccount(accountId = this?.accountId.toAccountId())
private fun AccountDelId?.toAccountWithId() = FinsAccount (accountId = this?.accountId.toAccountId(), accountLock = this?.accountLock.toAccountLock())
private fun OperationId?.toOperationWithId() = FinsOperation(operationId = this?.operationId.toOperationId())
private fun OperationDelId?.toOperationWithId() = FinsOperation (operationId = this?.operationId.toOperationId(), operationLock = this?.operationLock.toOperationLock())
private fun IRequest?.requestId() = this?.requestId?.let { FinsRequestId(it) } ?: FinsRequestId.NONE

private fun Debug?.transportToWorkMode(): FinsWorkMode = when (this?.mode) {
    RequestDebugMode.PROD -> FinsWorkMode.PROD
    RequestDebugMode.TEST -> FinsWorkMode.TEST
    RequestDebugMode.STUB -> FinsWorkMode.STUB
    null -> FinsWorkMode.PROD
}

private fun Debug?.transportToStubCase(): FinsStubs = when (this?.stub) {
    RequestDebugStubs.SUCCESS -> FinsStubs.SUCCESS
    RequestDebugStubs.BAD_FORMAT_USER_ID -> FinsStubs.BAD_FORMAT_USER_ID
    RequestDebugStubs.EMPTY_USER_ID -> FinsStubs.EMPTY_USER_ID
    RequestDebugStubs.NOT_FOUND_USER_ID -> FinsStubs.NOT_FOUND_USER_ID
    RequestDebugStubs.EMPTY_NAME -> FinsStubs.EMPTY_NAME
    RequestDebugStubs.BAD_FORMAT_AMOUNT -> FinsStubs.BAD_FORMAT_AMOUNT
    RequestDebugStubs.EMPTY_AMOUNT -> FinsStubs.EMPTY_AMOUNT
    RequestDebugStubs.BAD_FORMAT_ACCOUNT_ID -> FinsStubs.BAD_FORMAT_ACCOUNT_ID
    RequestDebugStubs.EMPTY_ACCOUNT_ID -> FinsStubs.EMPTY_ACCOUNT_ID
    RequestDebugStubs.NOT_FOUND_ACCOUNT_ID -> FinsStubs.NOT_FOUND_ACCOUNT_ID
    RequestDebugStubs.EMPTY_SEARCH_FILTER -> FinsStubs.EMPTY_SEARCH_FILTER
    RequestDebugStubs.BAD_FORMAT_FROM_DATE_TIME -> FinsStubs.BAD_FORMAT_FROM_DATE_TIME
    RequestDebugStubs.EMPTY_FROM_DATE_TIME -> FinsStubs.EMPTY_FROM_DATE_TIME
    RequestDebugStubs.BAD_FORMAT_TO_DATE_TIME -> FinsStubs.BAD_FORMAT_TO_DATE_TIME
    RequestDebugStubs.EMPTY_TO_DATE_TIME -> FinsStubs.EMPTY_TO_DATE_TIME
    RequestDebugStubs.BAD_FORMAT_FROM_ACCOUNT_ID -> FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID
    RequestDebugStubs.EMPTY_FROM_ACCOUNT_ID -> FinsStubs.EMPTY_FROM_ACCOUNT_ID
    RequestDebugStubs.NOT_FOUND_FROM_ACCOUNT_ID -> FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID
    RequestDebugStubs.BAD_FORMAT_TO_ACCOUNT_ID -> FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID
    RequestDebugStubs.EMPTY_TO_ACCOUNT_ID -> FinsStubs.EMPTY_TO_ACCOUNT_ID
    RequestDebugStubs.NOT_FOUND_TO_ACCOUNT_ID -> FinsStubs.NOT_FOUND_TO_ACCOUNT_ID
    RequestDebugStubs.BAD_FORMAT_OPERATION_DATE_TIME -> FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME
    RequestDebugStubs.EMPTY_OPERATION_DATE_TIME -> FinsStubs.EMPTY_OPERATION_DATE_TIME
    RequestDebugStubs.BAD_FORMAT_OPERATION_ID -> FinsStubs.BAD_FORMAT_OPERATION_ID
    RequestDebugStubs.EMPTY_OPERATION_ID -> FinsStubs.EMPTY_OPERATION_ID
    RequestDebugStubs.NOT_FOUND_OPERATION_ID -> FinsStubs.NOT_FOUND_OPERATION_ID
    RequestDebugStubs.ERROR_ACCOUNT_CONCURENT_ON_CHANGE -> FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_CHANGE
    RequestDebugStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE -> FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE
    RequestDebugStubs.ERROR_OPERATION_CONCURENT_ON_CHANGE -> FinsStubs.ERROR_OPERATION_CONCURENT_ON_CHANGE
    RequestDebugStubs.ERROR_OPERATION_CONCURENT_ON_DELETE -> FinsStubs.ERROR_OPERATION_CONCURENT_ON_DELETE
    RequestDebugStubs.DB_ERROR -> FinsStubs.DB_ERROR
    RequestDebugStubs.CANNOT_DELETE -> FinsStubs.CANNOT_DELETE

    null -> FinsStubs.NONE
}

fun FinsContext.fromTransport(request: AccountCreateRequest) {
    command = FinsCommand.ACCOUNTCREATE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountRequest = request.account?.toInternal(this.errors) ?: FinsAccount()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountReadRequest) {
    command = FinsCommand.ACCOUNTREAD
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountRequest = request.account.toAccountWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountUpdateRequest) {
    command = FinsCommand.ACCOUNTUPDATE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountRequest = request.account?.toInternal(this.errors) ?: FinsAccount()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountDeleteRequest) {
    command = FinsCommand.ACCOUNTDELETE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountRequest = request.account.toAccountWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}


fun FinsContext.fromTransport(request: AccountSearchRequest) {
    command = FinsCommand.ACCOUNTSEARCH
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountFilterRequest = request.searchFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: AccountHistoryRequest) {
    command = FinsCommand.ACCOUNTHISTORY
    requestId = request.requestId()
    userId = request.userId.toUserId()
    accountRequest = request.account.toAccountWithId()
    accountHistoryRequest = request.historyFilter?.toInternal(this.errors) ?: FinsHistFilter()

    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationCreateRequest) {
    command = FinsCommand.OPERATIONCREATE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    operationRequest = request.operation?.toInternal(this.errors) ?: FinsOperation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationReadRequest) {
    command = FinsCommand.OPERATIONREAD
    requestId = request.requestId()
    userId = request.userId.toUserId()
    operationRequest = request.operation.toOperationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationUpdateRequest) {
    command = FinsCommand.OPERATIONUPDATE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    operationRequest = request.operation?.toInternal(this.errors) ?: FinsOperation()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun FinsContext.fromTransport(request: OperationDeleteRequest) {
    command = FinsCommand.OPERATIONDELETE
    requestId = request.requestId()
    userId = request.userId.toUserId()
    operationRequest = request.operation.toOperationWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun SearchFilterObj?.toInternal(): FinsSrchFilter = FinsSrchFilter(
    searchFilter = this?.searchFilter ?: ""
)

private fun HistoryFilterObj?.toInternal(errors: MutableList<FinsError>): FinsHistFilter = FinsHistFilter(
    fromDateTime = this?.fromDateTime.toInstantWithValidation("fromDateTime", errors),
    toDateTime = this?.toDateTime.toInstantWithValidation("toDateTime", errors)
)

private fun Account.toInternal(errors: MutableList<FinsError>) = FinsAccount(
    description = this.description ?: "",
    name = this.name ?: "",
    amount = this.amount.toDoubleWithValidation("amount", errors),
    accountId = this.accountId.toAccountId(),
    accountLock = this.accountLock.toAccountLock(),
)

private fun AccountData.toInternal(errors: MutableList<FinsError>) = FinsAccount(
    name = this.name ?: "",
    description = this.description ?: "",
    amount = this.amount.toDoubleWithValidation("amount", errors),
)

private fun Operation.toInternal(errors: MutableList<FinsError>) = FinsOperation(
    description = this.description ?: "",
    amount = this.amount.toDoubleWithValidation("amount", errors),
    fromAccountId = this.fromAccountId.toAccountId(),
    toAccountId = this.toAccountId.toAccountId(),
    operationDateTime = this.operationDateTime.toInstantWithValidation("operationDateTime", errors),
    operationId = this.operationId.toOperationId(),
    operationLock = this.operationLock.toOperationLock(),
)

private fun OperationData.toInternal(errors: MutableList<FinsError>) = FinsOperation(
    description = this.description ?: "",
    amount = this.amount.toDoubleWithValidation("amount", errors),
    fromAccountId = this.fromAccountId.toAccountId(),
    toAccountId = this.toAccountId.toAccountId(),
    operationDateTime = this.operationDateTime.toInstantWithValidation("operationDateTime", errors),
)

private fun String?.toDoubleWithValidation( inModelField: String, errors: MutableList<FinsError>): Double {

    var double: Double = Double.NaN
    try {
        double = this?.toDouble() ?: Double.NaN
    } catch (e: Exception) {
        errors.add(
            this?.replace("<", "&lt;")?.replace(">", "&gt;").doubleValidationError(inModelField)
        )
    }
    return double
}

private fun String?.toInstantWithValidation(inModelField: String, errors: MutableList<FinsError>): Instant {
    var instant: Instant = Instant.NONE

    try {
        instant = this?.let { Instant.parse(it) } ?: Instant.NONE
    } catch (e: Exception) {
        errors.add(
            this?.replace("<", "&lt;")?.replace(">", "&gt;").instantValidationError(inModelField)
        )
    }
    return instant
}