package ru.otus.otuskotlin.sokolova.finances.common.models

data class FinsAccount(
    var userId: FinsUserId = FinsUserId.NONE,
    var name: String = "",
    var description: String = "",
    val accountId: FinsAccountId = FinsAccountId.NONE,
)
