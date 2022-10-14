package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountLock
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbAccountIdRequest(
    var userId: FinsUserId = FinsUserId.NONE,
    val accountId: FinsAccountId,
    val accountLock: FinsAccountLock = FinsAccountLock.NONE,
) {
    constructor(userId: FinsUserId, account: FinsAccount) : this(userId, account.accountId, account.accountLock)
}
