package ru.otus.otuskotlin.sokolova.finances.biz.general

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsWorkMode


fun ICorChainDsl<FinsContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != FinsWorkMode.STUB }
    handle {
        accountResponse = accountRepoDone
        accountsResponse = accountsRepoDone
        operationResponse = operationRepoDone
        operationsResponse = operationsRepoDone
        state = when (val st = state) {
            FinsState.RUNNING -> FinsState.FINISHING
            else -> st
        }
    }
}
