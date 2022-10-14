package ru.otus.otuskotlin.sokolova.finances.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationAccountHistoryTest {
    private val repo = RepoInMemory()
    private val settings = FinsSettings(
        repoTest = repo
    )
    private val processor = FinsProcessor(settings)
    private val command = FinsCommand.ACCOUNTHISTORY

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
//    fun validationAccountIdTest() = validationAccountIdTest(command, processor)

    @Test
    fun validationNotEmptyAccountIdTest() = validationNotEmptyAccountIdTest(command, processor)

    @Test
    fun validationFormatAccountIdTest() = validationFormatAccountIdTest(command, processor)

    @Test
    fun validationFromDateTimeTest() = validationFromDateTimeTest(command, processor)

    @Test
    fun validationFormatFromDateTimeTest() = validationFormatFromDateTimeTest(command, processor)

    @Test
    fun validationToDateTimeTest() = validationToDateTimeTest(command, processor)

    @Test
    fun validationFormatToDateTimeTest() = validationFormatToDateTimeTest(command, processor)

}

