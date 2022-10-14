package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSrchFilter
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId

data class DbAccountFilterRequest (
    var userId: FinsUserId = FinsUserId.NONE,
    val finsSrchFilter: FinsSrchFilter = FinsSrchFilter.NONE,
)