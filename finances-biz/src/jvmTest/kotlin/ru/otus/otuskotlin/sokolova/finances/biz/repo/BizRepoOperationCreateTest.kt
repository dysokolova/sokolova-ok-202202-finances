package ru.otus.otuskotlin.sokolova.finances.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoOperationCreateTest {

    private val command = FinsCommand.OPERATIONCREATE
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
    private val initAccount1 = FinsAccount(
        name = "abc",
        description = "abc",
        amount = 0.0,
        accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
    )
    private val initAccount2 = FinsAccount(
        name = "abc",
        description = "abc",
        amount = 0.0,
        accountId = FinsAccountId("40000000-0000-0000-0000-000000000000"),
    )
    private val repo by lazy { RepoInMemory(initAccountObjects = listOf(Pair(userId,initAccount1),Pair(userId,initAccount2))) }
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoCreateSuccessTest() = runTest {
        val operationToCreate = FinsOperation(
            description = "create object description",
            amount = 10.0,
            fromAccountId = initAccount1.accountId,
            toAccountId = initAccount2.accountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            operationRequest = operationToCreate,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
        assertEquals(operationToCreate.description, ctx.operationResponse.description)
        assertEquals(operationToCreate.fromAccountId, ctx.operationResponse.fromAccountId)
        assertEquals(operationToCreate.toAccountId, ctx.operationResponse.toAccountId)
        assertEquals(operationToCreate.operationDateTime, ctx.operationResponse.operationDateTime)
    }
}