package ru.otus.otuskotlin.sokolova.finances.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.helpers.addError
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getError
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getText

fun String.validateIdFormat(): Boolean {
    val regExp = Regex("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")
    return this.matches(regExp)
}

fun ICorChainDsl<FinsContext>.validationWithStubError(
    title: String,
    finsStub: FinsStubs,
    condition: FinsContext.() -> Boolean
) =
    worker {
        this.title = title
        on {
            this.condition()
        }
        handle {
            finsStub.getError(this.getText(stubCase))?.let { this.fail(it) }
        }
    }

fun ICorChainDsl<FinsContext>.validationFormatUserId(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_USER_ID) {
        userId != FinsUserId.NONE && !userId.asString().validateIdFormat()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyUserId(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_USER_ID) {
        userId.asString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyName(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_NAME) {
        accountValidating.name.isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatAmount(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_AMOUNT) {
        accountValidating.amount.isNaN() && operationValidating.amount.isNaN()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyAmount(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_AMOUNT) {
        accountValidating.amount.toString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_ACCOUNT_ID) {
        accountValidating.accountId != FinsAccountId.NONE && !accountValidating.accountId.asString().validateIdFormat()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_ACCOUNT_ID) {
        accountValidating.accountId.asString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptySearchFilter(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_SEARCH_FILTER) {
        accountFilterValidating.searchFilter.isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatFromDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_FROM_DATE_TIME) {
        accountHistoryValidating.fromDateTime == Instant.NONE
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyFromDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_FROM_DATE_TIME) {
        accountHistoryValidating.fromDateTime.toString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatToDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_TO_DATE_TIME) {
        accountHistoryValidating.toDateTime == Instant.NONE
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyToDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_TO_DATE_TIME) {
        accountHistoryValidating.toDateTime.toString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatFromAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID) {
        operationValidating.fromAccountId != FinsAccountId.NONE && !operationValidating.fromAccountId.asString()
            .validateIdFormat()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyFromAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_FROM_ACCOUNT_ID) {
        operationValidating.fromAccountId.asString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatToAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID) {
        operationValidating.toAccountId != FinsAccountId.NONE && !operationValidating.toAccountId.asString()
            .validateIdFormat()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyToAccountId(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_TO_ACCOUNT_ID) {
        operationValidating.toAccountId.asString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatOperationDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME) {
        operationValidating.operationDateTime == Instant.NONE
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyOperationDateTime(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_OPERATION_DATE_TIME) {
        operationValidating.operationDateTime.toString().isEmpty()
    }

fun ICorChainDsl<FinsContext>.validationFormatOperationId(title: String) =
    this.validationWithStubError(title, FinsStubs.BAD_FORMAT_OPERATION_ID) {
        operationValidating.operationId != FinsOperationId.NONE && !operationValidating.operationId.asString()
            .validateIdFormat()
    }

fun ICorChainDsl<FinsContext>.validationNotEmptyOperationId(title: String) =
    this.validationWithStubError(title, FinsStubs.EMPTY_OPERATION_ID) {
        operationValidating.operationId.asString().isEmpty()
    }


