package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.ACCOUNT_TMP
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class AccountReadStubTest {
    private val processor = FinsProcessor()
    val accountId = FinsAccountId("a7ca336c-49c8-4e80-86a9-e54ad1aff0a6")
    val userId = FinsUserId("64a1c288-26ce-4d41-aed3-dd897f6568bb")

    @Test
    fun read() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTREAD,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
        )
        processor.exec(ctx)
        assertEquals(ACCOUNT_TMP.name, ctx.accountResponse.name)
        assertEquals(ACCOUNT_TMP.description, ctx.accountResponse.description)
        assertEquals(ACCOUNT_TMP.amount, ctx.accountResponse.amount)
        assertEquals(ACCOUNT_TMP.accountId, ctx.accountResponse.accountId)
    }

    @Test
    fun emptyUserId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTREAD,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_USER_ID,
            accountRequest = FinsAccount(),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("UserId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTREAD,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            userId = userId,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTREAD,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID,
            userId = userId,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
