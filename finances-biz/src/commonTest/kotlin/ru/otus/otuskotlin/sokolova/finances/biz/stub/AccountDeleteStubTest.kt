package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountDeleteStubTest {

    private val processor = FinsProcessor()
    val accountId = FinsAccountId("daaf90ba-df2f-4f80-b240-df762321a970")
    val userId = FinsUserId("78b8bc7e-124e-460c-b29b-b5a8b5b556ee")

    @Test
    fun delete() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTDELETE,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            userId = userId,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
        assertEquals(FinsWorkMode.STUB, ctx.workMode)
        assertEquals(FinsStubs.SUCCESS, ctx.stubCase)}

    @Test
    fun notFoundAccountId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTDELETE,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.NOT_FOUND_ACCOUNT_ID,
            accountRequest = FinsAccount(),
        )
        processor.exec(ctx)
        assertEquals("notFound-AccountId", ctx.errors.firstOrNull()?.code)
        assertEquals("notFound", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
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
            command = FinsCommand.ACCOUNTDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_OPERATION_ID,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
