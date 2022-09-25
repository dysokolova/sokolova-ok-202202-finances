package ru.otus.otuskotlin.sokolova.finances.common.repo

import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError

interface IDbResponse<T> {
    val result: T?
    val isSuccess: Boolean
    val errors: List<FinsError>
}
