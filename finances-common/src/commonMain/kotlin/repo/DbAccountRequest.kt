package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbAccountRequest (
    var userId: FinsUserId = FinsUserId.NONE,
    val account: FinsAccount
)