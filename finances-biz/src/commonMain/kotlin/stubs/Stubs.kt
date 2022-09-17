package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsWorkMode

fun ICorChainDsl<FinsContext>.stubs(title: String, block: ICorChainDsl<FinsContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == FinsWorkMode.STUB && state == FinsState.RUNNING }
}
