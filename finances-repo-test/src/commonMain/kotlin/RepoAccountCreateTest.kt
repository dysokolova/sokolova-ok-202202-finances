package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccountId
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class RepoAccountCreateTest {
    abstract val repo: IRepository

    @Test
    fun createSuccess() {
        val result = runBlocking { repo.accountCreate(DbAccountRequest(userId,createObj)) }
        val expected = createObj.copy(accountId = result.result?.accountId ?: FinsAccountId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.name, result.result?.name)
        assertEquals(expected.description, result.result?.description)
        assertEquals(expected.amount, result.result?.amount)
        assertNotEquals(FinsAccountId.NONE, result.result?.accountId)
        assertEquals(emptyList(), result.errors)
    }

    companion object: BaseInitObjects() {
        val userId = FinsUserId("user-123")

        private val createObj = FinsAccount(
            name = "create object",
            description = "create object description",
            amount = 0.0,
        )
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = emptyList()
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()
    }
}
