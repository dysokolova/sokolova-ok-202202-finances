package ru.otus.otuskotlin.sokolova.finances.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState

fun ICorChainDsl<FinsContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == FinsState.NONE }
    handle { state = FinsState.RUNNING }
}
