package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountIdRequest


fun ICorChainDsl<FinsContext>.repoAccountDelete(title: String) = worker {
    this.title = title
    description = "Поиск в БД операций по счёту за период"
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountIdRequest(
            userId,
            accountRepoPrepare
        )
        val result = repo.accountDelete(request)
        if (!result.isSuccess) {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }

        accountRepoDone = accountRepoRead
    }
}
