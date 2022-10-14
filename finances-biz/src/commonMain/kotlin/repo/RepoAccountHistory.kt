package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSrchFilter
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountFilterRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountHistoryRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest


fun ICorChainDsl<FinsContext>.repoAccountHistory(title: String) = worker {
    this.title = title
    description = "Поиск в БД операций по счёту за период"
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountHistoryRequest(
            userId,
            accountRepoPrepare.accountId,
            accountHistoryValidated
        )
        val result = repo.accountHistory(request)
        val resultOperations = result.result
        if (result.isSuccess && resultOperations != null) {
            operationsRepoDone = resultOperations.toMutableList()
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
