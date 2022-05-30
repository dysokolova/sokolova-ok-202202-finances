package ru.otus.otuskotlin.sokolova.finances.api.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.ResponseResult
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError

fun buildError() = FinsError(
    field = "_", code = ResponseResult.ERROR.value
)


