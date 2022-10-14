package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountHistoryRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAccountHistoryTest {
    abstract val repo: IRepository

    @Test
    fun historySuccess() {
        val result = runBlocking { repo.accountHistory(DbAccountHistoryRequest(userId, successId, FinsHistFilter(fromDateTime, toDateTime))) }

        assertEquals(true, result.isSuccess)
        assertEquals(initOperationObjects.first().second, result.result?.first())
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun historyEmptyResult() {
        val result = runBlocking { repo.accountHistory(DbAccountHistoryRequest(userId, noOperationsId, FinsHistFilter(fromDateTime, toDateTime))) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.result)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitObjects() {
        val userId = FinsUserId("user-123")

        val fromDateTime = Instant.parse("2022-02-12T12:00:00.000Z")
        val toDateTime = Instant.parse("2023-02-12T12:00:00.000+03:00")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("search1", userId), createInitTestAccountModel("search2", userId), createInitTestAccountModel("search3", userId)
        )
        private val fromAccountId = initAccountObjects[0].second.accountId
        private val toAccountId = initAccountObjects[1].second.accountId
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = listOf(
            createInitTestOperationModel("read", fromAccountId, toAccountId, userId ),
        )

        val successId = FinsAccountId(initAccountObjects.first().second.accountId.asString())
        val noOperationsId = FinsAccountId("account-repo-search3")

    }
}
