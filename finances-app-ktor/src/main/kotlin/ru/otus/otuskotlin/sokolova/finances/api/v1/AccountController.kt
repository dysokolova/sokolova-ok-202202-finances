package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.AccountService
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.*

suspend fun ApplicationCall.accountCreate(accountService: AccountService) {
    val createAccountRequest = receive<AccountCreateRequest>()
    respond(
        FinsContext().apply { fromTransport(createAccountRequest)}.let {
            accountService.accountCreate(it)
        }.toTransportAccountCreate()
    )
}

suspend fun ApplicationCall.accountRead(accountService: AccountService) {
    val accountReadRequest = receive<AccountReadRequest>()
    respond(
        FinsContext().apply { fromTransport(accountReadRequest)}.let {
            accountService.accountRead(it, ::buildError)
        }.toTransportAccountRead()
    )
}

suspend fun ApplicationCall.accountUpdate(accountService: AccountService) {
    val accountUpdateRequest = receive<AccountUpdateRequest>()
    respond(
        FinsContext().apply { fromTransport(accountUpdateRequest) }.let {
            accountService.accountUpdate(it, ::buildError)
        }.toTransportAccountUpdate()
    )
}

suspend fun ApplicationCall.accountDelete(accountService: AccountService) {
    val accountDeleteRequest = receive<AccountDeleteRequest>()
    respond(
        FinsContext().apply { fromTransport(accountDeleteRequest) }.let {
            accountService.accountDelete(it, ::buildError)
        }.toTransportAccountDelete()
    )
}

suspend fun ApplicationCall.accountSearch(accountService: AccountService) {
    val accountSearchRequest = receive<AccountSearchRequest>()
    respond(
        FinsContext().apply { fromTransport(accountSearchRequest) }.let {
            accountService.accountSearch(it, ::buildError)
        }.toTransportAccountSearch()
    )
}

suspend fun ApplicationCall.accountHistory(accountService: AccountService) {
    val accountHistoryRequest = receive<AccountHistoryRequest>()
    respond(
        FinsContext().apply { fromTransport(accountHistoryRequest) }.let {
            accountService.accountHistory(it, ::buildError)
        }.toTransportAccountHistory()
    )
}
