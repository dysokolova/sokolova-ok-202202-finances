package ru.otus.otuskotlin.sokolova.finances.common.stubs.errors

import ru.otus.otuskotlin.sokolova.finances.biz.errors.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.notFound
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
fun FinsContext.getText(finsStub: FinsStubs): String {
    var text: String = when (finsStub) {
        FinsStubs.BAD_FORMAT_USER_ID -> userId.asString()
        FinsStubs.NOT_FOUND_USER_ID -> userId.asString()
        FinsStubs.BAD_FORMAT_AMOUNT -> (if (accountRequest.amount.isNaN()) operationRequest.amount else accountRequest.amount).toString()
        FinsStubs.BAD_FORMAT_ACCOUNT_ID -> accountRequest.accountId.asString()
        FinsStubs.NOT_FOUND_ACCOUNT_ID -> accountRequest.accountId.asString()
        FinsStubs.BAD_FORMAT_FROM_DATE_TIME -> accountHistoryRequest.fromDateTime.toString()
        FinsStubs.BAD_FORMAT_TO_DATE_TIME -> accountHistoryRequest.toDateTime.toString()
        FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID -> operationRequest.fromAccountId.asString()
        FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID -> operationRequest.fromAccountId.asString()
        FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID -> operationRequest.toAccountId.asString()
        FinsStubs.NOT_FOUND_TO_ACCOUNT_ID -> operationRequest.toAccountId.asString()
        FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME -> operationRequest.operationDateTime.toString()
        FinsStubs.BAD_FORMAT_OPERATION_ID -> operationRequest.operationId.asString()
        FinsStubs.NOT_FOUND_OPERATION_ID -> operationRequest.operationId.asString()
        else -> ""
    }
    return text
}

fun FinsStubs.getError(value: String = ""): FinsError? {
    var text = value.replace("<", "&lt;").replace(">", "&gt;")
    return when (this) {
        FinsStubs.SUCCESS -> null
        FinsStubs.BAD_FORMAT_USER_ID -> text.idValidationError("userId")
        FinsStubs.EMPTY_USER_ID -> emptyValidationError("userId")
        FinsStubs.NOT_FOUND_USER_ID -> text.notFound("userId")
        FinsStubs.EMPTY_NAME -> emptyValidationError("name")
        FinsStubs.BAD_FORMAT_AMOUNT -> text.doubleValidationError("amount")
        FinsStubs.EMPTY_AMOUNT -> emptyValidationError("amount")
        FinsStubs.BAD_FORMAT_ACCOUNT_ID -> text.idValidationError("accountId")
        FinsStubs.EMPTY_ACCOUNT_ID -> emptyValidationError("accountId")
        FinsStubs.NOT_FOUND_ACCOUNT_ID -> text.notFound("accountId")
        FinsStubs.EMPTY_SEARCH_FILTER -> emptyValidationError("searchFilter")
        FinsStubs.BAD_FORMAT_FROM_DATE_TIME -> text.instantValidationError("fromDateTime")
        FinsStubs.EMPTY_FROM_DATE_TIME -> emptyValidationError("fromDateTime")
        FinsStubs.BAD_FORMAT_TO_DATE_TIME -> text.instantValidationError("toDateTime")
        FinsStubs.EMPTY_TO_DATE_TIME -> emptyValidationError("toDateTime")
        FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID -> text.idValidationError("fromAccountId")
        FinsStubs.EMPTY_FROM_ACCOUNT_ID -> emptyValidationError("fromAccountId")
        FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID -> text.notFound("fromAccountId")
        FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID -> text.idValidationError("toAccountId")
        FinsStubs.EMPTY_TO_ACCOUNT_ID -> emptyValidationError("toAccountId")
        FinsStubs.NOT_FOUND_TO_ACCOUNT_ID -> text.notFound("toAccountId")
        FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME -> text.instantValidationError("operationDateTime")
        FinsStubs.EMPTY_OPERATION_DATE_TIME -> emptyValidationError("operationDateTime")
        FinsStubs.BAD_FORMAT_OPERATION_ID -> text.idValidationError("operationId")
        FinsStubs.EMPTY_OPERATION_ID -> emptyValidationError("operationId")
        FinsStubs.NOT_FOUND_OPERATION_ID -> text.notFound("operationId")
        FinsStubs.DB_ERROR -> FinsError(
            group = "internal",
            code = "internal-db",
            message = "Internal error"
        )
        FinsStubs.CANNOT_DELETE -> FinsError(
            group = "logic",
            code = "cannotDelete",
            message = "CannotDelete error"
        )
        FinsStubs.NONE -> errorValidation(
            "stub",
            "badFormat",
            "\"" + text + "\". Wrong value. See API specification. "
        )
    }
}


fun FinsStubs.getStubName(): String {
    return when (this) {
        FinsStubs.SUCCESS -> "success"
        FinsStubs.BAD_FORMAT_USER_ID -> "badFormatUserId"
        FinsStubs.EMPTY_USER_ID -> "emptyUserId"
        FinsStubs.NOT_FOUND_USER_ID -> "notFoundUserId"
        FinsStubs.EMPTY_NAME -> "emptyName"
        FinsStubs.BAD_FORMAT_AMOUNT -> "badFormatAmount"
        FinsStubs.EMPTY_AMOUNT -> "emptyAmount"
        FinsStubs.BAD_FORMAT_ACCOUNT_ID -> "badFormatAccountId"
        FinsStubs.EMPTY_ACCOUNT_ID -> "emptyAccountId"
        FinsStubs.NOT_FOUND_ACCOUNT_ID -> "notFoundAccountId"
        FinsStubs.EMPTY_SEARCH_FILTER -> "emptySearchFilter"
        FinsStubs.BAD_FORMAT_FROM_DATE_TIME -> "badFormatFromDateTime"
        FinsStubs.EMPTY_FROM_DATE_TIME -> "emptyFromDateTime"
        FinsStubs.BAD_FORMAT_TO_DATE_TIME -> "badFormatToDateTime"
        FinsStubs.EMPTY_TO_DATE_TIME -> "emptyToDateTime"
        FinsStubs.BAD_FORMAT_FROM_ACCOUNT_ID -> "badFormatFromAccountId"
        FinsStubs.EMPTY_FROM_ACCOUNT_ID -> "emptyFromAccountId"
        FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID -> "notFoundFromAccountId"
        FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID -> "badFormatToAccountId"
        FinsStubs.EMPTY_TO_ACCOUNT_ID -> "emptyToAccountId"
        FinsStubs.NOT_FOUND_TO_ACCOUNT_ID -> "notFoundToAccountId"
        FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME -> "badFormatOperationDateTime"
        FinsStubs.EMPTY_OPERATION_DATE_TIME -> "emptyOperationDateTime"
        FinsStubs.BAD_FORMAT_OPERATION_ID -> "badFormatOperationId"
        FinsStubs.EMPTY_OPERATION_ID -> "emptyOperationId"
        FinsStubs.NOT_FOUND_OPERATION_ID -> "notFoundOperationId"
        FinsStubs.DB_ERROR -> "dbError"
        FinsStubs.CANNOT_DELETE -> "cannotDelete"
        else -> ""
    }
}