package ru.otus.otuskotlin.sokolova.finances.backend.repo.test

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import kotlin.math.roundToInt
import kotlin.random.Random

abstract class BaseInitObjects(
) {
    abstract val initAccountObjects: List<Pair<FinsUserId, FinsAccount>>
    abstract val initOperationObjects: List<Pair<FinsUserId, FinsOperation>>

    fun createInitTestAccountModel(
        suf: String,
        userId: FinsUserId = FinsUserId("user-123"),
        i: Int = 1
    ) = Pair(
        userId, FinsAccount(
            name = "$suf stub",
            description = "$suf stub description",
            amount = i * 10.0,
            accountId = FinsAccountId("account-repo-$suf"),
        )
    )

    fun createInitTestOperationModel(
        suf: String,
        fromAccountId: FinsAccountId = FinsAccountId.NONE,
        toAccountId: FinsAccountId = FinsAccountId.NONE,
        userId: FinsUserId = FinsUserId("user-123"),
        i: Int = 0
    ) = Pair(
        userId, FinsOperation(
            description = "$suf stub description",
            amount = i * 10.0,
            fromAccountId = fromAccountId,
            toAccountId = toAccountId,
            operationDateTime = Instant.parse("2022-02-12T12:00:00.000Z"),
            operationId = FinsOperationId("operation-repo-$suf"),
        )
    )
}