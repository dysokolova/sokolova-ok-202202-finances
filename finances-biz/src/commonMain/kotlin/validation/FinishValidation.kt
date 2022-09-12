package ru.otus.otuskotlin.sokolova.finances.biz.validation

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState

fun ICorChainDsl<FinsContext>.finishAccountValidation(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        accountValidated = accountValidating
    }
}

fun ICorChainDsl<FinsContext>.finishAccountFilterValidation(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        accountFilterValidated = accountFilterValidating
    }
}

fun ICorChainDsl<FinsContext>.finishAccountHistoryValidation(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        accountValidated = accountValidating
        accountHistoryValidated = accountHistoryValidating
    }
}

fun ICorChainDsl<FinsContext>.finishOperationValidation(title: String) = worker {
    this.title = title
    on { state == FinsState.RUNNING }
    handle {
        operationValidated = operationValidating
    }
}