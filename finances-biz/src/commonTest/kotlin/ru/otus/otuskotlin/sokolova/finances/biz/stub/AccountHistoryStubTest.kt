package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.HIST_FILTER_TMP
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountHistoryStubTest {
    private val processor = FinsProcessor()
    val accountId = FinsAccountId("411b9c45-9b40-4ded-99ec-4a55d3e90f38")
    val userId = FinsUserId("bb5b0cef-9b15-4ce1-9c03-4896fef29598")

    @Test
    fun history() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTHISTORY,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            userId = userId,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)

        assertTrue(ctx.operationsResponse.size > 1)
        assertEquals(accountId, ctx.operationsResponse.get(0).fromAccountId)
        assertEquals(accountId, ctx.operationsResponse.get(1).toAccountId)
        assertEquals(HIST_FILTER_TMP.fromDateTime, ctx.operationsResponse.get(0).operationDateTime)
        assertEquals(HIST_FILTER_TMP.toDateTime, ctx.operationsResponse.get(5).operationDateTime)
        }

    @Test
    fun badAccountId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTHISTORY,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_ACCOUNT_ID,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals("AccountId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTHISTORY,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTHISTORY,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.CANNOT_DELETE,
            accountRequest = FinsAccount(
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
