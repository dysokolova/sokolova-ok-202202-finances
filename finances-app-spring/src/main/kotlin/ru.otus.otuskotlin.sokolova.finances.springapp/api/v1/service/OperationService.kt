package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.service

import org.springframework.stereotype.Service
import ru.otus.otuskotlin.sokolova.finances.springapp.api.v1.*
import ru.otus.otuskotlin.sokolova.finances.common.*
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.*
import ru.otus.otuskotlin.sokolova.finances.stubs.*
import ru.otus.otuskotlin.sokolova.finances.springapp.common.*


@Service
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

    fun operationRead(finsContext: FinsContext): FinsContext {
        val requestedId = finsContext.operationRequest.operationId

        return when (finsContext.stubCase) {
            FinsStubs.SUCCESS -> finsContext.successResponse {
                operationResponse = OperationStub.getModel().apply { this.operationId = requestedId }
            }
            else -> finsContext.errorResponse {
                it.copy(field = "operation.operationId", message = notFoundError(requestedId.asString()))
            }
        }
    }

    fun operationUpdate(context: FinsContext) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            operationResponse =
                OperationStub.getModel {
                    if (operationRequest.description != "") description = operationRequest.description
                    if (!operationRequest.amount.isNaN()) amount = operationRequest.amount
                    if (operationRequest.operationId != FinsOperationId.NONE) operationId = operationRequest.operationId
                }
        }
        else -> context.errorResponse {
            it.copy(field = "operation.operationId", message = notFoundError(context.operationRequest.operationId.asString()))
        }
    }


    fun operationDelete(context: FinsContext) = when (context.stubCase) {
        FinsStubs.SUCCESS -> context.successResponse {
            operationResponse = OperationStub.getModel { operationId = context.operationRequest.operationId }
        }
        else -> context.errorResponse {
            it.copy(
                field = "operation.operationId",
                message = notFoundError(context.operationRequest.operationId.asString())
            )
        }
    }
}