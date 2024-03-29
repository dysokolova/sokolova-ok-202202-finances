package ru.otus.otuskotlin.sokolova.finances.stubs

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.sokolova.finances.common.models.*


object AccountStub {
    private fun stub1() = FinsAccount(
        name = "Тинёк-осн",
        description = "основной счет в Тинькофф",
        amount = 120.0,
        accountId = FinsAccountId("123")
    )

    private fun stub2() = FinsAccount(
        name = "Тинёк-нз",
        description = "накопительный счет в Тинькофф",
        amount = 1000.0,
        accountId = FinsAccountId("2")
    )

    fun getModel(model: (FinsAccount.() -> Unit)? = null) = model?.let {
        stub1().apply(it)
    } ?: stub1()

    fun getModels() = listOf(
        stub1(),
        stub2()
    )

    fun FinsAccount.update(account: FinsAccount) = apply {
        name = account.name
        description = account.description
        amount = account.amount
        accountId = account.accountId
    }
}



object OperationStub {
    private fun stub1() = FinsOperation(
        description = "перевод",
        amount = 10.0,
        fromAccountId = FinsAccountId("1"),
        toAccountId = FinsAccountId("2"),
        operationDateTime = Clock.System.now(),
        operationId = FinsOperationId("111")
    )

    private fun stub2() = FinsOperation(
        description = "перевод",
        amount = 20.0,
        fromAccountId = FinsAccountId("1"),
        toAccountId = FinsAccountId("2"),
        operationDateTime = Clock.System.now(),
        operationId = FinsOperationId("222")
    )

    fun getModel(model: (FinsOperation.() -> Unit)? = null) = model?.let {
        stub1().apply(it)
    } ?: stub1()

    fun getModels() = listOf(
        stub1(),
        stub2()
    )

    fun FinsOperation.update(operation: FinsOperation) = apply {
        description = operation.description
        amount = operation.amount
        fromAccountId = operation.fromAccountId
        toAccountId = operation.toAccountId
        operationDateTime = operation.operationDateTime
        operationId = operation.operationId
    }
}





