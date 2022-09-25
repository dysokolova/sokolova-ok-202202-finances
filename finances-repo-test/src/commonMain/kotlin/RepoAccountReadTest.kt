package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountIdRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAccountReadTest {
    abstract val repo: IRepository

    @Test
    fun readSuccess() {
        val result = runBlocking { repo.accountRead(DbAccountIdRequest(userId, successId)) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub.second, result.result)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() {
        val result = runBlocking { repo.accountRead(DbAccountIdRequest(userId, notFoundId)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals(
            listOf(FinsError(field = "id", message = "Not Found")),
            result.errors
        )
    }

    companion object : BaseInitObjects() {
        val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("read")
        )
        private val readSuccessStub = initAccountObjects.first()

        val successId = FinsAccountId(readSuccessStub.second.accountId.asString())
        val notFoundId = FinsAccountId("ad-repo-read-notFound")
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()

    }
}
