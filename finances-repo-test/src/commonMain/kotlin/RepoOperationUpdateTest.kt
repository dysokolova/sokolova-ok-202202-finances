package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoOperationUpdateTest {
    abstract val repo: IRepository

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.operationUpdate(DbOperationRequest(userId, updateObj)) }

        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.description, result.result?.description)
        assertEquals(updateObj.amount, result.result?.amount)
        assertEquals(updateObj.operationId, result.result?.operationId)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() {
        val result = runBlocking { repo.operationUpdate(DbOperationRequest(userId, updateObjNotFound)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(FinsError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object : BaseInitObjects() {
        private val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            RepoOperationReadTest.createInitTestAccountModel("search1", userId, 1),
            RepoOperationReadTest.createInitTestAccountModel("search2", userId, 2),
            RepoOperationReadTest.createInitTestAccountModel("search3", userId, 3),
            RepoOperationReadTest.createInitTestAccountModel("search4", userId, 4)
        )

        private val fromAccountIdOld = initAccountObjects[0].second.accountId
        private val toAccountIdOld = initAccountObjects[1].second.accountId
        private val fromAccountId = initAccountObjects[2].second.accountId
        private val toAccountId = initAccountObjects[3].second.accountId
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = listOf(
            RepoOperationReadTest.createInitTestOperationModel("read", fromAccountIdOld, toAccountIdOld, userId),
        )

        val successId = FinsOperationId(initOperationObjects.first().second.operationId.asString())
        val notFoundId = FinsOperationId("ad-repo-read-notFound")

        private val updateObj = FinsOperation(
            description = "update object description",
            amount = 20.0,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            operationId = successId,
            )

        private val updateObjNotFound = FinsOperation(
            description = "update object not found description",
            amount = 0.0,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            operationId = notFoundId,
        )
    }
}
