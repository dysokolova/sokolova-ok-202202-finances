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
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoOperationUpdateTest {

    private val command = FinsCommand.OPERATIONUPDATE
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
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
    private val initOperation = FinsOperation(
        description = "create object description",
        amount = 10.0,
        fromAccountId = initAccount1.accountId,
        toAccountId = initAccount2.accountId,
        operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
        operationId = FinsOperationId("50000000-0000-0000-0000-000000000000"),
        operationLock = FinsOperationLock(uuidOld),
    )
    private val repo by lazy { RepoInMemory(initAccountObjects = listOf(Pair(userId,initAccount1),Pair(userId,initAccount2)), initOperationObjects = listOf(Pair(userId,initOperation)))}
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val operationToUpdate = FinsOperation(
            description = "create object description",
            amount = 10.0,
            fromAccountId = initAccount1.accountId,
            toAccountId = initAccount2.accountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            operationId = FinsOperationId("50000000-0000-0000-0000-000000000000"),
            operationLock = FinsOperationLock(uuidOld),
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            operationRequest = operationToUpdate,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
        assertEquals(operationToUpdate.description, ctx.operationResponse.description)
        assertEquals(operationToUpdate.fromAccountId, ctx.operationResponse.fromAccountId)
        assertEquals(operationToUpdate.toAccountId, ctx.operationResponse.toAccountId)
        assertEquals(operationToUpdate.operationDateTime, ctx.operationResponse.operationDateTime)
        assertEquals(operationToUpdate.operationId, ctx.operationResponse.operationId)
        assertNotEquals(operationToUpdate.operationLock, ctx.operationResponse.operationLock)
    }
}