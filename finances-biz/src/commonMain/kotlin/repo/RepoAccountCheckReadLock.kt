package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.errorConcurrency
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState


fun ICorChainDsl<FinsContext>.repoAccountCheckReadLock(title: String) = worker {
    this.title = title
    description = "Проверяем, что блокировка из запроса совпадает с блокировкой в БД"
    on { state == FinsState.RUNNING && accountValidated.accountLock != accountRepoRead.accountLock }
    handle {
        fail(errorConcurrency(violationCode = "changed", "Object has been inconsistently changed"))
        accountRepoDone = accountRepoRead
    }
}
