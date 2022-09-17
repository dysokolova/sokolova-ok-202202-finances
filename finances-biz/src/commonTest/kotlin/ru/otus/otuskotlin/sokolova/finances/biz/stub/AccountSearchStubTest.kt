package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountSearchStubTest {
    private val processor = FinsProcessor()
    val filter = FinsSrchFilter(searchFilter = "строка")
    val userId = FinsUserId("00e7cff0-63c4-4173-ba94-196c44292350")

    @Test
    fun search() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTSEARCH,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            userId = userId,
            accountFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.accountsResponse.size > 1)
        assertTrue(ctx.accountsResponse.get(0).name.contains(filter.searchFilter))
        assertTrue(ctx.accountsResponse.get(0).description.contains(filter.searchFilter))
    }

    @Test
    fun badUserId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTSEARCH,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_USER_ID,
            accountFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("UserId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTSEARCH,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            accountFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.ACCOUNTSEARCH,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.CANNOT_DELETE,
            accountFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(FinsAccount(), ctx.accountResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
