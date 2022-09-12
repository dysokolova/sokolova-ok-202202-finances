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
class AccountUpdateStubTest {
    private val processor = FinsProcessor()
    val userId = FinsUserId("edbbff08-dab9-4404-8274-ceec43bb5b28")
    val accountId = FinsAccountId("ebf612bc-7a95-4b1d-ab7f-00e9c6169792")
    val name = "name 123"
    val description = "desc 123"

    @Test
    fun update() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTUPDATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            accountRequest = FinsAccount(
                accountId = accountId,
                name = name,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(accountId, ctx.accountResponse.accountId)
        assertEquals(name, ctx.accountResponse.name)
        assertEquals(description, ctx.accountResponse.description)
    }

    @Test
    fun badAccountId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTUPDATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_ACCOUNT_ID,
            accountRequest = FinsAccount(),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("AccountId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun emptyAccountName() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTUPDATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_NAME,
            accountRequest = FinsAccount(
                accountId = accountId,
                name = "",
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("Name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badAmount() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTUPDATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_AMOUNT,
            accountRequest = FinsAccount(
                accountId = accountId,
                name = name,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("Amount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTUPDATE,
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
            command = FinsCommand.ACCOUNTUPDATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_SEARCH_FILTER,
            accountRequest = FinsAccount(
                accountId = accountId,
                name = name,
                description = description,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
