package ru.otus.otuskotlin.sokolova.finances.common.models

data class FinsAccount(
    var userId: FinsUserId = FinsUserId.NONE,
    var name: String = "",
    var description: String = "",
    var amount: Double = Double.NaN,
    var accountId: FinsAccountId = FinsAccountId.NONE,
)
