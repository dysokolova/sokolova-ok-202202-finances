package ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql.OperationsTable.fromAccountId
import ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql.OperationsTable.toAccountId
import ru.otus.otuskotlin.sokolova.finances.common.NONE
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.*
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.resultError
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbAccountResponse
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbAccountsResponse
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbOperationResponse
import ru.otus.otuskotlin.sokolova.finances.common.repo.DbResponse.Companion.toDbOperationsResponse
import ru.otus.otuskotlin.sokolova.finances.common.stubs.FinsStubs
import java.sql.SQLException
import java.util.*

class RepoSQL(
    url: String = "jdbc:postgresql://localhost:5432/financesdevdb",
    user: String = "postgres",
    password: String = "finances-pass",
    schema: String = "finances",
    initAccountObjects: Collection<Pair<FinsUserId, FinsAccount>> = emptyList(),
    initOperationObjects: Collection<Pair<FinsUserId, FinsOperation>> = emptyList(),
) : IRepository {
    private val db by lazy {
        SqlConnector(url, user, password, schema).connect(AccountsTable, OperationsTable, UsersTable)
    }
    private val mutex = Mutex()

    init {
        initAccountObjects.forEach {
            saveAccount(it)
        }

        initOperationObjects.forEach {
            saveOperation(it)
        }
    }


    private fun saveAccount(item: Pair<FinsUserId, FinsAccount>) =
        saveAccount(item.first, item.second)

    private fun saveAccount(user: FinsUserId, item: FinsAccount): DbResponse<FinsAccount> {
        if (user == FinsUserId.NONE) return resultError(FinsStubs.EMPTY_USER_ID)
        return safeTransaction({
            var isNew = false
            val dbAccount = AccountsTable.select {
                AccountsTable.id.eq(item.accountId.asString()) and AccountsTable.userId.eq(user.asString())
            }
            if (item.accountId == FinsAccountId.NONE || dbAccount.empty()) isNew = true

            if (isNew) {
                UsersTable.insertIgnore {
                    if (user != FinsUserId.NONE) {
                        it[id] = user.asString()
                    }
                }
                val resInsert = AccountsTable.insert {
                    if (item.accountId != FinsAccountId.NONE) {
                        it[id] = item.accountId.asString()
                    }
                    it[userId] = user.asString()
                    it[name] = item.name
                    it[description] = item.description
                    it[amount] = item.amount
                    it[accountLock] = item.accountLock.asString()
                }
                DbResponse<FinsAccount>(AccountsTable.from(resInsert), true)
            } else {
                if (UsersTable.select {
                        UsersTable.id.eq(user.asString())
                    }
                        .empty()) return@safeTransaction resultError(FinsStubs.NOT_FOUND_USER_ID)

                AccountsTable.update({
                    AccountsTable.id.eq(item.accountId.asString()) and AccountsTable.userId.eq(
                        user.asString()
                    )
                }) {
                    it[userId] = user.asString()
                    it[name] = item.name
                    it[description] = item.description
                    it[amount] = item.amount
                    it[accountLock] = item.accountLock.asString()
                }
                val resUpdate = AccountsTable.select {
                    AccountsTable.id.eq(item.accountId.asString()) and AccountsTable.userId.eq(user.asString())
                }.single()
                DbResponse<FinsAccount>(AccountsTable.from(resUpdate), true)
            }
        },
            {
                DbResponse<FinsAccount>(
                    result = null,
                    isSuccess = false,
                    errors = listOf(FinsError(message = message ?: localizedMessage))
                )
            })
    }

    private fun saveOperation(item: Pair<FinsUserId, FinsOperation>) =
        saveOperation(item.first, item.second)

    private fun saveOperation(user: FinsUserId, item: FinsOperation): DbResponse<FinsOperation> {

        return safeTransaction({
            if (UsersTable.select {
                    UsersTable.id.eq(user.asString())
                }.empty()) return@safeTransaction resultError(FinsStubs.NOT_FOUND_USER_ID)

            val dbFromAccount = AccountsTable.select {
                AccountsTable.id.eq(item.fromAccountId.asString()) and AccountsTable.userId.eq(user.asString())
            }
            val dbToAccount = AccountsTable.select {
                AccountsTable.id.eq(item.toAccountId.asString()) and AccountsTable.userId.eq(user.asString())
            }
            if (dbFromAccount.empty()) return@safeTransaction resultError(FinsStubs.NOT_FOUND_FROM_ACCOUNT_ID)
            if (dbToAccount.empty()) return@safeTransaction resultError(FinsStubs.NOT_FOUND_TO_ACCOUNT_ID)

            val dbOperation = OperationsTable.select {
                OperationsTable.id.eq(item.operationId.asString()) and OperationsTable.userId.eq(user.asString())
            }
            var isNew = false
            if ((item.operationId == FinsOperationId.NONE) or dbOperation.empty()) isNew = true
            AccountsTable.update({
                AccountsTable.id.eq(item.fromAccountId.asString()) and AccountsTable.userId.eq(user.asString())
            }) {
                it[amount] = dbFromAccount.single()[amount] - item.amount
                it[accountLock] = UUID.randomUUID().toString()
            }
            AccountsTable.update({
                AccountsTable.id.eq(item.toAccountId.asString()) and AccountsTable.userId.eq(
                    user.asString()
                )
            }) {
                it[amount] = dbToAccount.single()[amount] + item.amount
                it[accountLock] = UUID.randomUUID().toString()
            }
            if (isNew) {
                val resInsert = OperationsTable.insert {
                    if (item.operationId != FinsOperationId.NONE) {
                        it[id] = item.operationId.asString()
                    }
                    it[userId] = user.asString()
                    it[description] = item.description
                    it[amount] = item.amount
                    it[fromAccountId] = item.fromAccountId.asString()
                    it[toAccountId] = item.toAccountId.asString()
                    it[operationDateTime] = item.operationDateTime.toString()
                    it[operationLock] = item.operationLock.asString()
                }
                DbResponse<FinsOperation>(
                    OperationsTable.from(
                        resInsert
                    ), true
                )
            } else {
                AccountsTable.update({
                    AccountsTable.id.eq(dbOperation.single()[fromAccountId]) and AccountsTable.userId.eq(user.asString())
                }) {
                    it[amount] = AccountsTable.select {
                        AccountsTable.id.eq(dbOperation.single()[fromAccountId])
                    }.single()[amount] + dbOperation.single()[OperationsTable.amount]
                    it[accountLock] = UUID.randomUUID().toString()
                }
                AccountsTable.update({
                    AccountsTable.id.eq(dbOperation.single()[toAccountId]) and AccountsTable.userId.eq(
                        user.asString()
                    )
                }) {
                    it[amount] = AccountsTable.select {
                        AccountsTable.id.eq(dbOperation.single()[toAccountId])
                    }.single()[amount] - dbOperation.single()[OperationsTable.amount]
                    it[accountLock] = UUID.randomUUID().toString()
                }
                OperationsTable.update({
                    OperationsTable.id.eq(item.operationId.asString()) and OperationsTable.userId.eq(
                        user.asString()
                    )
                }) {
                    it[userId] = user.asString()
                    it[description] = item.description
                    it[amount] = item.amount
                    it[fromAccountId] = item.fromAccountId.asString()
                    it[toAccountId] = item.toAccountId.asString()
                    it[operationDateTime] = item.operationDateTime.toString()
                    it[operationLock] = item.operationLock.asString()
                }
                val resUpdate = OperationsTable.select {
                    OperationsTable.id.eq(item.operationId.asString()) and OperationsTable.userId.eq(user.asString())
                }.single()

                DbResponse<FinsOperation>(
                    OperationsTable.from(
                        resUpdate
                    ), true
                )
            }
        },
            {
                DbResponse<FinsOperation>(
                    result = null,
                    isSuccess = false,
                    errors = listOf(FinsError(message = message ?: localizedMessage))
                )
            })
    }


    override suspend fun accountCreate(rq: DbAccountRequest): DbAccountResponse {
        val account = Pair(rq.userId, rq.account.copy(accountLock = FinsAccountLock(UUID.randomUUID().toString())))
        return mutex.withLock {
            saveAccount(account).toDbAccountResponse()
        }
    }

    override suspend fun accountRead(rq: DbAccountIdRequest): DbAccountResponse {
        if (rq.userId == FinsUserId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_USER_ID).toDbAccountResponse()
        if (rq.accountId == FinsAccountId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_ACCOUNT_ID).toDbAccountResponse()

        return safeTransaction({
            if (UsersTable.select { UsersTable.id.eq(rq.userId.asString()) }
                    .empty()) return@safeTransaction resultError<FinsAccount>(
                FinsStubs.NOT_FOUND_USER_ID
            ).toDbAccountResponse()
            val result =
                AccountsTable.select { AccountsTable.id.eq(rq.accountId.asString()) and AccountsTable.userId.eq(rq.userId.asString()) }
            if (result.empty()) return@safeTransaction resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID).toDbAccountResponse()
            DbAccountResponse(
                AccountsTable.from(
                    result.single()
                ), true
            )
        }, {
            DbAccountResponse(
                result = null,
                isSuccess = false,
                listOf(FinsError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun accountUpdate(rq: DbAccountRequest): DbAccountResponse {
        if (rq.userId == FinsUserId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_USER_ID).toDbAccountResponse()
        if (rq.account.accountId == FinsAccountId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_ACCOUNT_ID).toDbAccountResponse()

        val oldLock = rq.account.accountLock.takeIf { it != FinsAccountLock.NONE }?.asString()
        val newAccount = rq.account.copy(accountLock = FinsAccountLock(UUID.randomUUID().toString()))

        return mutex.withLock {
            safeTransaction({
                val local =
                    AccountsTable.select {
                        AccountsTable.id.eq(rq.account.accountId.asString()) and AccountsTable.userId.eq(
                            rq.userId.asString()
                        )
                    }
                        .singleOrNull()?.let {
                            AccountsTable.from(it)
                        }
                        ?: return@safeTransaction resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID).toDbAccountResponse()

                return@safeTransaction when (oldLock) {
                    null, local.accountLock.asString() -> saveAccount(rq.userId, newAccount).toDbAccountResponse()
                    else -> resultError<FinsAccount>(FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_CHANGE).toDbAccountResponse()
                }
            }, {
                DbAccountResponse(
                    result = null,
                    isSuccess = false,
                    listOf(FinsError(message = message ?: localizedMessage))
                )
            })
        }
    }

    override suspend fun accountDelete(rq: DbAccountIdRequest): DbAccountResponse {
        if (rq.userId == FinsUserId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_USER_ID).toDbAccountResponse()
        if (rq.accountId == FinsAccountId.NONE) return resultError<FinsAccount>(FinsStubs.EMPTY_ACCOUNT_ID).toDbAccountResponse()

        return mutex.withLock {
            safeTransaction({
                if (UsersTable.select {
                        UsersTable.id.eq(rq.userId.asString())
                    }
                        .empty()) return@safeTransaction resultError<FinsAccount>(FinsStubs.NOT_FOUND_USER_ID).toDbAccountResponse()
                val dbAccount =
                    AccountsTable.select { AccountsTable.id.eq(rq.accountId.asString()) and AccountsTable.userId.eq(rq.userId.asString()) }
                if (dbAccount.empty()) return@safeTransaction resultError<FinsAccount>(FinsStubs.NOT_FOUND_ACCOUNT_ID).toDbAccountResponse()
                if (!OperationsTable.select {
                        (OperationsTable.userId eq rq.userId.asString()) and
                                ((OperationsTable.fromAccountId eq rq.accountId.asString()) or (OperationsTable.toAccountId eq rq.accountId.asString()))
                    }.empty()) resultError<FinsAccount>(FinsStubs.CANNOT_DELETE).toDbAccountResponse()
                val local = dbAccount.single().let { AccountsTable.from(it) }
                if (local.accountLock == rq.accountLock) {
                    AccountsTable.deleteWhere { AccountsTable.id eq rq.accountId.asString() }
                    DbResponse<FinsAccount>(result = local, isSuccess = true).toDbAccountResponse()
                } else {
                    resultError<FinsAccount>(FinsStubs.ERROR_ACCOUNT_CONCURENT_ON_DELETE).toDbAccountResponse()
                }
            }, {
                DbAccountResponse(
                    result = null,
                    isSuccess = false,
                    listOf(FinsError(message = message ?: localizedMessage))
                )
            })
        }
    }

    override suspend fun accountSearch(rq: DbAccountFilterRequest): DbAccountsResponse {
        if (rq.userId == FinsUserId.NONE) return resultError<List<FinsAccount>>(FinsStubs.EMPTY_USER_ID).toDbAccountsResponse()
        return safeTransaction({
            if (UsersTable.select {
                    UsersTable.id.eq(rq.userId.asString())
                }
                    .empty()) return@safeTransaction resultError<List<FinsAccount>>(FinsStubs.NOT_FOUND_USER_ID).toDbAccountsResponse()
            val results = AccountsTable.select {
                (AccountsTable.userId eq rq.userId.asString()) and ((AccountsTable.name like "%${rq.finsSrchFilter.searchFilter}%") or
                        (AccountsTable.description like "%${rq.finsSrchFilter.searchFilter}%"))
            }

            DbResponse<List<FinsAccount>>(result = results.map {
                AccountsTable.from(it)
            }, isSuccess = true).toDbAccountsResponse()
        }, {
            DbAccountsResponse(
                result = null,
                isSuccess = false,
                listOf(FinsError(message = message ?: localizedMessage))
            )
        })
    }

    override suspend fun accountHistory(rq: DbAccountHistoryRequest): DbOperationsResponse {

        return safeTransaction({
            if (rq.userId == FinsUserId.NONE) return@safeTransaction resultError<List<FinsOperation>>(FinsStubs.EMPTY_USER_ID).toDbOperationsResponse()
            if (UsersTable.select {
                    UsersTable.id.eq(rq.userId.asString())
                }
                    .empty()) return@safeTransaction resultError<List<FinsOperation>>(FinsStubs.NOT_FOUND_USER_ID).toDbOperationsResponse()

            if (rq.accountId == FinsAccountId.NONE) return@safeTransaction resultError<List<FinsOperation>>(FinsStubs.EMPTY_ACCOUNT_ID).toDbOperationsResponse()
            if (AccountsTable.select {
                    AccountsTable.id.eq(rq.accountId.asString()) and AccountsTable.userId.eq(rq.userId.asString())
                }
                    .empty()) return@safeTransaction resultError<List<FinsOperation>>(FinsStubs.NOT_FOUND_ACCOUNT_ID).toDbOperationsResponse()
            if (rq.finsHistFilter.fromDateTime == Instant.NONE) return@safeTransaction resultError<List<FinsOperation>>(
                FinsStubs.EMPTY_FROM_DATE_TIME
            ).toDbOperationsResponse()
            if (rq.finsHistFilter.toDateTime == Instant.NONE) return@safeTransaction resultError<List<FinsOperation>>(
                FinsStubs.EMPTY_TO_DATE_TIME
            ).toDbOperationsResponse()
            val results = OperationsTable.select {
                (OperationsTable.userId eq rq.userId.asString()) and
                        ((OperationsTable.fromAccountId eq rq.accountId.asString()) or (OperationsTable.toAccountId eq rq.accountId.asString())) and
                        ((OperationsTable.operationDateTime greaterEq rq.finsHistFilter.fromDateTime.toString())) and
                        ((OperationsTable.operationDateTime lessEq rq.finsHistFilter.toDateTime.toString()))
            }
            DbResponse<List<FinsOperation>>(result = results.map {
                OperationsTable.from(it)
            }, isSuccess = true).toDbOperationsResponse()

        }, {
            DbOperationsResponse(
                result = null,
                isSuccess = false,
                listOf(FinsError(message = message ?: localizedMessage))
            )
        })
    }


    override suspend fun operationCreate(rq: DbOperationRequest): DbOperationResponse {
        val operation =
            Pair(rq.userId, rq.operation.copy(operationLock = FinsOperationLock(UUID.randomUUID().toString())))
        return mutex.withLock {
            saveOperation(operation).toDbOperationResponse()
        }
    }

    override suspend fun operationRead(rq: DbOperationIdRequest): DbOperationResponse {
        return safeTransaction({

            if (UsersTable.select {
                    UsersTable.id.eq(rq.userId.asString())
                }
                    .empty()) return@safeTransaction resultError<FinsOperation>(FinsStubs.NOT_FOUND_USER_ID).toDbOperationResponse()
            val result = OperationsTable.select {
                OperationsTable.id.eq(rq.operationId.asString()) and OperationsTable.userId.eq(
                    rq.userId.asString()
                )
            }
            if (result.empty()) return@safeTransaction resultError<FinsOperation>(FinsStubs.NOT_FOUND_OPERATION_ID).toDbOperationResponse()


            DbOperationResponse(OperationsTable.from(result.single()), true)
        }, {
            val err = when (this) {
                is IllegalArgumentException -> FinsError(message = "More than one element with the same id")
                else -> FinsError(message = localizedMessage)
            }
            DbOperationResponse(result = null, isSuccess = false, errors = listOf(err))
        })
    }

    override suspend fun operationUpdate(rq: DbOperationRequest): DbOperationResponse {
        val key =
            rq.operation.operationId.takeIf { it != FinsOperationId.NONE }?.asString()
                ?: return resultError<FinsOperation>(FinsStubs.EMPTY_USER_ID).toDbOperationResponse()
        val oldLock = rq.operation.operationLock.takeIf { it != FinsOperationLock.NONE }?.asString()
        val newOperation = rq.operation.copy(operationLock = FinsOperationLock(UUID.randomUUID().toString()))

        return mutex.withLock {
            safeTransaction({
                val local =
                    OperationsTable.select { OperationsTable.id.eq(key) and OperationsTable.userId.eq(rq.userId.asString()) }
                        .singleOrNull()?.let {
                            OperationsTable.from(it)
                        }
                        ?: return@safeTransaction resultError<FinsOperation>(FinsStubs.NOT_FOUND_OPERATION_ID).toDbOperationResponse()

                return@safeTransaction when (oldLock) {
                    null, local.operationLock.asString() -> saveOperation(
                        rq.userId,
                        newOperation
                    ).toDbOperationResponse()
                    else -> resultError<FinsOperation>(FinsStubs.ERROR_OPERATION_CONCURENT_ON_CHANGE).toDbOperationResponse()
                }
            }, {
                DbOperationResponse(
                    result = null,
                    isSuccess = false,
                    listOf(FinsError(message = message ?: localizedMessage))
                )
            })
        }
    }

    override suspend fun operationDelete(rq: DbOperationIdRequest): DbOperationResponse {
        if (rq.userId == FinsUserId.NONE) return resultError<FinsOperation>(FinsStubs.EMPTY_USER_ID).toDbOperationResponse()
        if (rq.operationId == FinsOperationId.NONE) return resultError<FinsOperation>(FinsStubs.EMPTY_OPERATION_ID).toDbOperationResponse()

        return mutex.withLock {
            safeTransaction({
                if (UsersTable.select {
                        UsersTable.id.eq(rq.userId.asString())
                    }
                        .empty()) return@safeTransaction resultError<FinsOperation>(FinsStubs.NOT_FOUND_USER_ID).toDbOperationResponse()
                val dbLocal = OperationsTable.select {
                    OperationsTable.id.eq(rq.operationId.asString()) and OperationsTable.userId.eq(
                        rq.userId.asString()
                    )
                }
                if (dbLocal.empty()) return@safeTransaction resultError<FinsOperation>(FinsStubs.NOT_FOUND_OPERATION_ID).toDbOperationResponse()
                val local = OperationsTable.from(dbLocal.single())
                if (local.operationLock == rq.operationLock) {
                    AccountsTable.update({
                        AccountsTable.id.eq(dbLocal.single()[fromAccountId]) and AccountsTable.userId.eq(rq.userId.asString())
                    }) {
                        it[amount] = AccountsTable.select {
                            AccountsTable.id.eq(dbLocal.single()[fromAccountId])
                        }.single()[amount] + dbLocal.single()[OperationsTable.amount]
                        it[accountLock] = UUID.randomUUID().toString()
                    }
                    AccountsTable.update({
                        AccountsTable.id.eq(dbLocal.single()[toAccountId]) and AccountsTable.userId.eq(
                            rq.userId.asString()
                        )
                    }) {
                        it[amount] = AccountsTable.select {
                            AccountsTable.id.eq(dbLocal.single()[toAccountId])
                        }.single()[amount] - dbLocal.single()[OperationsTable.amount]
                        it[accountLock] = UUID.randomUUID().toString()
                    }
                    OperationsTable.deleteWhere { OperationsTable.id eq rq.operationId.asString() }
                    DbResponse<FinsOperation>(result = local, isSuccess = true).toDbOperationResponse()
                } else {
                    resultError<FinsOperation>(FinsStubs.ERROR_OPERATION_CONCURENT_ON_DELETE).toDbOperationResponse()
                }
            }, {
                DbOperationResponse(
                    result = null,
                    isSuccess = false,
                    listOf(FinsError(message = message ?: localizedMessage))
                )
            })
        }
    }

    /**
     * Transaction wrapper to safely handle caught exception and throw all sql-like exceptions. Also remove lot's of duplication code
     */
    private fun <T> safeTransaction(statement: Transaction.() -> T, handleException: Throwable.() -> T): T {
        return try {
            transaction(db, statement)
        } catch (e: SQLException) {
            throw e
        } catch (e: Throwable) {
            return handleException(e)
        }
    }


}