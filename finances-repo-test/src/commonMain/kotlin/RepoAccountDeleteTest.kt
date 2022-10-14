package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountIdRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAccountDeleteTest {
    abstract val repo: IRepository

    @Test
    fun deleteSuccess() {
        val result = runBlocking { repo.accountDelete(DbAccountIdRequest(userId, successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.accountDelete(DbAccountIdRequest(userId, notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals("AccountId", result.errors.first().field)
        assertEquals("notFound", result.errors.first().group)
    }

    companion object : BaseInitObjects() {
        val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("delete")
        )
        private val deleteSuccessStub = initAccountObjects.first()

        val successId = FinsAccountId(deleteSuccessStub.second.accountId.asString())
        val notFoundId = FinsAccountId("ad-repo-delete-notFound")
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()

    }
}
