package ru.otus.otuskotlin.sokolova.finances.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationAccountUpdateTest {
    private val repo = RepoInMemory()
    private val settings = FinsSettings(
        repoTest = repo
    )
    private val processor = FinsProcessor(settings)
    private val command = FinsCommand.ACCOUNTUPDATE
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
//    fun validationNameTest() = validationNameTest(command, processor)

    @Test
    fun validationNotEmptyNameTest() = validationNotEmptyNameTest(command, processor)
//
//    @Test
//    fun validationAmountTest() = validationAmountTest(command, processor)

    @Test
    fun validationFormatAmountTest() = validationFormatAmountTest(command, processor)

    @Test
    fun validationAccountIdTest() = validationAccountIdTest(command, processor)

    @Test
    fun validationNotEmptyAccountIdTest() = validationNotEmptyAccountIdTest(command, processor)

    @Test
    fun validationFormatAccountIdTest() = validationFormatAccountIdTest(command, processor)


}

