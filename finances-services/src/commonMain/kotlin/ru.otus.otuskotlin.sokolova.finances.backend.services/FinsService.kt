package ru.otus.otuskotlin.sokolova.finances.backend.services

import ru.otus.otuskotlin.sokolova.finances.biz.FinsProcessor
import ru.otus.otuskotlin.sokolova.finances.common.FinsContext
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsSettings


class FinsService (
    val settings: FinsSettings
) {
    private val processor = FinsProcessor(settings)

    suspend fun exec(context: FinsContext) = processor.exec(context)

    suspend fun createAccount(context: FinsContext) = processor.exec(context)
    suspend fun readAccount(context: FinsContext) = processor.exec(context)
    suspend fun updateAccount(context: FinsContext) = processor.exec(context)
    suspend fun deleteAccount(context: FinsContext) = processor.exec(context)
    suspend fun searchAccount(context: FinsContext) = processor.exec(context)
    suspend fun historyAccount(context: FinsContext) = processor.exec(context)
    suspend fun createOperation(context: FinsContext) = processor.exec(context)
    suspend fun readOperation(context: FinsContext) = processor.exec(context)
    suspend fun updateOperation(context: FinsContext) = processor.exec(context)
    suspend fun deleteOperation(context: FinsContext) = processor.exec(context)
}