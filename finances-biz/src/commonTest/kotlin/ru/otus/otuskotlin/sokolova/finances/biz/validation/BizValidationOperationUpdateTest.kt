package ru.otus.otuskotlin.sokolova.finances.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationOperationUpdateTest {
    private val repo = RepoInMemory()
    private val settings = FinsSettings(
        repoTest = repo
    )
    private val processor = FinsProcessor(settings)
    private val command = FinsCommand.OPERATIONUPDATE
//
//    @Test
//    fun validationUserIdTest() = validationUserIdTest(command, processor)
//
//    @Test
//    fun validationNotEmptyUserIdTest() = validationNotEmptyUserIdTest(command, processor)
//
//    @Test
//    fun validationFormatUserIdTest() = validationFormatUserIdTest(command, processor)

//    @Test
//    fun validationAmountTest() = validationAmountTest(command, processor)

    @Test
    fun validationFormatAmountTest() = validationFormatAmountTest(command, processor)

//    @Test
//    fun validationFromAccountIdTest() = validationFromAccountIdTest(command, processor)

    @Test
    fun validationNotEmptyFromAccountIdTest() = validationNotEmptyFromAccountIdTest(command, processor)

    @Test
    fun validationFormatFromAccountIdTest() = validationFormatFromAccountIdTest(command, processor)

//    @Test
//    fun validationToAccountIdTest() = validationToAccountIdTest(command, processor)

    @Test
    fun validationNotEmptyToAccountIdTest() = validationNotEmptyToAccountIdTest(command, processor)

    @Test
    fun validationFormatToAccountIdTest() = validationFormatToAccountIdTest(command, processor)

//    @Test
//    fun validationOperationDateTimeTest() = validationOperationDateTimeTest(command, processor)

    @Test
    fun validationFormatOperationDateTimeTest() = validationFormatOperationDateTimeTest(command, processor)

//    @Test
//    fun validationOperationIdTest() = validationOperationIdTest(command, processor)

    @Test
    fun validationNotEmptyOperationIdTest() = validationNotEmptyOperationIdTest(command, processor)

    @Test
    fun validationFormatOperationIdTest() = validationFormatOperationIdTest(command, processor)


}

