package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsHistFilter
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbAccountHistoryRequest (
    var userId: FinsUserId = FinsUserId.NONE,
    val accountId: FinsAccountId,
    val finsHistFilter: FinsHistFilter = FinsHistFilter.NONE,
)