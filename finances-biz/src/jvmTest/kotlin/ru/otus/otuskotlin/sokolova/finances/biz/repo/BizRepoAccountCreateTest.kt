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
class BizRepoAccountCreateTest {

    private val command = FinsCommand.ACCOUNTCREATE
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
    private val repo by lazy { RepoInMemory() }
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoCreateSuccessTest() = runTest {
        val accountToCreate = FinsAccount(
            name = "abc",
            description = "abc",
            amount = 0.0,
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            accountRequest = accountToCreate,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
        assertEquals(accountToCreate.name, ctx.accountResponse.name)
        assertEquals(accountToCreate.description, ctx.accountResponse.description)
        assertEquals(accountToCreate.amount, ctx.accountResponse.amount)
    }
}