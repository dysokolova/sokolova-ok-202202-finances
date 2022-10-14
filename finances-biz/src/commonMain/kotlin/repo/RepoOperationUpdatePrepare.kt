package ru.otus.otuskotlin.sokolova.finances.biz.repo

import com.crowdproj.kotlin.cor.ICorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState


fun ICorChainDsl<FinsContext>.repoOperationUpdatePrepare(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == FinsState.RUNNING }
    handle {
        operationRepoPrepare = operationRepoRead.deepCopy().apply {
            description = operationValidated.description
            amount = operationValidated.amount
            fromAccountId = operationValidated.fromAccountId
            toAccountId = operationValidated.toAccountId
            operationDateTime = operationValidated.operationDateTime
        }
    }
}
