package ru.otus.otuskotlin.sokolova.finances.api.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.OperationService
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.*

suspend fun ApplicationCall.operationCreate(operationService: OperationService) {
    val createOperationRequest = receive<OperationCreateRequest>()
    respond(
        FinsContext().apply { fromTransport(createOperationRequest)}.let {
            operationService.operationCreate(it)
        }.toTransportOperationCreate()
    )
}

suspend fun ApplicationCall.operationRead(operationService: OperationService) {
    val operationReadRequest = receive<OperationReadRequest>()
    respond(
        FinsContext().apply { fromTransport(operationReadRequest)}.let {
            operationService.operationRead(it, ::buildError)
        }.toTransportOperationRead()
    )
}

suspend fun ApplicationCall.operationUpdate(operationService: OperationService) {
    val operationUpdateRequest = receive<OperationUpdateRequest>()
    respond(
        FinsContext().apply { fromTransport(operationUpdateRequest) }.let {
            operationService.operationUpdate(it, ::buildError)
        }.toTransportOperationUpdate()
    )
}

suspend fun ApplicationCall.operationDelete(operationService: OperationService) {
    val operationDeleteRequest = receive<OperationDeleteRequest>()
    respond(
        FinsContext().apply { fromTransport(operationDeleteRequest) }.let {
            operationService.operationDelete(it, ::buildError)
        }.toTransportOperationDelete()
    )
}