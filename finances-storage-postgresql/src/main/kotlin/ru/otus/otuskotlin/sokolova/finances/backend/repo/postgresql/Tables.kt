package ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql

import kotlinx.datetime.toInstant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement
import ru.otus.otuskotlin.sokolova.finances.common.models.*
import java.util.*


object AccountsTable : StringIdTable("Accounts", "accountId") {
    val userId = reference("userId", UsersTable.id).index()
    val name = varchar("name", 128)
    val description = varchar("description", 512)
    val amount = double("amount")
    val accountLock = varchar("accountLock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = FinsAccount(
        name = res[name],
        description = res[description],
        amount = res[amount],
        accountId = FinsAccountId(res[id].toString()),
        accountLock = FinsAccountLock(res[accountLock])
    )

    fun from(res: ResultRow) = FinsAccount(
        name = res[name],
        description = res[description],
        amount = res[amount],
        accountId = FinsAccountId(res[id].toString()),
        accountLock = FinsAccountLock(res[accountLock])
    )

}

object OperationsTable : StringIdTable("Operations", "operationId") {
    val userId = reference("userId", UsersTable.id).index()
    val description = varchar("description", 512)
    val amount = double("amount")
    val fromAccountId = reference("fromAccountId", AccountsTable.id).index()
    val toAccountId = reference("toAccountId", AccountsTable.id).index()
    val operationDateTime = varchar("operationDateTime",50)
    val operationLock = varchar("operationLock", 50)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = FinsOperation(
        description = res[description],
        amount = res[amount],
        fromAccountId = FinsAccountId(res[fromAccountId].toString()),
        toAccountId = FinsAccountId(res[toAccountId].toString()),
        operationDateTime = res[operationDateTime].toInstant(),
        operationId = FinsOperationId(res[id].toString()),
        operationLock = FinsOperationLock(res[operationLock])
    )

    fun from(res: ResultRow) = FinsOperation(
        description = res[description],
        amount = res[amount],
        fromAccountId = FinsAccountId(res[fromAccountId].toString()),
        toAccountId = FinsAccountId(res[toAccountId].toString()),
        operationDateTime = res[operationDateTime].toInstant(),
        operationId = FinsOperationId(res[id].toString()),
        operationLock = FinsOperationLock(res[operationLock])
    )
}

object UsersTable : StringIdTable("Users", "userId") {
    override val primaryKey = PrimaryKey(id)
}

open class StringIdTable(name: String = "", columnName: String = "id", columnLength: Int = 50) : IdTable<String>(name) {
    override val id: Column<EntityID<String>> =
        varchar(columnName, columnLength).uniqueIndex().default(UUID.randomUUID().toString())
            .entityId()
    override val primaryKey by lazy { super.primaryKey ?: PrimaryKey(id) }
}

