package ru.otus.otuskotlin.sokolova.finances.biz

import com.crowdproj.kotlin.cor.*
import com.crowdproj.kotlin.cor.handlers.chain
import com.crowdproj.kotlin.cor.handlers.worker
import ru.otus.otuskotlin.sokolova.finances.biz.general.initRepo
import ru.otus.otuskotlin.sokolova.finances.biz.stubs.stubAccountHistorySuccess
import ru.otus.otuskotlin.sokolova.finances.biz.general.procedure
import ru.otus.otuskotlin.sokolova.finances.biz.stubs.*
import ru.otus.otuskotlin.sokolova.finances.biz.validation.*
import ru.otus.otuskotlin.sokolova.finances.biz.general.initStatus
import ru.otus.otuskotlin.sokolova.finances.biz.general.prepareResult
import ru.otus.otuskotlin.sokolova.finances.biz.repo.*
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*

class FinsProcessor(private val settings: FinsSettings = FinsSettings()) {
    suspend fun exec(ctx: FinsContext) = BuzinessChain.exec(ctx.apply { settings = this@FinsProcessor.settings })

    companion object {
        private val BuzinessChain = rootChain<FinsContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

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

                chain {
                    title = "Логика сохранения"
                    repoAccountCreatePrepare("Подготовка объекта для сохранения")
                    repoAccountCreate("Создание счёта в БД")
                }
                prepareResult("Подготовка ответа")
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

                chain {
                    title = "Логика для чтения данных по счёту"
                    repoAccountRead("Чтение счёта из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == FinsState.RUNNING }
                        handle { accountRepoDone = accountRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
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

                    stubErrorAccountConcurentOnChange("Имитация ошибки блокировки для изменения счёта ")
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

                chain {
                    title = "Логика изменения"
                    repoAccountRead("Чтение счёта из БД")
                    repoAccountCheckReadLockChange("Проверяем блокировку")
                    repoAccountUpdatePrepare("Подготовка объекта для обновления")
                    repoAccountUpdate("Обновление счёта в БД")
                }
                prepareResult("Подготовка ответа")
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

                    stubErrorAccountConcurentOnDelete("Имитация ошибки блокировки для удаления счёта ")
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

                chain {
                    title = "Логика удаления данных счёта"
                    repoAccountRead("Чтение счёта из БД")
                    repoAccountCheckReadLockDelete("Проверяем блокировку")
                    repoAccountDeletePrepare("Подготовка объекта для удаления")
                    repoAccountDelete("Удаление счёта из БД")
                }
                prepareResult("Подготовка ответа")
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

                repoAccountSearch("Поиск счетов в БД по фильтру")
                prepareResult("Подготовка ответа")
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

                chain {
                    title = "Поиск в БД операций по счёту за период"
                    repoAccountHistoryPrepare("Подготовка объекта для обращения в БД")
                    repoAccountHistory("Запрос истории")
                    prepareResult("Подготовка ответа")
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

                chain {
                    title = "Логика сохранения"
                    repoOperationCreatePrepare("Подготовка объекта для сохранения")
                    repoOperationCreate("Создание операции в БД")
                }
                prepareResult("Подготовка ответа")
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

                chain {
                    title = "Логика для чтения данных по операции"
                    repoOperationRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == FinsState.RUNNING }
                        handle { operationRepoDone = operationRepoRead }
                    }
                }
                prepareResult("Подготовка ответа")
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

                    stubErrorOperationConcurentOnChange("Имитация ошибки блокировки для изменения операции ")

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
                chain {
                    title = "Логика изменения"
                    repoOperationRead("Чтение операции из БД")
                    repoOperationCheckReadLockChange("Проверяем блокировку")
                    repoOperationUpdatePrepare("Подготовка объекта для обновления")
                    repoOperationUpdate("Обновление операции в БД")
                }
                prepareResult("Подготовка ответа")
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

                    stubErrorOperationConcurentOnDelete("Имитация ошибки блокировки для удаления операции ")
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
                chain {
                    title = "Логика удаления данных операции"
                    repoOperationRead("Чтение операции из БД")
                    repoOperationCheckReadLockDelete("Проверяем блокировку")
                    repoOperationDeletePrepare("Подготовка объекта для удаления")
                    repoOperationDelete("Удаление операции из БД")
                }
                prepareResult("Подготовка ответа")
            }

        }.build()
    }
}
