package ru.otus.otuskotlin.sokolova.finances.common

import kotlinx.datetime.Instant

import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
data class FinsContext (
    var command: FinsCommand = FinsCommand.NONE,
    var state: FinsState = FinsState.NONE,
    val errors: MutableList<FinsError> = mutableListOf(),

    var workMode: FinsWorkMode = FinsWorkMode.PROD,
    var stubCase: FinsStubs = FinsStubs.NONE,

    var requestId: FinsRequestId = FinsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var accountRequest: FinsAccount = FinsAccount(),
    var accountFilterRequest: FinsSrchFilter = FinsSrchFilter(),
    var accountHistoryRequest: FinsHistFilter = FinsHistFilter(),
    var accountResponse: FinsAccount = FinsAccount(),
    var accountsResponse: MutableList<FinsAccount> = mutableListOf(),
    var operationRequest: FinsOperation = FinsOperation(),
    var operationResponse: FinsOperation = FinsOperation(),
    var operationsResponse: MutableList<FinsOperation> = mutableListOf(),
)