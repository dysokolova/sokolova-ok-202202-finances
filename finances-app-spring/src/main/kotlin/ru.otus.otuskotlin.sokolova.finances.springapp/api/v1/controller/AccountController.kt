package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.service.AccountService
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.*

@RestController
@RequestMapping("v1/account")
class AccountController (
    private val accountService: AccountService
) {
    @PostMapping("create")
    fun accountCreate(@RequestBody accountCreateRequest: AccountCreateRequest) =
        FinsContext().apply { fromTransport(accountCreateRequest)}.let {
            accountService.accountCreate(it)
        }.toTransportAccountCreate()

    @PostMapping("read")
    fun accountRead(@RequestBody accountReadRequest: AccountReadRequest) =
        FinsContext().apply { fromTransport(accountReadRequest)}.let {
            accountService.accountRead(it)
        }.toTransportAccountRead()

    @PostMapping("update")
    fun accountUpdate(@RequestBody accountUpdateRequest: AccountUpdateRequest) =
        FinsContext().apply { fromTransport(accountUpdateRequest) }.let {
            accountService.accountUpdate(it)
        }.toTransportAccountUpdate()

    @PostMapping("delete")
    fun accountDelete(@RequestBody accountDeleteRequest: AccountDeleteRequest) =
        FinsContext().apply { fromTransport(accountDeleteRequest) }.let {
            accountService.accountDelete(it)
        }.toTransportAccountDelete()

    @PostMapping("search")
    fun accountSearch(@RequestBody accountSearchRequest: AccountSearchRequest) =
        FinsContext().apply { fromTransport(accountSearchRequest) }.let {
            accountService.accountSearch(it)
        }.toTransportAccountSearch()

    @PostMapping("history")
    fun accountHistory(@RequestBody accountHistoryRequest: AccountHistoryRequest) =
        FinsContext().apply { fromTransport(accountHistoryRequest) }.let {
            accountService.accountHistory(it)
        }.toTransportAccountHistory()
}