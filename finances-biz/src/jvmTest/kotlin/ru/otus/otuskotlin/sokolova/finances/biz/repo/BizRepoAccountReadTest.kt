package ru.otus.otuskotlin.sokolova.finances.biz.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.RepoInMemory
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoAccountReadTest {

    private val command = FinsCommand.ACCOUNTREAD
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
    private val initAccount = FinsAccount(
        name = "abc",
        description = "abc",
        amount = 0.0,
        accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
    )
    private val repo by lazy { RepoInMemory(initAccountObjects = listOf(Pair(userId,initAccount))) }
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoReadSuccessTest() = runTest {
        val accountToRead = FinsAccount(
            accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            accountRequest = accountToRead,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
        assertEquals(initAccount.name, ctx.accountResponse.name)
        assertEquals(initAccount.description, ctx.accountResponse.description)
        assertEquals(initAccount.amount, ctx.accountResponse.amount)
        assertEquals(accountToRead.accountId, ctx.accountResponse.accountId)
    }
}