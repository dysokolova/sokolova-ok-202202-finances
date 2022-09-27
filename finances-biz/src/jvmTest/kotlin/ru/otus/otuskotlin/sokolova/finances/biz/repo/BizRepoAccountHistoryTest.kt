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
class BizRepoAccountHistoryTest {

    private val command = FinsCommand.ACCOUNTHISTORY
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
    private val initOperation = FinsOperation(
        description = "create object description",
        amount = 10.0,
        fromAccountId = initAccount1.accountId,
        toAccountId = initAccount2.accountId,
        operationDateTime = Instant.parse("2022-02-13T12:00:00.000Z"),
        operationId = FinsOperationId("50000000-0000-0000-0000-000000000000"),
    )
    private val repo by lazy {
        RepoInMemory(
            initAccountObjects = listOf(
                Pair(userId, initAccount1),
                Pair(userId, initAccount2)
            ), initOperationObjects = listOf(Pair(userId, initOperation))
        )
    }
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoHistorySuccessTest() = runTest {
        val finsHistFilter = FinsHistFilter(
            fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        )
        val accountToHist = FinsAccount(
            accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            accountRequest = accountToHist,
            accountHistoryRequest = finsHistFilter,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
        assertEquals(initOperation.description, ctx.operationsResponse.first().description)
        assertEquals(initOperation.fromAccountId, ctx.operationsResponse.first().fromAccountId)
        assertEquals(initOperation.toAccountId, ctx.operationsResponse.first().toAccountId)
        assertEquals(initOperation.operationDateTime, ctx.operationsResponse.first().operationDateTime)
        assertEquals(initOperation.operationId, ctx.operationsResponse.first().operationId)
    }
}
