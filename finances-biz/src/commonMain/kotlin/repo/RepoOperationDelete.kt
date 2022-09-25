package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationIdRequest


fun ICorChainDsl<FinsContext>.repoOperationDelete(title: String) = worker {
    this.title = title
    description = "Удаление счёта из БД по ID"
    on { state == FinsState.RUNNING }
    handle {
        val request = DbOperationIdRequest(
            userId,
            operationRepoPrepare
        )
        val result = repo.operationDelete(request)
        if (!result.isSuccess) {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }

        operationRepoDone = operationRepoRead
    }
}
