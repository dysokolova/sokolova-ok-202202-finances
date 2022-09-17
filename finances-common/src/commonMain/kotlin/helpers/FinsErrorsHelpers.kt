package ru.otus.otuskotlin.sokolova.finances.common.helpers

import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsErrorLevels
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsState

fun Throwable.asFinsError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = FinsError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun FinsContext.addError(error: FinsError) = errors.add(error)
fun FinsContext.fail(error: FinsError) {
    addError(error)
    state = FinsState.FAILING
}



fun String?.notFound(
    inModelField: String,
    level: FinsErrorLevels = FinsErrorLevels.ERROR,
) : FinsError {
    val field = fieldName(inModelField)
  return FinsError(
        code = "notFound-$field",
        field = field,
        group = "notFound",
        message = "Nothing found by field \"$field\" = $this",
        level = level,
    )
}