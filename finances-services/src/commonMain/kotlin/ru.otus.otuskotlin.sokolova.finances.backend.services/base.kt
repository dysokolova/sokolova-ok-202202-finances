package ru.otus.otuskotlin.sokolova.finances.backend.services

import ru.otus.otuskotlin.sokolova.finances.common.*
import ru.otus.otuskotlin.sokolova.finances.common.models.*

fun FinsContext.errorResponse(buildError: () -> FinsError, error: (FinsError) -> FinsError) =
    apply {
        state = FinsState.FAILING
        errors.add(error(buildError()))
    }


fun FinsContext.successResponse(context: FinsContext.() -> Unit) = apply(context)
    .apply {
        state = FinsState.RUNNING
    }


val notFoundError: (String) -> String  = {
    "Not found by id $it"
}