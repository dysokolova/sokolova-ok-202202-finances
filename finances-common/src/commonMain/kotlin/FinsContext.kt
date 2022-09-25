package ru.otus.otuskotlin.sokolova.finances.common

import kotlinx.datetime.Instant

import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
data class FinsContext (
    var settings: FinsSettings = FinsSettings(),
    var command: FinsCommand = FinsCommand.NONE,
    var state: FinsState = FinsState.NONE,
    val errors: MutableList<FinsError> = mutableListOf(),

    var workMode: FinsWorkMode = FinsWorkMode.PROD,
    var stubCase: FinsStubs = FinsStubs.NONE,

    var clientSession: IClientSession<*> = IClientSession.NONE,

    var repo: IRepository = IRepository.NONE,

    var requestId: FinsRequestId = FinsRequestId.NONE,
    var timeStart: Instant = Instant.NONE,

    var userId: FinsUserId = FinsUserId.NONE,

    var accountRequest: FinsAccount = FinsAccount(),
    var accountFilterRequest: FinsSrchFilter = FinsSrchFilter(),
    var accountHistoryRequest: FinsHistFilter = FinsHistFilter(),

    var accountValidating: FinsAccount = FinsAccount(),
    var accountFilterValidating: FinsSrchFilter = FinsSrchFilter(),
    var accountHistoryValidating: FinsHistFilter = FinsHistFilter(),

    var accountValidated: FinsAccount = FinsAccount(),
    var accountFilterValidated: FinsSrchFilter = FinsSrchFilter(),
    var accountHistoryValidated: FinsHistFilter = FinsHistFilter(),

    var accountRepoRead: FinsAccount = FinsAccount(),
    var accountRepoPrepare: FinsAccount = FinsAccount(),
    var accountRepoDone: FinsAccount = FinsAccount(),
    var accountsRepoDone: MutableList<FinsAccount> = mutableListOf(),

    var accountResponse: FinsAccount = FinsAccount(),
    var accountsResponse: MutableList<FinsAccount> = mutableListOf(),

    var operationRequest: FinsOperation = FinsOperation(),

    var operationValidating: FinsOperation = FinsOperation(),

    var operationValidated: FinsOperation = FinsOperation(),

    var operationRepoRead: FinsOperation = FinsOperation(),
    var operationRepoPrepare: FinsOperation = FinsOperation(),
    var operationRepoDone: FinsOperation = FinsOperation(),
    var operationsRepoDone: MutableList<FinsOperation> = mutableListOf(),

    var operationResponse: FinsOperation = FinsOperation(),
    var operationsResponse: MutableList<FinsOperation> = mutableListOf(),
)