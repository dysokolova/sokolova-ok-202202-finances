package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSrchFilter
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountFilterRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest


fun ICorChainDsl<FinsContext>.repoAccountSearch(title: String) = worker {
    this.title = title
    description = "Поиск счетов в БД по фильтру"
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountFilterRequest(
            userId,
            accountFilterValidated
        )
        val result = repo.accountSearch(request)
        val resultAccounts = result.result
        if (result.isSuccess && resultAccounts != null) {
            accountsRepoDone = resultAccounts.toMutableList()
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
