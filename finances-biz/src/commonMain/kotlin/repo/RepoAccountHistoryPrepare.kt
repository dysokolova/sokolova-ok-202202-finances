package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState


fun ICorChainDsl<FinsContext>.repoAccountHistoryPrepare(title: String) = worker {
    this.title = title
    description = "Готовим данные к обращению в БД"
    on { state == FinsState.RUNNING }
    handle {
        accountRepoPrepare = accountValidated.deepCopy()
    }
}

