package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs

fun ICorChainDsl<FinsContext>.stubOperationDeleteSuccess(title: String) = worker {
    this.title = title
    on { stubCase == FinsStubs.SUCCESS && state == FinsState.RUNNING }
    handle {
        state = FinsState.FINISHING
    }
}
