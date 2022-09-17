package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub

fun ICorChainDsl<FinsContext>.stubOperationReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == FinsStubs.SUCCESS && state == FinsState.RUNNING }
    handle {
        state = FinsState.FINISHING
        val stub = FinsObjectsStub.prepareOperationResult {
            operationRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            operationRequest.amount.takeIf { !it.isNaN() }?.also { this.amount = it }
            operationRequest.fromAccountId.takeIf { it != FinsAccountId.NONE }?.also { this.fromAccountId = it }
            operationRequest.toAccountId.takeIf { it != FinsAccountId.NONE }?.also { this.toAccountId = it }
            operationRequest.operationId.takeIf { it != FinsOperationId.NONE }?.also { this.operationId = it }
            operationRequest.operationDateTime.takeIf { it != Instant.NONE }?.also { this.operationDateTime = it }
        }
        operationResponse = stub
    }
}
