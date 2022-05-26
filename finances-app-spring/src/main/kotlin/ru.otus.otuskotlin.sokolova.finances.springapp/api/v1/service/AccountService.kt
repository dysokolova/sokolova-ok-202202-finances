package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.service

import org.springframework.stereotype.Service
import ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.*
import ru.otus.otuskotlin.sokolova.finances.common.*
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.*
import ru.otus.otuskotlin.sokolova.finances.stubs.*
import ru.otus.otuskotlin.sokolova.finances.springapp.common.*


@Service
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

    fun accountRead(finsContext: FinsContext): FinsContext {
        val requestedId = finsContext.accountRequest.accountId

        return when (finsContext.stubCase) {
            FinsStubs.SUCCESS -> finsContext.successResponse {
                accountResponse = AccountStub.getModel().apply { this.accountId = requestedId }
            }
            else -> finsContext.errorResponse {
                it.copy(field = "account.accountId", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun accountUpdate(context: FinsContext)   = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            accountResponse =
                AccountStub.getModel() {
                    if (accountRequest.name != "") name = accountRequest.name
                    if (accountRequest.description != "") description = accountRequest.description
                    if (!accountRequest.amount.isNaN()) amount = accountRequest.amount
                    if (accountRequest.accountId != FinsAccountId.NONE) accountId = accountRequest.accountId
                }
        }
        else -> context.errorResponse {
            it.copy(field = "account.accountId", message = notFoundError(context.accountRequest.accountId.asString()))
        }
    }


    fun accountDelete(context: FinsContext) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            accountResponse = AccountStub.getModel { accountId = context.accountRequest.accountId }
        }
        else -> context.errorResponse {
            it.copy(
                field = "account.accountId",
                message = notFoundError(context.accountRequest.accountId.asString())
            )
        }
    }

    fun accountSearch(context: FinsContext): FinsContext {
        val accountFilterRequest = context.accountFilterRequest

        val searchFilter = accountFilterRequest.searchFilter

        return when(context.stubCase) {
         FinsStubs.SUCCESS -> context.successResponse {
                accountsResponse.addAll(
                    AccountStub.getModels()
                )
            }
            else -> context.errorResponse {
                it.copy(
                    message = "Nothing found by $searchFilter"
                )
            }
        }
    }

    fun accountHistory(context: FinsContext): FinsContext  {
        val accountHistoryRequest = context.accountHistoryRequest

        val fromDateTime = accountHistoryRequest.fromDateTime
        val toDateTime = accountHistoryRequest.toDateTime

        return when(context.stubCase) {
          FinsStubs.SUCCESS -> context.successResponse {
                operationsResponse.addAll(
                    OperationStub.getModels()
                )
            }
            else -> context.errorResponse {
                it.copy(
                    message = "Nothing found from $fromDateTime to $toDateTime"
                )
            }
        }
    }
}