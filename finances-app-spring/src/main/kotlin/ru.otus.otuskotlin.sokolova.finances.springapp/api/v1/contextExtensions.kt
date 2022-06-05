package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.*
import ru.otus.otuskotlin.sokolova.finances.common.models.*

fun buildError() = FinsError(
    field = "_", code = ResponseResult.ERROR.value
)


