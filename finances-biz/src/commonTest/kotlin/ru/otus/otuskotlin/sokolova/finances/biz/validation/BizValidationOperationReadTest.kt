package ru.otus.otuskotlin.sokolova.finances.biz.validation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsCommand
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationOperationReadTest {
    private val repo = RepoInMemory()
    private val settings = FinsSettings(
        repoTest = repo
    )
    private val processor = FinsProcessor(settings)
    private val command = FinsCommand.OPERATIONREAD
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
//    fun validationOperationIdTest() = validationOperationIdTest(command, processor)

    @Test
    fun validationNotEmptyOperationIdTest() = validationNotEmptyOperationIdTest(command, processor)

    @Test
    fun validationFormatOperationIdTest() = validationFormatOperationIdTest(command, processor)


}

