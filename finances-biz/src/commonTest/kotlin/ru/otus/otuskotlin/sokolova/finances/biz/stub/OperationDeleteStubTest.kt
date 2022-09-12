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
class OperationDeleteStubTest {
    private val processor = FinsProcessor()
    val userId = FinsUserId("edbbff08-dab9-4404-8274-ceec43bb5b28")
    val operationId = FinsOperationId("ebf612bc-7a95-4b1d-ab7f-00e9c6169792")


    @Test
    fun delete() = runTest {

        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.SUCCESS,
            operationRequest = FinsOperation(
                operationId = operationId
            ),
        )
        processor.exec(ctx)
        assertEquals(0, ctx.errors.size)
    }

    @Test
    fun badOperationId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_OPERATION_ID,
            operationRequest = FinsOperation(),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("OperationId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun emptyOperationId() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.EMPTY_OPERATION_ID,
            operationRequest = FinsOperation(
                operationId = operationId
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("OperationId", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun cannotDelete() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.CANNOT_DELETE,
            operationRequest = FinsOperation(
                operationId = operationId
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("logic", ctx.errors.firstOrNull()?.group)
        assertEquals("cannotDelete", ctx.errors.firstOrNull()?.code)

    }

    @Test
    fun databaseError() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.DB_ERROR,
            operationRequest = FinsOperation(
                operationId = operationId,
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = FinsContext(
            command = FinsCommand.OPERATIONDELETE,
            userId = userId,
            state = FinsState.NONE,
            workMode = FinsWorkMode.STUB,
            stubCase = FinsStubs.BAD_FORMAT_OPERATION_DATE_TIME,
            operationRequest = FinsOperation(
                operationId = operationId
            ),
        )
        processor.exec(ctx)
        assertEquals(FinsOperation(), ctx.operationResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
