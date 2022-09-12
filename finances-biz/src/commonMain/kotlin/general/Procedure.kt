package ru.otus.otuskotlin.sokolova.finances.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.chain
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState

fun ICorChainDsl<FinsContext>.procedure(title: String, command: FinsCommand, block: ICorChainDsl<FinsContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == FinsState.RUNNING }
}

