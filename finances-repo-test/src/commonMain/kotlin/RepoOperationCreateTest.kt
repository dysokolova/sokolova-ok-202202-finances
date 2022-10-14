package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoOperationCreateTest {
    abstract val repo: IRepository

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.operationCreate(DbOperationRequest(userId, createObj)) }
        val expected = createObj.copy(operationId = result.result?.operationId ?: FinsOperationId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.description, result.result?.description)
        assertEquals(expected.amount, result.result?.amount)
        assertNotEquals(FinsOperationId.NONE, result.result?.operationId)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitObjects() {
        private val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("search1", userId), createInitTestAccountModel("search2", userId)
        )
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()

        private val fromAccountId = initAccountObjects[0].second.accountId
        private val toAccountId = initAccountObjects[1].second.accountId

        private val createObj = FinsOperation(
            description = "create object description",
            amount = 10.0,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
        )
    }

}