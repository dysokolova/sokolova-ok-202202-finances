package ru.otus.otuskotlin.sokolova.finances.backend.services

import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.OperationStub

class OperationService {


    fun operationCreate(finsContext: FinsContext): FinsContext {
        val response = when (finsContext.workMode) {
            FinsWorkMode.PROD -> TODO()
            FinsWorkMode.TEST -> finsContext.operationRequest
            FinsWorkMode.STUB -> OperationStub.getModel()
        }
        return finsContext.successResponse {
            operationResponse = response
        }
    }

    fun operationRead(finsContext: FinsContext, buildError: () -> FinsError): FinsContext {
        val requestedId = finsContext.operationRequest.operationId

        return when (finsContext.stubCase) {
            FinsStubs.SUCCESS -> finsContext.successResponse {
                operationResponse = OperationStub.getModel().apply { this.operationId = requestedId }
            }
            else -> finsContext.errorResponse(buildError)  {
                it.copy(field = "operation.operationId", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun operationUpdate(context: FinsContext, buildError: () -> FinsError) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            operationResponse =
                OperationStub.getModel {
                    if (operationRequest.description != "") description = operationRequest.description
                    if (!operationRequest.amount.isNaN()) amount = operationRequest.amount
                    if (operationRequest.operationId != FinsOperationId.NONE) operationId = operationRequest.operationId
                }
        }
        else -> context.errorResponse(buildError)  {
            it.copy(field = "operation.operationId", message = notFoundError(context.operationRequest.operationId.asString()))
        }
    }


    fun operationDelete(context: FinsContext, buildError: () -> FinsError) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            operationResponse = OperationStub.getModel { operationId = context.operationRequest.operationId }
        }
        else -> context.errorResponse(buildError)  {
            it.copy(
                field = "operation.operationId",
                message = notFoundError(context.operationRequest.operationId.asString())
            )
        }
    }
}