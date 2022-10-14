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

fun String?.errorConcurrency(
    inModelField: String,
    violationCode: String,
    level: FinsErrorLevels = FinsErrorLevels.ERROR,
) : FinsError {
    val field = fieldName(inModelField)
    return FinsError(
        code = "concurrent-$violationCode",
        field = field,
        group = "notFound",
        message = "Concurrent object access error: Object has changed during request handling \"$field\" = $this",
        level = level,
    )
}

fun errorAdministration(
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    field: String = "",
    violationCode: String,
    description: String,
    level: FinsErrorLevels = FinsErrorLevels.ERROR,
) = FinsError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)
