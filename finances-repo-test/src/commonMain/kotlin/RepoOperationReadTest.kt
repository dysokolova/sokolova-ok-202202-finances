package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationIdRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoOperationReadTest {
    abstract val repo: IRepository

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.operationRead(DbOperationIdRequest(userId, successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(initOperationObjects.first().second, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.operationRead(DbOperationIdRequest(userId, notFoundId)) }

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
            createInitTestAccountModel("search1", userId), createInitTestAccountModel("search2", userId)
        )

        private val fromAccountId = initAccountObjects[0].second.accountId
        private val toAccountId = initAccountObjects[1].second.accountId
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = listOf(
            createInitTestOperationModel("read", fromAccountId, toAccountId, userId ),
        )

        val successId = FinsOperationId(initOperationObjects.first().second.operationId.asString())
        val notFoundId = FinsOperationId("ad-repo-read-notFound")

    }
}
