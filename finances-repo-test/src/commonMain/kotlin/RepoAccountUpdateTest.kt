package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbAccountRequest
import ru.otus.otuskotlin.sokolova.finances.common.repo.IRepository
import kotlin.test.Test
import kotlin.test.assertEquals

abstract class RepoAccountUpdateTest {
    abstract val repo: IRepository

    @Test
    fun updateSuccess() {
        val result = runBlocking { repo.accountUpdate(DbAccountRequest(userId, updateObj)) }

        assertEquals(true, result.isSuccess)
        assertEquals(updateObj.name, result.result?.name)
        assertEquals(updateObj.description, result.result?.description)
        assertEquals(updateObj.amount, result.result?.amount)
        assertEquals(updateObj.accountId, result.result?.accountId)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound(){
        val result = runBlocking { repo.accountUpdate(DbAccountRequest(userId, updateObjNotFound)) }

        assertEquals(false, result.isSuccess)
        assertEquals(null, result.result)
        assertEquals("AccountId", result.errors.first().field)
        assertEquals("notFound", result.errors.first().group)
    }

    companion object : BaseInitObjects() {
        val userId = FinsUserId("user-123")
        override val initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = listOf(
            createInitTestAccountModel("update")
        )
        private val readSuccessStub = initAccountObjects.first()

        val updateAccountId = initAccountObjects.first().second.accountId
        val updateAccountIdNotFound = FinsAccountId("ad-repo-read-notFound")

        private val updateObj = FinsAccount(
            name = "update object",
            description = "update object description",
            amount = 0.0,
            accountId = updateAccountId,
        )

        private val updateObjNotFound = FinsAccount(
            name = "update object not found",
            description = "update object not found description",
            amount = 0.0,
            accountId = updateAccountIdNotFound,
        )
        override val initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList()
    }
}
