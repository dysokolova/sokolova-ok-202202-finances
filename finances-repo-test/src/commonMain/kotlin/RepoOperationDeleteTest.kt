package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbOperationIdRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoOperationDeleteTest {
    abstract val repo: IRepository

    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.operationDelete(DbOperationIdRequest(userId, successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() {
        val result = runBlocking { repo.operationDelete(DbOperationIdRequest(userId, notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals("OperationId", result.errors.first().field)
        assertEquals("notFound", result.errors.first().group)
    }

    companion object : BaseInitObjects() {
        private val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("search1", userId,1), createInitTestAccountModel("search2", userId,2)
        )

        private val fromAccountId = initAccountObjects[0].second.accountId
        private val toAccountId = initAccountObjects[1].second.accountId
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = listOf(
            createInitTestOperationModel("delete", fromAccountId, toAccountId, userId ),
        )

        val successId = FinsOperationId(initOperationObjects.first().second.operationId.asString())
        val notFoundId = FinsOperationId("ad-repo-delete-notFound")

        private val deleteSuccessStub = initOperationObjects.first()


    }
}
