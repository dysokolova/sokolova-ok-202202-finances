package ru.otus.otuskotlin.sokolova.finances.biz.stubs

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub

fun ICorChainDsl<FinsContext>.stubAccountSearchSuccess(title: String) = worker {
    this.title = title
    on { stubCase == FinsStubs.SUCCESS && state == FinsState.RUNNING }
    handle {
        state = FinsState.FINISHING
        val stubSrchFilter = FinsObjectsStub.prepareSrchFilter {
            searchFilter.takeIf { it != "" }?.also { this.searchFilter = it }
        }
         accountsResponse.addAll(FinsObjectsStub.prepareSearchList(stubSrchFilter.searchFilter))
    }
}
