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
    class BizRepoAccountUpdateTest {

        private val command = FinsCommand.ACCOUNTUPDATE
        private val uuidOld = "10000000-0000-0000-0000-000000000001"
        private val uuidNew = "10000000-0000-0000-0000-000000000002"
        private val uuidBad = "10000000-0000-0000-0000-000000000003"
    private val userId = FinsUserId("20000000-0000-0000-0000-000000000000")
        private val initAccount = FinsAccount(
            name = "abc",
            description = "abc",
            amount = 0.0,
            accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
            accountLock = FinsAccountLock(uuidOld),
        )
        private val repo by lazy { RepoInMemory(initAccountObjects = listOf(Pair(userId,initAccount)), randomUuid = { uuidNew }) }
        private val settings by lazy {
            FinsSettings(
                repoTest = repo
            )
        }
        private val processor by lazy { FinsProcessor(settings) }

        @Test
        fun repoUpdateSuccessTest() = runTest {
            val accountToUpdate = FinsAccount(
                name = "abc1",
                description = "abc1",
                amount = 10.0,
                accountId = FinsAccountId("30000000-0000-0000-0000-000000000000"),
                accountLock = FinsAccountLock(uuidOld),
            )
            val ctx = FinsContext(
                command = command,
                state = FinsState.NONE,
                userId = userId,
                workMode = FinsWorkMode.TEST,
                accountRequest = accountToUpdate,
            )
            processor.exec(ctx)
            assertEquals(FinsState.FINISHING, ctx.state)
            assertEquals(accountToUpdate.name, ctx.accountResponse.name)
            assertEquals(accountToUpdate.description, ctx.accountResponse.description)
            assertEquals(accountToUpdate.amount, ctx.accountResponse.amount)
            assertEquals(accountToUpdate.accountId, ctx.accountResponse.accountId)
            assertEquals(uuidNew, ctx.accountResponse.accountLock.asString())
        }
//
//        @Test
//        fun repoUpdateConcurrentTest() = runTest {
//            val adToUpdate = FinsAccount(
//                id = FinsAccountId("123"),
//                title = "xyz",
//                description = "xyz",
//                adType = FinsDealSide.DEMAND,
//                visibility = FinsVisibility.VISIBLE_TO_GROUP,
//                lock = FinsAccountLock(uuidBad)
//            )
//            val ctx = FinsContext(
//                command = command,
//                state = FinsState.NONE,
//                workMode = FinsWorkMode.TEST,
//                adRequest = adToUpdate,
//            )
//            processor.exec(ctx)
//            assertEquals(FinsState.FAILING, ctx.state)
//            assertEquals(initAccount.id, ctx.adResponse.id)
//            assertEquals(initAccount.title, ctx.adResponse.title)
//            assertEquals(initAccount.description, ctx.adResponse.description)
//            assertEquals(initAccount.adType, ctx.adResponse.adType)
//            assertEquals(initAccount.visibility, ctx.adResponse.visibility)
//            assertEquals(uuidOld, ctx.adResponse.lock.asString())
//        }
//
//        @Test
//        fun repoUpdateNotFoundTest() = repoNotFoundTest(processor, command)
    }
