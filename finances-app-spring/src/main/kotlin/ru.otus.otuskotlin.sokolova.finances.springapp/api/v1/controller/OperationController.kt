package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.sokolova.finances.backend.services.OperationService
import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.mappers.v1.*
import ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.buildError

@RestController
@RequestMapping("v1/operation")
class OperationController (
    private val operationService: OperationService
) {
    @PostMapping("create")
    fun operationCreate(@RequestBody operationCreateRequest: OperationCreateRequest) =
        FinsContext().apply { fromTransport(operationCreateRequest)}.let {
            operationService.operationCreate(it)
        }.toTransportOperationCreate()

    @PostMapping("read")
    fun operationRead(@RequestBody operationReadRequest: OperationReadRequest) =
        FinsContext().apply { fromTransport(operationReadRequest)}.let {
            operationService.operationRead(it, ::buildError)
        }.toTransportOperationRead()

    @PostMapping("update")
    fun operationUpdate(@RequestBody operationUpdateRequest: OperationUpdateRequest) =
        FinsContext().apply { fromTransport(operationUpdateRequest) }.let {
            operationService.operationUpdate(it, ::buildError)
        }.toTransportOperationUpdate()

    @PostMapping("delete")
    fun operationDelete(@RequestBody operationDeleteRequest: OperationDeleteRequest) =
        FinsContext().apply { fromTransport(operationDeleteRequest) }.let {
            operationService.operationDelete(it, ::buildError)
        }.toTransportOperationDelete()

}