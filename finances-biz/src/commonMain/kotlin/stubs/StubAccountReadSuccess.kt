package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub

fun ICorChainDsl<FinsContext>.stubAccountReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == FinsStubs.SUCCESS && state == FinsState.RUNNING }
    handle {
        state = FinsState.FINISHING
        val stub = FinsObjectsStub.prepareAccountResult {
            accountRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
            accountRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
            accountRequest.amount.takeIf { !it.isNaN() }?.also { this.amount = it }
            accountRequest.accountId.takeIf { it != FinsAccountId.NONE }?.also { this.accountId = it }
        }
        accountResponse = stub
    }
}
