package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountFilterRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountIdRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAccountSearchTest {
    abstract val repo: IRepository

    @Test
    fun searchSuccess() {
        val result = runBlocking { repo.accountSearch(DbAccountFilterRequest(userId, FinsSrchFilter(""))) }

        assertEquals(true, result.isSuccess)
        assertEquals(readSuccessStub.second, result.result?.first())
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun searchEmptyResult() {
        val result = runBlocking { repo.accountSearch(DbAccountFilterRequest(userId, FinsSrchFilter("ad-repo-search-notFound"))) }

        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.result)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitObjects() {
        val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("search")
        )
        private val readSuccessStub = initAccountObjects.first()

        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()

    }
}
