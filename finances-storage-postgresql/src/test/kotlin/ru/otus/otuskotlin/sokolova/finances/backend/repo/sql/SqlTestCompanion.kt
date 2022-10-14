package ru.otus.otuskotlin.sokolova.finances.backend.repo.sql

import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.sokolova.finances.backend.repo.postgresql.RepoSQL
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsAccount
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsOperation
import ru.otus.otuskotlin.sokolova.finances.common.models.FinsUserId
import java.time.Duration


class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "finances-pass"
    private const val SCHEMA = "finances"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        initAccountObjects: Collection<Pair<FinsUserId, FinsAccount>> = emptyList(),
        initOperationObjects: Collection<Pair<FinsUserId, FinsOperation>> = emptyList()
    ): RepoSQL {
        return RepoSQL(url, USER, PASS, SCHEMA, initAccountObjects, initOperationObjects)
    }
}
