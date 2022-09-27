package ru.otus.otuskotlin.sokolova.finances.biz.errors

import ru.otus.otuskotlin.sokolova.finances.common.helpers.fieldName
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsError
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsErrorLevels

fun errorValidation(
    inModelField: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: FinsErrorLevels = FinsErrorLevels.ERROR,
) : FinsError {
    val field = fieldName(inModelField)
return FinsError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field \"$field\": $description",
    level = level,
)
}


fun String?.doubleValidationError(inModelField: String) = errorValidation(
    inModelField,
    "badFormat",
    "\"" + this + "\". The value must be of type double, for example  \"123.456\""
)

fun String?.instantValidationError(inModelField: String) = errorValidation(
    inModelField,
    "badFormat",
    "\"" + this + "\". Use ISO-8601 format, for example \"2010-06-01T22:19:44.475Z\""
)

fun String?.idValidationError(inModelField: String) = errorValidation(
    inModelField,
    "badFormat",
    "\"" + this + "\". Wrong value. See API specification."
)

fun emptyValidationError(inModelField: String) = errorValidation(
    inModelField,
    "empty",
    "field must not be empty"
)
