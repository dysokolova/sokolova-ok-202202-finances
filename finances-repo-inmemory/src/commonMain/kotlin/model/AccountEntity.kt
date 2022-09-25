package ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.model

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountLock
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class AccountEntity(

    var userId: String = "",
    var name: String? = null,
    var description: String? = null,
    var amount: String? = null,
    var accountId: String? = null,
    var accountLock: String? = null,
) {
    fun toInternal() = FinsAccount (
        name = name?: "",
        description = description?: "",
        amount = amount?.toDouble() ?: Double.NaN,
       // amount = amount.toDoubleWithValidation("amount", errors),
        accountId = accountId?.let { FinsAccountId(it) }?: FinsAccountId.NONE,
        accountLock = accountLock?.let { FinsAccountLock(it) } ?: FinsAccountLock.NONE,

            )

    constructor(userId: FinsUserId, model: FinsAccount) : this(
        userId = userId.asString(),
        name = model.name.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        amount = model.amount.toString().takeIf { it.isNotBlank() },
        accountId = model.accountId.asString().takeIf { it.isNotBlank() },
        accountLock = model.accountLock.asString().takeIf { it.isNotBlank() },
    )
}