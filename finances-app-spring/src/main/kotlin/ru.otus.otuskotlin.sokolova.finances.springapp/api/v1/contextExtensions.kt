package ru.otus.otuskotlin.sokolova.finances.springapp.api.v1

import ru.otus.otuskotlin.sokolova.finances.api.v1.models.*
import ru.otus.otuskotlin.sokolova.finances.common.*
import ru.otus.otuskotlin.sokolova.finances.common.models.*

fun FinsContext.errorResponse(error: (FinsError) -> FinsError) =
    apply {
        state = FinsState.FAILING
        errors.add(error(buildError()))
    }


fun FinsContext.successResponse(context: FinsContext.() -> Unit) = apply(context)
    .apply {
        state = FinsState.RUNNING
    }

private fun buildError() = FinsError(
    field = "_", code = ResponseResult.ERROR.value
)


