package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationIdRequest


fun ICorChainDsl<FinsContext>.repoOperationRead(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        val request = DbOperationIdRequest(
            userId,
            operationValidated
        )
        val result = repo.operationRead(request)
        val resultOperation = result.result
        if (result.isSuccess && resultOperation != null) {
            operationRepoRead = resultOperation
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
