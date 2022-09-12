package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getError
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getStubName
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getText

fun ICorChainDsl<FinsContext>.stubWorker(title: String, finsStub: FinsStubs) = worker {
    this.title = title
    on { stubCase == finsStub && state == FinsState.RUNNING }
    handle {
        finsStub.getError(this.getText(stubCase))?.let { this.fail(it) }
    }
}

fun ICorChainDsl<FinsContext>.stubValidationBadFormatUserId(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_USER_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptyUserId(title: String) = this.stubWorker(title, FinsStubs.EMPTY_USER_ID)
fun ICorChainDsl<FinsContext>.stubValidationEmptyName(title: String) = this.stubWorker(title, FinsStubs.EMPTY_NAME)
fun ICorChainDsl<FinsContext>.stubValidationBadFormatAmount(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_AMOUNT)

fun ICorChainDsl<FinsContext>.stubValidationEmptyAmount(title: String) = this.stubWorker(title, FinsStubs.EMPTY_AMOUNT)
fun ICorChainDsl<FinsContext>.stubValidationBadFormatAccountId(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptyAccountId(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptySearchFilter(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_SEARCH_FILTER)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatFromDateTime(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_FROM_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationEmptyFromDateTime(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_FROM_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatToDateTime(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_TO_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationEmptyToDateTime(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_TO_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatFromAccountId(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptyFromAccountId(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_FROM_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatToAccountId(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptyToAccountId(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_TO_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatOperationDateTime(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationEmptyOperationDateTime(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_OPERATION_DATE_TIME)

fun ICorChainDsl<FinsContext>.stubValidationBadFormatOperationId(title: String) =
    this.stubWorker(title, FinsStubs.BAD_FORMAT_OPERATION_ID)

fun ICorChainDsl<FinsContext>.stubValidationEmptyOperationId(title: String) =
    this.stubWorker(title, FinsStubs.EMPTY_OPERATION_ID)


fun ICorChainDsl<FinsContext>.stubNotFoundUserId(title: String) = this.stubWorker(title, FinsStubs.NOT_FOUND_USER_ID)
fun ICorChainDsl<FinsContext>.stubNotFoundAccountId(title: String) =
    this.stubWorker(title, FinsStubs.NOT_FOUND_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubNotFoundFromAccountId(title: String) =
    this.stubWorker(title, FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubNotFoundToAccountId(title: String) =
    this.stubWorker(title, FinsStubs.NOT_FOUND_TO_ACCOUNT_ID)

fun ICorChainDsl<FinsContext>.stubNotFoundOperationId(title: String) =
    this.stubWorker(title, FinsStubs.NOT_FOUND_OPERATION_ID)


fun ICorChainDsl<FinsContext>.stubDbError(title: String) = this.stubWorker(title, FinsStubs.DB_ERROR)
fun ICorChainDsl<FinsContext>.stubCannotDelete(title: String) = this.stubWorker(title, FinsStubs.CANNOT_DELETE)
fun ICorChainDsl<FinsContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        state = FinsState.FAILING
        this.errors.add(
            FinsError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.getStubName()}"
            )
        )
    }
}
