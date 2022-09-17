package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.stubs.FinsObjectsStub.prepareAccountResult
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountCreateStubTest {

    private val processor = FinsProcessor()
    val userId = FinsUserId("579d97b9-fade-4297-94a8-4efd6dde15e9")
    val name = "Наличные"
    val description = "основной счет в Тинькофф"
    val amount = 1200.0
    val accountId = FinsAccountId("e3f30df9-814e-42ae-b1a0-fc58f887ffbe")

    @Test
    fun create() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            accountRequest = FinsAccount(
                name = name,
                description = description,
                amount = amount,
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(name, ctx.accountResponse.name)
        assertEquals(description, ctx.accountResponse.description)
        assertEquals(amount, ctx.accountResponse.amount)
        assertEquals(accountId, ctx.accountResponse.accountId)
    }
    @Test
    fun createStub() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
        )
        processor.exec(ctx)
        val result = prepareAccountResult{}
        assertEquals(result.name, ctx.accountResponse.name)
        assertEquals(result.description, ctx.accountResponse.description)
        assertEquals(result.amount, ctx.accountResponse.amount)
        assertEquals(result.accountId, ctx.accountResponse.accountId)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_NAME,
            accountRequest = FinsAccount(
                name = name,
                description = description,
                amount = amount,
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("Name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badDescription() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_AMOUNT,
            accountRequest = FinsAccount(
                name = name,
                description = description,
                amount = amount,
                accountId = accountId,
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
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            accountRequest = FinsAccount(
                name = name,
                description = description,
                amount = amount,
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
            command = FinsCommand.ACCOUNTCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_ACCOUNT_ID,
            accountRequest = FinsAccount(
                name = name,
                description = description,
                amount = amount,
                accountId = accountId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
