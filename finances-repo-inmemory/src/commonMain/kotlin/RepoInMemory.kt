package ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.model.AccountEntity
import ru.otus.otuskotlin.sokolova.finances.backend.repository.inmemory.model.OperationEntity
import ru.otus.otuskotlin.sokolova.finances.common.helpers.errorConcurrency
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.resultError
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbAccountResponse
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbOperationResponse
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import ru.otus.otuskotlin.sokolova.finances.common.stubs.errors.getError

class RepoInMemory(
    initAccountObjects: List<Pair<FinsUserId, FinsAccount>> = emptyList(),
    initOperationObjects: List<Pair<FinsUserId, FinsOperation>> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : IRepository {
    /**
     * Инициализация кеша с установкой "времени жизни" данных после записи
     */

    private val mutex = Mutex()
    private val cacheAccount = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, AccountEntity>()

    init {
        initAccountObjects.forEach {
            saveAccount(it)
        }
    }

    private fun saveAccount(account: Pair<FinsUserId, FinsAccount>) {
        val entity = AccountEntity(account.first, account.second)
        if (entity.accountId == null) {
            return
        }
        cacheAccount.put(entity.accountId!!, entity)
    }

    private val cacheOperation = Cache.Builder()
        .expireAfterWrite(ttl)
        .build<String, OperationEntity>()

    init {
        initOperationObjects.forEach {
            saveOperation(it)
        }
    }

    private fun saveOperation(operation: Pair<FinsUserId, FinsOperation>) {
        val entity = OperationEntity(operation.first, operation.second)
        if (entity.operationId == null) {
            return
        }
        cacheOperation.put(entity.operationId!!, entity)
    }

    override suspend fun accountCreate(rq: DbAccountRequest): DbAccountResponse {
        var key = randomUuid()
        var account = rq.account.copy(accountId = FinsAccountId(key), accountLock = FinsAccountLock(randomUuid()))
        this.mutex.withLock {
            while (cacheAccount.get(key) != null) {
                key = randomUuid()
                account = rq.account.copy(accountId = FinsAccountId(key), accountLock = FinsAccountLock(randomUuid()))
            }
            val entity = AccountEntity(rq.userId, account)
            cacheAccount.put(key, entity)
        }
        return DbAccountResponse(
            result = account,
            isSuccess = true,
        )
    }

    override suspend fun accountRead(rq: DbAccountIdRequest): DbAccountResponse {
        val key = rq.accountId.takeIf { it != FinsAccountId.NONE }?.asString() ?: return resultError<FinsAccount>(
            FinsStubs.EMPTY_ACCOUNT_ID
        ).toDbAccountResponse()
        val local = cacheAccount.get(key)
        return when {
            local == null || local.userId != rq.userId.asString() -> resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID)
                .toDbAccountResponse()
            else -> local.let {
                DbAccountResponse(
                    result = it.toInternal(),
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun accountUpdate(rq: DbAccountRequest): DbAccountResponse {
        val key =
            rq.account.accountId.takeIf { it != FinsAccountId.NONE }?.asString() ?: return resultError<FinsAccount>(
                FinsStubs.EMPTY_ACCOUNT_ID
            ).toDbAccountResponse()
        val oldAccountLock = rq.account.accountLock.takeIf { it != FinsAccountLock.NONE }?.asString()
        val newAccount = rq.account.copy(accountLock = FinsAccountLock(randomUuid()))
        val entity = AccountEntity(rq.userId, newAccount)
        this.mutex.withLock {
            val local = cacheAccount.get(key)
            when {
                local == null || local.userId != rq.userId.asString() -> return resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID)
                    .toDbAccountResponse()

                local.accountLock == null || local.accountLock == oldAccountLock -> cacheAccount.put(key, entity)
                else -> return resultError<FinsAccount>(FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_CHANGE).toDbAccountResponse()
            }
        }
        return DbAccountResponse(
            result = newAccount,
            isSuccess = true,
        )
    }

    override suspend fun accountDelete(rq: DbAccountIdRequest): DbAccountResponse {

        val key = rq.accountId.takeIf { it != FinsAccountId.NONE }?.asString() ?: return resultError<FinsAccount>(
            FinsStubs.EMPTY_ACCOUNT_ID
        ).toDbAccountResponse()
        val oldAccountLock = rq.accountLock.asString()
        this.mutex.withLock {
            if (cacheOperation.asMap().asSequence()
                    .filter { entry ->
                        rq.userId.takeIf { it != FinsUserId.NONE }?.let {
                            it.asString() == entry.value.userId
                        } ?: true
                    }
                    .filter { entry ->
                        rq.accountId.takeIf { it != FinsAccountId.NONE }?.asString()?.let {
                            (it == entry.value.fromAccountId || it == entry.value.toAccountId)
                        } ?: true
                    }.firstOrNull() != null
            ) return resultError<FinsAccount>(FinsStubs.CANNOT_DELETE).toDbAccountResponse()
            val local = cacheAccount.get(key)
            when {
                (local == null) || (local.userId != rq.userId.asString()) -> {
                    return resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID).toDbAccountResponse()
                }

                (local.accountLock == null) || (local.accountLock == oldAccountLock) -> {
                    cacheAccount.invalidate(key)
                }
                else -> {
                    return resultError<FinsAccount>(FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE).toDbAccountResponse()
                }
            }
            return DbAccountResponse(
                result = null,
                isSuccess = true,
                errors = emptyList()
            )
        }
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun accountSearch(rq: DbAccountFilterRequest): DbAccountsResponse {
        val result = cacheAccount.asMap().asSequence()
            .filter { entry ->
                rq.userId.takeIf { it != FinsUserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                rq.finsSrchFilter.searchFilter.takeIf { it.isNotBlank() }?.let {
                    (entry.value.name?.contains(it) ?: false || entry.value.description?.contains(it) ?: false)
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbAccountsResponse(
            result = result,
            isSuccess = true
        )
    }

    override suspend fun accountHistory(rq: DbAccountHistoryRequest): DbOperationsResponse {
        val result = cacheOperation.asMap().asSequence()
            .filter { entry ->
                rq.userId.takeIf { it != FinsUserId.NONE }?.let {
                    it.asString() == entry.value.userId
                } ?: true
            }
            .filter { entry ->
                rq.accountId.takeIf { it != FinsAccountId.NONE }?.asString()?.let {
                    (it == entry.value.fromAccountId || it == entry.value.toAccountId)
                } ?: true
            }.filter { entry ->
                rq.finsHistFilter.fromDateTime.takeIf { it != Instant.NONE }?.let {
                    ((it <= (entry.value.operationDateTime?.toInstant() ?: Instant.NONE)))
                } ?: true
            }.filter { entry ->
                rq.finsHistFilter.toDateTime.takeIf { it != Instant.NONE }?.let {
                    ((it >= (entry.value.operationDateTime?.toInstant() ?: Instant.NONE)))
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        return DbOperationsResponse(
            result = result,
            isSuccess = true
        )
    }

    override suspend fun operationCreate(rq: DbOperationRequest): DbOperationResponse {

        val keyFromAccountId = rq.operation.fromAccountId.takeIf { it != FinsAccountId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_FROM_ACCOUNT_ID).toDbOperationResponse()
        val keyToAccountId = rq.operation.toAccountId.takeIf { it != FinsAccountId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_TO_ACCOUNT_ID).toDbOperationResponse()
        val errors: List<FinsError> = emptyList()
        var key = randomUuid()
        var operation =
            rq.operation.copy(operationId = FinsOperationId(key), operationLock = FinsOperationLock(randomUuid()))
        this.mutex.withLock {
            while (cacheOperation.get(key) != null) {
                key = randomUuid()
                operation =
                    rq.operation.copy(
                        operationId = FinsOperationId(key),
                        operationLock = FinsOperationLock(randomUuid())
                    )
            }
            val localFromAccount = cacheAccount.get(keyFromAccountId)
            if (localFromAccount == null || localFromAccount.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID
            ).toDbOperationResponse()
            val localToAccount = cacheAccount.get(keyToAccountId)
            if (localToAccount == null || localToAccount.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_TO_ACCOUNT_ID
            ).toDbOperationResponse()
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localFromAccount.toInternal().apply { amount -= rq.operation.amount })
            )
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localToAccount.toInternal().apply { amount += rq.operation.amount })
            )
            cacheOperation.put(key, OperationEntity(rq.userId, operation))
        }
        return DbOperationResponse(
            result = operation,
            isSuccess = true,
        )
    }

    override suspend fun operationRead(rq: DbOperationIdRequest): DbOperationResponse {
        val key = rq.operationId.takeIf { it != FinsOperationId.NONE }?.asString() ?: return resultError<FinsOperation>(
            FinsStubs.EMPTY_OPERATION_ID
        ).toDbOperationResponse()
        val local = cacheOperation.get(key)
        when {
            local == null || local.userId != rq.userId.asString() -> return resultError<FinsOperation>(FinsStubs.NOT_FOUND_OPERATION_ID).toDbOperationResponse()
            else -> return local.let {
                DbOperationResponse(
                    result = it.toInternal(),
                    isSuccess = true,
                )
            }
        }
    }

    override suspend fun operationUpdate(rq: DbOperationRequest): DbOperationResponse {

        val key = rq.operation.operationId.takeIf { it != FinsOperationId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_OPERATION_ID).toDbOperationResponse()
        val keyFromAccountIdNew = rq.operation.fromAccountId.takeIf { it != FinsAccountId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_FROM_ACCOUNT_ID).toDbOperationResponse()
        val keyToAccountIdNew = rq.operation.toAccountId.takeIf { it != FinsAccountId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_TO_ACCOUNT_ID).toDbOperationResponse()
        val operationNew = rq.operation.copy(operationLock = FinsOperationLock(randomUuid()))
        val operationLockIn = rq.operation.operationLock.takeIf { it != FinsOperationLock.NONE }?.asString()
        val errors: List<FinsError> = emptyList()
        this.mutex.withLock {
            val localOperation = cacheOperation.get(key)
            if (localOperation == null || localOperation.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_OPERATION_ID
            ).toDbOperationResponse()

            val keyFromAccountIdOld =
                localOperation.fromAccountId.takeIf { it != null }
                    ?: return resultError<FinsOperation>(FinsStubs.EMPTY_FROM_ACCOUNT_ID).toDbOperationResponse()
            val keyToAccountIdOld =
                localOperation.toAccountId.takeIf { it != null }
                    ?: return resultError<FinsOperation>(FinsStubs.EMPTY_TO_ACCOUNT_ID).toDbOperationResponse()

            val localFromAccountOld = cacheAccount.get(keyFromAccountIdOld)
            if (localFromAccountOld == null || localFromAccountOld.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID
            ).toDbOperationResponse()
            val localToAccountOld = cacheAccount.get(keyToAccountIdOld)
            if (localToAccountOld == null || localToAccountOld.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_TO_ACCOUNT_ID
            ).toDbOperationResponse()
            val amountOld = localOperation.amount?.toDouble()
                ?: return resultError<FinsOperation>(FinsStubs.BAD_FORMAT_AMOUNT).toDbOperationResponse()

            val localFromAccountNew = cacheAccount.get(keyFromAccountIdNew)
            if (localFromAccountNew == null || localFromAccountNew.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID
            ).toDbOperationResponse()
            val localToAccountNew = cacheAccount.get(keyToAccountIdNew)
            if (localToAccountNew == null || localToAccountNew.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_TO_ACCOUNT_ID
            ).toDbOperationResponse()
            val amountNew = rq.operation.amount

            if (localOperation.operationLock != null && localOperation.operationLock != operationLockIn) return resultError<FinsOperation>(
                FinsStubs.ERROR_OPERATION_CONCURENT_ON_CHANGE
            ).toDbOperationResponse()
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localFromAccountOld.toInternal().apply { amount += amountOld })
            )
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localToAccountOld.toInternal().apply { amount -= amountOld })
            )
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localFromAccountNew.toInternal().apply { amount -= amountNew })
            )
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localToAccountNew.toInternal().apply { amount += amountNew })
            )
            cacheOperation.put(key, OperationEntity(rq.userId, operationNew))
        }
        return DbOperationResponse(
            result = operationNew,
            isSuccess = true,
        )
    }

    override suspend fun operationDelete(rq: DbOperationIdRequest): DbOperationResponse {

        val key = rq.operationId.takeIf { it != FinsOperationId.NONE }?.asString()
            ?: return resultError<FinsOperation>(FinsStubs.EMPTY_OPERATION_ID).toDbOperationResponse()
        val operationLockIn = rq.operationLock.takeIf { it != FinsOperationLock.NONE }?.asString()
        val errors: List<FinsError> = emptyList()
        this.mutex.withLock {
            val localOperation = cacheOperation.get(key)
            if (localOperation == null || localOperation.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_OPERATION_ID
            ).toDbOperationResponse()

            val keyFromAccountIdOld =
                localOperation.fromAccountId.takeIf { it != null }
                    ?: return resultError<FinsOperation>(FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID).toDbOperationResponse()
            val keyToAccountIdOld =
                localOperation.toAccountId.takeIf { it != null }
                    ?: return resultError<FinsOperation>(FinsStubs.NOT_FOUND_TO_ACCOUNT_ID).toDbOperationResponse()

            val localFromAccountOld = cacheAccount.get(keyFromAccountIdOld)
            if (localFromAccountOld == null || localFromAccountOld.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID
            ).toDbOperationResponse()
            val localToAccountOld = cacheAccount.get(keyToAccountIdOld)
            if (localToAccountOld == null || localToAccountOld.userId != rq.userId.asString()) return resultError<FinsOperation>(
                FinsStubs.NOT_FOUND_TO_ACCOUNT_ID
            ).toDbOperationResponse()
            val amountOld = localOperation.amount?.toDouble()
                ?: return resultError<FinsOperation>(FinsStubs.BAD_FORMAT_AMOUNT).toDbOperationResponse()

            if (localOperation.operationLock != null && localOperation.operationLock != operationLockIn) return resultError<FinsOperation>(
                FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE
            ).toDbOperationResponse()
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localFromAccountOld.toInternal().apply { amount += amountOld })
            )
            cacheAccount.put(
                key,
                AccountEntity(rq.userId, localToAccountOld.toInternal().apply { amount -= amountOld })
            )
            cacheOperation.invalidate(key)
        }
        return DbOperationResponse(
            result = null,
            isSuccess = true,
            errors = emptyList()
        )
    }
}