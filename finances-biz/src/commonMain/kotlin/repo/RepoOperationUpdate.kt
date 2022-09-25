package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationRequest


fun ICorChainDsl<FinsContext>.repoOperationUpdate(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        val request = DbOperationRequest(
            userId,
            operationRepoPrepare.deepCopy().apply {
                description = operationValidated.description
                amount = operationValidated.amount
                fromAccountId = operationValidated.fromAccountId
                toAccountId = operationValidated.toAccountId
                operationDateTime = operationValidated.operationDateTime
            }
        )
        val result = repo.operationUpdate(request)
        val resultOperation = result.result
        if (result.isSuccess && resultOperation != null) {
            operationRepoDone = resultOperation
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
            operationRepoDone
        }
    }
}
