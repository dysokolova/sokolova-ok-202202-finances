package ru.otus.otuskotlin.sokolova.finances.backend.services

import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.AccountStub
import ru.otus.otuskotlin.sokolova.finances.stubs.OperationStub


class AccountService {


    fun accountCreate(finsContext: FinsContext): FinsContext {
        val response = when (finsContext.workMode) {
            FinsWorkMode.PROD -> TODO()
            FinsWorkMode.TEST -> finsContext.accountRequest
            FinsWorkMode.STUB -> AccountStub.getModel()
        }
        return finsContext.successResponse {
            accountResponse = response
        }
    }

    fun accountRead(finsContext: FinsContext, buildError: () -> FinsError): FinsContext {
        val requestedId = finsContext.accountRequest.accountId

        return when (finsContext.stubCase) {
            FinsStubs.SUCCESS -> finsContext.successResponse {
                accountResponse = AccountStub.getModel().apply { this.accountId = requestedId }
            }
            else -> finsContext.errorResponse(buildError)  {
                it.copy(field = "account.accountId", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun accountUpdate(context: FinsContext, buildError: () -> FinsError)   = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            accountResponse =
                AccountStub.getModel() {
                    if (accountRequest.name != "") name = accountRequest.name
                    if (accountRequest.description != "") description = accountRequest.description
                    if (!accountRequest.amount.isNaN()) amount = accountRequest.amount
                    if (accountRequest.accountId != FinsAccountId.NONE) accountId = accountRequest.accountId
                }
        }
        else -> context.errorResponse(buildError)  {
            it.copy(field = "account.accountId", message = notFoundError(context.accountRequest.accountId.asString()))
        }
    }


    fun accountDelete(context: FinsContext, buildError: () -> FinsError) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            accountResponse = AccountStub.getModel { accountId = context.accountRequest.accountId }
        }
        else -> context.errorResponse(buildError)  {
            it.copy(
                field = "account.accountId",
                message = notFoundError(context.accountRequest.accountId.asString())
            )
        }
    }

    fun accountSearch(context: FinsContext, buildError: () -> FinsError): FinsContext {
        val accountFilterRequest = context.accountFilterRequest

        val searchFilter = accountFilterRequest.searchFilter

        return when(context.stubCase) {
         FinsStubs.SUCCESS -> context.successResponse {
                accountsResponse.addAll(
                    AccountStub.getModels()
                )
            }
            else -> context.errorResponse(buildError)  {
                it.copy(
                    message = "Nothing found by $searchFilter"
                )
            }
        }
    }

    fun accountHistory(context: FinsContext, buildError: () -> FinsError): FinsContext  {
        val accountHistoryRequest = context.accountHistoryRequest

        val fromDateTime = accountHistoryRequest.fromDateTime
        val toDateTime = accountHistoryRequest.toDateTime

        return when(context.stubCase) {
          FinsStubs.SUCCESS -> context.successResponse {
                operationsResponse.addAll(
                    OperationStub.getModels()
                )
            }
            else -> context.errorResponse(buildError)  {
                it.copy(
                    message = "Nothing found from $fromDateTime to $toDateTime"
                )
            }
        }
    }
}