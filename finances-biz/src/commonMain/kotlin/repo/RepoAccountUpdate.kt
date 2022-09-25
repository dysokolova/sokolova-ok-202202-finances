package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest


fun ICorChainDsl<FinsContext>.repoAccountUpdate(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        val request = DbAccountRequest(
            userId,
            accountRepoPrepare.deepCopy().apply {
                name = accountValidated.name
                description = accountValidated.description
                amount = accountValidated.amount
            }
        )
        val result = repo.accountUpdate(request)
        val resultAccount = result.result
        if (result.isSuccess && resultAccount != null) {
            accountRepoDone = resultAccount
        } else {
            state = FinsState.FAILING
            errors.addAll(result.errors)
            accountRepoDone
        }
    }
}
