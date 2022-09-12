package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.FinsService
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand

suspend fun ApplicationCall.accountCreate(service: FinsService)  =
    controllerHelperV1<AccountCreateRequest, AccountCreateResponse>(FinsCommand.ACCOUNTCREATE) {
        service.createAccount(this)
    }

suspend fun ApplicationCall.accountRead(service: FinsService) =
    controllerHelperV1<AccountReadRequest, AccountReadResponse>(FinsCommand.ACCOUNTREAD) {
        service.readAccount(this)
    }

suspend fun ApplicationCall.accountUpdate(service: FinsService) =
    controllerHelperV1<AccountUpdateRequest, AccountUpdateResponse>(FinsCommand.ACCOUNTUPDATE) {
        service.updateAccount(this)
    }

suspend fun ApplicationCall.accountDelete(service: FinsService) =
    controllerHelperV1<AccountDeleteRequest, AccountDeleteResponse>(FinsCommand.ACCOUNTDELETE) {
        service.deleteAccount(this)
    }

suspend fun ApplicationCall.accountSearch(service: FinsService) =
    controllerHelperV1<AccountSearchRequest, AccountSearchResponse>(FinsCommand.ACCOUNTSEARCH) {
        service.searchAccount(this)
    }

suspend fun ApplicationCall.accountHistory(service: FinsService) =
    controllerHelperV1<AccountHistoryRequest, AccountHistoryResponse>(FinsCommand.ACCOUNTHISTORY) {
        service.historyAccount(this)
    }

suspend fun ApplicationCall.operationCreate(service: FinsService)  =
    controllerHelperV1<OperationCreateRequest, OperationCreateResponse>(FinsCommand.OPERATIONCREATE) {
        service.createOperation(this)
    }

suspend fun ApplicationCall.operationRead(service: FinsService) =
    controllerHelperV1<OperationReadRequest, OperationReadResponse>(FinsCommand.OPERATIONREAD) {
        service.readOperation(this)
    }

suspend fun ApplicationCall.operationUpdate(service: FinsService) =
    controllerHelperV1<OperationUpdateRequest, OperationUpdateResponse>(FinsCommand.OPERATIONUPDATE) {
        service.updateOperation(this)
    }

suspend fun ApplicationCall.operationDelete(service: FinsService) =
    controllerHelperV1<OperationDeleteRequest, OperationDeleteResponse>(FinsCommand.OPERATIONDELETE) {
        service.deleteOperation(this)
    }