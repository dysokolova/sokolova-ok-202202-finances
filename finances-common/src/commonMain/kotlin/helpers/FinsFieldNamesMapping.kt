package ru.otus.otuskotlin.sokolova.finances.common.helpers

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand

fun fieldName(
    inModelName: String
)
        = when (inModelName) {
    "userId" -> "UserId"
    "name" -> "Name"
    "description" -> "Description"
    "amount" -> "Amount"
    "accountId" -> "AccountId"
    "searchFilter" -> "SearchFilter"
    "fromDateTime" -> "fromDateTime"
    "toDateTime" -> "toDateTime"
    "fromAccountId" -> "FromAccountId"
    "toAccountId" -> "ToAccountId"
    "operationDateTime" -> "OperationDateTime"
    "operationId" -> "OperationId"
    else -> inModelName
}