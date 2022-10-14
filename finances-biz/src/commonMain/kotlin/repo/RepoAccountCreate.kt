package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest


fun ICorChainDsl<FinsContext>.repoAccountCreate(title: String) = worker {
    this.title = title
    description = "Добавление счёта в БД"
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountRequest(
            userId,
            accountRepoPrepare
        )
        val result = repo.accountCreate(request)
        val resultAccount = result.result
        if (result.isSuccess && resultAccount != null) {
            accountRepoDone = resultAccount
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
