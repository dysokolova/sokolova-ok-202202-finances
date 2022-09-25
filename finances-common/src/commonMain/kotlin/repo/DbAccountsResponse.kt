package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError

data class DbAccountsResponse (
    override val result: List<FinsAccount>?,
    override val isSuccess: Boolean,
    override val errors: List<FinsError> = emptyList(),
): IDbResponse<List<FinsAccount>>
