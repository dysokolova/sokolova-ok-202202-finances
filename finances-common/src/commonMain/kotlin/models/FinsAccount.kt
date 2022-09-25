package ru.otus.otuskotlin.sokolova.finances.common.models

data class FinsAccount(
    var name: String = "",
    var description: String = "",
    var amount: Double = Double.NaN,
    var accountId: FinsAccountId = FinsAccountId.NONE,
    var accountLock: FinsAccountLock = FinsAccountLock.NONE,
){
    fun deepCopy(
    ) = FinsAccount(
        name = this@FinsAccount.name,
        description = this@FinsAccount.description,
        amount = this@FinsAccount.amount,
        accountId = this@FinsAccount.accountId,
        accountLock = this@FinsAccount.accountLock,
    )
}

