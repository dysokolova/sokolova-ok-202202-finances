package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation

data class DbOperationResponse (
    override val result: FinsOperation?,
    override val isSuccess: Boolean,
    override val errors: List<FinsError> = emptyList(),
): IDbResponse<FinsOperation>