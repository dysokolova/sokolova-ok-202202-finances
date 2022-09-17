package ru.otus.otuskotlin.sokolova.finances.biz

import com.crowdproj.kotlin.cor.*
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.biz.stubs.stubAccountHistorySuccess
import ru.otus.otuskotlin.sokolova.finances.biz.general.procedure
import ru.otus.otuskotlin.sokolova.finances.biz.stubs.*
import ru.otus.otuskotlin.sokolova.finances.biz.validation.*
import ru.otus.otuskotlin.sokolova.finances.biz.general.initStatus
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperationId

class FinsProcessor() {
    suspend fun exec(ctx: FinsContext) = BuzinessChain.exec(ctx)

    companion object {
        private val BuzinessChain = rootChain<FinsContext> {
            initStatus("Инициализация статуса")

            procedure("Создание счёта", FinsCommand.ACCOUNTCREATE) {
                stubs("Обработка стабов") {
                    stubAccountCreateSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationEmptyName("Имитация ошибки валидации имени счета - пустое значение")

                    stubValidationBadFormatAmount("Имитация ошибки валидации суммы - неверный формат")
                    stubValidationEmptyAmount("Имитация ошибки валидации суммы")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в accountValidating") { accountValidating = accountRequest.deepCopy() }
                    worker("Очистка заголовка") { accountValidating.name = accountValidating.name.trim() }
                    worker("Очистка описания") { accountValidating.description = accountValidating.description.trim() }

                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyName("Проверка на непустой name")
                    validationNotEmptyAmount("Проверка на непустую сумму")
                    validationFormatAmount("Проверка формата суммы")

                    finishAccountValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Получить информацию о счете", FinsCommand.ACCOUNTREAD) {
                stubs("Обработка стабов") {
                    stubAccountReadSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatAccountId("Имитация ошибки валидации счёта - неверный формат")
                    stubValidationEmptyAccountId("Имитация ошибки валидации счёта - пустое значение")
                    stubNotFoundAccountId("Имитация ошибки notFound для счёта ")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в accountValidating") { accountValidating = accountRequest.deepCopy() }
                    worker("Очистка accountId") { accountValidating.accountId = FinsAccountId(accountValidating.accountId.asString().trim()) }

                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyAccountId("Проверка на непустой номер счёта")
                    validationFormatAccountId("Проверка формата номера счёта")

                    finishAccountValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Изменить счёт", FinsCommand.ACCOUNTUPDATE) {
                stubs("Обработка стабов") {
                    stubAccountUpdateSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationEmptyName("Имитация ошибки валидации имени счета - пустое значение")

                    stubValidationBadFormatAmount("Имитация ошибки валидации суммы - неверный формат")
                    stubValidationEmptyAmount("Имитация ошибки валидации суммы")

                    stubValidationBadFormatAccountId("Имитация ошибки валидации счёта - неверный формат")
                    stubValidationEmptyAccountId("Имитация ошибки валидации счёта - пустое значение")
                    stubNotFoundAccountId("Имитация ошибки notFound для счёта ")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в accountValidating") { accountValidating = accountRequest.deepCopy() }
                    worker("Очистка accountId") { accountValidating.accountId = FinsAccountId(accountValidating.accountId.asString().trim()) }
                    worker("Очистка заголовка") { accountValidating.name = accountValidating.name.trim() }
                    worker("Очистка описания") { accountValidating.description = accountValidating.description.trim() }


                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyName("Проверка на непустой name")
                    validationNotEmptyAmount("Проверка на непустую сумму")
                    validationFormatAmount("Проверка формата суммы")
                    validationNotEmptyAccountId("Проверка на непустой номер счёта")
                    validationFormatAccountId("Проверка формата номера счёта")

                    finishAccountValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Удалить счёт", FinsCommand.ACCOUNTDELETE) {
                stubs("Обработка стабов") {
                    stubAccountDeleteSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatAccountId("Имитация ошибки валидации счёта - неверный формат")
                    stubValidationEmptyAccountId("Имитация ошибки валидации счёта - пустое значение")
                    stubNotFoundAccountId("Имитация ошибки notFound для счёта ")

                    stubCannotDelete("Ошибка: невозможно удалить")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в accountValidating") { accountValidating = accountRequest.deepCopy() }
                    worker("Очистка accountId") { accountValidating.accountId = FinsAccountId(accountValidating.accountId.asString().trim()) }

                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyAccountId("Проверка на непустой номер счёта")
                    validationFormatAccountId("Проверка формата номера счёта")

                    finishAccountValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Поиск счетов", FinsCommand.ACCOUNTSEARCH) {
                stubs("Обработка стабов") {
                    stubAccountSearchSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationEmptySearchFilter("Имитация ошибки валидации строки поиска - пустое значение")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в adFilterValidating") { accountFilterValidating = accountFilterRequest.deepCopy() }

                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptySearchFilter("Проверка на непустую строку поиска")

                    finishAccountFilterValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("История операций по счету", FinsCommand.ACCOUNTHISTORY) {
                stubs("Обработка стабов") {
                    stubAccountHistorySuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatAccountId("Имитация ошибки валидации счёта - неверный формат")
                    stubValidationEmptyAccountId("Имитация ошибки валидации счёта - пустое значение")
                    stubNotFoundAccountId("Имитация ошибки notFound для счёта ")

                    stubValidationBadFormatFromDateTime("Имитация ошибки валидации даты \"с\" - неверный формат")
                    stubValidationEmptyFromDateTime("Имитация ошибки валидации даты \"с\" - пустое значение")

                    stubValidationBadFormatToDateTime("Имитация ошибки валидации даты \"по\" - неверный формат")
                    stubValidationEmptyToDateTime("Имитация ошибки валидации даты \"по\" - пустое значение")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в accountValidating") { accountValidating = accountRequest.deepCopy() }
                    worker("Копируем поля в accountValidating") { accountHistoryValidating = accountHistoryRequest.deepCopy() }
                    worker("Очистка id") { accountValidating.accountId = FinsAccountId(accountValidating.accountId.asString().trim()) }


                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyAccountId("Проверка на непустой номер счёта")
                    validationFormatAccountId("Проверка формата номера счёта")
                    validationFormatFromDateTime("Проверка формата даты \"c\"")
                    validationNotEmptyFromDateTime("Проверка на непустую  дату \"c\"")
                    validationFormatToDateTime("Проверка формата даты \"по\"")
                    validationNotEmptyToDateTime("Проверка на непустую  дату \"по\"")

                    finishAccountHistoryValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Создание операции", FinsCommand.OPERATIONCREATE) {
                stubs("Обработка стабов") {
                    stubOperationCreateSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatAmount("Имитация ошибки валидации суммы - неверный формат")
                    stubValidationEmptyAmount("Имитация ошибки валидации суммы")

                    stubValidationBadFormatFromAccountId("Имитация ошибки валидации счёта  \"с\" - неверный формат")
                    stubValidationEmptyFromAccountId("Имитация ошибки валидации счёта \"с\" - пустое значение")
                    stubNotFoundFromAccountId("Имитация ошибки notFound для счёта  \"с\"")

                    stubValidationBadFormatToAccountId("Имитация ошибки валидации счёта  \"по\" - неверный формат")
                    stubValidationEmptyToAccountId("Имитация ошибки валидации счёта \"по\" - пустое значение")
                    stubNotFoundToAccountId("Имитация ошибки notFound для счёта  \"по\"")

                    stubValidationBadFormatOperationDateTime("Имитация ошибки валидации даты операции - неверный формат")
                    stubValidationEmptyOperationDateTime("Имитация ошибки валидации даты операции - пустое значение")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в operationValidating") { operationValidating = operationRequest.deepCopy() }
                    worker("Очистка описания") { operationValidating.description = operationValidating.description.trim() }
                    worker("Очистка номера счёта \"c\"") { operationValidating.fromAccountId = FinsAccountId(operationValidating.fromAccountId.asString().trim()) }
                    worker("Очистка номера счёта \"по\"") { operationValidating.toAccountId = FinsAccountId(operationValidating.toAccountId.asString().trim()) }



                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyAmount("Проверка на непустую сумму")
                    validationFormatAmount("Проверка формата суммы")
                    validationNotEmptyFromAccountId("Проверка на непустой номер счёта \"c\"")
                    validationFormatFromAccountId("Проверка формата номера счёта \"c\"")
                    validationNotEmptyToAccountId("Проверка на непустой номер счёта \"по\"")
                    validationFormatToAccountId("Проверка формата номера счёта \"по\"")
                    validationNotEmptyOperationDateTime("Проверка формата даты операции")
                    validationFormatOperationDateTime("Проверка на непустую  операции")


                    finishOperationValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Получить информацию об операции", FinsCommand.OPERATIONREAD) {
                stubs("Обработка стабов") {
                    stubOperationReadSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatOperationId("Имитация ошибки валидации операции - неверный формат")
                    stubValidationEmptyOperationId("Имитация ошибки валидации операции - пустое значение")
                    stubNotFoundOperationId("Имитация ошибки notFound для операции ")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в operationValidating") { operationValidating = operationRequest.deepCopy() }
                    worker("Очистка operationId") { operationValidating.operationId = FinsOperationId(operationValidating.operationId.asString().trim()) }


                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyOperationId("Проверка на непустой номер операции")
                    validationFormatOperationId("Проверка формата номера операции")

                    finishOperationValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Изменить операцию", FinsCommand.OPERATIONUPDATE) {
                stubs("Обработка стабов") {
                    stubOperationUpdateSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatAmount("Имитация ошибки валидации суммы - неверный формат")
                    stubValidationEmptyAmount("Имитация ошибки валидации суммы")

                    stubValidationBadFormatFromAccountId("Имитация ошибки валидации счёта  \"с\" - неверный формат")
                    stubValidationEmptyFromAccountId("Имитация ошибки валидации счёта \"с\" - пустое значение")
                    stubNotFoundFromAccountId("Имитация ошибки notFound для счёта  \"с\"")

                    stubValidationBadFormatToAccountId("Имитация ошибки валидации счёта  \"по\" - неверный формат")
                    stubValidationEmptyToAccountId("Имитация ошибки валидации счёта \"по\" - пустое значение")
                    stubNotFoundToAccountId("Имитация ошибки notFound для счёта  \"по\"")

                    stubValidationBadFormatOperationDateTime("Имитация ошибки валидации даты операции - неверный формат")
                    stubValidationEmptyOperationDateTime("Имитация ошибки валидации даты операции - пустое значение")

                    stubValidationBadFormatOperationId("Имитация ошибки валидации операции - неверный формат")
                    stubValidationEmptyOperationId("Имитация ошибки валидации операции - пустое значение")
                    stubNotFoundOperationId("Имитация ошибки notFound для операции ")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в operationValidating") { operationValidating = operationRequest.deepCopy() }
                    worker("Очистка описания") { operationValidating.description = operationValidating.description.trim() }
                    worker("Очистка номера счёта \"c\"") { operationValidating.fromAccountId = FinsAccountId(operationValidating.fromAccountId.asString().trim()) }
                    worker("Очистка номера счёта \"по\"") { operationValidating.toAccountId = FinsAccountId(operationValidating.toAccountId.asString().trim()) }
                    worker("Очистка operationId") { operationValidating.operationId = FinsOperationId(operationValidating.operationId.asString().trim()) }


                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyAmount("Проверка на непустую сумму")
                    validationFormatAmount("Проверка формата суммы")
                    validationNotEmptyFromAccountId("Проверка на непустой номер счёта \"c\"")
                    validationFormatFromAccountId("Проверка формата номера счёта \"c\"")
                    validationNotEmptyToAccountId("Проверка на непустой номер счёта \"по\"")
                    validationFormatToAccountId("Проверка формата номера счёта \"по\"")
                    validationNotEmptyOperationDateTime("Проверка формата даты операции")
                    validationFormatOperationDateTime("Проверка на непустую  операции")
                    validationNotEmptyOperationId("Проверка на непустой номер операции")
                    validationFormatOperationId("Проверка формата номера операции")

                    finishOperationValidation("Успешное завершение процедуры валидации")
                }
            }
            procedure("Удалить операцию", FinsCommand.OPERATIONDELETE) {
                stubs("Обработка стабов") {
                    stubOperationDeleteSuccess("Имитация успешной обработки")

                    stubValidationBadFormatUserId("Имитация ошибки валидации пользователя - неверный формат")
                    stubValidationEmptyUserId("Имитация ошибки валидации пользователя - пустое значение")
                    stubNotFoundUserId("Имитация ошибки notFound для пользователя ")

                    stubValidationBadFormatOperationId("Имитация ошибки валидации операции - неверный формат")
                    stubValidationEmptyOperationId("Имитация ошибки валидации операции - пустое значение")
                    stubNotFoundOperationId("Имитация ошибки notFound для операции ")

                    stubCannotDelete("Ошибка: невозможно удалить")

                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                chain {
                    title = "Валидация запроса"
                    worker("Копируем поля в operationValidating") { operationValidating = operationRequest.deepCopy() }
                    worker("Очистка operationId") { operationValidating.operationId = FinsOperationId(operationValidating.operationId.asString().trim()) }


                    validationNotEmptyUserId("Проверка на непустой userId")
                    validationFormatUserId("Проверка формата userId")
                    validationNotEmptyOperationId("Проверка на непустой номер операции")
                    validationFormatOperationId("Проверка формата номера операции")

                    finishOperationValidation("Успешное завершение процедуры валидации")
                }
            }

        }.build()
    }
}
