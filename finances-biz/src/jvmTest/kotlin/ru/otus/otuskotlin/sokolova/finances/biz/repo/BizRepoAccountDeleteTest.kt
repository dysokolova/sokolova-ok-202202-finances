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
class BizRepoAccountDeleteTest {


    private val command = FinsCommand.ACCOUNTDELETE
    private val uuidOld = "10000000-0000-0000-0000-000000000001"
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
    private val initAccount = FinsAccount(
        name = "abc",
        description = "abc",
        amount = 0.0,
        accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
        accountLock = FinsAccountLock(uuidOld),
    )
    private val repo by lazy { RepoInMemory(initAccountObjects = listOf(Pair(userId, initAccount))) }
    private val settings by lazy {
        FinsSettings(
            repoTest = repo
        )
    }
    private val processor by lazy { FinsProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val accountToDelete = FinsAccount(
            accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
            accountLock = FinsAccountLock(uuidOld),
        )
        val ctx = FinsContext(
            command = command,
            state = FinsState.NONE,
            userId = userId,
            workMode = FinsWorkMode.TEST,
            accountRequest = accountToDelete,
        )
        processor.exec(ctx)
        assertEquals(FinsState.FINISHING, ctx.state)
    }
}
