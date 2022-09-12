package ru.otus.otuskotlin.sokolova.finances.biz.stub

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class OperationCreateStubTest {
    private val processor = FinsProcessor()
    val userId = FinsUserId("edbbff08-dab9-4404-8274-ceec43bb5b28")
    val fromAccountId = FinsAccountId("e69e94c8-4c6f-45ac-9a56-5b13b1147bbc")
    val toAccountId = FinsAccountId("ef349779-f895-478c-94f1-19b50fa2eaf9")
    val operationDateTime = Clock.System.now()
    val description = "платёж"
    val amount = 20.0

    @Test
    fun create() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            operationRequest = FinsOperation(
                description = description,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                operationDateTime = operationDateTime,
            ),
        )
        processor.exec(ctx)
        assertEquals(description, ctx.operationResponse.description)
        assertEquals(amount, ctx.operationResponse.amount)
        assertEquals(fromAccountId, ctx.operationResponse.fromAccountId)
        assertEquals(toAccountId, ctx.operationResponse.toAccountId)
        assertEquals(operationDateTime, ctx.operationResponse.operationDateTime)
    }

    @Test
    fun badToAccountId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_TO_ACCOUNT_ID,
            operationRequest = FinsOperation(),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("ToAccountId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun emptyFromAccountId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_FROM_ACCOUNT_ID,
            operationRequest = FinsOperation(
                description = description,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                operationDateTime = operationDateTime,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("FromAccountId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
    @Test
    fun badAmount() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_AMOUNT,
            operationRequest = FinsOperation(
                description = description,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                operationDateTime = operationDateTime,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("Amount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            operationRequest = FinsOperation(
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONCREATE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_OPERATION_ID,
            operationRequest = FinsOperation(
                description = description,
                amount = amount,
                fromAccountId = fromAccountId,
                toAccountId = toAccountId,
                operationDateTime = operationDateTime,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
