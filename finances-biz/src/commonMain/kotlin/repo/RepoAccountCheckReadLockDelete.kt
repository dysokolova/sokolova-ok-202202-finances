package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.helpers.fail
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getError
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getText


fun ICorChainDsl<FinsContext>.repoAccountCheckReadLockDelete(title: String) = worker {
    this.title = title
    description = "Проверяем, что блокировка из запроса совпадает с блокировкой в БД"
    on { state == FinsState.RUNNING && accountValidated.accountLock != accountRepoRead.accountLock }
    handle {
        FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE.getError(this.getText(FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE))?.let { this.fail(it) }
        accountRepoDone = accountRepoRead
    }
}
