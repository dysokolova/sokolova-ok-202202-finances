package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountIdRequest


fun ICorChainDsl<FinsContext>.repoAccountRead(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountIdRequest(
            userId,
            accountValidated
        )
        val result = repo.accountRead(request)
        val resultAccount = result.result
        if (result.isSuccess && resultAccount != null) {
            accountRepoRead = resultAccount
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
        }
    }
}
