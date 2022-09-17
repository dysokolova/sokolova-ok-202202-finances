package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub

fun ICorChainDsl<FinsContext>.stubAccountHistorySuccess(title: String) = worker {
    this.title = title
    on { stubCase == FinsStubs.SUCCESS && state == FinsState.RUNNING }
    handle {
        state = FinsState.FINISHING
        val stubAccountRequest = FinsObjectsStub.prepareAccountResult {
            accountRequest.accountId.takeIf { it != FinsAccountId.NONE }?.also { this.accountId = it }
        }
        val stubHistFilter = FinsObjectsStub.prepareHistFilter {
            accountHistoryRequest.fromDateTime.takeIf { it != Instant.NONE }?.also { this.fromDateTime = it }
            accountHistoryRequest.toDateTime.takeIf { it != Instant.NONE }?.also { this.toDateTime = it }
        }
        operationsResponse.addAll(FinsObjectsStub.prepareHistoryList(stubAccountRequest.accountId, stubHistFilter))
    }
}
